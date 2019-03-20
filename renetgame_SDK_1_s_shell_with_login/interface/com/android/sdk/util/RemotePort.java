package com.android.sdk.util;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.sdk.lg.port.HYSDK;
import com.android.sdk.lg.port.UserBean;
import com.android.sdk.port.InitInfo;
import com.android.sdk.port.PayInfo;
import com.android.sdk.port.RoleBean;
import com.android.sdk.shcore.LoginInfo;

import android.content.Context;
import android.os.Handler;
import dalvik.system.DexClassLoader;

public class RemotePort {
	private final static String CLASS_NAME_RemoteSdk = StringTable.getStringFromTable(new int[]{61,80,64,77,59,5,30,56,80,65,30,77,10,30,50,77,35,8,63,37,65,5,77,56,40,64,80,4,40,77,48,40,64,80,4,40,21,30,50});//"com.android.sdk.plugin.remote.RemoteSdk";
	private static RemotePort self = null;
	private Context mCtx = null;
	private DexClassLoader mLoader = null;

	private RemotePort(Context ctx) {
		this.mCtx = ctx.getApplicationContext();
	}

	public static RemotePort getInstance(Context ctx) {
		if (self == null) {
			self = new RemotePort(ctx);
		}
		return self;
	}

	private DexClassLoader getLoader() {
		if (mLoader == null) {
            String apppath = MyFileUtil.getPluginDexPath(mCtx)
            		+ File.separator 
            		+ Constant.PLUGIN_JAR_NAME;
	        File appf = new File(apppath);
	        //如果T卡及应用目录下都无jar，则从assets下拷贝到应用目录下
	        if (appf.exists() == false){
	            MyFileUtil.copyFileFromAssets(mCtx, 
	                    apppath,
	                    Constant.PLUGIN_JAR_NAME);
	        }
		    
			String jarPath = Constant.PLUGIN_DEX_PATH + File.separator
					+ Constant.PLUGIN_JAR_NAME;
			mLoader = Loader.loadDex(mCtx,jarPath, null, null);
		}
		return mLoader;
	}
	
