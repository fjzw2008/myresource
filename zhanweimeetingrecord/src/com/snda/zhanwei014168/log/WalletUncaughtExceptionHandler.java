package com.snda.zhanwei014168.log;

import java.lang.Thread.UncaughtExceptionHandler;


/***************************************** * 
 * 监听CRASH异常
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络  *  
 *****************************************/
public class WalletUncaughtExceptionHandler implements UncaughtExceptionHandler{

	private static final String TAG = WalletUncaughtExceptionHandler.class.getName();
	
	private UncaughtExceptionHandler exceptionHandler;
	
	public WalletUncaughtExceptionHandler(){
		this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.getInstance().saveCrashExceptionLog(TAG, ex);
		if(exceptionHandler != null){
			exceptionHandler.uncaughtException(thread, ex);
		}
	}

}
