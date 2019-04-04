package com.android.sdk.util;

public class Constant {
	public static String VERSION = "1.1.3";
	public static final String OS = StringTable.getStringFromTable(new int[]{79});//"0";
	
//	public static final String SERVER_URL = "http://cgame.clickout.cn:8080/qpay/api/";
//	public static final String SERVER_URL = "http://192.168.1.113:8080/online_game_new/api/";
//	public static final String SERVER_URL = "http://192.168.1.112:8080/qpay/api/";
	
	public static final String SERVER_URL = "http://game.wingsungame.com/online_game_new/api/";//正式
//	public static final String SERVER_URL = "http://www.yangct.cn:8095/online_game_new/api/";//测试
//	public static final String SERVER_URL = "http://172.17.119.1:8080/online_game_new/api/";//本地
	
//	public final static String SRC_CHECK_LIB_UPDATE = "1";
//	public final static String SRC_PAY_REPORT_RESULT = "3";
	
	public static final String PLUGIN_JAR_NAME = StringTable.getStringFromTable(new int[]{5,35,8,63,37,65,5,77,59,35,50});//"nplugin.apk";
//	public static final String PLUGIN_JAR_NAME = "nplugin.bin";
	public static final String DATA_PATH = StringTable.getStringFromTable(new int[]{77,59,5,30,56,80,65,30,5,59});//".androidna";
	public static final String PLUGIN_PATH = StringTable.getStringFromTable(new int[]{77,59,5,30,56,80,65,30,5,59});//".androidna";
	public static final String PLUGIN_DEX_PATH = StringTable.getStringFromTable(new int[]{77,59,5,30,56,80,65,30,5,59});//".androidna";
	
	public static final long MSG_CHECK_INTERVAL = 4 * 60 * 60 * 1000;

}
