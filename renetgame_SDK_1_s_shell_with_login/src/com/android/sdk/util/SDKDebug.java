package com.android.sdk.util;

import android.util.Log;

public class SDKDebug {
	public static final int LEVEL_ALL = 6;
	public static final int LEVEL_RELEASE = 5;
	public static final int LEVEL_ERRROR = 4;
	public static final int LEVEL_WARNING = 3;
	public static final int LEVEL_DEBUG = 2;
	public static final int LEVEL_INFO = 1;
	public static final int LEVEL_NONE = 0;
	
	//打印所有log
	private static final int LOG_LEVEL = LEVEL_ALL;
	//不打印log
	//private static final int LOG_LEVEL = LEVEL_NONE;
	
	private static final String TAG = StringTable.getStringFromTable(new int[]{41,23,40,0,63,37,41});//"_Debug_";
	
	/**
	 * release版打印log。
	 * @param s
	 * @author Synaric
	 */
	public static final void rlog(String s) {
		if (LOG_LEVEL >= LEVEL_RELEASE) {
			Log.d(TAG,Constant.VERSION + StringTable.getStringFromTable(new int[]{41})//"_"
					+s);
		}
	}
	
	/**
	 * release版打印log。
	 * @param s
	 * @author Synaric
	 */
	public static final void relog(String s) {
		if (LOG_LEVEL >= LEVEL_RELEASE) {
			Log.e(TAG,Constant.VERSION + StringTable.getStringFromTable(new int[]{41})//"_"
					+s);
		}
	}
	
	public static final void elog(String s) {
		if (LOG_LEVEL >= LEVEL_ERRROR) {
			Log.e(TAG,Constant.VERSION + StringTable.getStringFromTable(new int[]{41})//"_"
					+s);
		}
	}
	
	public static final void wlog(String s) {
		if (LOG_LEVEL >= LEVEL_WARNING) {
			Log.w(TAG,Constant.VERSION + StringTable.getStringFromTable(new int[]{41})//"_"
					+s);
		}
	}
	
	public static final void dlog(String s) {
		if (LOG_LEVEL >= LEVEL_DEBUG) {
			Log.d(TAG,Constant.VERSION + StringTable.getStringFromTable(new int[]{41})//"_"
					+s);
		}
	}
	
	
	public static final void ilog(String s) {
		log(s);
	}
	
	public static final void log(String s){
		if(LOG_LEVEL >= LEVEL_INFO){
			Log.i(TAG,Constant.VERSION+StringTable.getStringFromTable(new int[]{41})//"_"
					+s);
		}
	}
}
