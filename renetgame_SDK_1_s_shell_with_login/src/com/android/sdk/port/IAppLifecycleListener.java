package com.android.sdk.port;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Application生命周期回调。
 * 渠道如果有Application实现要求，需要实现一个代理Application，并实现这个接口。
 * Application的生命周期中会回调相应的方法。
 * 
 * 注意，第三方sdk集成本sdk的时候，未必调用所有生命周期方法。
 * 鸿游（CGame）调用onCreate()、onConfigurationChanged()。
 * @author Synaric
 *
 */
public interface IAppLifecycleListener {

	/**
	 * 这个方法保证必定调用，因为Application实例化时，attachBaseContext()必定调用。
	 * @param base
	 */
	void proxyAttachBaseContext(Context base);
	
	void proxyOnConfigurationChanged(Configuration newConfig);
	
	void proxyOnCreate();
	
	void proxyOnLowMemory();
	
	void proxyOnTerminate();
}
