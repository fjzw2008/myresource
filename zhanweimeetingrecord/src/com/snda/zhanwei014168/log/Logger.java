package com.snda.zhanwei014168.log;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.snda.zhanwei014168.MeetingApplication;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

/*****************************************
 * 日志记录工具类
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 * @author 源作者：qieke.ruanzijie
 *****************************************/
public class Logger {

	private static final String TAG = Logger.class.getName();
	/*
	 * log级别
	 */
	public static final int LOG_LEVEL_INFO = 0;
	public static final int LOG_LEVEL_DEBUG = 1;
	public static final int LOG_LEVEL_EXCEPTION = 2;
	public static final int LOG_LEVEL_CRASH = 3;
	/*
	 * 日志文件名
	 */
	private static String LOG_PATH = "";
	private static final String LOG_INFO_FILENAME = "LOG_I";
	private static final String LOG_DEBUG_FILENAME = "LOG_D";
	private static final String LOG_EXCEPTION_FILENAME = "LOG_E";
	private static final String LOG_CRASH_FILENAME = "LOG_C";
	private static final String LOG_FILE_EXTNAME = ".log";
	/*
	 * 日志文件大小
	 */
	public static final int CRASH_LOG_MAX_FILE_LENGTH = 2 * 1024;
	public static final int EXCEPTION_LOG_MAX_FILE_LENGTH = 500 * 1024;
	
	private static Logger m_instance = null;
	private Object FILE_LOCK = new Object();

	public synchronized static Logger getInstance() {
		if (m_instance == null) {
			m_instance = new Logger();
		} 
		return m_instance;
	}
	
	private Logger() {
		LOG_PATH = createLogPath();
	}

