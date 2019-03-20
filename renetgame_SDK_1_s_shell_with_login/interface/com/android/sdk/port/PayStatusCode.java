package com.android.sdk.port;

public class PayStatusCode {
	public static final int INIT_SUCCESS = 0;
	public static final int INIT_FAILED = -1;
	
	public static final int PAY_SUCCESS = 0;
	public static final int ERROR_PAY_FAILED = -1;
	public static final int PAY_CANCEL = -2;
	public static final int PAY_INIT_FAILED  = -3;
	public static final int PAY_UNKNOWN = -4;
	
	public static final int LOGIN_SUCCESS = 0;	//登录成功
	public static final int LOGIN_FAILED = 1;	//登录失败
	
	public static final int LOGOUT_SUCCESS = 0;	//登出成功
	public static final int LOGOUT_FAILED = 1;	//登出失败
	
	public static final int EXIT_GAME = 0;		//确认退出
	public static final int EXIT_CANCEL = 1;	//取消退出
	
}
//     1	0	0 //初始化成功
//     1	1	0//初始化失败
//		0	0	0//支付成功
//		0	1	0//支付失败
//		0	1	1//支付取消
