package com.android.sdk.port;

import com.android.sdk.util.MyFileUtil;
import com.android.sdk.util.SDKDebug;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

/**
 * CP方如果没有自己的Application，直接使用这个Application即可；
 * 否则，需要让自己的Application继承HYApplication。
 * @author Synaric
 *
 */
public class HYApplication extends Application {
	
	private IAppLifecycleListener listener;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		listener = initApplicationProxy();
		
		if (listener != null) {
			SDKDebug.rlog("IAppLifecycleListener.attachBaseContext()");
			listener.proxyAttachBaseContext(base);
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (listener != null) {
			SDKDebug.rlog("IAppLifecycleListener.onConfigurationChanged()");
			listener.proxyOnConfigurationChanged(newConfig);
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (listener != null) {
			if (isMainProcess()) {
				SDKDebug.rlog("IAppLifecycleListener.onCreate()");
				listener.proxyOnCreate();
			}
		}
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (listener != null) {
			SDKDebug.rlog("IAppLifecycleListener.onLowMemory()");
			listener.proxyOnLowMemory();
		}
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		if (listener != null) {
			SDKDebug.rlog("IAppLifecycleListener.onTerminate()");
			listener.proxyOnTerminate();
		}
	}
	
	public Application getProxyApplication() {
		return (Application) listener;
	}
	
	private boolean isMainProcess() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        int myPid = android.os.Process.myPid();
        String mainProcessName = this.getPackageName();
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
	
	private IAppLifecycleListener initApplicationProxy() {
		//获取代理名
		String info = MyFileUtil.readInitInfoFromAssets(this, "hyinfo.store");
		if (TextUtils.isEmpty(info)) {
			SDKDebug.relog("HYApplication.initApplication(): empty info!");
			return null;
		}
		
		JsonParser parser = new JsonParser();
		JsonObject initData = (JsonObject) parser.parse(info);
		String applicationRealize = initData.get("applicationRealize").getAsString();
		PlatformInfoStore.setInitData(initData);
		
		if (TextUtils.isEmpty(applicationRealize)) {
			SDKDebug.rlog("HYApplication.initApplication(): empty value applicationRealize");
			return null;
		}
		
		SDKDebug.rlog("HYApplication.initApplication(): applicationRealize = " + applicationRealize);
		
		//实例化代理
		try {
			Class<?> clz = Class.forName(applicationRealize);
			return (IAppLifecycleListener) clz.newInstance();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		SDKDebug.relog("HYApplication.initApplication(): application == null");
		return null;
	}
}
