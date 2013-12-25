package com.snda.zhanwei014168.util;

import java.io.File;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


/*****************************************
 * 基本工具类
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class Util {
	
	public static String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * 判断日期字符是否符合规范
	 * @param s
	 * @return
	 */
	public static boolean isValidDate(String s) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		dateFormat.setLenient(false);
		try {
			  s = doReplaceDomain(s);
	          dateFormat.parse(s);
	          return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String doReplaceDomain(String s) {
		return s.replace(".", "-");
		
	}
	
	/**
	 * 获取图片存储路径
	 * @param s
	 * @return
	 */
	public static String getPhotoPath(){
		File dir = new File(Environment.getExternalStorageDirectory(), "/snda/meeting/photo");
		if(!dir.exists() || !dir.isDirectory()){
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}
	
	/**
	 * 根据URI获取图片
	 * @param imageUri
	 * @param activity
	 * @return
	 */
	public static File convertImageUriToFile (Uri imageUri, Activity activity)  { 
    	Cursor cursor = null; 
    	try { 
    		String [] proj={MediaStore.Images.Media.DATA,
    				MediaStore.Images.Media._ID,
    				MediaStore.Images.ImageColumns.ORIENTATION};
    		cursor = activity.managedQuery( imageUri,  
    				             proj, 	// Which columns to return  
    				             null,  // WHERE clause; which rows to return (all rows)  
    				             null,  // WHERE clause selection arguments (none)  
    				             null); // Order-by clause (ascending by name)  
    		int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    		int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
    		
    		if (cursor.moveToFirst()) {  
    			         String orientation =  cursor.getString(orientation_ColumnIndex);  
    			         return new File(cursor.getString(file_ColumnIndex));  
    			     }  
    		return null;
    	}catch(Exception e){
    		return null;
    	}finally{  
    		if (cursor != null) {  
    			cursor.close();  
    		}  
    	}  
    }
}
