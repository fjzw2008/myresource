package com.snda.zhanwei014168;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.snda.zhanwei014168.adapter.IssueListAdapter;
import com.snda.zhanwei014168.adapter.MeetingListAdapter;
import com.snda.zhanwei014168.adapter.TaskListAdapter;
import com.snda.zhanwei014168.datatype.Issue;
import com.snda.zhanwei014168.datatype.Meeting;
import com.snda.zhanwei014168.datatype.Task;
import com.snda.zhanwei014168.util.Constants;


/*****************************************
 * 监听列表数据，当有数据删除时，执行本地删除操作
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class DeleteReceiver extends BroadcastReceiver { 	
	
    public static final String TAG = "DeleteReceiver";  

    private ArrayList<Meeting> mMeetingList;
    private MeetingListAdapter mMeetingAdapter;
    
    private ArrayList<Issue> mIssueList;
    private IssueListAdapter mIssueAdapter;
    
    private ArrayList<Task> mTaskList;
    private TaskListAdapter mTaskAdapter;
    
    private int mMeetingId;
    private int mIssueId;
    private int mTaskId;
   
    
    public void setParameter(ArrayList<Meeting> meetingList,MeetingListAdapter meetingAdapter) {
    	mMeetingList = meetingList;
    	mMeetingAdapter = meetingAdapter;    	
    }
    
    
    public void setParameter(ArrayList<Issue> issueList,IssueListAdapter issueAdapter) {
    	mIssueList = issueList;
    	mIssueAdapter = issueAdapter;
    	
    }
    
    public void setParameter(ArrayList<Task> taskList,TaskListAdapter taskAdapter) {
    	mTaskList = taskList;
    	mTaskAdapter = taskAdapter;    
    }
    
    @Override 
    public void onReceive(Context context, Intent intent) {      	
    	Bundle bundle = intent.getExtras();
    	mMeetingId = bundle.getInt(Constants.INTENT_EXTRA_MEETING_ID,0);
		mIssueId = bundle.getInt(Constants.INTENT_EXTRA_ISSUE_ID,0);
		mTaskId = bundle.getInt(Constants.INTENT_EXTRA_TASK_ID,0);
		
		if (mMeetingId > 0) {
			if ( mMeetingList != null && mMeetingList.size()!= 0 && mMeetingAdapter != null) {
				int n_size = mMeetingList.size();				
				for (int i= 0; i < n_size; i++) {
					Meeting data = (Meeting)mMeetingList.get(i);	    					
					if (data.id == mMeetingId) {
						mMeetingList.remove(i);  
						n_size -= 1 ;
	                }								
				}
				mMeetingAdapter.setDataList(mMeetingList);
				mMeetingAdapter.notifyDataSetChanged();			    	
			}	    	
		}
		
		if (mIssueId > 0) {
			if ( mIssueList != null && mIssueList.size()!= 0 && mIssueAdapter != null) {
				int n_size = mIssueList.size();				
				for (int i= 0; i < n_size; i++) {
					Issue data = (Issue)mIssueList.get(i);	    					
					if (data.id == mIssueId) {
						mIssueList.remove(i);  
						n_size -= 1 ;
	                }								
				}
				mIssueAdapter.setDataList(mIssueList);
				mIssueAdapter.notifyDataSetChanged();			    	
			}			
		}
    	
		if (mTaskId > 0) {
			if ( mTaskList != null && mTaskList.size()!= 0 && mTaskAdapter != null) {
				int n_size = mTaskList.size();				
				for (int i= 0; i < n_size; i++) {
					Task data = (Task)mTaskList.get(i);	    					
					if (data.id == mTaskId) {
						mTaskList.remove(i);  
						n_size -= 1 ;
	                }								
				}
				mTaskAdapter.setDataList(mTaskList);
				mTaskAdapter.notifyDataSetChanged();			    	
			}
			
		}
    }
    		
	
    
	
}