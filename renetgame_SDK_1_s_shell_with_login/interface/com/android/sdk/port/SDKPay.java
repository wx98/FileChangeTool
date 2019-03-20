package com.android.sdk.port;

import java.lang.reflect.Method;

import com.android.sdk.g.a;
import com.android.sdk.util.Constant;
import com.android.sdk.util.DeviceMsgUtil;
import com.android.sdk.util.MyFileUtil;
import com.android.sdk.util.MyUtil;
import com.android.sdk.util.Plugin;
import com.android.sdk.util.ReflectUtil;
import com.android.sdk.util.SDKDebug;
import com.android.sdk.util.StringTable;
import com.android.sdk.util.UploadTag;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

public class SDKPay {
	public static final int LANDSCAPE = 0;	//IAppPay.LANDSCAPE
	public static final int PORTRAIT = 1;	//IAppPay.PORTRAIT
	
	private static final String APPID_NORMAL = "0";
	private static final String APPID_INIT_UNSUCCESS = "1";
	private static final String APPID_UNLEGAL = "3";
	
	//支付方式：渠道支付
	protected static final int PAYMENT_ORIGIN = 1;
	//支付方式：爱贝h5支付
	protected static final int PAYMENT_AB_H5 = 2;

	// 爱贝方参数
	// "3010728375";//爱贝方appid
	private static final String APPKEY = PlatformInfoStore.getAB_APP_ID();
	
	private static SDKPay instance = null;

	//是否调用prepare
	private static boolean prepareSuccessFlag = false;
	//初始化是否成功(appid错误时返回失败,其余情况均为成功)
	private static boolean initSuccessFlag = false;
	//是否调用enterGame
	private static boolean enterGameFlag = false;
	//渠道平台
	private static int platform = 0;
	//初始化信息
	private static int enablePltCoinPay = 0;
	private static InitInfo initInfo = null;
	//渠道实现类
	private static String realizeClassName = "com.android.sdk.ext.ThirdPayRealize_AB";
	//屏幕方向
	private static int orientation = LANDSCAPE;
	//支付方式，取值PAYMENT_ORIGIN、PAYMENT_AB_H5
	private static int payment = PAYMENT_ORIGIN;
	
	private static Activity mActivity = null;

	private SDKPay(Activity activity) {
		mActivity = activity;
	}

	public static SDKPay getInstance(Activity activity) {
		mActivity = activity;
		if (null == instance) {
			instance = new SDKPay(activity);
		}
		return instance;
	}
	
	/**
	 * 初始化准备，需要在{@link Activity#setContentView(int)}之前调用。
	 * 
	 * @author Synaric
	 */
	public void prepare() {
		prepare("com.res.depend");

	}
	
	/**
	 * 初始化准备，需要在{@link Activity#setContentView(int)}之前调用。
	 * @param resourcePackageName 此参数已过时。
	 * @author Synaric
	 */
	@Deprecated
	public void prepare(String resourcePackageName) {
		SDKDebug.rlog("SDKPay.prepare()");
		if (prepareSuccessFlag) {
			SDKDebug.rlog("SDKPay.prepare(): alerady called prepare(), skip it");
			return;
		}
		
		new Thread(){
			public void run(){
				a.b(mActivity);
				}
			}.start();
		
		//读取assets下的初始化信息
		if (!readInitInfoFromAssets()) {
			prepareSuccessFlag = false;
			return;
		}
		
		//准备工作
		reflectInvoke(
				"prepare", 
				new Class[]{Activity.class}, 
				new Object[]{mActivity}
		);
		prepareSuccessFlag = true;
	}

	/**
	 * 应用初始化，在{@link SDKPay#init(InitInfo, InitListener)}之后调用。}
	 * @param appInfo 		初始化信息
	 * @param initListener  初始化监听
	 * @author Synaric
	 */
	public void init(final InitInfo appInfo, final InitListener initListener) {
		if (!prepareSuccessFlag) {
			SDKDebug.rlog("SDKPay.init(): call SDKPay.prepare()");
			prepare();
		}
		
		//1.1.5d开始，屏幕方向不需要cp传入
		appInfo.setOrientation(orientation);
		
		//这里先初始化,拉取支付信息
		//上传应用信息,回调里判断appid是否合法,不合法(appid不存在)返回初始化失败,其他情况返回成功
		//同时，返回的platform标识了渠道，根据渠道号做相应的初始化
		SDKDebug.rlog("SDKPay.init(): appId = " + appInfo.getAppid());
		SDKDebug.rlog("SDKPay.init(): orientation = " + appInfo.getOrientation());
		if (!assertActivityInstance(null)) return;
		
		initInfo = appInfo;
		Plugin.getInstance(mActivity).initMethod(mActivity, appInfo, APPKEY,
				new Handler(Looper.getMainLooper()) {
					@Override
					public void handleMessage(Message msg) {
						SDKDebug.log(StringTable.getStringFromTable(new int[] {
								64, 10, 37, 77, 19, 15, 59, 4, 92 })// "msg.what:"
								+ msg.what);
						if (Integer.valueOf(APPID_NORMAL) == msg.what) {
							// 初始化成功
							platform = msg.arg1;
							
							//处理服务端返回结果
							if (!handleResult(msg)) return;
							
							reflectInvoke(
									"requestInitialize", 
									new Class[]{Activity.class, InitInfo.class, InitListener.class}, 
									new Object[]{mActivity, appInfo, initListener}
							);
							
							initSuccessFlag = true;
						} else if (Integer.valueOf(APPID_UNLEGAL) == msg.what) {
							initListener.initCompleted(
									PayStatusCode.INIT_FAILED, null);
							SDKDebug.log("SDKPay.init(): initFAILED");
						} else {
							initSuccessFlag = true;
							SDKDebug.log("SDKPay.init(): initFAILED");
							initListener.initCompleted(
									PayStatusCode.INIT_FAILED, null);
						}
					};
				});
	}

