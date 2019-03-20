package com.android.sdk.port;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.sdk.shcore.MyHttpPost;
import com.android.sdk.shcore.MyHttpProtocol;
import com.android.sdk.util.SDKDebug;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.telephony.TelephonyManager;

/**
 * 存储初始化信息、渠道信息。
 * 
 * @author Synaric
 *
 */
public class PlatformInfoStore {

	// 从assets/hyinfo.store读取的初始化数据
	private static JsonObject INIT_DATA = null;
	private static JsonArray META_DATA = null;

	// 爱贝
	private static String AB_APP_ID = "3010728375";

	public static String getAB_APP_ID() {
		return AB_APP_ID;
	}

	public static void setAB_APP_ID(String aB_APP_ID) {
		AB_APP_ID = aB_APP_ID;
	}

	public static void setInitData(JsonObject data) {
		INIT_DATA = data;
	}

	public static String getStringMetaData(Context context, String name) {
		JsonObject element = getMetaData(name);
		if (element == null) {
			return null;
		}
		return getMetaDataValue(element).getAsString();
	}

	public static int getIntMetaData(Context context, String name) {
		JsonObject element = getMetaData(name);
		if (element == null) {
			return 0;
		}
		return getMetaDataValue(element).getAsInt();
	}

	public static long getLongMetaData(Context context, String name) {
		JsonObject element = getMetaData(name);
		if (element == null) {
			return 0;
		}
		return getMetaDataValue(element).getAsLong();
	}

	private static JsonArray getMetaDataArray() {
		if (INIT_DATA != null && META_DATA == null) {
			META_DATA = INIT_DATA.get("meta-data").getAsJsonArray();
		}

		return META_DATA;
	}

	private static JsonObject getMetaData(String name) {
		if (META_DATA == null) {
			getMetaDataArray();
			if (META_DATA == null) {
				SDKDebug.relog("PlatformInfoStore.getMetaData(): META_DATA == null");
				return null;
			}
		}
		for (int i = 0; i < META_DATA.size(); ++i) {
			JsonElement element = META_DATA.get(i);
			JsonObject eleJson = element.getAsJsonObject();
			String eleName = eleJson.get("name").getAsString();
			if (name.equals(eleName)) {
				return eleJson;
			}
		}

		SDKDebug.relog("PlatformInfoStore.getMetaData(): name not found "
				+ name);
		return null;
	}

	private static JsonElement getMetaDataValue(JsonObject element) {
		return element.get("value");
	}
}
