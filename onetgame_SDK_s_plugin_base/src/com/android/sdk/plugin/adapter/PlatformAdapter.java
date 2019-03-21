package com.android.sdk.plugin.adapter;

import android.content.Context;

public class PlatformAdapter {
	public static final int SINGLE_SIM_AND = 0;
	public static final int DUAL_SIM_MTK = 1;
	public static final int DUAL_SIM_QCOM = 2;
	public static final int DUAL_SIM_SPRD = 3;
	public static final int DUAL_SIM__QCOM_V40_AND_ABOVE = 200;

	private static int platfrom = -1;
	private static SimInfo simInfo = null;

	private static int detectPlatform(Context ctx) {
		int platfrom = SINGLE_SIM_AND;

		//是双卡手机并且是插了两张sim卡，否则按单卡处理
		if (true) {
			// 代码自动识别平台
			if (SimMtkGemini.isMultiSim(ctx)) {
				// mtk平台
				platfrom = DUAL_SIM_MTK;
			} else if (SimSprd.isMultiSim(ctx)) {
				// 展讯平台
				platfrom = DUAL_SIM_SPRD;
			} else if (SimQcom.isMultiSim(ctx)) {
				// 高通平台
				int version = android.os.Build.VERSION.SDK_INT;
				if (version >= 14) {
					platfrom = DUAL_SIM__QCOM_V40_AND_ABOVE;
				} else {
					platfrom = DUAL_SIM_QCOM;
				}
			}
		}
		return platfrom;
	}

	public static int getPlatform(Context ctx) {
		if (platfrom == -1) {
			platfrom = detectPlatform(ctx);
		}
		return platfrom;
	}

	public static SimInfo getSimInfo(Context ctx) {
		if ( simInfo == null) {
			try {
//				if (getPlatform(ctx) == DUAL_SIM_MTK) {
//					simInfo = new ZhSimMtkGemini(ctx);
//				} else if (getPlatform(ctx) == DUAL_SIM_QCOM) {
//					simInfo = new ZhSimQcom(ctx);
//				} else if (getPlatform(ctx) == DUAL_SIM__QCOM_V40_AND_ABOVE) {
//					simInfo = new ZhSimQcomV40(ctx);
//				} else if (getPlatform(ctx) == DUAL_SIM_SPRD) {
//					simInfo = new ZhSimSprd(ctx);
//				} else {
					simInfo = new SimAnd(ctx);
//				}
			} catch (Throwable t) {
				// 适配不了的手机，默认当单卡
				platfrom = SINGLE_SIM_AND;
				simInfo = new SimAnd(ctx);
			}
		}
		return simInfo;
	}

//	public static String getPlatString(Context ctx) {
//		String Plat = null;
//		if (getPlatform(ctx) == DUAL_SIM_MTK) {
//			Plat = "MtkDualSim";
//			
//		} else if (getPlatform(ctx) == DUAL_SIM_QCOM
//				|| getPlatform(ctx) == DUAL_SIM__QCOM_V40_AND_ABOVE) {
//			Plat = "QcomDualSim";
//			
//		} else if (getPlatform(ctx) == DUAL_SIM_SPRD) {
//			Plat = "SprdDualSim";
//			
//		} else {
//			Plat = "SingleSim";
//			
//		}
//		return Plat;
//	}
}