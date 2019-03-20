package com.android.sdk.plugin.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.android.sdk.plugin.adapter.PlatformAdapter;
import com.android.sdk.plugin.adapter.PlatformUtil;

public class DeviceUtil {
	public static String getDeviceid(Context mContext) {
		return PlatformAdapter.getSimInfo(mContext).getImei();
	}

	public static String getDeviceid_2(Context mContext) {
		return "";
	}

	public static String getImsi(Context mContext) {
		return PlatformAdapter.getSimInfo(mContext).getImsi1();
	}

	public static String getImsi_2(Context mContext) {
		// 判断 wanghc
		return PlatformAdapter.getSimInfo(mContext).getImsi2();
	}

	public static String getIccid(Context mContext) {
		return PlatformAdapter.getSimInfo(mContext).getSimId1();
	}

	public static String getIccid_2(Context mContext) {
		return PlatformAdapter.getSimInfo(mContext).getSimId2();
	}

	public static String getOp(Context mContext) {
		return String.valueOf(PlatformAdapter.getSimInfo(mContext).getOperator1());
	}

	public static String getOp_2(Context mContext) {
		return String.valueOf(PlatformAdapter.getSimInfo(mContext).getOperator2());
	}

	public static String getPhoneNum(Context mContext) {
		return PlatformAdapter.getSimInfo(mContext).getPhoneNumber1();
	}

	public static String getPhoneNum_2(Context mContext) {
		return PlatformAdapter.getSimInfo(mContext).getPhoneNumber2();
	}

	public static String getAndroidid(Context mContext) {
		try {
			return Secure.getString(mContext.getContentResolver(),
					Secure.ANDROID_ID);
		} catch (RuntimeException e) {
			Log.i("info", "exception" + e.getMessage());
			return "000000000000000";
		}
	}

	public static String getCID(Context mContext) {
		return String.valueOf(PlatformAdapter.getSimInfo(mContext).getCID());
	}

	public static String getLAC(Context mContext) {
		return String.valueOf(PlatformAdapter.getSimInfo(mContext).getLAC());
	}

	public static String getMNC(Context mContext) {
		return String.valueOf(PlatformAdapter.getSimInfo(mContext).getMNC());
	}

	public static String getMCC(Context mContext) {
		return String.valueOf(PlatformAdapter.getSimInfo(mContext).getMCC());
	}

	// 计算屏幕分辨率
	@SuppressLint("NewApi")
	public static String getResolution(Context mContext) {
		try {
			WindowManager wm = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			
			String result = dm.widthPixels + "x" + dm.heightPixels;
			try {
				Point point = new Point();
				wm.getDefaultDisplay().getRealSize(point);  
				result = point.x + "x" + point.y;
				
			} catch (Exception e) {
				MyUncaughtExceptionHandler.writeError(e, "getResolution_inner");
			}
			return result;
		} catch (Exception e) {
			MyUncaughtExceptionHandler.writeError(e, "getResolution");
			return StringUtil.EMPTY_STRING;
		}
	}

