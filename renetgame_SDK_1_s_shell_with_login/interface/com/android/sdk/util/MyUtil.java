package com.android.sdk.util;

import org.json.JSONObject;

import com.android.sdk.port.PayInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class MyUtil {

	public static String getStringFromMetaData(Context context, String key) {
		try {
			ApplicationInfo appInfo = null;
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			Object value = appInfo.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String payInfoToString(PayInfo payInfo, String uuid,
			String channelid, int payResult, String userid, String APPKEY,String ext,int payType) {
		JSONObject json = new JSONObject();
		try {
			json.put(UploadTag.priceTag, payInfo.getPrice());// 价格 元
			json.put(UploadTag.orderidTag, payInfo.getOrderid());// 订单号 cp填写
			json.put(UploadTag.appidTag, payInfo.getAppid());// 我们自己 appid
			json.put(UploadTag.appkeyTag, APPKEY);// aby appid
			json.put(UploadTag.waresnameTag, payInfo.getWaresname());// 计费点名称
			json.put(UploadTag.channelidTag, channelid);// 我们自己 渠道号
			json.put(UploadTag.payResultTag, payResult);// 支付结果
			json.put(UploadTag.corderTag, uuid);//
			json.put(UploadTag.useridTag, userid);
			json.put(UploadTag.osTag, Constant.OS);
			json.put(UploadTag.sdkVerTag, Constant.VERSION);
			
			json.put(UploadTag.extTag,ext);
			
			json.put(UploadTag.typeTag, String.valueOf(payType));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public static String getApplicationName(Context context) { 
        PackageManager packageManager = null; 
        ApplicationInfo applicationInfo = null; 
        try { 
            packageManager = context.getApplicationContext().getPackageManager(); 
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0); 
        } catch (PackageManager.NameNotFoundException e) { 
            applicationInfo = null; 
        } 
        String applicationName =  
        (String) packageManager.getApplicationLabel(applicationInfo); 
        return applicationName; 
    } 

}
