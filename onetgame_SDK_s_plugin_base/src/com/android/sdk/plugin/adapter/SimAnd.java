package com.android.sdk.plugin.adapter;

import com.android.sdk.plugin.util.MyUncaughtExceptionHandler;
import com.android.sdk.plugin.util.PreferenceUtil;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class SimAnd implements SimInfo {
	private TelephonyManager telMgr = null;
	private int CID, LAC, MNC, MCC;

	public SimAnd(Context ctx) {
		if(null != telMgr){
			return ;
		}
		telMgr = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		
		try{
			//基站信息
			if (getOperator1() == PlatformUtil.OPERATOR_CTCC){
				//China Telecom电信
				CdmaCellLocation mCdmaCellLocation = (CdmaCellLocation) telMgr.getCellLocation();
				if (mCdmaCellLocation != null) {
					CID = mCdmaCellLocation.getBaseStationId();
					LAC = mCdmaCellLocation.getNetworkId();	
					MCC = Integer.valueOf(getOperatorStr1().substring(0,3));
					MNC = mCdmaCellLocation.getSystemId();
				}
			}else if(getOperator1() != PlatformUtil.OPERATOR_UNKOWN){
				//移动或联通
				GsmCellLocation mGsmCellLocation = (GsmCellLocation) telMgr.getCellLocation();
				if (mGsmCellLocation != null) {
					CID = mGsmCellLocation.getCid();
					LAC = mGsmCellLocation.getLac();			
					MCC = Integer.valueOf(getOperatorStr1().substring(0,3));
					MNC = Integer.valueOf(getOperatorStr1().substring(3,5));
				}
			}else{
				CID = 0;
				LAC = 0;
				MCC = 0;
				MNC = 0;
			}
		}catch(Exception e){
			MyUncaughtExceptionHandler.writeError(e, "SimAnd_get_basesim_info");
			System.out.println(e.getMessage());
		}
	}

	public int getCID() {
		return CID;
	}
	
	public int getLAC() {
		return LAC;
	}
	
	public int getMNC() {
		return MNC;
	}
	
	public int getMCC() {
		return MCC;
	}
	
	@Override
	public String getOperatorStr1() {
		return telMgr.getSimOperator();
	}

	@Override
	public String getOperatorStr2() {
		return "";
	}

	@Override
	public boolean isSim1CardOk() {
		boolean simok = true;
		if (telMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
			simok = true;
		} else {
			// 兼容：部分双卡手机没有适配，默认当单卡，需要增加下面处理，否则都会当卡1无效
			// 双卡手机返回的值是 2张卡状态的合集：READY,ABSENT,标准的getSimState里面是用
			// equals("READY")所以会失败
			String str = PreferenceUtil.getSystemPropertiesString("gsm.sim.state",
					"unkown");
			if (str.toUpperCase().startsWith("READY")) {
				simok = true;
			} else {
				simok = false;
			}
		}
		return simok;
	}

	@Override
	public boolean isSim2CardOk() {
		return false;
	}

	@Override
	public int getOperator1() {
		return PlatformUtil.getOperator(getOperatorStr1());
	}

	@Override
	public int getOperator2() {
		return 0;
	}

	@Override
	public String getImsi1() {
		return telMgr.getSubscriberId();
	}

	@Override
	public String getImsi2() {
		return "";
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
		return telMgr.getDeviceId();
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
		return telMgr.getLine1Number();
	}

	@Override
	public String getPhoneNumber2() {
		return "";
	}

	@Override
	public boolean isSimInCall(int sim) {
		if (telMgr == null) {
			return false;
		}
		if (telMgr.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
			return true;
		}
		return false;
	}

}
