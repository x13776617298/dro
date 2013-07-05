package com.babytree.apps.comm.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

public class BaseUtil {

	private BaseUtil() {
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 返回当前程序版本号
	 */
	public static String getAppVersionCode(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return String.valueOf(pi.versionCode);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	/**
	 * 返回mac地址
	 */
	public static String getMacAddress(Context context) {
		String mac = "";
		try {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			mac = info.getMacAddress();
			if (mac == null || mac.equalsIgnoreCase("null")) {
				mac = "";
			}
			return mac;
		} catch (Exception e) {
			return mac;
		}
	}

	/**
	 * 网络状态判断
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;

	}

	/**
	 * 获取网络
	 * 
	 * @param context
	 * @return
	 */
	public static String getExtraInfo(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = connectivity.getActiveNetworkInfo();
		if (nwInfo == null) {
			return null;
		}
		Log.e("BabytreeTag", nwInfo.toString());
		String extraInfo = nwInfo.getExtraInfo();
		String typeName = nwInfo.getTypeName();
		if (typeName != null && typeName.equalsIgnoreCase("WIFI")) {
			return typeName;
		}
		return extraInfo;
	}

	/**
	 * 返回网络信息
	 * 
	 * @author wangshuaibo
	 * @param context
	 * @return
	 */
	public static String getNetType(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo nwInfo = connectivity.getActiveNetworkInfo();
			if (nwInfo == null) {
				return "unknow";
			}
			return nwInfo.toString();
		} catch (Exception ex) {
			return "unknow";
		}
	}

	/**
	 * 获取手机系统对应的SDK_INT
	 * 
	 * @return
	 */
	public static int getSDKINT() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 去除List重复内容
	 * 
	 * @param arlList
	 * @param list
	 * @return 返回去除重复之后的list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList removeDuplicate(ArrayList arlList, ArrayList list) {
		for (Object object : list) {
			if (!arlList.contains(object)) {
				arlList.add(object);
			}
		}
		return arlList;
	}

}
