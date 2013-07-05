package com.babytree.apps.comm.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.babytree.apps.comm.ctr.BabytreeController;
import com.babytree.apps.comm.exception.ServerException;
import com.umeng.analytics.MobclickAgent;

public class ExceptionUtil {

	/**
	 * 打印异常,网络异常
	 * 
	 * @param ex
	 * @param context
	 */
	private static void catchException(Exception ex, Context context) {
		MobclickAgent.reportError(context.getApplicationContext(),
				printException(ex).toString());
	}

	public static void catchException(String errorMessage, Context context) {
		if (!"".equals(errorMessage))
			MobclickAgent.reportError(context.getApplicationContext(),
					BaseUtil.getAppVersionName(context) + ":" + errorMessage);
	}

	public static StringBuffer printException(Exception ex) {
		StringBuffer ret = new StringBuffer();
		if (ex != null) {

			ret.append(ex.toString());
			// Don't use getStackTrace() as it calls clone()
			// Get stackTrace, in case stackTrace is reassigned
			StackTraceElement[] stack = ex.getStackTrace();
			for (java.lang.StackTraceElement element : stack) {
				ret.append("\tat " + element);
			}
			ret.append("\n");

		}
		return ret;
	}

	public static StringBuffer printParams(ArrayList<NameValuePair> values) {
		StringBuffer ret = new StringBuffer();
		if (values != null) {
			for (NameValuePair nameValuePair : values) {
				ret.append("&");
				ret.append(nameValuePair.toString());
			}
			ret.append("\n");
		}

		return ret;
	}

	private static String printException(Exception ex,
			ArrayList<NameValuePair> params, String jsonString) {
		return ExceptionUtil.printParams(params)
				.append(ExceptionUtil.printException(ex))
				.append(jsonString == null ? "" : jsonString).toString();
	}

	public static DataResult switchException(DataResult result, Exception ex,
			ArrayList<NameValuePair> params, String jsonString) {
		Log.e("BabytreeTag", ex.toString(), ex);
		if (result == null) {
			result = new DataResult();
		}
		// 判断异常类型
		if (ex instanceof ConnectException) {
			result.message = BabytreeController.ConnectExceptionMessage;
			result.status = BabytreeController.ConnectExceptionCode;
		} else if (ex instanceof ServerException) {
			result.message = BabytreeController.ServerExceptionMessage;
			result.status = BabytreeController.ServerExceptionCode;
			// TODO 去掉手动捕捉的异常信息
			// result.error = printException(ex, params, jsonString);
		} else if (ex instanceof UnsupportedEncodingException) {
			result.message = BabytreeController.UnsupportedEncodingExceptionMessage;
			result.status = BabytreeController.UnsupportedEncodingExceptionCode;
			// TODO 去掉手动捕捉的异常信息
			// result.error = printException(ex, params, jsonString);
		} else if (ex instanceof IOException) {
			result.message = BabytreeController.IOExceptionMessage;
			result.status = BabytreeController.IOExceptionCode;
		} else if (ex instanceof JSONException) {
			result.message = BabytreeController.JSONExceptionMessage;
			result.status = BabytreeController.JSONExceptionCode;
			// TODO 去掉手动捕捉的异常信息
			// result.error = printException(ex, params, jsonString);
		} else if (ex instanceof UnknownHostException) {
			result.message = BabytreeController.NetworkExceptionMessage;
			result.status = BabytreeController.NetworkExceptionCode;
		} else {
			// else
			result.message = BabytreeController.SystemExceptionMessage;
			result.status = BabytreeController.SystemExceptionCode;
			// TODO 去掉手动捕捉的异常信息
			// result.error = printException(ex, params, jsonString);
		}

		return result;

	}

}
