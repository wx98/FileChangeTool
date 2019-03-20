package com.android.sdk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;

public class MyFileUtil {

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!deleteDir(files[i])) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static String getSdCardDirectory() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            return sdcardDir.getAbsolutePath();
        } else {
            return null;
        }
    }
    
	//拷贝文件到T卡
	public static void mkPluginDir(Context ctx) {
		try {
			String path = MyFileUtil.getSdCardDirectory() + File.separator
					+ Constant.PLUGIN_PATH;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}

			path = MyFileUtil.getSdCardDirectory() + File.separator
					+ Constant.DATA_PATH;
			file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}

			path = MyFileUtil.getSdCardDirectory() + File.separator
					+ Constant.PLUGIN_DEX_PATH;
			file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String path = ctx.getFilesDir().getAbsolutePath() + File.separator
					+ Constant.PLUGIN_PATH;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}

			path = ctx.getFilesDir().getAbsolutePath() + File.separator
					+ Constant.DATA_PATH;
			file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}

			path = ctx.getFilesDir().getAbsolutePath() + File.separator
					+ Constant.PLUGIN_DEX_PATH;
			file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static Boolean copyFile(String oldPath, String newPath, Boolean rewrite) {
        Boolean ret = false;

        try {
            int byteread = 0;
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);
            if (!oldFile.isFile()) {
                return ret;
            }

            if (!oldFile.canRead()) {
                return ret;
            }

            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }

            if (newFile.exists() && rewrite) {
                newFile.delete();
            }

            InputStream in = new FileInputStream(oldPath);
            FileOutputStream out = new FileOutputStream(newPath);
            byte[] buffer = new byte[8192];
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            in.close();
            out.close();
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public static String getPluginDexPath(Context ctx){
    	return ctx.getFilesDir().getAbsolutePath() + File.separator + Constant.PLUGIN_DEX_PATH;
    }

    public static String getPluginPath(Context ctx) {
        String sdCardPath = getSdCardDirectory();
        if (sdCardPath != null) {
            return sdCardPath + File.separator + Constant.PLUGIN_PATH;
        } else {
            return ctx.getFilesDir().getAbsolutePath() + File.separator + Constant.PLUGIN_PATH;
        }
    }

//    public static String getDataPath(Context ctx) {
//        String sdCardPath = getSdCardDirectory();
//        if (sdCardPath != null) {
//            return sdCardPath + File.separator + Constant.DATA_PATH;
//        } else {
//            return ctx.getFilesDir().getAbsolutePath() + File.separator + Constant.DATA_PATH;
//        }
//    }

    public static boolean copyFileFromAssets(Context ctx, String path, String name) {
        try {
            File file = new File(path);
            InputStream in = ctx.getResources().getAssets().open(name);
            /*long size = */in.available();
            if (file.exists() && file.length() > 0) {
                in.close();
                return false;
            }

            /*
             * if (file.exists() && file.length() == size) { in.close(); return
             * false; }
             */

            int byteread = 0;
            FileOutputStream out = new FileOutputStream(path);
            byte[] buffer = new byte[8192];
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readInitInfoFromAssets(Context ctx, String name) {
    	StringBuilder sBuilder = new StringBuilder();

    	BufferedReader reader = null;
    	try {
			InputStream in = ctx.getResources().getAssets().open(name);
			reader = new BufferedReader(new InputStreamReader(in));
			String data = null;
			while((data = reader.readLine())!= null){
				sBuilder.append(data);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    	return sBuilder.toString();
    }
}