	//
	public boolean RemoteSdk_sdkInit(InitInfo appInfo/*,String os,String sdkver*/,String appkey,Handler mHandler){
	    try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);
        	//init
            String s = appInfo.toJson(mCtx,appkey).toString();
            Object ret = ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.sdkInitTag,
            		platform,
                    new Class[] {Context.class, String.class ,Handler.class}, 
                    new Object[] {mCtx, s,mHandler});
            
            if ((ret != null) && (ret instanceof Boolean)){
            	return (Boolean)ret;
            }
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public void RemoteSdk_uploadIncreaseInfoToServer(String appid,String userid,String channelid){
		 try{
	    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
	    				CLASS_NAME_RemoteSdk,
	    				UploadTag.getInstanceTag, 
	                    new Class[0], 
	                    new Object[0]);
	            ShellInvoke.invokeMethod(getLoader(), 
	            		CLASS_NAME_RemoteSdk, 
	            		UploadTag.uploadIncrementInfoTag,
	            		platform,
	                    new Class[] {Context.class,String.class}, 
	                    new Object[] {mCtx,translateInfo(appid, userid, channelid)});
		    }catch(Exception e){
		        e.printStackTrace();
		    }
		}
	
	private String translateInfo(String appid,String userid,String channelid){
		JSONObject json = new JSONObject();
		try {
			json.put(UploadTag.appidTag, appid);
			json.put(UploadTag.useridTag, userid);
			json.put(UploadTag.channelidTag, channelid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public boolean RemoteSdk_getHYUserId(LoginInfo loginInfo, Handler handler) {
		try{
			Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);
			
			String request = loginInfo.getRequestMsgStr(mCtx);
			
			SDKDebug.log("start login: request = " + request);
			SDKDebug.log("start login: method = " + UploadTag.getHYUserId);
			
            ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.getHYUserId,
            		platform,
                    new Class[] {Context.class, String.class, Handler.class}, 
                    new Object[] {mCtx, request, handler});

            return true;

	    }catch(Exception e){
			SDKDebug.log("startPay:RemoteSdkRequestServerOrder:Exception"+e.toString());
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public void RemoteSdk_uploadPayResultToServer(String payInfo) {
	    try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
    				UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);
    		
            ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.uploadClientResultTag,
            		platform,
                    new Class[] {Context.class,String.class}, 
                    new Object[] {mCtx,payInfo});
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	public void Remote_525GetAccount(Context context,String url,String userid,String token,Handler requestHandler){
		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
				CLASS_NAME_RemoteSdk,
	            "getInstance", 
	            new Class[]{}, 
	            new Object[]{});
		
		Object ret = ShellInvoke.invokeMethod(getLoader(), 
				CLASS_NAME_RemoteSdk, 
	        	"get525Account",
	        	platform,
	            new Class[] {Context.class,String.class,String.class,String.class,Handler.class }, 
	            new Object[] {context,url,userid,token,requestHandler});
	}
	
	//请丢服务器生成支付订单
	public boolean RemoteSdkRequestServerOrder(PayInfo payInfo, RoleBean roleBean, Handler mHandler){

	    try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);

            String s = payInfo.getRequestServerOrderMsg(roleBean);
            //.substring(1, payInfo.toString().length()-1).replaceAll("\"", "").replaceAll(":", "=").replaceAll(",", "&")
			SDKDebug.log("startPay:s====" + s);
			
            Object ret = ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.requestServerOrder,
            		platform,
                    new Class[] {Context.class, String.class, Handler.class}, 
                    new Object[] {mCtx, s, mHandler});

            if ((ret != null) && (ret instanceof Boolean)){
            	return (Boolean)ret;
            }

	    }catch(Exception e){
			SDKDebug.log("startPay:RemoteSdkRequestServerOrder:Exception"+e.toString());
	        e.printStackTrace();
	    }
	    return false;
	}
	
	//h5支付专用，请求支付网关地址
	public void RemoteSdkRequestH5Pay(String userId, PayInfo payInfo, Handler mHandler) {
		try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);

            String s = payInfo.getRequestH5PayMsg(userId);
			SDKDebug.log("start H5 pay:s====" + s);
			
            Object ret = ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.requestH5Pay,
            		platform,
                    new Class[] {Context.class, String.class, Handler.class}, 
                    new Object[] {mCtx, s, mHandler});

	    }catch(Exception e){
			SDKDebug.log("startPay:RemoteSdkRequestH5Pay:Exception"+e.toString());
	        e.printStackTrace();
	    }
	}
	
	//提交角色信息
	public void RemoteSdkSubmitRoleInfo(RoleBean roleBean, String account, 
			String password, String appId, String channelId, String deviceId, Handler mHandler) {
		try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);
    		
    		JSONObject json = new JSONObject();
    		json.put("account", account);
    		json.put("passwd", password);
    		json.put("appid", appId);
    		json.put("channelid", channelId);
    		json.put("deviceid", deviceId);
    		json.put("roleid", roleBean.getRoleId());
    		json.put("rolename", roleBean.getRoleName());
    		json.put("serverid", roleBean.getServerId());
    		json.put("servername", roleBean.getServerName());
    		String s = json.toString();
			SDKDebug.log("submit role info:s====" + s);
			
            Object ret = ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.submitRoleInfo,
            		platform,
                    new Class[] {Context.class, String.class, Handler.class}, 
                    new Object[] {mCtx, s, mHandler});

	    }catch(Exception e){
			SDKDebug.log("submitRoleInfo:Exception"+e.toString());
	        e.printStackTrace();
	    }
	}
	
	//平台币购买游戏道具
	public void RemoteSdkPlatformCoinPay(UserBean userBean, String orderId, Handler mHandler) {
		try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);
    		
    		JSONObject json = new JSONObject();
    		json.put("account", userBean.getAccount());
    		json.put("passwd", userBean.getPasswd());
    		json.put("mobile", userBean.getMobile());
    		json.put("orderid", orderId);   
    		String s = json.toString();
			SDKDebug.log("platform coin pay:s====" + s);
			
            Object ret = ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.platformCoinPay,
            		platform,
                    new Class[] {Context.class, String.class, Handler.class}, 
                    new Object[] {mCtx, s, mHandler});

	    }catch(Exception e){
			SDKDebug.log("platformCoinPay:Exception"+e.toString());
	        e.printStackTrace();
	    }
	}
	
	//检查订单状态
	public boolean RemoteSdkVerifytPayResult(PayInfo payInfo, Handler mHandler){

	    try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);

    		
            String s = payInfo.getCheckOrderMsgStr();

			SDKDebug.log("VerifytPayResult:s====" + s);
			
            Object ret = ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.verifytPayResult,
            		platform,
                    new Class[] {Context.class, String.class, String.class, Handler.class}, 
                    new Object[] {mCtx, payInfo.getNotifyUrl(), s, mHandler});

            if ((ret != null) && (ret instanceof Boolean)){
            	return (Boolean)ret;
            }

	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public void RemoteSdkNativeVerifytPayResult(PayInfo payInfo, Handler mHandler){
		try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);

    		
            String s = payInfo.getNativeCheckOrderMsgStr();

			SDKDebug.log("NativeVerifytPayResult:s====" + s);
			
            ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.nativeVerifytPayResult,
            		platform,
                    new Class[] {Context.class, String.class, Handler.class}, 
                    new Object[] {mCtx, s, mHandler});

	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	public void RemoteSdkSyncPayPPY(String request, Handler mHandler) {
		try{
    		Object platform = ShellInvoke.invokeStaticMethod(getLoader(), 
    				CLASS_NAME_RemoteSdk,
                    UploadTag.getInstanceTag, 
                    new Class[0], 
                    new Object[0]);

			SDKDebug.log("NativeVerifytPayResult:s====" + request);
			
            ShellInvoke.invokeMethod(getLoader(), 
            		CLASS_NAME_RemoteSdk, 
            		UploadTag.nativeVerifytPayResult,
            		platform,
                    new Class[] {Context.class, String.class, Handler.class}, 
                    new Object[] {mCtx, request, mHandler});

	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
}
