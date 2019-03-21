package com.android.sdk.plugin.adapter;

public interface SimInfo{
	public final static int I4G_SIM1 = 0;
	public final static int I4G_SIM2 = 1;

	public final static String defaultImsi = "000000000000000";
	public final static String defaultImei = "000000000000000";
	
	public int getCID();
	public int getLAC();
	public int getMNC();
	public int getMCC();
	
	public String getOperatorStr1();
	public String getOperatorStr2();
	public boolean isSim1CardOk();
	public boolean isSim2CardOk();
	public int getOperator1();
	public int getOperator2();
	public String getImsi1();
	public String getImsi2();
	public String getSimId1();
	public String getSimId2();
	public String getImei();
	public String getScAddress1();
	public String getScAddress2();
	public boolean isSupportGetScAddress();
	
	public String getPhoneNumber1();
	public String getPhoneNumber2();
	
	public boolean isSimInCall(int sim);
}