package com.babytree.apps.comm.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件夹操作
 * 
 */
public class FolderOper {

	public static boolean createSDCardDir(String dirPath) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + dirPath;
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
				return true;
			} else {
				return true;
			}

		} else {
			return false;
		}

	}

	/**
	 * 使用mkdirs创建多级文件夹 eg .
	 * 
	 * @param folderName
	 * @return
	 * @throws Exception
	 */
	public static boolean createMultiFolder(File folderdir) throws Exception {
		boolean bol = false;
		try {
			if (!folderdir.isDirectory()) {
				folderdir.mkdirs(); // mkdir只创建一个文件夹，并且父目录必需存在
				bol = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return bol;
	}

	/**
	 * 判断手机是否存在sd卡,并且有读写权限
	 * 
	 * @return
	 */
	public static boolean isExistSdcard(Context context) {
		boolean flag = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			flag = true;
		} else {

		}
		return flag;
	}

	public static String SdcardDir(Context context) {
		/**
		 * Environment.getExternalStorageState()方法用于获取SDCard的状态，
		 * Environment.MEDIA_MOUNTED 存在SDCard，并具有可读可写权限
		 */
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

}
