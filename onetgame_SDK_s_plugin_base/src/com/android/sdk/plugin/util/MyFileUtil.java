package com.android.sdk.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyFileUtil {
	public static void writeFile(String filePath, byte[] data) {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(filePath, true);
			fos.write(data);
			fos.flush();
		} catch (FileNotFoundException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
		}
	}
	
	public static byte[] readFile(String filePath) {
		FileInputStream fis = null;
		byte[] data = null;

		File f = new File(filePath);
		if (!f.exists()) {
			return null;
		}
		try {
			fis = new FileInputStream(f);
			int length = fis.available();
			data = new byte[length];
			if (data != null) {
				fis.read(data);
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fis = null;
			}
		}
		return data;
	}
}
