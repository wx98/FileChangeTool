package com.android.sdk.shcore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.android.sdk.util.Constant;
import com.android.sdk.util.SDKDebug;

public class MyHttpProtocol {
	
	public static final int SYNC_PAY_PPY = 17;
	
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
