package com.android.sdk.plugin.net;

public interface DownLoadListener {
    public static int FINISH = 5;
    public static int ERROR = 4;
    public static int CANCEL = 3;
	
	/**
	 * eventCode 自定义两个状态码,3-代表cancel,4-代表error，5-代表finish, 
	 * eventDescription中传输取消描述、错误描述和finsih描述。
	 * 状态码3未完成
	 * */
	public void HttpTransfersEvent(String taskname, int eventCode, String eventDescription);
	
	public void HttpTransfersBodyReceived(String taskname, String receivedUrl, byte[] bodyData,int size);
}
