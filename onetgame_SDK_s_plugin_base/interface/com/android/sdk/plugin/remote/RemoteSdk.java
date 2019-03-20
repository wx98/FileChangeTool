package com.android.sdk.plugin.remote;

import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.sdk.plugin.port.Upload;
import com.android.sdk.plugin.util.ApkManager;
import com.android.sdk.plugin.util.BatteryReceriver;
import com.android.sdk.plugin.util.Constant;
import com.android.sdk.plugin.util.DeviceUtil;
import com.android.sdk.plugin.util.MyDebug;
import com.android.sdk.plugin.util.MyUncaughtExceptionHandler;
import com.android.sdk.plugin.util.NetworkUtil;
import com.android.sdk.plugin.util.UploadTag;
import com.android.sdk.plugin.util.PreferenceUtil;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class RemoteSdk {
	private static RemoteSdk self = null;

	// private boolean isSdkInit = false;

	// private Context context;

	private RemoteSdk() {
		// this.isSdkInit = false;
	}

	public static RemoteSdk getInstance() {
		if (self == null) {
			self = new RemoteSdk();
		}
		return self;
	}

	/**
	 * @Description: 初始化,上传信息到服务端以便统计日活等数据
	 * @param ctx
	 * @param appInfo
	 * @param mHandler
	 * @return
	 * @return Boolean
	 */
	public Boolean sdkInit(Context ctx, String appInfo, Handler mHandler) {
		if (ctx != null && appInfo != null /* && isSdkInit == false */) {
			
			setUncaughtExceptionHandler();
			getInformation(ctx);
			Upload.netInitSdk(setInitBaseInfo(ctx, setBaseInfo(ctx, appInfo)),
					mHandler);
			
			// isSdkInit = true;
			return true;
		}
		return false;
	}

	/**
	 * Sets the default uncaught exception handler
	 */
	private void setUncaughtExceptionHandler() {
		UncaughtExceptionHandler systemDefaultHandler = Thread
				.getDefaultUncaughtExceptionHandler();

		if (systemDefaultHandler instanceof MyUncaughtExceptionHandler)
			return;// 已经注册过

		// 设置自定义的异常处理器
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
	}

	/**
	 * @Description: 
	 *               获取IP和定位信息,并保存到sharepreference中,例:{ip:'101.81.224.71',address:'上海市
	 *               电信'}
	 * @param mContext
	 * @return void
	 */
	private void getInformation(Context mContext) {
		// locate
		NetworkUtil.getPosition(mContext);

		// battery
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		mContext.registerReceiver(new BatteryReceriver(), filter);
	}

	/**
	 * @Description: 拼装统计新增字符串(只有在初始化时会被调用)
	 * @param mContext
	 * @param appInfo
	 * @return
	 * @return String
	 */
	private String setInitBaseInfo(Context mContext, String appInfo) {
		try {
			JSONObject json = new JSONObject(appInfo);
			String[] temp = ApkManager.getPackageInfo(mContext);
			try {
				json.put(UploadTag.installTime,
						URLEncoder.encode(temp[0], "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				MyUncaughtExceptionHandler.writeError(e1, "setInitBaseInfo_encode_installtime");
				e1.printStackTrace();
			}
			json.put(UploadTag.appVersion, temp[1]);
			json.put(UploadTag.appname, temp[2]);
			// json.put(UploadTag.pkgname, temp[3]);
			temp = null;
			json.put(UploadTag.wifiIPAddress, NetworkUtil.getIPAddress());
			temp = NetworkUtil.getWifiInfo(mContext);
			json.put(UploadTag.SSID, temp[0]);
			json.put(UploadTag.BSSID, temp[1]);
			temp = null;
			json.put(UploadTag.screenSize, DeviceUtil.getResolution(mContext));
			json.put(UploadTag.screenBrightness,
					DeviceUtil.getScreenBrightness(mContext));
			json.put(UploadTag.deviceModel, DeviceUtil.getDeviceModel());
			json.put(UploadTag.deviceModelType, "");
			json.put(UploadTag.brandTag, DeviceUtil.getDeviceBrand());
			json.put(UploadTag.manufacturerTag,
					DeviceUtil.getDeviceManufacturer());
			json.put(UploadTag.jailbrokenTag, DeviceUtil.getIsRooted());
			json.put(UploadTag.languageTag, DeviceUtil.getLanguage(mContext));
			json.put(UploadTag.connectivityTag,
					NetworkUtil.getNetInfo(mContext));
			json.put(UploadTag.deviceModelName, DeviceUtil.getDevice());
			json.put(UploadTag.androidVerTag, DeviceUtil.getSDKVersionInt());
			json.put(UploadTag.releaseTag, DeviceUtil.getSDKVerName());
			json.put(UploadTag.cpucoreTag, DeviceUtil.getCpuCoreNum());
			json.put(UploadTag.cpufreqTag, DeviceUtil.getMaxCpuFreq());
			json.put(UploadTag.systemUptime, DeviceUtil.getUptime());
			json.put(UploadTag.diskSpace, DeviceUtil.getTotalRom() + "MB");
			json.put(UploadTag.freeDiskSpanceInRaw, DeviceUtil.getAvailRom()
					+ "MB");
			json.put(UploadTag.netTypeTag, DeviceUtil.getNetworkType(mContext));

			// json.put(UploadTag.coreversion, Constant.VERSION);
			// json.put(UploadTag.deviceid, DeviceUtil_2.getDeviceid(mContext));
			json.put(UploadTag.deviceid2, DeviceUtil.getDeviceid_2(mContext));
			// json.put(UploadTag.imsi, DeviceUtil_2.getImsi(mContext));
			json.put(UploadTag.imsi2, DeviceUtil.getImsi_2(mContext));
			// json.put(UploadTag.iccid, DeviceUtil_2.getIccid(mContext));
			json.put(UploadTag.opCodeTag, DeviceUtil.getOp(mContext));
			json.put(UploadTag.opCode2Tag, DeviceUtil.getOp_2(mContext));
			json.put(UploadTag.phoneNumTag, DeviceUtil.getPhoneNum(mContext));
			json.put(UploadTag.phoneNum2Tag, DeviceUtil.getPhoneNum_2(mContext));
			json.put(UploadTag.androidIdTag, DeviceUtil.getAndroidid(mContext));
			json.put(UploadTag.cidTag, DeviceUtil.getCID(mContext));
			json.put(UploadTag.lacTag, DeviceUtil.getLAC(mContext));
			json.put(UploadTag.mncTag, DeviceUtil.getMNC(mContext));
			json.put(UploadTag.mccTag, DeviceUtil.getMCC(mContext));

			try {
				Thread.sleep(1 * 1000 / 2);
			} catch (InterruptedException e1) {
				MyUncaughtExceptionHandler.writeError(e1, "setInitBaseInfo_thread_sleep");
				e1.printStackTrace();
			}
			temp = PreferenceUtil.readData(mContext);
			json.put(UploadTag.publicNetIP, temp[0]);
			try {
				json.put(UploadTag.publicNetAddress,
						URLEncoder.encode(temp[1], "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				MyUncaughtExceptionHandler.writeError(e, "setInitBaseInfo_encode_ipaddress");
				e.printStackTrace();
			}
			json.put(UploadTag.charging, temp[2]);
			json.put(UploadTag.batteryLevel, temp[3]);
			temp = null;
			return json.toString();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "setInitBaseInfo_encode_jsonexception");
			e.printStackTrace();
			return appInfo;
		}
	}

	/**
	 * @Description: 拼装基本信息字符串
	 * @param mContext
	 * @param ori
	 * @return
	 * @return String
	 */
	private String setBaseInfo(Context mContext, String ori) {
		try {
			JSONObject json = new JSONObject(ori);
			json.put(UploadTag.deviceid, DeviceUtil.getDeviceid(mContext));// 设备识别码
			json.put(UploadTag.imsi, DeviceUtil.getImsi(mContext));
			json.put(UploadTag.iccid, DeviceUtil.getIccid(mContext));
			json.put(UploadTag.coreversion, Constant.VERSION);// jar
			json.put(UploadTag.pkgname, mContext.getPackageName());

			return json.toString();
		} catch (JSONException e) {
			// e.printStackTrace();
			MyUncaughtExceptionHandler.writeError(e, "setBaseInfo_jsonexception");
			return ori;
		}
	}

	/**
	 * @Description: 拼装统计创角玩家字符串(仅2个字段)
	 * @param mContext
	 * @param translateString
	 * @return void
	 */
	public void uploadIncrementInfo(Context mContext, String translateString) {
		try {
			JSONObject json = new JSONObject(translateString);
			json.put(UploadTag.deviceid, DeviceUtil.getDeviceid(mContext));// deviceid
			json.put(UploadTag.pkgname, mContext.getPackageName());// pkgname
			Upload.uploadIncrementInfoToServer(json.toString());
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadIncrementInfo_jsonexception");
			e.printStackTrace();
		}
	}
	
	public void getHYUserId(Context mContext, String request, Handler handler) {
		MyDebug.log("RemoteSDK.geHYUserId()");
		Upload.getHYUserId(request, handler);
	}
	
	public void requestH5Pay(Context mContext, String request, Handler handler) {
		MyDebug.log("RemoteSDK.requestH5Pay()");
		Upload.requestH5Pay(request, handler);
	}

	// s:zhpayInfo.tojson.toString
	/**
	 * @Description: 上传计费结果到服务端
	 * @param mContext
	 * @param s
	 * @return void
	 */
	public void uploadPayResultToServer(Context mContext, String s) {
		Upload.uploadPayResultToServer(setBaseInfo(mContext, s));
	}
	
	
	/**
	 * @Description 访问525服务器获取用户登录账户名account
	 * @param context
	 * @param url
	 * @param userid
	 * @param token
	 * @param requestHandler
	 */
	public void get525Account(Context context,String url,String userid,String token,Handler requestHandler){
		NetworkUtil.get525Account(url, userid, token, requestHandler);
	}
	
	/**
	 * @Description SDK调用服务器 请求生成订单
	 * @param context
	 * @param url
	 * @param userid
	 * @param token
	 * @param requestHandler
	 */
	public void requestServerOrder(Context context, String token, Handler requestHandler){
		//NetworkUtil.requestOrderUtil(Constant.SERVER_URL + Constant.REQUEST_SERVER_ORDER, userid, token, requestHandler);
		NetworkUtil.requestOrderUtil(Constant.SERVER_URL + Constant.REQUEST_SERVER_ORDER, token,  requestHandler);
	}
	
	public void submitRoleInfo(Context context, String request, Handler requestHandler) {
		Upload.submitRoleInfo(request, requestHandler);
	}
	
	public void platformCoinPay(Context context, String request, Handler requestHandler) {
		Upload.platformCoinPay(request, requestHandler);
	}
	
	//确认订单信息（第三方）
	public void verifytPayResult(Context context, String url, String token, Handler requestHandler){
		MyDebug.log("verifytPayResult:url==="+url+"===token==="+token);
		NetworkUtil.verifytPayResultUtil(url, token, requestHandler);
	}
	
	//确认订单信息（本地后端）
	public void nativeVerifytPayResult(Context mContext, String request, Handler handler) {
		MyDebug.log("nativeVerifytPayResult");
		Upload.nativeVerifytPayResultUtil(mContext, request, handler);
	}
}
