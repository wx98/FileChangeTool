package com.android.sdk.util;

public class UnitUtil {

	/**
	 * 元转换为分。
	 * @param moneyYuan
	 * @return
	 * @author Synaric
	 */
	public static int strYuan2IntFen(String moneyYuan) {
		int priceUnitFen = 0;
		float priceUnitYuan = Float.valueOf(moneyYuan);
		priceUnitFen = (int) priceUnitYuan * 100;
		return priceUnitFen;
	}
	
	/**
	 * 元转换为分。
	 * @param moneyYuan
	 * @return
	 * @author Synaric
	 */
	public static String strYuan2StrFen(String moneyYuan) {
		int priceUnitFen = strYuan2IntFen(moneyYuan);
		return String.valueOf(priceUnitFen);
	}
	
	/**
	 * 文本元转换为浮点元。
	 * @param moneyYuan
	 * @return
	 * @author Synaric
	 */
	public static Double strYuan2DoubleYuan(String moneyYuan) {
		return Double.valueOf(moneyYuan);
	}
	
	/**
	 * 文本元转换为浮点元。
	 * @param moneyYuan
	 * @return
	 * @author Synaric
	 */
	public static Float strYuan2FloatYuan(String moneyYuan) {
		return Float.valueOf(moneyYuan);
	}
}
