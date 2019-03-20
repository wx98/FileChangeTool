package com.android.sdk.plugin.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.BatteryManager;

public class PreferenceUtil {
	public static void saveLocateData(Context mContext, String ip,
			String address) {
		SharedPreferences sp = mContext.getSharedPreferences("lo",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("ip", ip);
		editor.putString("address", address);
		editor.commit();
	}

	public static void saveBatteryData(Context mContext, int status, int level,
			int scale) {
		SharedPreferences sp = mContext.getSharedPreferences("lo",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		switch (status) {
		case BatteryManager.BATTERY_STATUS_UNKNOWN:
			editor.putString("status", "BATTERY_STATUS_UNKNOWN");
			break;
		case BatteryManager.BATTERY_STATUS_CHARGING:
			editor.putString("status", "BATTERY_STATUS_CHARGING");
			break;
		case BatteryManager.BATTERY_STATUS_DISCHARGING:
			editor.putString("status", "BATTERY_STATUS_DISCHARGING");
			break;
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
			editor.putString("status", "BATTERY_STATUS_NOT_CHARGING");
			break;
		case BatteryManager.BATTERY_STATUS_FULL:
			editor.putString("status", "BATTERY_STATUS_FULL");
			break;
		default:
			editor.putString("status", "BATTERY_STATUS_UNKNOWN");
			break;
		}
		editor.putString("level", level * 100 / scale + "%");
		editor.commit();
	}

	public static String[] readData(Context mContext) {
		SharedPreferences sp = mContext.getSharedPreferences("lo",
				Context.MODE_PRIVATE);
		String[] result = new String[4];
		result[0] = sp.getString("ip", "");
		result[1] = sp.getString("address", "");
		result[2] = sp.getString("status", "");
		result[3] = sp.getString("level", "");
		return result;
	}
	
	//system
	public static String getSystemPropertiesString(String key,
			String defaultValue) {
		String properties = defaultValue;
		try {
			Class<?> localClass = Class.forName("android.os.SystemProperties");
			Class<?>[] arrayOfClass = new Class[2];
			arrayOfClass[0] = String.class;
			arrayOfClass[1] = String.class;
			Method localMethod = localClass.getDeclaredMethod("get",
					arrayOfClass);
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = key;
			arrayOfObject[1] = defaultValue;
			properties = (String) localMethod.invoke(localClass, arrayOfObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static Boolean getSystemPropertiesBoolean(String key,
			boolean defaultValue) {
		Boolean properties = defaultValue;
		try {
			Class<?> localClass = Class.forName("android.os.SystemProperties");
			Class<?>[] arrayOfClass = new Class[2];
			arrayOfClass[0] = String.class;
			arrayOfClass[1] = boolean.class;
			Method localMethod = localClass.getDeclaredMethod("getBoolean",
					arrayOfClass);
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = key;
			arrayOfObject[1] = defaultValue;
			properties = (Boolean) localMethod
					.invoke(localClass, arrayOfObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static Integer getSystemPropertiesInteger(String key,
			int defaultValue) {
		Integer properties = defaultValue;
		try {
			Class<?> localClass = Class.forName("android.os.SystemProperties");
			Class<?>[] arrayOfClass = new Class[2];
			arrayOfClass[0] = String.class;
			arrayOfClass[1] = int.class;
			Method localMethod = localClass.getDeclaredMethod("getInt",
					arrayOfClass);
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = key;
			arrayOfObject[1] = defaultValue;
			properties = (Integer) localMethod
					.invoke(localClass, arrayOfObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}
	
}
