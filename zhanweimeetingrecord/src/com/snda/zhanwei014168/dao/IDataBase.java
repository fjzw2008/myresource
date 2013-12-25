package com.snda.zhanwei014168.dao;

import java.util.ArrayList;

import android.content.ContentValues;

import com.snda.zhanwei014168.datatype.Issue;
import com.snda.zhanwei014168.datatype.Meeting;
import com.snda.zhanwei014168.datatype.Photo;
import com.snda.zhanwei014168.datatype.Task;


/*****************************************
 * 数据库操作接口
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/

public interface IDataBase {	
	
	
	/***************
	 * 增加一个会议
	 * @param meeting
	 * @return
	 ***************/
	public boolean addMeeting(Meeting metting);
	
	/***************
	 * 增加一个议题
	 * @param issue
	 * @return
	 ***************/
	public boolean addIssue(Issue issue);	
	
	/****************
	 * 增加一个任务
	 * @param task
	 * @return
	 ****************/
	public boolean addTask(Task task);
	
	/****************
	 * 增加一张图片
	 * @param photo
	 * @return
	 ****************/
	public boolean addPhoto(Photo photo);		
	

	
	/**************
	 * 查询所有会议
	 *************/
	public ArrayList<Meeting> queryMeetings();	

	/***********************
	 * 查询议题
	 * @param mettingId 
	 * @return
	 ***********************/
	public ArrayList<Issue> queryIssue(int mettingId);	
	
	/**
	 * 查询会议下的所有任务
	 */
	public ArrayList<Task> queryTasks(int metting_id);
	
	/**********************************
	 * 查询会议下的一定时间内完成或者未完成的任务
	 * @param metting_id 会议ID
	 * @param isover  任务是否完成 1为完成，0为未完成
	 * @param startDate 开始日期
	 * @param endDate 截止日期
	 * @return
	 **********************************/
	public ArrayList<Task> queryOrderTasks(int metting_id, int isover, String startDate,String endDate);	
	
	/******************
	 * 查询某会议的图片
	 ******************/
	public ArrayList<Photo> queryMeetingPhoto(int meetingId);
	
	/*******************
	 * 查询一个会议详细
	 *******************/
	public Meeting queryMeetingDetail(int meetingid);	
	
	/*******************
	 * 查询一个议题详细
	 *******************/
	public Issue queryIssueDetail(int issueid);
	
	/*******************
	 * 查询一个任务详细
	 *******************/
	public Task queryTaskDetail(int taskid);
	
	
	
	/******************
	 * 删除某会议的图片
	 ******************/
	public boolean deleteMeetingPhoto(int photoId);
	
	/*******************
	 * 删除一个会议
	 *******************/
	public boolean deleteMeetingDetail(int meetingid);	
	
	/*******************
	 * 删除一个议题
	 *******************/
	public boolean deleteIssueDetail(int issueid);
	
	/*******************
	 * 删除一个任务
	 *******************/
	public boolean deleteTaskDetail(int taskid);
	
	
	
	
	/*******************
	 * 更新一个会议
	 *******************/
	public boolean updateMeetingDetail(int meetingid, Meeting meeting);	
	
	/*******************
	 * 更新一个议题
	 *******************/
	public boolean updateIssueDetail(int issueid, Issue issue);
	
	/*******************
	 * 更新一个任务
	 *******************/
	public boolean updateTaskDetail(int taskid, Task task);
	
	
}
