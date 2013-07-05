package com.babytree.apps.comm.net;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.text.TextUtils;

import com.babytree.apps.comm.exception.ServerException;
import com.babytree.apps.comm.tools.BabytreeLog;
import com.babytree.apps.comm.util.BaseUtil;
import com.babytree.apps.comm.util.ExceptionUtil;
import com.github.droidfu.http.BetterHttp;
import com.github.droidfu.http.BetterHttpRequest;
import com.github.droidfu.http.BetterHttpResponse;
import com.github.droidfu.http.BetterHttpResponseImpl;

public class BabytreeHttp {

	private static final String NET_ENCODING = "UTF-8";
	public static final String USER_NAME = "mobiletester";
	public static final String PASSWORD = "c65jt4k588";

	// 应用标识(每个接口调用都需要传)
	private static String app_id = "";

	private static Context appContext = null;
	// mac地址(每个接口调用都需要传)
	private static String mac = "";
	// 版本号(每个接口调用都需要传)
	private static String v = "";
	// 地里位置(每个接口调用都需要传)
	private static double latitude = 0;
	// 地里位置(每个接口调用都需要传)
	private static double longitude = 0;

	static {
		BetterHttp.setupHttpClient();
	}

	public static void setLocation(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	public static void setAppId(String appId) {

		if (TextUtils.isEmpty(app_id)) {
			app_id = appId;
		}
	}

	public static void setContext(Context context) {
		if (appContext == null) {
			appContext = context.getApplicationContext();
			BetterHttp.setContext(appContext);
			// 获取版本号
			v = BaseUtil.getAppVersionName(appContext);
			// 获取mac
			mac = BaseUtil.getMacAddress(appContext);
		}
	}

	public static void setCommonParams(List<NameValuePair> params) throws ConnectException {
		if (appContext == null) {
			throw new ConnectException("The appContext is null.");
		}
		if (TextUtils.isEmpty(app_id)) {
			throw new ConnectException("The app_id is null.");
		}
		if (params != null) {

			params.add(new BasicNameValuePair("client_type", "android"));
			params.add(new BasicNameValuePair("mac", mac));
			params.add(new BasicNameValuePair("app_id", app_id));
			params.add(new BasicNameValuePair("version", v));
			params.add(new BasicNameValuePair("latitude", latitude == 0 ? "" : String.valueOf(latitude)));
			params.add(new BasicNameValuePair("longitude", longitude == 0 ? "" : String.valueOf(longitude)));
		}
	}

	private static void setAuthInfo(String host, boolean isDev) {
		if (BetterHttp.getHttpClient() != null) {
			if (isDev) {
				Credentials credentials = new UsernamePasswordCredentials(USER_NAME, PASSWORD);
				BetterHttp.getHttpClient().getCredentialsProvider()
						.setCredentials(new AuthScope(host, 80), credentials);
			} else {
				BetterHttp.getHttpClient().setCredentialsProvider(null);
			}
		}
	}

	private static String getHost(String url) {
		String ret = url.substring(7, url.indexOf('/', 7));
		return ret;
	}

	public static String post(String url, ArrayList<NameValuePair> params) throws UnsupportedEncodingException,
			ConnectException, IOException, ServerException {
		setAuthInfo(getHost(url), true);
		setCommonParams(params);
		if ("Dev".equals(v)) {
			BabytreeLog.d(url + ExceptionUtil.printParams(params).toString());
		}
		HttpEntity entity = new UrlEncodedFormEntity(params, NET_ENCODING);
		BetterHttpRequest request = BetterHttp.post(url, entity);
		BetterHttpResponse response = request.send();
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			return response.getResponseBodyAsString();
		} else {
			throw new ServerException("The network info is :" + BaseUtil.getNetType(appContext)
					+ ";The request url is :" + url + ExceptionUtil.printParams(params).toString()
					+ "The response status code is :" + response.getStatusCode());
		}
	}

	/**
	 * 上传图片
	 * 
	 * @param url
	 *            api地址
	 * @param params
	 *            参数集合
	 * @param file
	 *            文件绝对路径
	 * @return json格式结果
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String postPhoto(String url, ArrayList<NameValuePair> params, File file) throws ParseException,
			IOException {
		setAuthInfo(getHost(url), true);
		setCommonParams(params);
		if ("Dev".equals(v)) {
			BabytreeLog.d(url + ExceptionUtil.printParams(params).toString());
		}
		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart("upload_file", cbFile);
		for (int i = 0; i < params.size(); i++) {
			mpEntity.addPart(params.get(i).getName(),
					new StringBody(params.get(i).getValue(), Charset.forName(HTTP.UTF_8)));
		}
		BetterHttpRequest request = BetterHttp.post(url, mpEntity);
		BetterHttpResponse response = request.send();
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			return response.getResponseBodyAsString();
		} else {
			throw new ServerException("The network info is :" + BaseUtil.getNetType(appContext)
					+ ";The request url is :" + url + ExceptionUtil.printParams(params).toString()
					+ "The response status code is :" + response.getStatusCode());
		}
	}

	public static String get(String u, List<NameValuePair> params) throws UnsupportedEncodingException,
			ConnectException, IOException, ServerException {
		setAuthInfo(getHost(u), true);
		setCommonParams(params);
		String url = u + "?" + buildQueryString(params);

		if ("Dev".equals(v)) {
			BabytreeLog.d(url);
		}

		BetterHttpRequest request = BetterHttp.get(url);
		BetterHttpResponseImpl response = (BetterHttpResponseImpl) request.send();
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			return response.getResponseBodyAsString();
		} else
			throw new ServerException("The network info is :" + BaseUtil.getNetType(appContext)
					+ ";The request url is :" + url + ";The response status code is :" + response.getStatusCode());
	}

	private static String buildQueryString(List<NameValuePair> params) {
		StringBuilder ret = new StringBuilder();
		int size = params.size();
		for (int i = 0; i < size; i++) {
			NameValuePair nameValuePair = params.get(i);
			try {
				if (i != size - 1) {
					if (nameValuePair.getName() == null) {
						ret.append("&");
					} else {
						String value = nameValuePair.getValue();
						if (value == null) {
							value = "";
						}
						ret.append(nameValuePair.getName()).append("=").append(URLEncoder.encode(value, "UTF-8"))
								.append("&");
					}
				} else {
					if (nameValuePair.getName() == null) {
						ret.append("&");
					} else {
						String value = nameValuePair.getValue();
						if (value == null) {
							value = "";
						}
						ret.append(nameValuePair.getName()).append("=").append(URLEncoder.encode(value, "UTF-8"));
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return ret.toString().replaceAll(" ", "%20");
	}

	public static String postHttps(String url, ArrayList<NameValuePair> params) throws UnsupportedEncodingException,
			ConnectException, IOException, ServerException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, KeyManagementException, UnrecoverableKeyException {
		setCommonParams(params);
		if ("Dev".equals(v)) {
			BabytreeLog.d(ExceptionUtil.printParams(params).toString());
		}

		// HTTPS
		HttpParams httpParams = new BasicHttpParams();
		for (NameValuePair nameValuePair : params) {
			httpParams.setParameter(nameValuePair.getName(), nameValuePair.getValue());
		}

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null, null);
		SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", sf, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, registry);

		HttpClient httpClient = new DefaultHttpClient(ccm, httpParams);
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		httpPost.setEntity(entity);

		HttpResponse response = httpClient.execute(httpPost);
		int sCode = response.getStatusLine().getStatusCode();
		if (sCode == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else {
			throw new ServerException("The response status code is " + sCode);
		}
	}
}
