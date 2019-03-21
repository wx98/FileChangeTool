package com.android.sdk.plugin.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class MyHttpProtocol {
//	public static final String statusTag = "status";
//	public static final String msgTag = "msg";
	
//	public static final String shellVerionTag = "shellVer";
//	public static final String netTypeTag = "net";
//	public static final String isRootedTag = "root";
//	public static final String androidVerTag = "andver";
//	public static final String androidSdkIntTag = "sdkint";
//	public static final String modelTag = "model";
//	public static final String brandTag = "brand";
//	public static final String opCodeTag = "op";
//	public static final String opCode2Tag = "op2";
//	public static final String imsiTag = "imsi";
//	public static final String imsi2Tag = "imsi2";
//	public static final String imeiTag = "imei";
//	public static final String imei2Tag = "imei2";
//	public static final String macAddressTag = "mac";
//	public static final String androidIdTag = "andid";
//	public static final String languageTag = "lang";
//	public static final String channelTag = "channel";
//	public static final String deviceIdTag = "devid";
//	public static final String cpIdTag = "cpid";
//	public static final String appIdTag = "appid";
//	public static final String pkgNameTag = "pkgname";
//	public static final String pkgVersionTag = "pkgver";
//	public static final String pkgSignTag = "pkgsign";
//	public static final String netTimeTag = "nettime";
//	public static final String activeTag = "active";
//	public static final String blockMd5Tag = "blockmd5";
//	public static final String codeMd5Tag = "codemd5";
	
//	private static final String libsTag = "libs";
//	private static final String nameTag = "name";
//	private static final String versionTag = "version";
//	private static final String crc32Tag = "crc";
//	private static final String urlTag = "url";
	
	public static String getHost() {
		return Constant.SERVER_URL;
	}
	
	public static String deCodeRspContent(String content, String charsetName) {
		String out = null;
		try {
			byte[] decbyte = MyBase64.decode(content, MyBase64.DEFAULT);
			out = new String(decbyte, charsetName);
			out = URLDecoder.decode(out, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static String enCodeReqContent(String content) {
		String out = null;
		try {
			out = URLEncoder.encode(content, "utf-8");
			out = MyBase64.encodeToString(out.getBytes(), MyBase64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return out;
	}
	
//	public static JSONObject genPluginUpdateReqest(Context ctx, String channel, PluginSignItem signItem){
//		JSONObject postValues = new JSONObject();
//        try{
//            //android_version	string	操作系统版本
//            postValues.put(androidVerTag, DeviceUtil.getSDKVerName());
//            postValues.put(androidSdkIntTag, DeviceUtil.getSDKVersionInt());
//            //imsi	string 	imsi编号
//            postValues.put(imsiTag, DeviceUtil.getIMSI(ctx));
//            //imei	string	imei号
//            postValues.put(imeiTag, DeviceUtil.getIMEI(ctx));
//            //mac	string	wifi芯片mac地址
//            postValues.put(macAddressTag, NetworkUtil.getMacAddress(ctx));
//            //model	string	机型信息
//            postValues.put(modelTag, DeviceUtil.getDeviceModel());
//            postValues.put(brandTag, DeviceUtil.getDeviceBrand());
//            //net	int	网络类型
//            postValues.put(netTypeTag, NetworkUtil.getNetworkType(ctx));
//            //language	string	当前语言
//            postValues.put(languageTag, DeviceUtil.getLang(ctx));
//            //base_lib_version	string	壳版本号
//            postValues.put(shellVerionTag, Constant.VERSION);
//            //channel	string	渠道号
//            postValues.put(channelTag, channel);
//            //libs	array	更新的lib数组
//            JSONArray array = new JSONArray();
//            if (signItem != null){
//        		JSONObject node = new JSONObject();
//        		node.put(nameTag, signItem.getName());
//        		node.put(versionTag, signItem.getVersion());
//        		array.put(node);
//            }
//            else{
//        		JSONObject node = new JSONObject();
//        		node.put(nameTag, Constant.PLUGIN_JAR_NAME);
//        		node.put(versionTag, Constant.VERSION);
//        		array.put(node);
//            }
//            postValues.put(libsTag, array);
//            
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return postValues;
//	}
	
//	public static PluginHttpData parseLibRespone(String recvStr){
//    	try{
//    		PluginHttpData result = new PluginHttpData();
//    		JSONObject json = new JSONObject(recvStr);
//    		result.setStatus(json.optInt(statusTag));
//    		result.setMsg(json.optString(msgTag));
//    	
//    		int size = 0;
//    		JSONArray array = json.optJSONArray(libsTag);
//    		if (array != null && (size = array.length()) > 0){
//    			List<PluginSignItem> libs = new ArrayList<PluginSignItem>();
//    			for (int i=0; i<size; i++){
//    				PluginSignItem lib = new PluginSignItem();
//                    JSONObject object = array.getJSONObject(i);
//                    
//                    lib.setName(object.optString(nameTag));
//                    lib.setVersion(object.optString(versionTag));
//                    lib.setUrl(object.optString(urlTag));
//                    lib.setCrc32(Long.parseLong(object.optString(crc32Tag)));
//                    
//                    libs.add(lib);
//    			}
//        		result.setPluginlist(libs);
//    		}
//    		return result;
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//        return null;
//    }
}
