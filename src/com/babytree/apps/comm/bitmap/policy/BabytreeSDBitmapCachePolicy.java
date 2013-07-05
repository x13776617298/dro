package com.babytree.apps.comm.bitmap.policy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;

import com.babytree.apps.comm.bitmap.core.BitmapCommonUtils;
import com.babytree.apps.comm.bitmap.policy.SDBitmapCacheConfig.CachePolicyConfig;

/**
 * 缓存清理策略
 * 
 * @author pengxh
 * 
 */
public class BabytreeSDBitmapCachePolicy {

	// 缓存目录
	private File cacheDir;
	// 删除缓存的对比时间
	private static long compareTimeMillis;
	// 执行删除缓存的的当前时间
	private long currTimeMillis;
	// 缓存文件总大小
	private long totalCacheSize = 0L;

	private HashMap<Long, File> fileListMap = new HashMap<Long, File>();;

	/**
	 * 缓存清理策略配置
	 */
	private SDBitmapCacheConfig.CachePolicyConfig policyConfig;

	private final ExecutorService executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	private final Callable<Void> cleanCacheCallable = new Callable<Void>() {

		@SuppressWarnings("deprecation")
		@Override
		public Void call() throws Exception {

			// 清理成功标识
			boolean issucc = false;

			// DO delete files
			switch (policyConfig) {
			case SIZE_LIMIT:
				// DO
				CachePolicyConfig.SIZE_LIMIT.initConfig();
				CachePolicyConfig.SIZE_LIMIT.logInfo();
				deleteContentsByCacheSize(cacheDir);
				break;
			case DATE_LIMIT:
				// DO
				CachePolicyConfig.DATE_LIMIT.initConfig();
				CachePolicyConfig.DATE_LIMIT.logInfo();
				compareTimeMillis = SDBitmapCacheConfig.getDeleteMillis();
				currTimeMillis = System.currentTimeMillis();
				deleteContentsByDays(cacheDir, currTimeMillis);
				break;
			// case DATE_SIZE_LIMIT:
			// CachePolicyConfig.DATE_SIZE_LIMIT.initConfig();
			// CachePolicyConfig.DATE_SIZE_LIMIT.logInfo();
			// compareTimeMillis = SDBitmapCacheConfig.getDeleteMillis();
			// currTimeMillis = System.currentTimeMillis();
			// // TODO
			// deleteContentsByDaysAndSize(cacheDir, currTimeMillis);
			// break;
			case ALLFILES:
				// DO
				CachePolicyConfig.ALLFILES.initConfig();
				CachePolicyConfig.ALLFILES.logInfo();
				issucc = deleteContents(cacheDir);
				break;
			case APP_CACHE:
				// DO
				break;
			default:
				break;
			}
			if (policyConfig.cacheClearListener != null) {
				if (issucc) {
					policyConfig.cacheClearListener.clearSuccess();
				} else {
					policyConfig.cacheClearListener.clearFail();
				}
			}
			return null;
		}
	};

	public BabytreeSDBitmapCachePolicy() {
		cacheDir = SDBitmapCacheConfig.getAPPDiskCacheDirectory();
	}

	public BabytreeSDBitmapCachePolicy(CachePolicyConfig policyConfig) {
		cacheDir = SDBitmapCacheConfig.getAPPDiskCacheDirectory();
		if (policyConfig == null) {
			this.policyConfig = CachePolicyConfig.SIZE_LIMIT;
		} else {
			this.policyConfig = policyConfig;
		}
	}

	/**
	 * 执行存储卡缓存清理
	 * 
	 * @return
	 */
	public void executeCachePolicy() {
		// Do Policy
		executorService.submit(cleanCacheCallable);
	}

