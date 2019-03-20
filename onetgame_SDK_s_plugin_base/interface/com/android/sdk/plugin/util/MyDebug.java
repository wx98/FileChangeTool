package com.android.sdk.plugin.util;

import android.util.Log;

public class MyDebug {
	private static final boolean flag = true;
	private static final String TAG = PluginStringTable.getPluginStringFromTable(new int[]{51,39,23,40,0,63,37,41,35,8,63,37,65,5});//"NADebug_plugin";
	
	public static final void log(String s){
		if(flag){
			Log.i(TAG,Constant.VERSION+"_"+s);
		}
	}
	
}