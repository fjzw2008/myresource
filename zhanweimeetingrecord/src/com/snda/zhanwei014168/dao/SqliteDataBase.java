package com.snda.zhanwei014168.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*****************************************
 * 数据库操作基本类
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/

public class SqliteDataBase {
	
	private static final String TAG = SqliteDataBase.class.getName();		

	private static final String CMD_CREATE_TABLT = "CREATE TABLE IF NOT EXISTS ";
	private static final String CMD_DROP_TABLT   = "DROP TABLE IF EXISTS ";		
	private static final String	DB_NAME			 = "meeting.db";	
	private static final int DB_VERSION			 = 1;
	
	private Context mContext 					 = null;	
	public SQLiteDatabase mSQLiteDatabase 		 = null;
	private DatabaseHelper	mDatabaseHelper		 = null;	
	

	private static class DatabaseHelper extends SQLiteOpenHelper{
		DatabaseHelper(Context context){
			super(context, DB_NAME, null, DB_VERSION);
		}

		/********
		 * 创建表
		 ********/
		@Override
		public void onCreate(SQLiteDatabase db){		
			db.execSQL(TMeeting.DB_TABLE_MEETING_CREATE);
			db.execSQL(TIssue.DB_TABLE_ISSUE_CREATE);
			db.execSQL(TTask.DB_TABLE_TASK_CREATE);
			db.execSQL(TPhoto.DB_TABLE_PHOTO_CREATE);
		}

