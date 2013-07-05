package com.babytree.apps.comm.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Base implements Serializable {
	private static final long serialVersionUID = 1L;
	public int _id = 0;

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
	 * json取long值
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	protected static long getLong(JSONObject jsonObject, String key) {

		long tmp = 0L;
		try {
			tmp = jsonObject.getLong(key);
		} catch (JSONException e) {
			tmp = 0L;
		}
		return tmp;
	}
}
