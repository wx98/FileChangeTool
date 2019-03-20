package com.android.sdk.util;

public class UploadTag {
	//映射方法
	public final static String getInstanceTag = StringTable.getStringFromTable(new int[]{37,40,4,60,5,10,4,59,5,61,40});//"getInstance";
	public static final String sdkInitTag = StringTable.getStringFromTable(new int[]{10,30,50,60,5,65,4});//"sdkInit";
	public static final String uploadClientResultTag = StringTable.getStringFromTable(new int[]{63,35,8,80,59,30,17,59,27,48,40,10,63,8,4,29,80,21,40,56,58,40,56});//"uploadPayResultToServer";
	public static final String uploadIncrementInfoTag = StringTable.getStringFromTable(new int[]{63,35,8,80,59,30,60,5,61,56,40,64,40,5,4,60,5,87,80});//"uploadIncrementInfo";
	//请求userId
	public static final String getHYUserId = StringTable.getStringFromTable(new int[]{37,40,4,9,11,24,10,40,56,60,30});
	//请求生成支付订单
	public static final String requestServerOrder = StringTable.getStringFromTable(new int[]{56,40,3,63,40,10,4,21,40,56,58,40,56,84,56,30,40,56});//"requestServerOrder";
	//请求h5支付网关地址
	public static final String requestH5Pay = StringTable.getStringFromTable(new int[]{56,40,3,63,40,10,4,9,91,17,59,27});
	//上报角色信息
	public static final String submitRoleInfo = "submitRoleInfo";
	//平台币购买
	public static final String platformCoinPay = "platformCoinPay";
	//确认订单情况（第三方）
	public static final String verifytPayResult = StringTable.getStringFromTable(new int[]{58,40,56,65,87,27,4,17,59,27,48,40,10,63,8,4});//"verifytPayResult";
	//确认订单情况（本地后端）
	public static final String nativeVerifytPayResult = StringTable.getStringFromTable(new int[]{5,59,4,65,58,40,75,40,56,65,87,27,4,17,59,27,48,40,10,63,8,4});//"nativeVerifytPayResult";
	
	//配置
	public static final String channelTag =StringTable.getStringFromTable(new int[]{61,15,59,8});// "chal";
	
	//上传字段
	public static final String orientationTag = StringTable.getStringFromTable(new int[]{80,56,65,40,5,4,59,4,65,80,5});//"orientation";
	public static final String priceTag = StringTable.getStringFromTable(new int[]{35,56,65,61,40});//"price";
	public static final String orderidTag = StringTable.getStringFromTable(new int[]{80,56,30,40,56,65,30});//"orderid";
	public static final String appidTag = StringTable.getStringFromTable(new int[]{59,35,35,65,30});//"appid";
	public static final String appkeyTag = StringTable.getStringFromTable(new int[]{59,35,35,50,40,27});//"appkey";
	public static final String waresnameTag = StringTable.getStringFromTable(new int[]{19,59,56,40,10,5,59,64,40});//"waresname";
	public static final String channelidTag = StringTable.getStringFromTable(new int[]{61,15,59,5,5,40,8,65,30});//"channelid";
	public static final String payResultTag = StringTable.getStringFromTable(new int[]{35,59,27,56,40,10,63,8,4});//"payresult";
	public static final String corderTag = StringTable.getStringFromTable(new int[]{61,80,56,30,40,56});//"corder";
	public static final String useridTag = StringTable.getStringFromTable(new int[]{63,10,40,56,65,30});//"userid";
	public static final String osTag = StringTable.getStringFromTable(new int[]{80,10});//"os";
	public static final String sdkVerTag = StringTable.getStringFromTable(new int[]{10,30,50,58,40,56});//"sdkver";
	
	public static final String extTag = StringTable.getStringFromTable(new int[]{40,2,4});//"ext";
	public static final String typeTag = StringTable.getStringFromTable(new int[]{4,27,35,40});//"type";
}