		/*************
		 * 更新数据库
		 *************/
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			db.execSQL(CMD_DROP_TABLT + DB_NAME);
			onCreate(db);
		}
	}

	public SqliteDataBase(Context context){
		mContext = context;
	}

	/************
	 * 打开数据库
	 * @throws SQLException
	 ***********/
	public void open() throws SQLException{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}
	/************
	 * 关闭数据库
	 ************/
	public void close(){
		mDatabaseHelper.close();
	}

	/*************
	 * 插入一条记录
	 * @return
	 *************/
	public long insertData(String tableName, ContentValues values){
		return mSQLiteDatabase.insert(tableName, "id", values);
	}
	
	/**
	 * 删除一条记录
	 * @param URLString
	 */
	public int delete(String tableName, String where, String[] whereValue){
		return mSQLiteDatabase.delete(tableName, where, whereValue);
	}
	/********************
	 * 清空表
	 * @param tableName
	 ********************/
	public void dropTable(String tableName){	
		mSQLiteDatabase.execSQL(CMD_DROP_TABLT + tableName);
		if(tableName.equals(TMeeting.TABLE_NAME)){
			mSQLiteDatabase.execSQL(TMeeting.DB_TABLE_MEETING_CREATE);
		}else if (tableName.equals(TIssue.TABLE_NAME)){
			mSQLiteDatabase.execSQL(TIssue.DB_TABLE_ISSUE_CREATE);
		}else if (tableName.equals(TTask.TABLE_NAME)){
			mSQLiteDatabase.execSQL(TTask.DB_TABLE_TASK_CREATE);
		}else if (tableName.equals(TPhoto.TABLE_NAME)){
			mSQLiteDatabase.execSQL(TPhoto.DB_TABLE_PHOTO_CREATE);
		}
	}
	/***********
	 * 查询数据
	 ***********/
	public Cursor fetchData(String tableName, String[] columns, String where, String[] whereValue,String orderBy) throws SQLException{
		Cursor mCursor = mSQLiteDatabase.query(true, tableName, columns, where, whereValue, null, null, orderBy, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/*****************
	 * 判断记录是否存在
	 *****************/
	public boolean isExist(String tableName, String[] columns, String where, String[] whereValue) throws SQLException{
		boolean isConcent = false;
		Cursor mCursor = mSQLiteDatabase.query(true, tableName, columns, where, whereValue, null, null, null, null);
		
		if ((mCursor != null) && (mCursor.getCount() > 0)){
			isConcent = true;
		}
		mCursor.close();
		return isConcent;
	}
	
	/***********
	 * 更新数据
	 * @return
	 ***********/
	public boolean updateData(String tableName, ContentValues data, String where, String[] whereValue){
		return mSQLiteDatabase.update(tableName, data, where, whereValue) > 0;
	}	
	
	public static class TMeeting {
		
		public static final String TABLE_NAME 					= "meeting";
		public static final String COLUMN_ID 					= "id";
		public static final String COLUMN_MEETING_DATE 			= "meeting_date";	// 会议日期
		public static final String COLUMN_START_TIME 			= "start_time";		// 会议开始时间
		public static final String COLUMN_END_TIME 				= "end_time";		// 会议结束时间
		public static final String COLUMN_PLACE 				= "place"; 			// 会议地点
		public static final String COLUMN_THEME					= "theme"; 			// 会议主题
		public static final String COLUMN_HOST					= "host"; 			// 会议主持
		public static final String COLUMN_PARTICIPANTS 			= "participants";  	// 会议参与人员		
	
		public static final String DB_TABLE_MEETING_CREATE =

			CMD_CREATE_TABLT + TABLE_NAME + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 			
			+ COLUMN_MEETING_DATE + " TEXT, " 
			+ COLUMN_START_TIME	+ " TEXT, " 
			+ COLUMN_END_TIME + " TEXT, "
			+ COLUMN_PLACE + " TEXT, " 
			+ COLUMN_THEME + " TEXT, " 	
			+ COLUMN_HOST + " TEXT, "
			+ COLUMN_PARTICIPANTS + " TEXT)";
	}
	
	public static class TIssue {
		
		public static final String TABLE_NAME 			= "issue";
		public static final String COLUMN_ID 			= "id";
		public static final String COLUMN_PEOPLE 		= "people";			// 发言人
		public static final String COLUMN_THEME 		= "theme";			// 发言主题
		public static final String COLUMN_SPEAK_TIME 	= "speak_time";		// 发言时间
		public static final String COLUMN_MEETING_ID 	= "meeting_id";		// 所属会议的ID	
	
	
		public static final String DB_TABLE_ISSUE_CREATE =

			CMD_CREATE_TABLT + TABLE_NAME + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 			
			+ COLUMN_PEOPLE + " TEXT, " 
			+ COLUMN_THEME	+ " TEXT, "				
			+ COLUMN_SPEAK_TIME + " TEXT, "
			+ COLUMN_MEETING_ID + " INTEGER)";
	}
	
	public static class TTask {
		
		public static final String TABLE_NAME 				= "task";
		public static final String COLUMN_ID 				= "id";
		public static final String COLUMN_TASK_NAME 		= "task_name";			// 任务名
		public static final String COLUMN_PERSON_INCHARGE 	= "person_incharge";	// 任务负责人
		public static final String COLUMN_START_DATE 		= "start_date";			// 任务开始日期
		public static final String COLUMN_END_DATE 			= "end_date";			// 任务截止日期
		public static final String COLUMN_TASK_RESULT		= "task_result";		// 任务结果
		public static final String COLUMN_TASK_STANDARD 	= "task_standard";		// 任务考核标准
		public static final String COLUMN_ISOVER 			= "isover";				// 任务是否完成
		public static final String COLUMN_MEETING_ID 		= "meeting_id";			// 所属会议的ID	
	
	
		public static final String DB_TABLE_TASK_CREATE =

			CMD_CREATE_TABLT + TABLE_NAME + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 			
			+ COLUMN_TASK_NAME + " TEXT, " 
			+ COLUMN_PERSON_INCHARGE	+ " TEXT, "		
			+ COLUMN_START_DATE + " TEXT, " 
			+ COLUMN_END_DATE	+ " TEXT, "	
			+ COLUMN_TASK_RESULT + " TEXT, " 
			+ COLUMN_TASK_STANDARD + " TEXT, "	
			+ COLUMN_ISOVER + " INTEGER DEFAULT 0, "
			+ COLUMN_MEETING_ID + " INTEGER)";
	}

	
	public static class TPhoto {
		
		public static final String TABLE_NAME 			= "photo";
		public static final String COLUMN_ID 			= "id";
		public static final String COLUMN_URL 			= "url";			// 图片路径
		public static final String COLUMN_MEETING_ID 	= "meeting_id";		// 所属会议的ID	
	
	
		public static final String DB_TABLE_PHOTO_CREATE =

			CMD_CREATE_TABLT + TABLE_NAME + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 			
			+ COLUMN_URL + " TEXT, " 					
			+ COLUMN_MEETING_ID + " INTEGER)";
	}

}
