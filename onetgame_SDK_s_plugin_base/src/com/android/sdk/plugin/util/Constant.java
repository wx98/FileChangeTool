package com.android.sdk.plugin.util;

public class Constant {
	public static final String VERSION = "1.0.9";
	
//	public static final String SERVER_URL = "http://cgame.clickout.cn:8080/qpay/api/";
//	public static final String SERVER_URL = "http://172.17.119.1:8080/online_game_new/api/";//本地
//	public static final String SERVER_URL = "http://120.26.55.153:8095/online_game_new/api/";//测试
//	public static final String SERVER_URL = "http://192.168.1.110:8080/online_game_new/api/";//liukaibendi
	public static final String SERVER_URL = "http://game.wingsungame.com/online_game_new/api/";//正式
//	public static final String SERVER_URL = "http://www.yangct.cn:8095/online_game_new/api/";//测试
	
	public static final String EXCEPTIONTAG = "inc.pl";
	
//	public final static String SRC_CHECK_LIB_UPDATE = "1";
	public final static String SRC_PAY_INIT = "2"; 
	public final static String SRC_PAY_REPORT_RESULT = "3";//计费接口返回
	public final static String SRC_PAY_UPLOAD_INCREMENT = "4";//新增,统计新增创角数
	public final static String SRC_PAY_FEEDBACK = "5"; // 客户端错误日志反馈
	public final static String SRC_GET_HY_USERID = "14";//根据渠道uid返回本平台uid
	public final static String SRC_REQUEST_H5_PAY = "15";//请求h5支付网关
	public final static String SRC_NATIVE_PAY_RESULT = "16";//确认支付结果（本地后端确认，用于h5支付）
	public final static String SRC_SUBMIT_ROLE_INFO = "28";//上报角色信息
	public final static String SRC_PLATFORM_COIN_PAY = "29";//平台币支付
	
	public static final String PLUGIN_JAR_NAME = "nplugin.apk";
	public static final String PLUGIN_PATH = ".androidna";
	public static final String PLUGIN_DEX_PATH = ".androidna";
	public static final String PLUGIN_SIGN_NAME = "sign.tx";
	
//	public static final String DATA_PATH = ".androidna";
	
	
	public static final String REQUEST_SERVER_ORDER = "13";//请求生成订单

}
