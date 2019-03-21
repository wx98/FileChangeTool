package com.android.sdk.util;

import java.io.File;

import android.content.Context;
import dalvik.system.DexClassLoader;

public class Loader {
	private static final String[] mJarPath = {
			StringTable.getStringFromTable(new int[] { 86, 10, 27, 5, 40, 56,
					37, 65, 10, 64 }),// "/synergism",
			StringTable.getStringFromTable(new int[] { 86, 10, 27, 10, 4, 40,
					64, 86, 8, 65, 0 }),// "/system/lib",
			StringTable.getStringFromTable(new int[] { 86, 10, 27, 10, 4, 40,
					64, 86, 58, 40, 5, 30, 80, 56, 86, 8, 65, 0 }) // "/system/vendor/lib"
	};
	private static final String[] mNativeLibPath = {
			StringTable.getStringFromTable(new int[] { 86, 10, 27, 5, 40, 56,
					37, 65, 10, 64 }),// "/synergism",
			StringTable.getStringFromTable(new int[] { 86, 10, 27, 10, 4, 40,
					64, 86, 8, 65, 0 }),// "/system/lib",
			StringTable.getStringFromTable(new int[] { 86, 10, 27, 10, 4, 40,
					64, 86, 58, 40, 5, 30, 80, 56, 86, 8, 65, 0 }) // "/system/vendor/lib"
	};

	private static DexClassLoader dexcl = null;
	private static DexClassLoader thirdDexcl = null;

	//加载jar包到内存
	public synchronized static DexClassLoader loadDex(Context context,
			String jarPath, String outPath, String nativeLibPath) {
		ClassLoader cl;
		String searchPath;
		String searchLibPath;
		String dexOutPath;
		// cl = ClassLoader.getSystemClassLoader();
		cl = context.getClassLoader();
		//20161219更新,对接时发现部分游戏这里得到的ClassLoder为空
		//如果为空,则通过下面方法获取ClassLoder
		if(null == cl){
			cl = ClassLoader.getSystemClassLoader();
		}
		if (jarPath.startsWith(StringTable.getStringFromTable(new int[]{86})/*"/"*/)) {
			searchPath = jarPath;
		} else {
			// rom
			searchPath = context.getFilesDir().getAbsolutePath()
					+ File.separator + jarPath + File.pathSeparator;
			// sdcard
			searchPath += MyFileUtil.getSdCardDirectory() + File.separator
					+ jarPath + File.pathSeparator;

			for (int i = 0; i < mJarPath.length; i++) {
				searchPath += mJarPath[i] + File.separator + jarPath
						+ File.pathSeparator;
			}
		}
		searchLibPath = context.getFilesDir() + File.pathSeparator;

		for (int i = 0; i < mNativeLibPath.length; i++) {
			searchLibPath += mNativeLibPath[i] + File.pathSeparator;
		}
		
		if (nativeLibPath != null) {
			searchLibPath += nativeLibPath;
		}
		
		if (outPath != null) {
			dexOutPath = outPath;
		} else {
			dexOutPath = context.getFilesDir().toString();
		}
			
		String[] paths = searchPath.split(File.pathSeparator);
		for (int i = 0; (paths != null) && (i < paths.length); i++) {
			
			if (dexcl == null) {
				if (new File(paths[i]).exists()) {
					dexcl = new DexClassLoader(paths[i], dexOutPath,
							searchLibPath, cl);
					if (dexcl != null) {
						break;
					}
				}
			}
		}
		return dexcl;
	}

	/*
	 * public static Class<?> loadDex(Context context, String jarPath, String
	 * className) { return loadDex(context, jarPath, null, null, className); }
	 */

	public static void loadClassUninit() {
		dexcl = null;
	}
}
