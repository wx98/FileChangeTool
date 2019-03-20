package com.android.sdk.plugin.adapter;

import java.lang.reflect.Method;

import com.android.sdk.plugin.util.PreferenceUtil;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SimQcomV40 implements SimInfo{
	private TelephonyManager telMgr;

	public SimQcomV40(Context ctx) {
		telMgr = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	@Override
	public String getOperatorStr1() {
		return getSimOperator(PlatformUtil.ZH_SIM1);
	}

	@Override
	public String getOperatorStr2() {
		return getSimOperator(PlatformUtil.ZH_SIM2);
	}

	@Override
	public boolean isSim1CardOk() {
		boolean simok = false;
		if (getSimState(telMgr, PlatformUtil.ZH_SIM1) == TelephonyManager.SIM_STATE_READY) {
			simok = true;
		}
		return simok;
	}

	@Override
	public boolean isSim2CardOk() {
		boolean simok = false;
		if (getSimState(telMgr, PlatformUtil.ZH_SIM2) == TelephonyManager.SIM_STATE_READY) {
			simok = true;
		}
		return simok;
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
		return getSubscriberId(PlatformUtil.ZH_SIM1);
	}

	@Override
	public String getImsi2() {
		return getSubscriberId(PlatformUtil.ZH_SIM2);
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
		String imei1 = getDeviceId(PlatformUtil.ZH_SIM1);
		String imei2 = getDeviceId(PlatformUtil.ZH_SIM2);
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
		return getLine1Number(PlatformUtil.ZH_SIM1);
	}

	@Override
	public String getPhoneNumber2() {
		return getLine1Number(PlatformUtil.ZH_SIM2);
	}

	@Override
	public boolean isSimInCall(int sim) {
		if (telMgr == null) {
			return false;
		}
		if (getCallState(sim) != TelephonyManager.CALL_STATE_IDLE) {
			return true;
		}
		return false;
	}
	
	public static boolean isMultiSim(Context ctx){
		boolean ret = false;
		try{
//			Class<?> telMngrClass = Class.forName("android.telephony.TelephonyManager");
//			Method isMultiSimEnabled = telMngrClass.getDeclaredMethod("isMultiSimEnabled");
//			ret = (Boolean)isMultiSimEnabled.invoke(null);
//			ret = android.os.SystemProperties.getBoolean("persist.dsds.enabled", false);
			
			ret = PreferenceUtil.getSystemPropertiesBoolean("persist.dsds.enabled", false);
		}catch(Exception e){
			ret = false;
		}
		//判断卡槽情况
		if (ret == true){
			TelephonyManager telmngr = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			int simtate1 = getSimState(telmngr, PlatformUtil.ZH_SIM1);
			int simtate2 = getSimState(telmngr, PlatformUtil.ZH_SIM2);
			if (simtate1 != TelephonyManager.SIM_STATE_READY 
					|| simtate2 != TelephonyManager.SIM_STATE_READY){
				ret = false;
			}
		}
		
		return ret;
	}


	private String getSubscriberId(int sim) {
		String imsi = null;
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getSubscriberIdMethod = telMgr.getClass()
					.getDeclaredMethod("getSubscriberId", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = sim;
			imsi = (String) getSubscriberIdMethod.invoke(telMgr, paramObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imsi;
	}

	private String getDeviceId(int sim) {
		String imei = null;
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getDeviceIdMethod = telMgr.getClass().getDeclaredMethod(
					"getDeviceId", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = sim;
			imei = (String) getDeviceIdMethod.invoke(telMgr, paramObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imei;
	}

	private String getSimOperator(int sim) {
		String opstr = null;
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getSimOperatorMethod = telMgr.getClass()
					.getDeclaredMethod("getSimOperator", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = sim;
			opstr = (String) getSimOperatorMethod.invoke(telMgr, paramObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return opstr;
	}

	private String getLine1Number(int sim) {
		String num = null;
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getLine1NumberMethod = telMgr.getClass()
					.getDeclaredMethod("getLine1Number", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = sim;
			num = (String) getLine1NumberMethod.invoke(telMgr, paramObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	private static int getSimState(TelephonyManager telmngr, int sim) {
		int state = TelephonyManager.SIM_STATE_ABSENT;
		try {
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getSimStateMethod = telmngr.getClass().getDeclaredMethod(
					"getSimState", paramClass);

			Object[] paramObject = new Object[1];
			paramObject[0] = sim;
			state = (Integer) getSimStateMethod.invoke(telmngr, paramObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}
	
	private int getCallState(int simId){
		int CallState = TelephonyManager.CALL_STATE_IDLE;
		try{
			Class<?>[] paramClass = new Class[1];
			paramClass[0] = int.class;
			Method getCallStateGeminiMethod = telMgr.getClass().getDeclaredMethod("getCallState", paramClass);
	
			Object[] paramObject = new Object[1];
	        paramObject[0] = simId;
	        CallState = (Integer)getCallStateGeminiMethod.invoke(telMgr, paramObject);
		}catch(Exception e){
			e.printStackTrace();
			CallState = TelephonyManager.CALL_STATE_IDLE;
		}
		return CallState;
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
