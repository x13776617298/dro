package com.babytree.apps.comm.ctr;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BabytreeController {

	protected final static String TAG = BabytreeController.class.getSimpleName();

	public final static String NetworkExceptionMessage = "亲,您的网络不给力啊";

	public final static String SystemExceptionMessage = "系统错误";

	public final static String ConnectExceptionMessage = "亲,您的网络不给力啊";

	public final static String ServerExceptionMessage = "服务器处理错误";

	public final static String UnsupportedEncodingExceptionMessage = "无法识别的编码";

	public final static String IOExceptionMessage = "亲,您的网络不给力啊";

	public final static String JSONExceptionMessage = "数据解析失败,请重试";

	public final static int SystemExceptionCode = -2;

	public final static int NetworkExceptionCode = -1;

	public final static int ConnectExceptionCode = 1;

	public final static int ServerExceptionCode = 2;

	public final static int UnsupportedEncodingExceptionCode = 3;

	public final static int IOExceptionCode = 4;

	public final static int JSONExceptionCode = 5;

	/**
	 * 访问成功值标记
	 */
	public static final int SUCCESS_CODE = 0;

	/**
	 * json取int值
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	protected static int getInt(JSONObject jsonObject, String key) {

		int tmp = 0;
		try {
			tmp = jsonObject.getInt(key);
		} catch (JSONException e) {
			tmp = 0;
		}
		return tmp;
	}

	/**
	 * json取int值
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	protected static String getStringInt(JSONObject jsonObject, String key) {

		String tmp = "0";
		try {
			tmp = jsonObject.getString(key);
		} catch (JSONException e) {
			tmp = "0";
		}
		return tmp;
	}

}