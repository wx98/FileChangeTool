package com.android.sdk.plugin.adapter;

import java.lang.reflect.Method;

import com.android.sdk.plugin.util.PreferenceUtil;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SimSprd implements SimInfo {
	private Context context;
	private TelephonyManager telMgr1;
	private TelephonyManager telMgr2;

	public SimSprd(Context ctx) {
		this.context = ctx;
		this.telMgr1 = (TelephonyManager) ctx.getSystemService(getServiceName(
				Context.TELEPHONY_SERVICE, 0));
		this.telMgr2 = (TelephonyManager) ctx.getSystemService(getServiceName(
				Context.TELEPHONY_SERVICE, 1));
	}

	@Override
	public String getOperatorStr1() {
		if (telMgr1 != null) {
			return telMgr1.getSimOperator();
		}
		return "";
	}

	@Override
	public String getOperatorStr2() {
		if (telMgr2 != null) {
			return telMgr2.getSimOperator();
		}
		return "";
	}

	@Override
	public boolean isSim1CardOk() {
		boolean sim1ok = true;
		if (isCardReady(context, telMgr1, 0) == false) {
			sim1ok = false;
		}
		return sim1ok;
	}

	@Override
	public boolean isSim2CardOk() {
		boolean sim2ok = true;
		if (isCardReady(context, telMgr2, 0) == false) {
			sim2ok = false;
		}
		return sim2ok;
	}

	@Override
	public int getOperator1() {
		if (isSim1CardOk() == true) {
			return PlatformUtil.getOperator(getOperatorStr1());
		}
		return 0;
	}

	@Override
	public int getOperator2() {
		if (isSim2CardOk() == true) {
			return PlatformUtil.getOperator(getOperatorStr2());
		}
		return 0;
	}

	@Override
	public String getImsi1() {
		if (telMgr1 != null) {
			return telMgr1.getSubscriberId();
		}
		return "";
	}

	@Override
	public String getImsi2() {
		if (telMgr2 != null) {
			return telMgr2.getSubscriberId();
		}
		return "";
	}

	@Override
	public String getSimId1(){
		return telMgr1.getSimSerialNumber();
	}
	
	@Override
	public String getSimId2(){
		return "";
	}
	
	@Override
	public String getImei() {
		if (telMgr1 != null) {
			return telMgr1.getDeviceId();
		}
		if (telMgr2 != null) {
			return telMgr2.getDeviceId();
		}
		return "";
	}

	@Override
	public String getScAddress1() {
		return "";
	}

	@Override
	public String getScAddress2() {
		return "";
	}

	@Override
	public boolean isSupportGetScAddress() {
		return false;
	}

	@Override
	public String getPhoneNumber1() {
		if (telMgr1 != null) {
			return telMgr1.getLine1Number();
		}
		return "";
	}

	@Override
	public String getPhoneNumber2() {
		if (telMgr2 != null) {
			return telMgr2.getLine1Number();
		}
		return "";
	}

	@Override
	public boolean isSimInCall(int sim) {
		boolean isInCall = false;
		if (sim == PlatformUtil.ZH_SIM1) {
			if (telMgr1 != null
					&& telMgr1.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
				isInCall = true;
			}
		} else if (sim == PlatformUtil.ZH_SIM2) {
			if (telMgr2 != null
					&& telMgr2.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
				isInCall = true;
			}
		}
		return isInCall;
	}
	
	public static boolean isMultiSim(Context ctx){
		boolean isMSim = false;
		try{
			Class<?> SprdPhoneProxyClass = Class.forName("com.android.internal.telephony.SprdPhoneProxy");
			if (SprdPhoneProxyClass != null){
				int phoneCount = getPhoneCount();
				if (phoneCount > 1){
					isMSim = true;
				}
			}
		}catch(Exception e){
			isMSim = false;
		}
		//判断卡槽情况
		if (isMSim == true){
			TelephonyManager telmngr = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			boolean simtate1 = isCardReady(ctx, telmngr, PlatformUtil.ZH_SIM1);
			boolean simtate2 = isCardReady(ctx, telmngr, PlatformUtil.ZH_SIM2);
			if (simtate1 != true 
					|| simtate2 != true){
				isMSim = false;
			}
		}
		
		return isMSim;
	}

	private static int getPhoneCount() {
		// return android.os.SystemProperties.getInt("persist.msms.phone_count",
		// 1);
		int phoneCount = 1;
		try {
			phoneCount = PreferenceUtil.getSystemPropertiesInteger(
					"persist.msms.phone_count", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phoneCount;
	}

	private String getServiceName(String defaultServiceName, int phoneId) {
		if (getPhoneCount() > 1) {
			if (phoneId == getPhoneCount()) {
				return defaultServiceName;
			}
			return defaultServiceName + phoneId;
		} else {
			return defaultServiceName;
		}
	}

	private static String getSetting(String defaultSetting, int phoneId) {
		String str = defaultSetting;
		try {
			Class<?> PhoneFactoryClass = Class
					.forName("com.android.internal.telephony.PhoneFactory");
			Class<?>[] paramClass = new Class[2];
			paramClass[0] = String.class;
			paramClass[1] = int.class;
			Method getSettingMethod = PhoneFactoryClass.getDeclaredMethod(
					"getSetting", paramClass);

			Object[] paramObject = new Object[2];
			paramObject[0] = defaultSetting;
			paramObject[1] = phoneId;
			str = (String) getSettingMethod.invoke(null, paramObject);
		} catch (Exception e) {
			if (phoneId == 0) {
				str = defaultSetting;
			}
			str = defaultSetting + phoneId;
		}
		return str;
	}

	private static boolean isCardReady(Context context, TelephonyManager telMgr, int sim) {
		boolean isReady = false;

		if (telMgr == null) {
			return isReady;
		}
		try {
			if (telMgr.hasIccCard()) {
				@SuppressWarnings("deprecation")
				int isAirplaneModeOn = android.provider.Settings.System.getInt(
						context.getContentResolver(),
						android.provider.Settings.System.AIRPLANE_MODE_ON, 0);
				int isStandby = android.provider.Settings.System.getInt(
						context.getContentResolver(),
						getSetting("sim_standby", sim), 1);

				if (isAirplaneModeOn == 0 && isStandby == 1) {
					isReady = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isReady;
	}

	@Override
	public int getCID() {
		return 0;
	}

	@Override
	public int getLAC() {
		return 0;
	}

	@Override
	public int getMNC() {
		return 0;
	}

	@Override
	public int getMCC() {
		return 0;
	}
}
