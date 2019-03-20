package com.android.sdk.shcore;

import java.lang.Thread.UncaughtExceptionHandler;

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
    
    public static void writeError(Exception ex, String append){
        //不做处理
    }
    
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		//不做处理
	}

}