	/**
	 * log文件存储路径
	 * @return
	 */
	private String createLogPath(){
		File dir = new File(Environment.getExternalStorageDirectory(), "/snda/meeting"+File.separator+"log");
		if(!dir.exists() || !dir.isDirectory()){
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}
	
	public void d(String tag, String message){
		Log.d(tag, message);
		saveLogToFile(String.format("%s: %s", tag, message), LOG_LEVEL_DEBUG);
	}
	
	public void i(String tag, String message){
		Log.i(tag, message);
		saveLogToFile(String.format("%s: %s", tag, message), LOG_LEVEL_INFO);
	}
	
	public void e(String tag, String message){
		Log.e(tag, message);
		saveLogToFile(String.format("%s: %s", tag, message), LOG_LEVEL_EXCEPTION);
	}
	
	public void e(String tag, Throwable ex){
		Log.e(tag, "Qieke Exception:", ex);
		saveLogToFile(String.format("%s: %s", tag, getDebugReport(ex)), LOG_LEVEL_EXCEPTION);
	}
	
	//记录crash日志
	private String getDebugReport(Throwable exception){
		NumberFormat theFormatter = new DecimalFormat("#0.");
		StringBuilder theErrReport = new StringBuilder();
		theErrReport.append(MeetingApplication.getContext().getPackageName()+" generated the following exception:\n");
		if(exception != null){
			theErrReport.append(exception.toString()+"\n\n");
			//stack trace
			StackTraceElement[] theStackTrace = exception.getStackTrace();
			if(theStackTrace.length > 0){
				theErrReport.append("======== Stack trace =======\n");
				int length = theStackTrace.length;
				for(int i=0;i<length;i++){
					theErrReport.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
				}
				theErrReport.append("=====================\n\n");
			}
			Throwable theCause = exception.getCause();
			if(theCause != null){
				theErrReport.append("======== Cause ========\n");
				theErrReport.append(theCause.toString()+"\n\n");
				theStackTrace = theCause.getStackTrace();
				int length = theStackTrace.length;
				for(int i=0;i<length;i++){
					theErrReport.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
				}
				theErrReport.append("================\n\n");
			}
			PackageManager pm = MeetingApplication.getContext().getPackageManager();
			PackageInfo pi;
			try{
				pi = pm.getPackageInfo(MeetingApplication.getContext().getPackageName(), 0);
			}catch(NameNotFoundException e){
				pi = new PackageInfo();
				pi.versionName = "unknown";
				pi.versionCode = 0;
			}
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			theErrReport.append("======== Environment =======\n");
			theErrReport.append("Time="+format.format(now)+"\n");
			theErrReport.append("Device="+Build.FINGERPRINT+"\n");
			try {
				Field mfField = Build.class.getField("MANUFACTURER");
				theErrReport.append("Manufacturer="+mfField.get(null)+"\n");
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			theErrReport.append("Model="+Build.MODEL+"\n");
			theErrReport.append("Product="+Build.PRODUCT+"\n");
			theErrReport.append("App="+MeetingApplication.getContext().getPackageName()+", version "+pi.versionName+" (build "+pi.versionCode+")\n");
			theErrReport.append("=========================\nEnd Report");
		}else{
			theErrReport.append("the exception object is null\n");
		}
		
		return theErrReport.toString();
	}
	
	public void saveCrashExceptionLog(String tag, Throwable exception){
		String report = getDebugReport(exception);
		Log.d(tag, report);
		//写入文件
		saveLogToFile(report, LOG_LEVEL_CRASH);
	}
	
	/**
	 * 将日志写入文件。
	 * crash日志每个文件最大为2K，超过大小的进行拆分。
	 * 对于拆分的文件命名为文件名加时间
	 * exception日志，每个文件最大为500k，超过大小的进行拆分，最多存在两个，旧的在后面加上_B的后缀
	 * debug日志，只有在debug模式下才会写入文件，且无文件大小限制
	 * info日志，同上
	 * @param message
	 */
	public void saveLogToFile(String message, int mode){
		String text = message;
		//sdcard可用
		if(MeetingApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				LOG_PATH = createLogPath();
				switch(mode){
				case LOG_LEVEL_INFO:
				case LOG_LEVEL_DEBUG:
					String dfullName = "";
					if(mode == LOG_LEVEL_INFO){
						dfullName = LOG_PATH + File.separator + LOG_INFO_FILENAME + LOG_FILE_EXTNAME;
					}else{
						dfullName = LOG_PATH + File.separator + LOG_DEBUG_FILENAME + LOG_FILE_EXTNAME;
					}
					String currentTimeString = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss ").format(new Date());
					text = currentTimeString + message;
					writeFile(dfullName, text);
					break;
				case LOG_LEVEL_EXCEPTION:
					String efullName = LOG_PATH + File.separator + LOG_EXCEPTION_FILENAME + LOG_FILE_EXTNAME;
					File efile = new File(efullName);
					if(efile.exists() && efile.isFile()){
						if(efile.length() >= EXCEPTION_LOG_MAX_FILE_LENGTH){
							String newEFileName = LOG_PATH + File.separator + LOG_EXCEPTION_FILENAME + "_B" + LOG_FILE_EXTNAME;
							File bfile = new File(newEFileName);
							if(bfile.exists() && bfile.isFile()){
								deleteFile(newEFileName);
							}
							efile.renameTo(new File(newEFileName));
						}
					}
					writeFile(efullName, text);
					break;
				case LOG_LEVEL_CRASH:
					String fullName = LOG_PATH + File.separator + LOG_CRASH_FILENAME + LOG_FILE_EXTNAME;
					File file = new File(fullName);
					if(file.exists() && file.isFile()){
						if(file.length() >= CRASH_LOG_MAX_FILE_LENGTH){
							String newFileName = LOG_PATH + File.separator + LOG_CRASH_FILENAME 
														  + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
														  + LOG_FILE_EXTNAME;
							file.renameTo(new File(newFileName));
						}
					}
					writeFile(fullName, text);
					break;
				default:break;
				}
			}
		}
	}
	
	/**
	 * 写入文件
	 * @param data
	 */
	private void writeFile(String fileName, String data) {
		BufferedWriter buf = null;
		try {
			buf = new BufferedWriter(new FileWriter(fileName, true));
			buf.write(data, 0, data.length());
			buf.newLine();
		}catch(Exception e) {
		}finally{
			try{
				if(buf != null) buf.close();
			}catch(IOException e){
			}
		}
	
	}
	
	/**
	 * 删除文件
	 * @param fullFileName
	 */
	public void deleteFile(String fullFileName){
		if(MeetingApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				File file = new File(fullFileName);
				if(file.exists()){
					file.delete();
				}
			}
		}
	}
	/**
	 * 删除某一类型的日志
	 * @param mode
	 */
	public void deleteFileByMode(int mode){
		if(MeetingApplication.isSDCardAvailable()){
			synchronized(FILE_LOCK){
				String name = "";
				switch(mode){
				case LOG_LEVEL_INFO:
					name = LOG_INFO_FILENAME;
					break;
				case LOG_LEVEL_DEBUG:
					name = LOG_DEBUG_FILENAME;
					break;
				case LOG_LEVEL_EXCEPTION:
					name = LOG_EXCEPTION_FILENAME;
					break;
				case LOG_LEVEL_CRASH:
					name = LOG_CRASH_FILENAME;
					break;
				default:break;
				}
				if(!name.equals("")){
					LOG_PATH = createLogPath();
					File dir = new File(LOG_PATH);
					File[] files = dir.listFiles();
					int length = files.length;
					for(int i=0;i<length;i++){
						if(files[i].getName().startsWith(name)){
							File f = files[i];
							f.delete();
						}
					}
				}
			}
		}
	}
	
}
