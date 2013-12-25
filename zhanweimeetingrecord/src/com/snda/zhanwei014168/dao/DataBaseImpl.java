package com.snda.zhanwei014168.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.snda.zhanwei014168.MeetingApplication;
import com.snda.zhanwei014168.dao.SqliteDataBase.TIssue;
import com.snda.zhanwei014168.dao.SqliteDataBase.TMeeting;
import com.snda.zhanwei014168.dao.SqliteDataBase.TPhoto;
import com.snda.zhanwei014168.dao.SqliteDataBase.TTask;
import com.snda.zhanwei014168.datatype.Issue;
import com.snda.zhanwei014168.datatype.Meeting;
import com.snda.zhanwei014168.datatype.Photo;
import com.snda.zhanwei014168.datatype.Task;
import com.snda.zhanwei014168.util.Util;


/*****************************************
 * 数据库操作接口实现
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/


public class DataBaseImpl implements IDataBase {
	
	
	private static final String TAG = DataBaseImpl.class.getName();

	private static DataBaseImpl mInstance = null;
	private SqliteDataBase mDataBase = null;

//	private String[] mMeetingColumns;
//	private String[] mIssueColumns;
//	private String[] mTaskColumns;	
//	private String[] mPhotoColumns;

	private DataBaseImpl(Context context) {
		mDataBase = new SqliteDataBase(context);
		mDataBase.open();	
//		mMeetingColumns = new String[] {TMeeting.COLUMN_ID,TMeeting.COLUMN_MEETING_DATE,TMeeting.COLUMN_START_TIME,
//				TMeeting.COLUMN_END_TIME,TMeeting.COLUMN_PLACE,TMeeting.COLUMN_THEME,TMeeting.COLUMN_HOST,TMeeting.COLUMN_PARTICIPANTS};
//		mIssueColumns = new String[] {TIssue.COLUMN_ID,TIssue.COLUMN_PEOPLE,TIssue.COLUMN_THEME,TIssue.COLUMN_SPEAK_TIME,TIssue.COLUMN_MEETING_ID};
//		mTaskColumns = new String[] {TTask.COLUMN_ID,TTask.COLUMN_TASK_NAME,TTask.COLUMN_PERSON_INCHARGE,TTask.COLUMN_START_DATE,
//				TTask.COLUMN_END_DATE,TTask.COLUMN_TASK_RESULT,TTask.COLUMN_TASK_STANDARD,TTask.COLUMN_ISOVER,TTask.COLUMN_MEETING_ID};
//		mPhotoColumns = new String[] {TPhoto.COLUMN_ID,TPhoto.COLUMN_URL,TPhoto.COLUMN_MEETING_ID};
	}

	public static synchronized DataBaseImpl getInstance() {
		if (mInstance == null) {
			mInstance = new DataBaseImpl(MeetingApplication.getContext());
		}
		return mInstance;
	}

	@Override
	public boolean addTask(Task task) {
	
		ContentValues data = new ContentValues();
		data.put(TTask.COLUMN_TASK_NAME,task.mName);
		data.put(TTask.COLUMN_START_DATE,Util.doReplaceDomain(task.mStartDate));
		data.put(TTask.COLUMN_END_DATE,Util.doReplaceDomain(task.mEndDate));		
		data.put(TTask.COLUMN_PERSON_INCHARGE,task.mPeople);
		data.put(TTask.COLUMN_TASK_RESULT,task.mTaskResult);
		data.put(TTask.COLUMN_TASK_STANDARD,task.mCheckStandard);	
		data.put(TTask.COLUMN_ISOVER,task.mIsOver);
		data.put(TTask.COLUMN_MEETING_ID,task.meeting_id);
		long id = mDataBase.insertData(TTask.TABLE_NAME,data);
		return id > -1;	
	}

	@Override
	public boolean addIssue(Issue issue) {
		ContentValues data = new ContentValues();
		data.put(TIssue.COLUMN_THEME,issue.mTheme);	
		data.put(TIssue.COLUMN_PEOPLE, issue.mPeople);
		data.put(TIssue.COLUMN_SPEAK_TIME, issue.mSpeakTime);				
		data.put(TIssue.COLUMN_MEETING_ID,issue.meeting_id);
		long id = mDataBase.insertData(TIssue.TABLE_NAME,data);
		return id > -1;	
	}

	@Override
	public boolean addMeeting(Meeting meeting) {
		ContentValues data = new ContentValues();
		data.put(TMeeting.COLUMN_MEETING_DATE,meeting.mDate );
		data.put(TMeeting.COLUMN_PLACE,meeting.mPlace );
		data.put(TMeeting.COLUMN_THEME,meeting.mTheme);		
		data.put(TMeeting.COLUMN_START_TIME, meeting.mStartTime);
		data.put(TMeeting.COLUMN_END_TIME,meeting.mEndTime );
		data.put(TMeeting.COLUMN_HOST,meeting.mHost);	
		data.put(TMeeting.COLUMN_PARTICIPANTS,meeting.mParticipants);
		long id = mDataBase.insertData(TMeeting.TABLE_NAME,data);
		return id > -1;	
		
	}

	@Override
	public boolean addPhoto(Photo photo) {
		ContentValues data = new ContentValues();
		data.put(TPhoto.COLUMN_URL,photo.mPhotoPath);
		data.put(TPhoto.COLUMN_MEETING_ID,photo.meeting_id);		
		long id = mDataBase.insertData(TPhoto.TABLE_NAME,data);
		return id > -1;	
	}


	@Override
	public boolean deleteIssueDetail(int issueid) {
		int id = mDataBase.delete(TIssue.TABLE_NAME, TIssue.COLUMN_ID+"=?", new String[]{String.valueOf(issueid)});
		return id > -1;
	}

	@Override
	public boolean deleteMeetingDetail(int meetingid) {
		int id = mDataBase.delete(TMeeting.TABLE_NAME, TMeeting.COLUMN_ID+"=?", new String[]{String.valueOf(meetingid)});
		return id > -1;
	}

	@Override
	public boolean deleteMeetingPhoto(int photoId) {
		int id = mDataBase.delete(TPhoto.TABLE_NAME, TPhoto.COLUMN_ID+"=?", new String[]{String.valueOf(photoId)});
		return id > -1;
	}

	@Override
	public boolean deleteTaskDetail(int taskid) {
		int id = mDataBase.delete(TTask.TABLE_NAME, TTask.COLUMN_ID+"=?", new String[]{String.valueOf(taskid)});
		return id > -1;
	}

	@Override
	public ArrayList<Issue> queryIssue(int meetingId) {
		Cursor cur = mDataBase.fetchData(TIssue.TABLE_NAME, null, 
				TIssue.COLUMN_MEETING_ID+"=?", new String[]{String.valueOf(meetingId)}, null);
		ArrayList<Issue> list = new ArrayList<Issue>();
		if(cur != null && cur.getCount() > 0){
			cur.moveToFirst();
			do{
				Issue issue = new Issue();
				issue.id = cur.getInt(cur.getColumnIndex(TIssue.COLUMN_ID));
				issue.meeting_id = cur.getInt(cur.getColumnIndex(TIssue.COLUMN_MEETING_ID));
				issue.mPeople = cur.getString(cur.getColumnIndex(TIssue.COLUMN_PEOPLE));
				issue.mTheme = cur.getString(cur.getColumnIndex(TIssue.COLUMN_THEME));
				issue.mSpeakTime = cur.getString(cur.getColumnIndex(TIssue.COLUMN_SPEAK_TIME));
				list.add(issue);
			}while(cur.moveToNext());
		}
		return list;
	}

	@Override
	public Issue queryIssueDetail(int issueid) {
		Cursor cur = mDataBase.fetchData(TIssue.TABLE_NAME, null, 
				TIssue.COLUMN_ID+"=?", new String[]{String.valueOf(issueid)}, null);
		Issue issue = null;
		if(cur != null && cur.getCount() > 0){
			cur.moveToFirst();
			issue = new Issue();
			issue.id = cur.getInt(cur.getColumnIndex(TIssue.COLUMN_ID));
			issue.meeting_id = cur.getInt(cur.getColumnIndex(TIssue.COLUMN_MEETING_ID));
			issue.mPeople = cur.getString(cur.getColumnIndex(TIssue.COLUMN_PEOPLE));
			issue.mTheme = cur.getString(cur.getColumnIndex(TIssue.COLUMN_THEME));
			issue.mSpeakTime = cur.getString(cur.getColumnIndex(TIssue.COLUMN_SPEAK_TIME));
		}
		return issue;
	}

	@Override
	public Meeting queryMeetingDetail(int meetingid) {
		Cursor cur = mDataBase.fetchData(TMeeting.TABLE_NAME, null, 
				TIssue.COLUMN_ID+"=?", new String[]{String.valueOf(meetingid)}, null);
		Meeting meeting = null;
		if(cur != null && cur.getCount() > 0){
			cur.moveToFirst();
			meeting = new Meeting();
			meeting.id = cur.getInt(cur.getColumnIndex(TMeeting.COLUMN_ID));
			meeting.mDate = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_MEETING_DATE));
			meeting.mStartTime = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_START_TIME));
			meeting.mEndTime = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_END_TIME));
			meeting.mPlace = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_PLACE));
			meeting.mTheme = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_THEME));
			meeting.mHost = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_HOST));
			meeting.mParticipants = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_PARTICIPANTS));
		}
		return meeting;
	}

	@Override
	public ArrayList<Photo> queryMeetingPhoto(int meetingId) {
		Cursor cur = mDataBase.fetchData(TPhoto.TABLE_NAME, 
				null, TPhoto.COLUMN_MEETING_ID + "=?", new String[]{String.valueOf(meetingId)},null);
		ArrayList<Photo> list = new ArrayList<Photo>();
		if(cur != null && cur.getCount() > 0){
			cur.moveToFirst();
			do{
				Photo photo = new Photo();
				photo.id = cur.getInt(cur.getColumnIndex(TPhoto.COLUMN_ID));
				photo.mPhotoPath = cur.getString(cur.getColumnIndex(TPhoto.COLUMN_URL));
				photo.meeting_id = cur.getInt(cur.getColumnIndex(TPhoto.COLUMN_MEETING_ID));
				list.add(photo);
			} while (cur.moveToNext());
		}
		cur.close();
		return list;
	}

	@Override
	public ArrayList<Meeting> queryMeetings() {		
		Cursor cur = mDataBase.fetchData(
				TMeeting.TABLE_NAME, null, null, null,null);
		ArrayList<Meeting> list = new ArrayList<Meeting>();
		if (cur != null && cur.getCount() > 0) {
			do {
				Meeting result = new Meeting();
				result.id = cur.getInt(cur.getColumnIndex(TMeeting.COLUMN_ID));
				result.mDate = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_MEETING_DATE));
				result.mPlace = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_PLACE));
				result.mTheme = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_THEME));
				result.mStartTime = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_START_TIME));
				result.mEndTime = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_END_TIME));
				result.mHost = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_HOST));
				result.mParticipants = cur.getString(cur.getColumnIndex(TMeeting.COLUMN_PARTICIPANTS));			
				list.add(result);
			} while (cur.moveToNext());			
		}
		cur.close();
		return list;
		
	}

	@Override
	public ArrayList<Task> queryOrderTasks(int meetingId, int isover,
			String startDate, String endDate) {
		startDate = Util.doReplaceDomain(startDate);
		endDate = Util.doReplaceDomain(endDate);		
		StringBuffer where = new StringBuffer();
		where.append(TTask.COLUMN_MEETING_ID+" = ?");
		where.append(" and ").append(TTask.COLUMN_ISOVER+" = ?");
		if (startDate != null || !startDate.equals("")) {
			where.append(" and ").append(TTask.COLUMN_START_DATE+" >= ?");			
		} else {
			startDate = null;
		}		
		where.append(" and ").append(TTask.COLUMN_END_DATE+" <= ?");
		Cursor cur = mDataBase.fetchData(TTask.TABLE_NAME, null, 
				where.toString(), new String[]{String.valueOf(meetingId), String.valueOf(isover),
				startDate, endDate}, null);
		ArrayList<Task> list = new ArrayList<Task>();
		if(cur != null && cur.getCount() > 0){
			cur.moveToFirst();
			do{
				Task task = new Task();
				task.id = cur.getInt(cur.getColumnIndex(TTask.COLUMN_ID));
				task.meeting_id = cur.getInt(cur.getColumnIndex(TTask.COLUMN_MEETING_ID));
				task.mPeople = cur.getString(cur.getColumnIndex(TTask.COLUMN_PERSON_INCHARGE));
				task.mName = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_NAME));
				task.mStartDate = cur.getString(cur.getColumnIndex(TTask.COLUMN_START_DATE));
				task.mEndDate = cur.getString(cur.getColumnIndex(TTask.COLUMN_END_DATE));
				task.mTaskResult = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_RESULT));
				task.mCheckStandard = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_STANDARD));
				task.mIsOver = cur.getInt(cur.getColumnIndex(TTask.COLUMN_ISOVER));
				list.add(task);
			}while(cur.moveToNext());
		}
		return list;
	}

	@Override
	public Task queryTaskDetail(int taskid) {
		Cursor cur = mDataBase.fetchData(TTask.TABLE_NAME, null, 
				TTask.COLUMN_ID+"=?", new String[]{String.valueOf(taskid)}, null);
		Task task = null;
		if(cur != null && cur.getCount() > 0){
			cur.moveToFirst();
			task = new Task();
			task.id = cur.getInt(cur.getColumnIndex(TTask.COLUMN_ID));
			task.meeting_id = cur.getInt(cur.getColumnIndex(TTask.COLUMN_MEETING_ID));
			task.mPeople = cur.getString(cur.getColumnIndex(TTask.COLUMN_PERSON_INCHARGE));
			task.mName = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_NAME));
			task.mStartDate = cur.getString(cur.getColumnIndex(TTask.COLUMN_START_DATE));
			task.mEndDate = cur.getString(cur.getColumnIndex(TTask.COLUMN_END_DATE));
			task.mTaskResult = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_RESULT));
			task.mCheckStandard = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_STANDARD));
			task.mIsOver = cur.getInt(cur.getColumnIndex(TTask.COLUMN_ISOVER));
		}
		return task;
	}

	@Override
	public ArrayList<Task> queryTasks(int meetingId) {
		StringBuffer where = new StringBuffer();
		where.append(TTask.COLUMN_MEETING_ID+" = ?");
		Cursor cur = mDataBase.fetchData(TTask.TABLE_NAME, null, 
				where.toString(), new String[]{String.valueOf(meetingId)}, null);
		ArrayList<Task> list = new ArrayList<Task>();
		if(cur != null && cur.getCount() > 0){
			cur.moveToFirst();
			do{
				Task task = new Task();
				task.id = cur.getInt(cur.getColumnIndex(TTask.COLUMN_ID));
				task.meeting_id = cur.getInt(cur.getColumnIndex(TTask.COLUMN_MEETING_ID));
				task.mPeople = cur.getString(cur.getColumnIndex(TTask.COLUMN_PERSON_INCHARGE));
				task.mName = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_NAME));
				task.mStartDate = cur.getString(cur.getColumnIndex(TTask.COLUMN_START_DATE));
				task.mEndDate = cur.getString(cur.getColumnIndex(TTask.COLUMN_END_DATE));
				task.mTaskResult = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_RESULT));
				task.mCheckStandard = cur.getString(cur.getColumnIndex(TTask.COLUMN_TASK_STANDARD));
				task.mIsOver = cur.getInt(cur.getColumnIndex(TTask.COLUMN_ISOVER));
				list.add(task);
			}while(cur.moveToNext());
		}
		return list;
	}

	@Override
	public boolean updateIssueDetail(int issueid, Issue issue) {
		ContentValues data = new ContentValues();
		data.put(TIssue.COLUMN_PEOPLE, issue.mPeople);
		data.put(TIssue.COLUMN_SPEAK_TIME, issue.mSpeakTime);
		data.put(TIssue.COLUMN_THEME,issue.mTheme);			
		data.put(TIssue.COLUMN_MEETING_ID,issue.meeting_id);
		return mDataBase.updateData(TIssue.TABLE_NAME, data, TIssue.COLUMN_ID+"=?", new String[]{String.valueOf(issueid)});
	}

	@Override
	public boolean updateMeetingDetail(int meetingid, Meeting meeting) {
		ContentValues data = new ContentValues();
		data.put(TMeeting.COLUMN_MEETING_DATE,meeting.mDate );
		data.put(TMeeting.COLUMN_PLACE,meeting.mPlace );
		data.put(TMeeting.COLUMN_THEME,meeting.mTheme);		
		data.put(TMeeting.COLUMN_START_TIME, meeting.mStartTime);
		data.put(TMeeting.COLUMN_END_TIME,meeting.mEndTime );
		data.put(TMeeting.COLUMN_HOST,meeting.mHost);	
		data.put(TMeeting.COLUMN_PARTICIPANTS,meeting.mParticipants);
		return mDataBase.updateData(TMeeting.TABLE_NAME, data, TIssue.COLUMN_ID+"=?", new String[]{String.valueOf(meetingid)});
	}

	@Override
	public boolean updateTaskDetail(int taskid, Task task) {
		ContentValues data = new ContentValues();
		data.put(TTask.COLUMN_TASK_NAME,task.mName);
		data.put(TTask.COLUMN_START_DATE,task.mStartDate);
		data.put(TTask.COLUMN_END_DATE,task.mEndDate);		
		data.put(TTask.COLUMN_PERSON_INCHARGE,task.mPeople);
		data.put(TTask.COLUMN_TASK_RESULT,task.mTaskResult);
		data.put(TTask.COLUMN_TASK_STANDARD,task.mCheckStandard);	
		data.put(TTask.COLUMN_ISOVER,task.mIsOver);
		data.put(TTask.COLUMN_MEETING_ID,task.meeting_id);
		return mDataBase.updateData(TTask.TABLE_NAME, data, TTask.COLUMN_ID+"=?", new String[]{String.valueOf(taskid)});
	}

}
