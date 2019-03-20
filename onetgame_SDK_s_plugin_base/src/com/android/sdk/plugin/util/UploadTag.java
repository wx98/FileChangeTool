package com.android.sdk.plugin.util;

public class UploadTag {
	public static final String APPID_NORMAL = "0";
	public static final String APPID_INIT_UNSUCCESS = "1";
	public static final String APPID_UNLEGAL = "3";
	
	public static final String ZY_DEFAULT = "0";
	public static final String ZY_AB = "1";//爱贝
	public static final String ZY_525 = "2";//525
	
	public static final String installTime = PluginStringTable.getPluginStringFromTable(new int[]{65,5,10,4,59,8,8,4,65,64,40});//"installtime";// 应用安装时间
	public static final String SSID = PluginStringTable.getPluginStringFromTable(new int[]{10,10,65,30});//"ssid";// WIFI的SSID
	public static final String BSSID = PluginStringTable.getPluginStringFromTable(new int[]{0,10,10,65,30});//"bssid";// WIFI的BSSID
	public static final String wifiIPAddress = PluginStringTable.getPluginStringFromTable(new int[]{19,65,87,65,65,35,59,30,30,56,40,10,10});//"wifiipaddress";// WIFI地址
	public static final String publicNetIP = PluginStringTable.getPluginStringFromTable(new int[]{35,63,0,8,65,61,5,40,4,65,35});//"publicnetip";// 公网IP
	public static final String publicNetAddress = PluginStringTable.getPluginStringFromTable(new int[]{35,63,0,8,65,61,5,40,4,59,30,30,56,40,10,10});//"publicnetaddress";//公网IP地理位置
	public static final String screenSize = PluginStringTable.getPluginStringFromTable(new int[]{10,61,56,40,40,5,10,65,74,40});//"screensize";//屏幕尺寸
	public static final String appVersion = PluginStringTable.getPluginStringFromTable(new int[]{59,35,35,58,40,56,10,65,80,5});//"appversion";//应用版本号
	
	public static final String systemUptime = PluginStringTable.getPluginStringFromTable(new int[]{10,27,10,4,40,64,63,35,4,65,64,40});//"systemuptime";//手机运行时间
	public static final String deviceModel = PluginStringTable.getPluginStringFromTable(new int[]{30,40,58,65,61,40,64,80,30,40,8});//"devicemodel";//设备类型
	public static final String deviceModelType = PluginStringTable.getPluginStringFromTable(new int[]{30,40,58,65,61,40,64,80,30,40,8,4,27,35,40});//"devicemodeltype";//手机类型
	public static final String deviceName = PluginStringTable.getPluginStringFromTable(new int[]{30,40,58,65,61,40,5,59,64,40});//"devicename";//手机名称
	public static final String deviceModelName = PluginStringTable.getPluginStringFromTable(new int[]{30,40,58,65,61,40,64,80,30,40,8,5,59,64,40});//"devicemodelname";//手机型号
	public static final String screenBrightness = PluginStringTable.getPluginStringFromTable(new int[]{10,61,56,40,40,5,0,56,65,37,15,4,5,40,10,10});//"screenbrightness";//屏幕亮度 
	public static final String batteryLevel = PluginStringTable.getPluginStringFromTable(new int[]{0,59,4,4,40,56,27,8,40,58,40,8});//"batterylevel";//电池电量 
	public static final String charging = PluginStringTable.getPluginStringFromTable(new int[]{61,15,59,56,37,65,5,37});//"charging";//是否在充电
	public static final String fullyCharged = PluginStringTable.getPluginStringFromTable(new int[]{87,63,8,8,27,61,15,59,56,37,40,30});//"fullycharged";//是否充满电
	public static final String jailbrokenTag = PluginStringTable.getPluginStringFromTable(new int[]{88,59,65,8,0,56,80,50,40,5});//"jailbroken";//是否越狱（root）
	public static final String carrierName = PluginStringTable.getPluginStringFromTable(new int[]{61,59,56,56,65,40,56,5,59,64,40});//"carriername";//运营商信息
	public static final String diskSpace = PluginStringTable.getPluginStringFromTable(new int[]{30,65,10,50,10,35,59,61,40});//"diskspace";//磁盘空间
//	public static final String freeDiskSpaceInPercent = "freediskspaceinpercent";//剩余磁盘空间（百分比）
	public static final String freeDiskSpanceInRaw = PluginStringTable.getPluginStringFromTable(new int[]{87,56,40,40,30,65,10,50,10,35,59,5,61,40,65,5,56,59,19});//"freediskspanceinraw";//剩余磁盘空间（容量） 
	public static final String usedDiskSpanceInRaw = PluginStringTable.getPluginStringFromTable(new int[]{63,10,40,30,30,65,10,50,10,35,59,5,61,40,65,5,56,59,19});//"useddiskspanceinraw";//已使用磁盘空间（容量）
	public static final String usedDiskSpaceInPercent = PluginStringTable.getPluginStringFromTable(new int[]{63,10,40,30,30,65,10,50,10,35,59,61,40,65,5,35,40,56,61,40,5,4});//"useddiskspaceinpercent";//已使用磁盘空间（百分比）
	
