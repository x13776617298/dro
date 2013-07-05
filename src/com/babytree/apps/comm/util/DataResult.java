package com.babytree.apps.comm.util;

public class DataResult {
	public int status = -1;// 成功0
	public String message = "";// 提示信息
	public String error = "";// 异常
	public int totalSize = 0;// 列表总数
	public Object data = null;// list
	public long lastTimestamp = 0;// 暂时不用
}
