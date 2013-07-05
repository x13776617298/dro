package com.babytree.apps.comm.bitmap.policy;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.babytree.apps.comm.bitmap.core.BitmapCommonUtils;
import com.babytree.apps.comm.bitmap.core.BitmapProcess;
import com.babytree.apps.comm.bitmap.policy.BabytreeSDBitmapCachePolicy.CacheClearListener;

/**
 * 缓存清理策略配置
 * 
 * @author pengxh
 * 
 */
public abstract class SDBitmapCacheConfig {
	/**
	 * 缓存清理策略类型
	 * 
	 */
	public enum CachePolicyConfig {
		/**
		 * 缓存空间限制
		 */
		SIZE_LIMIT {

			@Override
			void logInfo() {
				log("空间限制");
				if (debug) {
					Log.d(TAG, "Config info -- cacheSize = " + this.cacheSize + "MB -> " + CACHE_SIZE + "B");
				}
			}

			@Override
			void initConfig() throws Exception {
				CACHE_SIZE = (long) this.cacheSize * 1024 * 1024;
				BitmapProcess.DEFAULT_CACHE_SIZE = (int) CACHE_SIZE;
			}

		},
		/**
		 * 缓存时间限制
		 */
		DATE_LIMIT {

			@Override
			void logInfo() {
				log("时间限制");
				if (debug) {
					Log.d(TAG, "Config info -- days = " + this.days + "天 ");
				}
			}

			@Override
			void initConfig() throws Exception {
				DAYS_REMAIN = (long) days * 24 * 3600 * 1000;
			}
		},
		/**
		 * 缓存时间+空间同时限制
		 */
		// DATE_SIZE_LIMIT {
		//
		// @Override
		// void logInfo() {
		// log("时间+空间限制");
		// if (debug) {
		// Log.d(TAG, "Config info -- date&days = " + this.days +
		// "天 , cacheSize = " + this.cacheSize + "MB -> " + CACHE_SIZE + "B");
		// }
		// }
		//
		// @Override
		// void initConfig() throws Exception {
		// DATE_LIMIT.initConfig();
		// SIZE_LIMIT.initConfig();
		// }
		// },
		/**
		 * 无限制,删除所有缓存文件
		 */
		ALLFILES {
			@Override
			void logInfo() {
				log("无限制,删除所有文件");
			}

			@Override
			void initConfig() throws Exception {
				// TODO Auto-generated method stub

			}

		},
		/**
		 * 应用内部缓存(无作用，不建议使用)
		 */
		@Deprecated
		APP_CACHE {

			@Override
			void logInfo() {
				// log("未检测到SD卡,执行app内部缓存");
			}

			@Override
			void initConfig() throws Exception {
				CACHE_SIZE = (long) this.innerCacheSize * 1024 * 1024;
			}
		};

		abstract void logInfo();

		/**
		 * 初始化配置信息
		 * 
		 * @throws Exception
		 */
		abstract void initConfig() throws Exception;

		public boolean debug = true;

		/**
		 * 清理缓存监听
		 */
		public CacheClearListener cacheClearListener = null;

		/**
		 * 天数
		 */
		public int days = 15;

		/**
		 * 缓存大小 MB
		 */
		public int cacheSize = 100;// 100MB

		/**
		 * 内部缓存大小 MB
		 */
		protected int innerCacheSize = 10;// 10MB
	}

	private static String TAG = SDBitmapCacheConfig.class.getSimpleName();
	public static final File SDRootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
	/**
	 * 缓存目录
	 */
	private static File babytreeCacheDir;

	/**
	 * 默认的缓存空间大小
	 */
	private static long CACHE_SIZE; // 100MB

	/**
	 * 默认的缓存有效天数
	 */
	private static long DAYS_REMAIN; // 15 days ago

	private static long MIN_FREE_SPACE = (long) 200 * 1024 * 1024; // 200MB

	// 调试日志开关
	public static boolean DEBUG = false;

	public static File getDiskRootDirectory() {
		return SDRootDir;
	}

	public static long getMINFreeSpace() {
		return MIN_FREE_SPACE;
	}

	public static File getAPPDiskCacheDirectory() {
		if (DEBUG && babytreeCacheDir != null) {
			Log.d(TAG, "当前使用的缓存目录为 " + babytreeCacheDir);
		}
		return babytreeCacheDir;
	}

	public static void setAPPDiskCacheDirectory(Context context) {
		babytreeCacheDir = BitmapCommonUtils.getDiskCacheDir(context, "");

	}

	public static long getCacheSize() {
		return CACHE_SIZE;
	}

	public static long getDeleteMillis() {
		return DAYS_REMAIN;
	}

	private static void log(String policyTag) {
		if (DEBUG) {
			Log.d(TAG, "执行策略 -- " + policyTag);
		}
	}

}