	// 计算屏幕亮度
	public static String getScreenBrightness(Context mContext) {
		int value = 0;
		ContentResolver cr = mContext.getContentResolver();
		try {
			value = Settings.System.getInt(cr,
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
		}
		return value * 100 / 255 + "%";
	}

	public static String getDeviceModel() {
		try {
			String var0 = Build.MODEL;
			return var0 != null && var0.trim().length() > 0 ? var0
					: StringUtil.EMPTY_STRING;
		} catch (Exception e) {

		}
		return StringUtil.EMPTY_STRING;
	}

	public static String getDeviceBrand() {
		try {
			return StringUtil.makeSafe(Build.BRAND);
		} catch (Exception e) {
		}
		return StringUtil.EMPTY_STRING;
	}

	public static String getDeviceManufacturer() {
		try {
			return Build.MANUFACTURER;
		} catch (Exception e) {

		}
		return StringUtil.EMPTY_STRING;
	}

	public static int getIsRooted() {
		try {
			String su = "/system/bin/su";
			String xsu = "/system/xbin/su";

			File file = new File(su);
			if (file.exists()) {
				return 1;
			}
			file = new File(xsu);
			if (file.exists()) {
				return 1;
			}
		} catch (Exception e) {
		}
		return 0;
	}

	public static String getLanguage(Context context) {
		try {
			String lang = context.getResources().getConfiguration().locale
					.getLanguage();
			return lang;
		} catch (Exception e) {

		}
		return StringUtil.EMPTY_STRING;
	}
	
	public static String getDevice() {
		try {
			return Build.DEVICE;
		} catch (Exception e) {

		}
		return StringUtil.EMPTY_STRING;
	}
	
	public static int getSDKVersionInt() {
		try {
			return Build.VERSION.SDK_INT;
		} catch (Exception e) {

		}
		return 0;
	}

	public static String getSDKVerName() {
		try {
			String var0 = VERSION.RELEASE;
			return var0 != null && var0.trim().length() > 0 ? var0 : StringUtil.EMPTY_STRING;
			// return android.os.Build.VERSION.RELEASE;
		} catch (Exception e) {

		}
		return StringUtil.EMPTY_STRING;
	}
	
	public static int getCpuCoreNum() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}
		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			// Default to return 1 core
			return 1;
		}
	}

	public static String getMaxCpuFreq() {
		String result = StringUtil.EMPTY_STRING;
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}
	
	public static String getUptime() {
		return String.valueOf(SystemClock.elapsedRealtime());
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getTotalRom() {
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = 0;
			long totalBlocks = 0;
//			long blockSize = stat.getBlockSizeLong();
//			long totalBlocks = stat.getBlockCountLong();
			if(juegement()){
				try {
					blockSize = stat.getBlockSize();
					totalBlocks = stat.getBlockCount();
				} catch (Exception e) {
				}
			}else{
				blockSize = stat.getBlockSize();
				totalBlocks = stat.getBlockCount();
			}
			return totalBlocks * blockSize / 1024 / 1024;
		} catch (Exception e) {

		}
		return 0;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getAvailRom() {
		try {
			// Available rom memory
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = 0;
			long availableBlocks = 0;
			if(juegement()){
				try {
					blockSize = stat.getBlockSizeLong();
					availableBlocks = stat.getAvailableBlocksLong();
				} catch (Exception e) {
					blockSize = stat.getBlockSize();
					availableBlocks = stat.getAvailableBlocks();
				}
			}else{
				blockSize = stat.getBlockSize();
				availableBlocks = stat.getAvailableBlocks();
			}
			long romavail = blockSize * availableBlocks / 1024 / 1024;
			return romavail;
		} catch (Exception e) {

		}
		return 0;
	}
	
	public static int getNetworkType(Context context) {
		if (NetworkUtil.isWifiNetworkAvailable(context)) {
			return PlatformUtil.NET_TYPE_WIFI;
		}

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			switch (tm.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_UMTS: /* UMTS (3G) */
			case TelephonyManager.NETWORK_TYPE_HSDPA: /*
													 * HSDPA (3G - Transitional)
													 */
			case TelephonyManager.NETWORK_TYPE_HSUPA: /*
													 * HSUPA (3G - Transitional)
													 */
			case TelephonyManager.NETWORK_TYPE_HSPA: /* HSPA (3G - Transitional) */
			case TelephonyManager.NETWORK_TYPE_EVDO_0: /* EVDO revision 0 (3G) */
			case TelephonyManager.NETWORK_TYPE_EVDO_A: /*
														 * EVDO revision A (3G -
														 * Transitional)
														 */
				/* using literals to make backwards compatible */
			case 12: // NETWORK_TYPE_EVDO_B /*EVDO revision B (3G -
						// Transitional)*/
				return PlatformUtil.NET_TYPE_3G;

			case TelephonyManager.NETWORK_TYPE_IDEN: /* iDen (2G) */
			case TelephonyManager.NETWORK_TYPE_1xRTT: /*
													 * 1xRTT (2G - Transitional)
													 */
			case TelephonyManager.NETWORK_TYPE_GPRS: /* GPRS (2.5G) */
			case TelephonyManager.NETWORK_TYPE_EDGE: /* EDGE (2.75G) */
			case TelephonyManager.NETWORK_TYPE_CDMA: /*
													 * CDMA: Either IS95A or
													 * IS95B (2G)
													 */
				return PlatformUtil.NET_TYPE_2G;
			}
		}

		return PlatformUtil.NET_TYPE_UNKNOW;
	}
	
	private static boolean juegement(){
		//判断api版本是否大于18
		if(getSDKVersionInt() >= 18){
			return true;
		}else{
			return false;
		}
	}

}
