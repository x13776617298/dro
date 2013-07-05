package com.babytree.apps.comm.tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * json解析器方法类
 * 
 * @author wangbingqi
 * 
 */
public class JsonParserTolls {
	/**
	 * 解析json并且取一个非空的字符串
	 * 
	 * @param jsonObj
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static String getStr(JSONObject jsonObj, String key) {
		String s = "";
		if (jsonObj.has(key)) {
			try {
				s = jsonObj.getString(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return s;
	}

	/**
	 * 解析json并且取一个默认设置的字符串
	 * 
	 * @param jsonObj
	 * @param key
	 * @param defaultStr
	 *            默认的
	 * @return
	 */
	public static String getStr(JSONObject jsonObj, String key, String defaultStr) {
		String s = defaultStr;
		if (jsonObj.has(key)) {
			try {
				s = jsonObj.getString(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return s;
	}

	/**
	 * 解析json
	 * 
	 * @param jsonObj
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static String getStrWithE(JSONObject jsonObj, String key) throws JSONException {
		if (jsonObj.has(key)) {
			return jsonObj.getString(key);
		} else
			return "";
	}

	/**
	 * 获取jsonObj
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static JSONObject getJsonObj(JSONObject json, String key) {
		JSONObject jsonObject = null;
		if (json.has(key)) {
			try {
				jsonObject = json.getJSONObject(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonObject;
	}

	/**
	 * 获取一个json数组
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static JSONArray getJsonArray(JSONObject json, String key) {
		JSONArray jsonArray = null;
		if (json.has(key)) {
			try {
				jsonArray = json.getJSONArray(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	/**
	 * 获取Json
	 * 
	 * @param jsonarray
	 * @param i
	 * @return
	 */
	public static JSONObject getJsonObj(JSONArray jsonarray, int i) {
		JSONObject jsonObject = null;
		try {
			jsonObject = jsonarray.getJSONObject(i);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;

	}

	/**
	 * 获取json的int值
	 * 
	 * @param jsonObj
	 * @param key
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public static int getInt(JSONObject jsonObj, String key, int defValue) {
		int i = defValue;
		if (jsonObj.has(key)) {
			try {
				i = jsonObj.getInt(key);
			} catch (JSONException e) {
				i = defValue;
			}
		}
		return i;
	}
	/**
	 * 获取json的long值
	 * 
	 * @param jsonObj
	 * @param key
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public static long getLong(JSONObject jsonObj, String key, long defValue) {
		long i = defValue;
		if (jsonObj.has(key)) {
			try {
				i = jsonObj.getLong(key);
			} catch (JSONException e) {
				i = defValue;
			}
		}
		return i;
	}


}