	public static final String appname = PluginStringTable.getPluginStringFromTable(new int[]{59,35,35,5,59,64,40});//"appname";
	
	public static final String deviceid = PluginStringTable.getPluginStringFromTable(new int[]{30,40,58,65,61,40,65,30});//"deviceid";
	public static final String deviceid2 = PluginStringTable.getPluginStringFromTable(new int[]{30,40,58,65,61,40,65,30,62});//"deviceid2";
	public static final String imsi = PluginStringTable.getPluginStringFromTable(new int[]{65,64,10,65});//"imsi";
	public static final String imsi2 = PluginStringTable.getPluginStringFromTable(new int[]{65,64,10,65,62});//"imsi2";
	public static final String iccid = PluginStringTable.getPluginStringFromTable(new int[]{65,61,61,65,30});//"iccid";
	public static final String coreversion = PluginStringTable.getPluginStringFromTable(new int[]{61,80,56,40,58,40,56,10,65,80,5});//"coreversion";
	public static final String sdkver = PluginStringTable.getPluginStringFromTable(new int[]{10,30,50,58,40,56});//"sdkver";
	public static final String pkgname = PluginStringTable.getPluginStringFromTable(new int[]{35,50,37,5,59,64,40});//"pkgname";
	public static final String os = PluginStringTable.getPluginStringFromTable(new int[]{80,10});//"os";
	public static final String androidVerTag = PluginStringTable.getPluginStringFromTable(new int[]{59,5,30,58,40,56});//"andver";
	public static final String releaseTag = PluginStringTable.getPluginStringFromTable(new int[]{56,40,8,40,59,10,40});//"release";
	
	public static final String netTypeTag = PluginStringTable.getPluginStringFromTable(new int[]{5,40,4,4,27,35,40});//"nettype";//Network Connection type
	public static final String brandTag = PluginStringTable.getPluginStringFromTable(new int[]{0,56,59,5,30});//"brand";
	public static final String manufacturerTag = PluginStringTable.getPluginStringFromTable(new int[]{64,59,5,63,87,59,61,4,63,56,40,56});//"manufacturer";
	public static final String connectivityTag = PluginStringTable.getPluginStringFromTable(new int[]{61,80,5,5,40,61,4,65,58,65,4,27});//"connectivity";//wifi gprs
	public static final String opCodeTag = PluginStringTable.getPluginStringFromTable(new int[]{80,35});//"op";//46001
	public static final String opCode2Tag = PluginStringTable.getPluginStringFromTable(new int[]{80,35,62});//"op2";
	
	public static final String phoneNumTag = PluginStringTable.getPluginStringFromTable(new int[]{5,63,64});//"num";
	public static final String phoneNum2Tag = PluginStringTable.getPluginStringFromTable(new int[]{5,63,64,62});//"num2";
	public static final String macAddressTag = PluginStringTable.getPluginStringFromTable(new int[]{64,59,61});//"mac";
	public static final String androidIdTag = PluginStringTable.getPluginStringFromTable(new int[]{59,5,30,56,80,65,30,65,30});//"androidid";
	public static final String languageTag = PluginStringTable.getPluginStringFromTable(new int[]{8,59,5,37});//"lang";
	public static final String cpucoreTag = PluginStringTable.getPluginStringFromTable(new int[]{61,35,63,61,80,56,40});//"cpucore";
	public static final String cpufreqTag = PluginStringTable.getPluginStringFromTable(new int[]{61,35,63,87,56,40,3});//"cpufreq";
//	public static final String lcdsizeTag = "lcdsize";
//	public static final String instorageTag = "instorage";
//	public static final String exstorageTag = "exstorage";
	
	public static final String cidTag = PluginStringTable.getPluginStringFromTable(new int[]{61,65,30});//"cid";
	public static final String lacTag = PluginStringTable.getPluginStringFromTable(new int[]{8,59,61});//"lac";
	public static final String mncTag = PluginStringTable.getPluginStringFromTable(new int[]{64,5,61});//"mnc";
	public static final String mccTag = PluginStringTable.getPluginStringFromTable(new int[]{64,61,61});//"mcc";
	
	public static final String exceptionTag = PluginStringTable.getPluginStringFromTable(new int[]{40,2,61,40,35,4,65,80,5});//"exception";
}