package com.android.sdk.plugin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ApkManager {
	// 0 installTime ;1versionName; 2 apkname;3 pkgname(packagename不在这里获取)
	@SuppressLint("NewApi")
	public static String[] getPackageInfo(Context mContext) {
		String result[] = new String[3];
		PackageManager packageManager = mContext.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					mContext.getPackageName(), 0);
			long firstInstallTime = packageInfo.firstInstallTime;// 应用第一次安装的时间
			Date date = new Date(firstInstallTime);
			result[0] = new SimpleDateFormat("yyyy年MM月dd日HH时mm分",
					Locale.getDefault()).format(date);
			result[1] = packageInfo.versionName;// 应用现在的版本名称
			ApplicationInfo applicationInfo = packageManager
					.getApplicationInfo(mContext.getPackageName(), 0);
			result[2] = (String) packageManager
					.getApplicationLabel(applicationInfo);
			// result[3] = (String) applicationInfo.loadLabel(packageManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