	/**
	 * 删除指定目录下的文件
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private boolean deleteContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			throw new NullPointerException("目录不能为空");
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContents(file);
			} else {
				if (!file.delete()) {
					throw new IOException("failed to delete file: " + file);
				} else {
				}
			}
		}
		return true;
	}

	/**
	 * 删除文件(时间+空间)
	 */
	private void deleteContentsByDaysAndSize(File dir, long currentTimeMillis) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
			// throw new IllegalArgumentException("not a directory: " + dir);
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContentsByDays(file, currentTimeMillis);
			} else {
				// 当缓存文件最近使用和当前时间的时间间隔大于配置的时间限制，则删除缓存文件
				if ((currentTimeMillis - file.lastModified()) > compareTimeMillis) {
					if (!file.delete()) {
						throw new IOException("failed to delete file: " + file);
					} else {
					}
				}
			}
		}
	}

	/**
	 * 删除文件(时间)
	 */
	private void deleteContentsByDays(File dir, long currentTimeMillis) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
			// throw new IllegalArgumentException("not a directory: " + dir);
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContentsByDays(file, currentTimeMillis);
			} else {
				// 当缓存文件最近使用和当前时间的时间间隔大于配置的时间限制，则删除缓存文件
				if ((currentTimeMillis - file.lastModified()) > compareTimeMillis) {
					if (!file.delete()) {
						throw new IOException("failed to delete file: " + file);
					} else {
					}
				}
			}
		}
	}

	/**
	 * 删除文件(空间)
	 */
	private void deleteContentsByCacheSize(File dir) throws IOException {
		// 扫描所有缓存文件
		scanCacheFiles(dir);
		// 计算缓存总大小
		calcCacheSize(fileListMap);
		long d = BitmapCommonUtils.getUsableSpace(SDBitmapCacheConfig.getDiskRootDirectory())
				- SDBitmapCacheConfig.getMINFreeSpace();
		boolean useFreeSpace = (d < 0) ? true : false;
		// 删除文件
		while (totalCacheSize > SDBitmapCacheConfig.getCacheSize() || useFreeSpace) {
			doDeleteSizeLimit();
			calcCacheSize(fileListMap);
		}
		totalCacheSize = 0L;// 重置总大小值
	}

	/**
	 * 删除文件(APP内部空间)
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private void deleteContentsByInnerCacheSize(File dir, Context context) throws IOException, SecurityException,
			NoSuchMethodException {
		// context.getc
		PackageManager pm = context.getPackageManager();
		Class[] arrayOfClass = new Class[2];
		Class localClass2 = Long.TYPE;
		arrayOfClass[0] = localClass2;
		// arrayOfClass[1] = IPackageDataObserver.class;
		Method localMethod = pm.getClass().getMethod("freeStorageAndNotify", arrayOfClass);
		Long localLong = Long.valueOf(getEnvironmentSize() - 1L);
		Object[] arrayOfObject = new Object[2];
		arrayOfObject[0] = localLong;
		// // localMethod.invoke(pm,localLong,new IPackageDataObserver.Stub(){
		// public void onRemoveCompleted(String packageName,boolean succeeded)
		// throws RemoteException {
		// }});
	}

	private static long getEnvironmentSize() {
		File localFile = Environment.getDataDirectory();
		long l1;
		if (localFile == null)
			l1 = 0L;
		while (true) {

			String str = localFile.getPath();
			StatFs localStatFs = new StatFs(str);
			long l2 = localStatFs.getBlockSize();
			l1 = localStatFs.getBlockCount() * l2;
			return l1;
		}
	}

	private void doDeleteSizeLimit() throws IOException {
		int count = fileListMap.size();
		if (count > 0) {

			Object[] keys = fileListMap.keySet().toArray();
			Arrays.sort(keys, new Comparator<Object>() {

				@Override
				public int compare(Object lhs, Object rhs) {
					// 倒序排序
					return (int) ((Long) lhs - (Long) rhs);
				}
			});

			// 删除20%文件
			int num = count / 5;
			for (int i = 0; i <= num; i++) {
				File file = fileListMap.get(keys[i]);
				if (file.delete()) {
					fileListMap.remove(keys[i]);
				} else {
					throw new IOException("failed to delete file: " + file);
				}
			}

		}
	}

	/**
	 * 扫描缓存文件
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public HashMap<Long, File> scanCacheFiles(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			return null;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				scanCacheFiles(file);
			} else {
				fileListMap.put(file.lastModified() + System.currentTimeMillis(), file);
			}

		}
		return fileListMap;
	}

	/**
	 * 计算缓存大小
	 * 
	 */
	public long calcCacheSize(Map<Long, File> map) {
		long tmpSize = 0L;
		Set<Long> keys = map.keySet();
		for (Long key : keys) {
			File file = map.get(key);
			tmpSize += file.length();
		}
		totalCacheSize = tmpSize;
		return totalCacheSize;
	}

	/**
	 * 缓存清理结果监听
	 * 
	 * @author pengxh
	 * 
	 */
	public interface CacheClearListener {

		/**
		 * 清理成功
		 * 
		 * @return
		 */
		void clearSuccess();

		/**
		 * 清理失败
		 * 
		 * @return
		 */
		void clearFail();

	}
}
