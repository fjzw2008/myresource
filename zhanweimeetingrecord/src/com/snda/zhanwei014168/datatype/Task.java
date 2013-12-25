package com.snda.zhanwei014168.datatype;

import java.io.Serializable;

/*****************************************
 * 议题基本类型 
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class Task extends BaseObject implements Serializable{

	private static final long serialVersionUID = 3L;

	public int id;
	
	public int meeting_id;	
	
	/***************
	 * 任务责任人
	 ***************/
	public String mPeople;
	
	/***************
	 * 任务名
	 ****************/
	public String mName;
	
	/***************
	 * 任务开始日期
	 ***************/
	public String mStartDate;	

	/***************
	 * 任务结束日期
	 ***************/
	public String mEndDate;	
	
	/***************
	 * 提交的成果
	 ***************/
	public String mTaskResult;
	
	/***************
	 * 任务考核标准
	 ***************/
	public String mCheckStandard;
	
	/***************
	 * 任务是否完成，1为完成，0为未完成
	 ***************/
	public int mIsOver;
}
