package com.babytree.apps.comm.service;

import android.app.Application;

import com.babytree.apps.comm.bitmap.BabytreeBitmapCache;
import com.babytree.apps.comm.bitmap.policy.SDBitmapCacheConfig;
import com.babytree.apps.comm.bitmap.policy.SDBitmapCacheConfig.CachePolicyConfig;
import com.babytree.apps.comm.net.BabytreeHttp;
import com.babytree.apps.comm.tools.BabytreeLog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 公共Application<br>
 * 包含:<br>
 * 1.百度定位
 * 
 * @author wangshuaibo
 * 
 */
public class BabytreeApplication extends Application {

	/**
	 * 百度定位监听
	 */
	private BDLocationListener mBdLocationListener;

	/**
	 * 应用ID
	 */
	private String mAppId = "";

	/**
	 * Umeng分享
	 */
	private UMSocialService umSocialService;

	/**
	 * 百度定位服务
	 */
	protected LocationClient mLocationClient;

	/**
	 * 如果子类重写此方法,一定要调用super.onCreate();
	 */
	@Override
	public void onCreate() {
		BabytreeLog.d("Application onCreate.");

		super.onCreate();

		BabytreeLog.d("Application setContext");
		BabytreeHttp.setContext(this);

		// 百度定位
		try {
			mLocationClient = new LocationClient(this);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true); // 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
			option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
			option.setProdName(mAppId); // 设置产品线名称
			option.setScanSpan(5000); // 定时定位，每隔5秒钟定位一次。
			option.disableCache(true);// 禁止启用缓存定位
			option.setPoiNumber(5);// 最多返回POI个数
			option.setPoiDistance(1000);// poi查询距离
			option.setPoiExtraInfo(true);// 是否需要POI的电话和地址等详细信息
			mLocationClient.setLocOption(option);
			if (mBdLocationListener == null)
				mLocationClient.registerLocationListener(new MLocationListenner());
			else
				mLocationClient.registerLocationListener(mBdLocationListener);
			mLocationClient.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// 友盟分享相关
		umSocialService = UMServiceFactory.getUMSocialService("Android", RequestType.SOCIAL);
		// 设置可分享平台(默认支持腾讯微薄,新浪微薄,人人网)
		SocializeConfig config = new SocializeConfig();
		config.setPlatforms(new SHARE_MEDIA[] { SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.RENREN });
		umSocialService.setGlobalConfig(config);

		// 图片缓存相关
		// 设置调试参数
		CachePolicyConfig policyConfig = CachePolicyConfig.SIZE_LIMIT;
		policyConfig.debug = false;
		SDBitmapCacheConfig.DEBUG = false;
		// 启用缓存清理
		BabytreeBitmapCache.startClearer(getApplicationContext(), policyConfig);

	}

	/**
	 * 设置百度定位监听
	 * @author wangshuaibo
	 * @param listener
	 */
	protected void setBDLocationListener(BDLocationListener listener) {
		mBdLocationListener = listener;
	}

	/**
	 * 设置友盟分享配置信息
	 * 
	 * @author wangshuaibo
	 * @param config
	 */
	public void setUmengSocialConfig(SocializeConfig config) {
		if (config != null) {
			umSocialService.setGlobalConfig(config);
		}
	}

	/**
	 * 获取友盟分享服务
	 * 
	 * @author wangshuaibo
	 * @return
	 */
	public UMSocialService getUmSocialService() {
		return umSocialService;
	}

	/**
	 * 如果子类重写此方法,一定要调用super.onTerminate();
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		BabytreeLog.d("Application onTerminate.");

		// 百度定位
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}

	}

	/**
	 * 如果子类重写此方法,一定要调用super.onTerminate();
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	/**
	 * 设置AppId,需要在程序启动的时候调用,super.onCreate之前<Br>
	 * 同时设置接口请求需要的Appid
	 * 
	 * @author wangshuaibo
	 * @param appId
	 * @throws Exception
	 */
	protected void setAppId(String appId) {
		mAppId = appId;
		BabytreeHttp.setAppId(mAppId);
		// 设置appId
		BabytreeLog.d("Application setAppId.");
	}

	private class MLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			BabytreeLog.d("Request location success.");
			BabytreeLog.d("The location info latitude " + location.getLatitude() + " longitude "
					+ location.getLongitude() + ".");
			BabytreeHttp.setLocation(location.getLatitude(), location.getLongitude());
			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.stop();
				mLocationClient = null;
			}

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

}