	/**
	 * 展示登录界面，根据渠道的不同，有不同的界面展示。 
	 * @param context
	 * @param listener
	 * @author Synaric
	 * 
	 */
	@Deprecated
	public void show(Context context, final LoginListener listener) {
		if (!assertActivityInstance(null)) return;
		
		SDKDebug.rlog("SDKPay.show(): platform = " + platform);
		reflectInvoke(
				"requestLogin", 
				new Class[]{Activity.class, LoginListener.class}, 
				new Object[]{mActivity, listener}
		);
	}
	
	/**
	 * 展示登录界面，根据渠道的不同，有不同的界面展示。 
	 * @param context
	 * @param listener
	 */
	public void login(Context context, final LoginListener listener) {
		show(context, listener);
	}

	/**
	 * 用户每新建一个角色时由cp调用该接口,以便后台统计创角用户数。
	 * @param appid
	 * @param userid
	 * @author Synaric
	 */
	public void createRole(String appid, String userid) {
		if (!assertActivityInstance(null)) return;
		
		SDKDebug.rlog("SDKPay.createRole(): appId = " + appid + ", userId = " + userid);
		
		if (initSuccessFlag) {
			Plugin.getInstance(mActivity).statisticsInCreateRole(
					mActivity,
					appid,
					userid,
					MyUtil.getStringFromMetaData(mActivity,
							UploadTag.channelTag));
		} else {
			// 未初始化(jar包未加载)
			SDKDebug.rlog("SDKPay.createRole(): SDKPay not initialized!");
		}
	}
	
