package com.android.sdk.plugin.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SimMtkGemini implements SimInfo {
	private TelephonyManager telMgr;

	public SimMtkGemini(Context ctx) {
		telMgr = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	@Override
	public String getOperatorStr1() {
		return getSimOperatorGemini(PlatformUtil.ZH_SIM1);
	}

	@Override
	public String getOperatorStr2() {
		return getSimOperatorGemini(PlatformUtil.ZH_SIM2);
	}

	@Override
	public boolean isSim1CardOk() {
		boolean simok = false;
		if (getSimStateGemini(telMgr, PlatformUtil.ZH_SIM1) == TelephonyManager.SIM_STATE_READY) {
			simok = true;
		}
		return simok;
	}

	@Override
	public boolean isSim2CardOk() {
		boolean simok = false;
		if (getSimStateGemini(telMgr, PlatformUtil.ZH_SIM2) == TelephonyManager.SIM_STATE_READY) {
			simok = true;
		}
		return simok;
	}

	@Override
	public int getOperator1() {
		return PlatformUtil.getOperator(getOperatorStr1());
	}

	@Override
	public int getOperator2() {
		return PlatformUtil.getOperator(getOperatorStr2());
	}

	@Override
	public String getImsi1() {
		return getSubscriberIdGemini(PlatformUtil.ZH_SIM1);
	}

	@Override
	public String getImsi2() {
		return getSubscriberIdGemini(PlatformUtil.ZH_SIM2);
	}

	@Override
	public String getSimId1(){
		return telMgr.getSimSerialNumber();
	}
	
	@Override
	public String getSimId2(){
		return "";
	}
	
	@Override
	public String getImei() {
		String imei1 = getDeviceIdGemini(PlatformUtil.ZH_SIM1);
		String imei2 = getDeviceIdGemini(PlatformUtil.ZH_SIM2);
		if (imei1 != null) {
			return imei1;
		}
		if (imei2 != null) {
			return imei2;
		}
		return "";
	}

	@Override
	public String getScAddress1() {
		return getScAddressGemini(PlatformUtil.ZH_SIM1);
	}

	@Override
	public String getScAddress2() {
		return getScAddressGemini(PlatformUtil.ZH_SIM2);
	}

	@Override
	public boolean isSupportGetScAddress() {
		return true;
	}

	@Override
	public String getPhoneNumber1() {
		return getPhoneNumberGemini(PlatformUtil.ZH_SIM1);
	}

	@Override
	public String getPhoneNumber2() {
		return getPhoneNumberGemini(PlatformUtil.ZH_SIM2);
	}

	@Override
	public boolean isSimInCall(int sim) {
		if (telMgr == null) {
			return false;
		}
		if (getCallStateGemini(sim) != TelephonyManager.CALL_STATE_IDLE) {
			return true;
		}
		return false;
	}

	public static boolean isMultiSim(Context ctx) {
		boolean ret = false;
		try {
			Class<?> FeatureOption = Class
					.forName("com.mediatek.featureoption.FeatureOption");
			Field MTK_GEMINI_SUPPORT = FeatureOption
					.getDeclaredField("MTK_GEMINI_SUPPORT");
			ret = MTK_GEMINI_SUPPORT.getBoolean(null);
		} catch (Exception e) {
			// MTK_And 4.1需要用下面的方式判断
			try {
				TelephonyManager tm = (TelephonyManager) ctx
						.getSystemService(Context.TELEPHONY_SERVICE);
				if (tm != null) {
					Field mtkGeminiSupport = tm.getClass().getDeclaredField(
							"mtkGeminiSupport");
					boolean isAccessibleBackup = mtkGeminiSupport
							.isAccessible();
					mtkGeminiSupport.setAccessible(true);
					ret = mtkGeminiSupport.getBoolean(null);
					mtkGeminiSupport.setAccessible(isAccessibleBackup);
				}
			} catch (Exception e1) {

				ret = false;
			}
		}
		//判断卡槽情况
		if (ret == true){
			TelephonyManager telmngr = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			int simtate1 = getSimStateGemini(telmngr, PlatformUtil.ZH_SIM1);
			int simtate2 = getSimStateGemini(telmngr, PlatformUtil.ZH_SIM2);
			if (simtate1 != TelephonyManager.SIM_STATE_READY 
					|| simtate2 != TelephonyManager.SIM_STATE_READY){
				ret = false;
			}
		}
		
		return ret;
	}

	private String getSimOperatorGemini(int simId) {
		String SimOperator = "";
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getSimOperatorGeminiMethod = telMgr.getClass()
					.getDeclaredMethod("getSimOperatorGemini", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = simId;
			SimOperator = (String) getSimOperatorGeminiMethod.invoke(telMgr,
					paramObject);
		} catch (Exception e) {
			e.printStackTrace();
			SimOperator = "";
		}
		return SimOperator;
	}

	private Object getITelephony() {
		try {
			Method getITelephonyMethod = Class.forName(
					telMgr.getClass().getName()).getDeclaredMethod(
					"getITelephony", new Class[0]);
			boolean bool = getITelephonyMethod.isAccessible();
			getITelephonyMethod.setAccessible(true);
			Object ITelephonyObject = (Object) getITelephonyMethod.invoke(
					telMgr, new Object[0]);
			getITelephonyMethod.setAccessible(bool);
			return ITelephonyObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getScAddressGemini(int simId) {
		String scAddress = "";
		try {
			Class<?> ITelephonyClass = Class
					.forName("com.android.internal.telephony.ITelephony");
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getScAddressGeminiMethod = ITelephonyClass
					.getDeclaredMethod("getScAddressGemini", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = simId;
			scAddress = (String) getScAddressGeminiMethod.invoke(
					getITelephony(), paramObject);
		} catch (Exception e) {
			e.printStackTrace();
			scAddress = "";
		}
		return scAddress;
	}

	private String getSubscriberIdGemini(int simId) {
		String SubscriberId = "";
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getSubscriberIdGeminiMethod = telMgr.getClass()
					.getDeclaredMethod("getSubscriberIdGemini", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = simId;
			SubscriberId = (String) getSubscriberIdGeminiMethod.invoke(telMgr,
					paramObject);
		} catch (Exception e) {
			e.printStackTrace();
			SubscriberId = "";
		}
		return SubscriberId;
	}

	private String getDeviceIdGemini(int simId) {
		String DeviceId = "";
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getDeviceIdGeminiMethod = telMgr.getClass()
					.getDeclaredMethod("getDeviceIdGemini", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = simId;
			DeviceId = (String) getDeviceIdGeminiMethod.invoke(telMgr,
					paramObject);
		} catch (Exception e) {
			e.printStackTrace();
			DeviceId = "";
		}
		return DeviceId;
	}

	private static int getSimStateGemini(TelephonyManager telmngr, int simId) {
		int SimState = TelephonyManager.SIM_STATE_ABSENT;
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getSimStateGeminiMethod = telmngr.getClass()
					.getDeclaredMethod("getSimStateGemini", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = simId;
			SimState = (Integer) getSimStateGeminiMethod.invoke(telmngr,
					paramObject);
		} catch (Exception e) {
			e.printStackTrace();
			SimState = TelephonyManager.SIM_STATE_ABSENT;
		}
		return SimState;
	}

	private int getCallStateGemini(int simId) {
		int CallState = TelephonyManager.CALL_STATE_IDLE;
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getCallStateGeminiMethod = telMgr.getClass()
					.getDeclaredMethod("getCallStateGemini", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = simId;
			CallState = (Integer) getCallStateGeminiMethod.invoke(telMgr,
					paramObject);
		} catch (Exception e) {
			e.printStackTrace();
			CallState = TelephonyManager.CALL_STATE_IDLE;
		}
		return CallState;
	}

	private String getPhoneNumberGemini(int simId) {
		String phoenNumber = "";
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getCallStateGeminiMethod = telMgr.getClass()
					.getDeclaredMethod("getLine1NumberGemini", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = simId;
			phoenNumber = (String) getCallStateGeminiMethod.invoke(telMgr,
					paramObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phoenNumber;
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
