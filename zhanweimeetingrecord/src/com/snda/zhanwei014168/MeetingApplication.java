package com.snda.zhanwei014168;

import com.snda.sdw.woa.callback.CallBack;
import com.snda.sdw.woa.interfaces.OpenAPI;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.log.WalletUncaughtExceptionHandler;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Environment;

/*****************************************
 * 全局状态保存类
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class MeetingApplication extends Application {

	private static final String TAG = MeetingApplication.class.getName();	
	
	private static MeetingApplication mInstance = null;	
	public static final String APPLICATION_NAME = "MeetingRecord";
	private static boolean sdCardAvailable = true;
	private static boolean m_bOAStarted = false;
	@Override
	public void onCreate() {
		mInstance = this;
		Thread.setDefaultUncaughtExceptionHandler(new WalletUncaughtExceptionHandler());	
		new MediaCardStateBroadcastReceiver().register();
	
	}

	public static Context getContext() {
		if (mInstance != null) {
			return mInstance.getApplicationContext();
		} else {
			return null;
		}			
	}	
	public static boolean isSDCardAvailable() {
		String status = Environment.getExternalStorageState();
		if (sdCardAvailable && status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	private class MediaCardStateBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
				sdCardAvailable = false;
			} else if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
				sdCardAvailable = true;
			}
		}

		public void register() {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			intentFilter.addDataScheme("file");
			registerReceiver(this, intentFilter);
		}
	}
	
	public static boolean isOAStarted(){
		return m_bOAStarted;
	}
	
	public static void startOA(Activity context)  {
		OpenAPI.startOA(getApplicationVersionName(mInstance), context, new CallBack() {
			@Override
			public void onSuccess(String jsonString) {
				Logger.getInstance().d(TAG, jsonString);
				m_bOAStarted = true;
			}
			
			@Override
			public void onFailure(String errorJsonString) {
				super.onFailure(errorJsonString);
				Logger.getInstance().d(TAG, errorJsonString);
				m_bOAStarted = false;
			}
		});
	}
	private static String appVersionName = null;
	
	private static String getApplicationVersionName(ContextWrapper contextWrapper) {
		if (appVersionName != null)
			return appVersionName;
		try {
			PackageInfo pinfo = contextWrapper.getPackageManager()
					.getPackageInfo(contextWrapper.getPackageName(), 0);
			appVersionName = pinfo.versionName;
			return appVersionName;
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			Logger.getInstance().e(TAG, e);
			return "";
		}
	}
}
