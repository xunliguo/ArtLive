package com.example.rh.artlive.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.duanqu.qupai.jni.ApplicationGlue;
import com.example.rh.artlive.R;
import com.example.rh.artlive.db.EaseUser;
import com.example.rh.artlive.db.Myinfo;
import com.example.rh.artlive.db.UserDao;
import com.example.rh.artlive.live.CommonUtil;
import com.example.rh.artlive.live.FakeServer;
import com.example.rh.artlive.live.LiveKit;
import com.example.rh.artlive.util.Constant;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.TFileUtils;
import com.example.rh.artlive.widget.ResourcesManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.mob.MobApplication;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DemoApplication extends MobApplication {

	private static Context mContext;
	private static Handler mHandler;
	private static Thread mMainThread;
	private static int mMainThreadId;
	public static final String WX_APPID = "wx33715f77a6554167";
	private IWXAPI api;

	public static Context applicationContext;
	private static DemoApplication instance;
	private String username = "";
	private Map<String, EaseUser> contactList;

	private static Context context;
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	//直播聊天室

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		mContext = getApplicationContext();
		mHandler = new Handler();
		mMainThread = Thread.currentThread();
		mMainThreadId = android.os.Process.myTid();
		instance = this;

		//检测内存泄漏
//		if (LeakCanary.isInAnalyzerProcess(this)) {
//		}else {
//			LeakCanary.install(this);
//		}
		//检测内存泄漏结束

		applicationContext = this;
		instance = this;
		context = this;
		//直播聊天室结束
		LiveKit.init(context, FakeServer.getAppKey());
		System.loadLibrary("gnustl_shared");
//        System.loadLibrary("ijkffmpeg");//目前使用微博的ijkffmpeg会出现1K再换wifi不重连的情况
		System.loadLibrary("qupai-media-thirdparty");
//        System.loadLibrary("alivc-media-jni");
		System.loadLibrary("qupai-media-jni");
		ApplicationGlue.initialize(this);
		//直播聊天室结束


		//三方登录分享
		ResourcesManager.getInstace(MobSDK.getContext());
		MobSDK.init(this);
		//三方登录分享结束

		api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
		api.registerApp(WX_APPID);
		//微信授权

		// 初始化环信sdk
		init(applicationContext);
		// 初始化环信sdk结束

		//加载图片放大预览
		DisplayImageOptions defaultOptions = new DisplayImageOptions
				.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)//缓存一百张图片
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
		//加载图片放大预览结束
	}


	//每次进入app，遍历assets目录下所有的文件，是否在data/data目录下都已经存在，不存在则拷贝
	private void initAssetsFile() {

		boolean needCopy = false;

		// 创建data/data目录
		File file = getApplicationContext().getFilesDir();
		String path = file.toString() + "/arm64-v8a/";

		// 遍历assets目录下所有的文件，是否在data/data目录下都已经存在
		try {
			String[] fileNames = getApplicationContext().getAssets().list("arm64-v8a");
			for (int i = 0; fileNames != null && i < fileNames.length; i++) {
				if (!TFileUtils.isFileExists(path + fileNames[i])) {
					needCopy = true;
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (needCopy) {
			CommonUtil.copyFilesFassets(getApplicationContext(), "arm64-v8a", path);
			Log.e("文件");
		}
	}





	/** 获取Handler对象 */
	public static Handler getHandler() {
		return mHandler;
	}

	/** 获取主线程对象 */
	public static Thread getMainThread() {
		return mMainThread;
	}

	/** 获取主线程ID */
	public static int getMainThreadId() {
		return mMainThreadId;
	}


	public static Context getContext() {
		return context;
	}

	public static DemoApplication getInstance() {
		return instance;
	}

	/*
	 * 第一步：sdk的一些参数配置 EMOptions 第二步：将配置参数封装类 传入SDK初始化
	 */

	public void init(Context context) {
		// 第一步
		EMOptions options = initChatOptions();
		// 第二步
		boolean success = initSDK(context, options);
		if (success) {
			// 设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
			EMClient.getInstance().setDebugMode(true);
 			// 初始化数据库
			initDbDao(context);
		}
	}

	private void initDbDao(Context context) {
		userDao = new UserDao(context);
	}

	public void setCurrentUserName(String username) {
		this.username = username;
		Myinfo.getInstance(instance).setUserInfo(Constant.KEY_USERNAME, username);
	}

	public String getCurrentUserName() {
		if (TextUtils.isEmpty(username)) {
			username = Myinfo.getInstance(instance).getUserInfo(Constant.KEY_USERNAME);
		}
		return username;
	}

	private UserDao userDao;

	private EMOptions initChatOptions() {

		// 获取到EMChatOptions对象
		EMOptions options = new EMOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(true);
		// 设置是否需要已读回执
		options.setRequireAck(true);
		// 设置是否需要已送达回执
		options.setRequireDeliveryAck(false);
		return options;
	}

	private boolean sdkInited = false;

	public synchronized boolean initSDK(Context context, EMOptions options) {
		if (sdkInited) {
			return true;
		}

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);

		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回
		if (processAppName == null || !processAppName.equalsIgnoreCase(applicationContext.getPackageName())) {
			// 则此application::onCreate 是被service 调用的，直接返回
			return false;
		}
		if (options == null) {
			EMClient.getInstance().init(context, initChatOptions());
		} else {
			EMClient.getInstance().init(context, options);
		}
		sdkInited = true;
		return true;
	}



	/**
	 * check the application process name if process name is not qualified, then
	 * we think it is a service process and we will not init SDK
	 * 
	 * @param pID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {

					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	public void setContactList(Map<String, EaseUser> contactList) {

		this.contactList = contactList;

		userDao.saveContactList(new ArrayList<EaseUser>(contactList.values()));

	}

	public Map<String, EaseUser> getContactList() {

		if (contactList == null) {

			contactList = userDao.getContactList();

		}
		return contactList;

	}

	/**
	 * 退出登录
	 * 
	 * @param unbindDeviceToken
	 *            是否解绑设备token(使用GCM才有)
	 * @param callback
	 *            callback
	 */
	public void logout(boolean unbindDeviceToken, final EMCallBack callback) {

		EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

			@Override
			public void onSuccess() {

				if (callback != null) {
					callback.onSuccess();
				}

			}

			@Override
			public void onProgress(int progress, String status) {
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

			@Override
			public void onError(int code, String error) {
				if (callback != null) {
					callback.onError(code, error);
				}
			}
		});
	}
}
