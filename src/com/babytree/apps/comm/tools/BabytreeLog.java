package com.babytree.apps.comm.tools;

import android.util.Log;

/**
 * 日志打印全局调用
 * 
 * @author wangshuaibo
 * 
 */
public final class BabytreeLog {

	private static final String TAG = "BabytreeTag";

	private BabytreeLog() {
	}

	/**
	 * 
	 * @author wangshuaibo
	 * @param bool
	 */
	public static void enableLog(boolean bool) {
		if (show != bool) {
			show = bool;
		}
	}

	private static boolean show = true;

	public static void d(String tag, String str) {
		if (show)
			Log.d(tag, str);
	}

	public static void e(String tag, String str) {
		if (show)
			Log.e(tag, str);
	}

	public static void i(String tag, String str) {
		if (show)
			Log.i(tag, str);
	}

	public static void v(String tag, String str) {
		if (show)
			Log.v(tag, str);
	}

	public static void d(String tag, String str, Throwable tr) {
		if (show)
			Log.d(tag, str, tr);
	}

	public static void e(String tag, String str, Throwable tr) {
		if (show)
			Log.e(tag, str, tr);
	}

	public static void i(String tag, String str, Throwable tr) {
		if (show)
			Log.i(tag, str, tr);
	}

	public static void v(String tag, String str, Throwable tr) {
		if (show)
			Log.v(tag, str, tr);
	}

	public static void d(String str) {
		if (show)
			Log.d(TAG, str);
	}

	public static void e(String str) {
		if (show)
			Log.e(TAG, str);
	}

	public static void i(String str) {
		if (show)
			Log.i(TAG, str);
	}

	public static void v(String str) {
		if (show)
			Log.v(TAG, str);
	}

	public static void d(String str, Throwable tr) {
		if (show)
			Log.d(TAG, str, tr);
	}

	public static void e(String str, Throwable tr) {
		if (show)
			Log.e(TAG, str, tr);
	}

	public static void i(String str, Throwable tr) {
		if (show)
			Log.i(TAG, str, tr);
	}

	public static void v(String str, Throwable tr) {
		if (show)
			Log.v(TAG, str, tr);
	}

}
