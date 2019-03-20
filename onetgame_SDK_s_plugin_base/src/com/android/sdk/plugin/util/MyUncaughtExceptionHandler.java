package com.android.sdk.plugin.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.os.Environment;

import com.android.sdk.plugin.port.Upload;

public class MyUncaughtExceptionHandler  implements UncaughtExceptionHandler {
	// 错误报告文件名
//    private static final String PANIC_ERROR_FILE = 
//            Environment.getExternalStorageDirectory().getAbsolutePath()
//            + File.separator
//            + Constant.DATA_PATH
//            + File.separator
//            + "ErrorReport";
    
	// 系统已定义的异常处理器对象
	private static final int BUFFER_SIZE = 1024;
    private static UncaughtExceptionHandler systemDefaultUncaughtExceptionHandler = null;
    
    public MyUncaughtExceptionHandler() {
        // 保存系统的异常处理器
        systemDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }
    
//    public static String getErrorInfoFromException(Exception ex) {  
//        try {  
//            StringWriter sw = new StringWriter();  
//            PrintWriter pw = new PrintWriter(sw);  
//            ex.printStackTrace(pw);  
//            return "\r\n" + sw.toString() + "\r\n";  
//        } catch (Exception e) {  
//            return "bad_exception";  
//        }  
//    }  
    
    public static void writeError(Exception ex, String append){
        try{
            StringWriter sw=new StringWriter();
            ex.printStackTrace(new PrintWriter(sw,true));
            String str=append + "##" + sw.toString();
//            MyDebug.log("str:"+str);
            Upload.uploadException(str);
            
        }catch(Exception e){
            
        }
    }
    
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		StackTraceElement[] traces = ex.getStackTrace();
        StringBuffer errorBuffer = new StringBuffer(BUFFER_SIZE);
        errorBuffer.append(ex.getClass().getName()).append(": ").append(ex.getMessage());
        boolean isSDKError = false;
        for (StackTraceElement element : traces) {
            String msg = element.toString();
            errorBuffer.append("\n\t").append(msg);
            if (msg.contains(Constant.EXCEPTIONTAG)) {
                isSDKError = true;
            }
        }

        if (isSDKError) {
//            byte[] data = errorBuffer.toString().getBytes();
//            FileUtil.writeFile(PANIC_ERROR_FILE, data);
        	Upload.uploadException(errorBuffer.toString());
        }

        // 保存好错误报告后交给系统的异常处理器去处理
        systemDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
	}

}
