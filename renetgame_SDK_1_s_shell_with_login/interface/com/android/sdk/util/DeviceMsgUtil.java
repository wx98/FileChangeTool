package com.android.sdk.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceMsgUtil {
	public String imei;
	
	public String iccid ;
	public String imsi ;
	
	public Context context;

	private static DeviceMsgUtil instance = null;
	private TelephonyManager telManager ;

	public DeviceMsgUtil(Context context) {
		this.context = context;
		telManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
		
	}
	
	public static DeviceMsgUtil getInstance(Context context) {
		if (null == instance) {
			instance = new DeviceMsgUtil(context);
		}
		return instance;
	}
	
	public String getImei() {
		imei = telManager.getDeviceId();
		return imei == null || imei.length() == 0 ? "" : imei;
	}

	

	public String getIccid() {
		iccid = telManager.getSimSerialNumber();
		return iccid == null || iccid.length() == 0 ? "" : iccid;
	}

	

	public String getImsi() {
		imsi = telManager.getSubscriberId();
		return imsi == null || imsi.length() == 0 ? "" : imsi;
	}

	
}
