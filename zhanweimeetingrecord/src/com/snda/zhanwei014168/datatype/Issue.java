package com.snda.zhanwei014168.datatype;

import java.io.Serializable;

/*****************************************
 * 议题基本类型 
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class Issue extends BaseObject implements Serializable{

	private static final long serialVersionUID = 2L;

	public int id;
	
	public int meeting_id;	
	
	/***************
	 * 发言人
	 ***************/
	public String mPeople;
	
	/***************
	 * 议题主题
	 ****************/
	public String mTheme;
	
	/***************
	 * 发言时间
	 ***************/
	public String mSpeakTime;
	

	
	
	

}
