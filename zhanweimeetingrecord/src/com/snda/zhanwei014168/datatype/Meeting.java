package com.snda.zhanwei014168.datatype;

import java.io.Serializable;

/*****************************************
 * 会议基本类型 
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class Meeting extends BaseObject implements Serializable{

	private static final long serialVersionUID = 1L;

	public int id;
	
	/****************
	 * 记录会议的日期
	 ****************/
	public String mDate;
	
	/***************
	 * 会议开始时间
	 ***************/
	public String mStartTime;
	
	/***************
	 * 会议结束时间
	 ***************/
	public String mEndTime;
	
	/***************
	 * 会议地点
	 ***************/
	public String mPlace;
	
	/***************
	 * 会议主题
	 ***************/
	public String mTheme;
	
	/***************
	 * 会议主持人
	 ***************/
	public String mHost;
	
	/***************
	 * 会议参与人员
	 ***************/
	public String mParticipants;
	
	
	public float m_latitude;
	
	public float m_longitude;
	

}
