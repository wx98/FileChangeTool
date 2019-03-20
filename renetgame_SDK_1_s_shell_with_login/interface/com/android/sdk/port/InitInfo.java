package com.android.sdk.port;

import org.json.JSONObject;

import android.content.Context;

import com.android.sdk.util.Constant;
import com.android.sdk.util.MyUtil;
import com.android.sdk.util.UploadTag;

public class InitInfo {
	private String appid;//appid(我们自己)
	private int orientation;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public JSONObject toJson(Context mContext,String appkey) {
		try {
			JSONObject json = new JSONObject();
			json.put(UploadTag.osTag, Constant.OS);
			json.put(UploadTag.appidTag, appid);
			json.put(UploadTag.channelidTag, MyUtil.getStringFromMetaData(mContext, UploadTag.channelTag));
			json.put(UploadTag.appkeyTag, appkey);
			json.put(UploadTag.orientationTag, orientation);
			json.put(UploadTag.sdkVerTag, Constant.VERSION);

			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
}