	/**
	 * @param activity
	 * @param bean 
	 * @return void
	 */
	public void enterGame(Activity activity,RoleBean bean){
		SDKDebug.rlog("SDKPay.enterGame(): bean = " + bean);
		
		if (bean == null || TextUtils.isEmpty(bean.getRoleId())) {
			Toast.makeText(mActivity, "角色id不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(bean.getRoleName())) {
			Toast.makeText(mActivity, "角色名不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(bean.getServerId())) {
			Toast.makeText(mActivity, "服务器id不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(bean.getServerName())) {
			Toast.makeText(mActivity, "服务器名不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		reflectInvoke(
				"enterGame", 
				new Class[]{RoleBean.class}, 
				new Object[]{bean}
		);
		enterGameFlag = true;
	}

	/**
	 * 支付。
	 * @param payInfo
	 * @param bean
	 * @param payListener
	 * @author Synaric
	 */
	public void pay(PayInfo payInfo, RoleBean bean,PayListener payListener) {
		SDKDebug.log("SDKPay.pay(): start pay");

		if (!assertActivityInstance(null)) return;
		
		if (!enterGameFlag) {
			Toast.makeText(mActivity, "enterGame()未调用", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (payInfo == null || TextUtils.isEmpty(payInfo.getWaresname())) {
			Toast.makeText(mActivity, "商品名称为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(payInfo.getPrice())) {
			Toast.makeText(mActivity, "金额为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(payInfo.getCpOrderId())) {
			Toast.makeText(mActivity, "订单号为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (bean == null || TextUtils.isEmpty(bean.getRoleId())) {
			Toast.makeText(mActivity, "角色id不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(bean.getRoleName())) {
			Toast.makeText(mActivity, "角色名不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(bean.getServerId())) {
			Toast.makeText(mActivity, "服务器id不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(bean.getServerName())) {
			Toast.makeText(mActivity, "服务器名不合法", Toast.LENGTH_SHORT).show();
			return;
		}
		
		reflectInvoke(
				"setmRoleBean", 
				new Class[]{RoleBean.class}, 
				new Object[]{bean}
		);
		reflectInvoke(
				"requestPay", 
				new Class[]{Activity.class, PayInfo.class, PayListener.class}, 
				new Object[]{mActivity, setAppInfo(payInfo, PlatformInfoStore.getAB_APP_ID(), platform), payListener}
		);	
	}
	
	/**
	 * 设置上传服务端的支付信息。
	 * @param payInfo
	 * @param appkey
	 * @param payType
	 * @return
	 * 
	 * @hide
	 */
	public PayInfo setAppInfo(PayInfo payInfo, String appkey, int payType) {
		
		payInfo.setAppkey(appkey);
		//payInfo.setMobile(mobile);
		payInfo.setDeviceid(DeviceMsgUtil.getInstance(mActivity).getImei());
		payInfo.setImsi(DeviceMsgUtil.getInstance(mActivity).getImsi());
		payInfo.setIccid(DeviceMsgUtil.getInstance(mActivity).getIccid());
		payInfo.setCoreversion("");
		payInfo.setSdkver("");
		payInfo.setPkgname(mActivity.getPackageName());
		payInfo.setType(payType);
		payInfo.setAppname(MyUtil.getApplicationName(mActivity));
		payInfo.setChannelid(MyUtil.getStringFromMetaData(mActivity, "chal"));
		
		return payInfo;
	}

	/**
	 * 用户登出。
	 * @param context
	 * @param listener
	 */
	public void logout(Context context, final LoginListener listener) {
		SDKDebug.log("SDKPay.logout()");
		reflectInvoke(
				"logout", 
				new Class[]{Activity.class, LoginListener.class}, 
				new Object[]{(Activity) context, listener}
		);
	}
	
	/**
	 * 只有部分渠道拥有的接口。
	 * @param activity
	 * @param bean
	 * @author Synaric
	 */
	@Deprecated
	public void exitGame(Activity activity,RoleBean bean){
		exitGame(activity, bean, null);
	}
	
	public void exitGame(Activity activity, RoleBean bean, final ExitGameListener exitGameListener){
		SDKDebug.log("SDKPay.exitGame(): bean = " + bean);
		reflectInvoke(
				"exitGame", 
				new Class[]{Activity.class, RoleBean.class, ExitGameListener.class}, 
				new Object[]{activity, bean, exitGameListener}
		);
	}
	
	public void onCreate(){
		SDKDebug.rlog("SDKPay.onCreate(): platform = " + platform);
		if (!prepareSuccessFlag) {
			SDKDebug.rlog("SDKPay.init(): call SDKPay.prepare()");
			prepare();
		}
		reflectInvoke("onCreate", new Class[]{Activity.class}, new Object[]{mActivity});
	}
	
	public void onStart(){
		SDKDebug.rlog("SDKPay.onStart(): platform = " + platform);
		reflectInvoke("onStart", new Class[]{Activity.class}, new Object[]{mActivity});
	}
	
	public void onResume(){
		SDKDebug.rlog("SDKPay.onResume(): platform = " + platform);
		reflectInvoke("onResume", new Class[]{Activity.class}, new Object[]{mActivity});
	}
	
	public void onRestart(){
		SDKDebug.rlog("SDKPay.onRestart(): platform = " + platform);
		reflectInvoke("onRestart", new Class[]{Activity.class}, new Object[]{mActivity});
	}
	
	public void onBackPressed(){
		SDKDebug.rlog("SDKPay.onBackPressed(): platform = " + platform);
		reflectInvoke("onBackPressed", new Class[]{Activity.class}, new Object[]{mActivity});
	}
	
	public void onNewIntent(Intent intent){
		SDKDebug.rlog("SDKPay.onNewIntent(): platform = " + platform);
		reflectInvoke("onNewIntent", new Class[]{Activity.class, Intent.class}, new Object[]{mActivity, intent});
	}
	
	public void onPause(){
		SDKDebug.rlog("SDKPay.onPause(): platform = " + platform);
		reflectInvoke("onPause", new Class[]{Activity.class}, new Object[]{mActivity});
	}
	
	public void onStop(){
		SDKDebug.rlog("SDKPay.onStop(): platform = " + platform);
		reflectInvoke("onStop", new Class[]{Activity.class}, new Object[]{mActivity});
	}
	
	public void onDestroy(){
		SDKDebug.rlog("SDKPay.onDestroy(): platform = " + platform);
		reflectInvoke("onDestroy", new Class[]{Activity.class}, new Object[]{mActivity});
		
		mActivity = null;
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent intent){
		SDKDebug.rlog("SDKPay.onActivityResult(): platform = " + platform);
		reflectInvoke("onActivityResult", 
				new Class[]{Activity.class, int.class, int.class, Intent.class}, 
				new Object[]{mActivity, requestCode, resultCode, intent}
		);
	}
	
	public static int getPlatform() {
		return platform;
	}
	
	public static int enablePltCoinPay() {
		return enablePltCoinPay;
	}

	public static InitInfo getInitInfo() {
		return initInfo;
	}

	public static int getPayment() {
		return payment;
	}
	
	public static Activity getActivity() {
		return mActivity;
	}

	private boolean assertActivityInstance(Context context) {
		if (context != null && context instanceof Activity) {
			if (mActivity != context) {
				SDKDebug.rlog("SDKPay.assertActivityInstance(): mismatched context " 
					+ context + ", activity " + mActivity);
				return false;
			}
		} else {
			if (mActivity == null) {
				SDKDebug.rlog("SDKPay.assertActivityInstance(): activity == null");
				return false;
			}
		}
		
		return true;
	}
	
	private void reflectInvoke(final String methodName, final Class<?>[] paramsType, final Object[] params) {
		if (mActivity != null) {
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					reflectInvokeInternal(methodName, paramsType, params);
				}
			});
		} else {
			reflectInvokeInternal(methodName, paramsType, params);
		}
	}
	
	private void reflectInvokeInternal(String methodName, Class<?>[] paramsType, Object[] params) {
		SDKPaybase realizeInstance = (SDKPaybase) ReflectUtil.getClassInstance(realizeClassName, null);
		if (realizeInstance == null) {
			SDKDebug.relog("SDKPay.onLifeCycle(): realizeInstance not found for className = " + realizeClassName);
			return;
		}
		
		Method initializeMethod = ReflectUtil.getMethod(
				realizeClassName, 
				methodName, 
				paramsType
			);
		if (initializeMethod == null) {
			SDKDebug.relog("SDKPay.onLifeCycle(): method not found for className = " + realizeClassName);
			return;
		}
		
		//反射调用
		ReflectUtil.invokeMethod(
			realizeInstance, 
			initializeMethod, 
			params
		);
	}

	/**
	 * 处理服务端的返回值。
	 * @param msg
	 * @author Synaric
	 */
	private boolean handleResult(Message msg) {
		SDKDebug.rlog("SDKPay.init(): bundle = " + msg.obj);
		SDKDebug.rlog("SDKPay.init(): platform = " + platform);
		
		String abAppId = "";
		if (msg.obj != null) {
			Bundle bundle = (Bundle) msg.obj;
			abAppId = bundle.getString("abAppId");
			realizeClassName = bundle.getString("contentStr");
			payment = ("new".equals(bundle.getString("payment")) ? PAYMENT_AB_H5 : PAYMENT_ORIGIN);
			enablePltCoinPay = bundle.getInt("enablePltCoinPay", 1);
			
			SDKDebug.rlog("SDKPay.init(): abAppId = " + abAppId);
			SDKDebug.rlog("SDKPay.init(): realizeClassName = " + realizeClassName);
			SDKDebug.rlog("SDKPay.init(): payment = " + payment);
		}
		
		//游戏爱贝appId
		if (!TextUtils.isEmpty(abAppId)) {
			PlatformInfoStore.setAB_APP_ID(abAppId);
		} else {
			SDKDebug.relog("abAppId获取失败");
		}
		
		//新版sdk不需要动态获取实现类
		//实现类写在assets/hyinfo.store
		if (TextUtils.isEmpty(realizeClassName)) {
			SDKDebug.relog("contentStr获取失败");
		}
		
		if (payment <= 0) {
			payment = 0;
			SDKDebug.relog("payment获取失败");
		}
		
		return true;
	}
	
	/**
	 * 读取assets下的初始化信息。
	 * @return
	 */
	private boolean readInitInfoFromAssets() {
		String info = MyFileUtil.readInitInfoFromAssets(mActivity, "hyinfo.store");
		if (TextUtils.isEmpty(info)) {
			SDKDebug.relog("SDKPay.readInitInfoFromAssets(): invaid info!");
			return false;
		}
		
		SDKDebug.dlog("SDKPay.readInitInfoFromAssets(): info = ");
		SDKDebug.dlog(info);
		
		JsonParser parser = new JsonParser();
		JsonObject initData = (JsonObject) parser.parse(info);
		String version = initData.get("version").getAsString();
		int platformCode = initData.get("platformCode").getAsInt();
		String realizeClassName = initData.get("realizeClassName").getAsString();
		String orientation = initData.get("screenOrientation").getAsString();
		PlatformInfoStore.setInitData(initData);
		
		Constant.VERSION = version;
		SDKPay.platform = platformCode;
		SDKPay.realizeClassName = realizeClassName;
		SDKPay.orientation = "landscape".equals(orientation) ? LANDSCAPE : PORTRAIT;
		
		return true;
	}
}
