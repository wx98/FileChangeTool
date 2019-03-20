package com.android.sdk.shcore;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.sdk.util.DeviceMsgUtil;
import com.android.sdk.util.MyUtil;
import com.android.sdk.util.UploadTag;

import android.content.Context;

/**
 * 上报用户登录信息。
 * @author Synaric
 *
 */
public class LoginInfo {
	
	private String userId;
	
	private String appId;
	
	private int platform;
	
	private String deviceId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getRequestMsgStr(Context context) {
		JSONObject json = new JSONObject();
		try {
			json.put("appid", appId);
			json.put("deviceid", DeviceMsgUtil.getInstance(context).getImei());
			json.put("channel", MyUtil.getStringFromMetaData(context, UploadTag.channelTag));
			json.put("account", userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}
