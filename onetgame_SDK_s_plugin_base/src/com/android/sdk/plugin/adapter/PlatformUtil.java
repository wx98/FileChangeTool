package com.android.sdk.plugin.adapter;

public class PlatformUtil {
	// operator
	public final static int OPERATOR_UNKOWN = 0;
	public final static int OPERATOR_CMCC = 1;
	public final static int OPERATOR_CUCC = 2;
	public final static int OPERATOR_CTCC = 3;

	// sim
	public final static int ZH_SIM1 = 0;
	public final static int ZH_SIM2 = 1;

	// net type
	public static final int NET_TYPE_UNKNOW = 0;
	public static final int NET_TYPE_WIFI = 1;
	public static final int NET_TYPE_2G = 2;
	public static final int NET_TYPE_3G = 3;
	public static final int NET_TYPE_4G = 4;

	public static int getOperator(String opstr) {
		if (opstr == null || opstr.length() == 0) {
			return OPERATOR_UNKOWN;
		} else {
			if (opstr.equals("46000") || opstr.equals("46002")
					|| opstr.equals("46007")) {
				return OPERATOR_CMCC;
			} else if (opstr.equals("46001")) {
				return OPERATOR_CUCC;
			} else if (opstr.equals("46003")) {
				return OPERATOR_CTCC;
			}
		}
		return OPERATOR_UNKOWN;
	}

	/*
	 * MD5运算
	 */
	// public static String getMD5(String str) {
	// try {
	// MessageDigest md = MessageDigest.getInstance("MD5");
	// byte[] messageDigest = md.digest(str.getBytes());
	// BigInteger number = new BigInteger(1, messageDigest);
	// String md5_value = number.toString(16);
	// while (md5_value.length() < 32) {
	// md5_value = "0" + md5_value;
	// }
	// return md5_value;
	// } catch (NoSuchAlgorithmException e) {
	// return null;
	// }
	// }

	// public static String getPackageSign(Context context) {
	// try {
	// PackageInfo info = null;
	// info = context.getPackageManager().getPackageInfo(
	// context.getPackageName(), PackageManager.GET_SIGNATURES);
	// if (info != null) {
	// Signature[] signs = info.signatures;
	// if (signs != null && signs.length != 0) {
	// return signs[0].toCharsString();
	// }
	// }
	// } catch (NameNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// return "";
	// }

	// public static String getPackageSignMD5(Context context) {
	// String ret = "";
	// String sign = getPackageSign(context);
	// if (sign != null && sign.length() != 0) {
	// ret = getMD5(sign);
	// }
	// return ret;
	// }

	// /** 判断某权限是否存在 */
	// public static boolean isSupportedPerimission(Context context,
	// String permissionStr) {
	// try {
	// context.enforceCallingOrSelfPermission(permissionStr,
	// "zhpay need " + permissionStr);
	// return true;
	// } catch (Throwable se) {
	// return false;
	// }
	// }
}
