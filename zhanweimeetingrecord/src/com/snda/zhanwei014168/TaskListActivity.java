package com.snda.zhanwei014168;

import java.util.ArrayList;
import com.snda.zhanwei014168.adapter.TaskListAdapter;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Task;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.Constants;
import com.snda.zhanwei014168.util.Util;
import com.snda.zhanwei014168.widget.CustomDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


/*****************************************
 * 任务列表，可支持在日期范围内查找未完成或者已经完成的任务
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class TaskListActivity extends BaseListActivity {
	private final static String TAG = "TaskListAcitivity";
	
	private ListView mListView;
	private TaskListAdapter mListAdapter;
	private ArrayList<Task> mDataList;
	private DeleteReceiver mDeleteReceiver;
	private int mMeetingId;	
	private Button m_btnSearch;
	
	private int mIsOverForSearch;
	private String mStartDateForSearch;
	private String mEndDateForSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
        	finish();
        }
        Logger.getInstance().d(TAG, "TaskListActivity  loading...");	
        mMeetingId = bundle.getInt(Constants.INTENT_EXTRA_MEETING_ID);
		ensureUi();
		new GetTaskListTask().execute();
		mDeleteReceiver = new DeleteReceiver();
		registerReceiver(mDeleteReceiver, new IntentFilter(Constants.INTENT_ACTION_DELETE));
	}
	
	private void ensureUi() {
		m_tvCommonMainTitle.setText("任务列表");
    	mListView = getListView();     
    	mListView.setDivider(new ColorDrawable(0xFFE3E2E3));
    	mListView.setDividerHeight(1);
    	mListAdapter = new TaskListAdapter(this,mMeetingId);		
		mListAdapter.setDataList(mDataList); 		
		if (mDataList != null) {
			mDeleteReceiver.setParameter(mDataList, mListAdapter);
		}
		mListAdapter.notifyDataSetChanged(); 			
        mListView.setAdapter(mListAdapter);            
        mListView.setSmoothScrollbarEnabled(true);    
        m_btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);	
				intent.putExtra(Constants.INTENT_EXTRA_MEETING_ID, mMeetingId);
				startActivityForResult(intent, 0);				
			}
		});
        
        
        m_btnSearch = (Button) findViewById(R.id.common_titlebar_search);
        m_btnSearch.setVisibility(View.VISIBLE);
        m_btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doSearch();				
			}
		});
	}
	
	private void doSearch() {
		
		final CustomDialog dialog = new CustomDialog(this);
		dialog.setTitle("请在下面输入框中输入\n正确格式的筛选条件");
		dialog.setConfirmOnclick("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String isover = dialog.getM_UserName().getText().toString().trim();
				if (TextUtils.isEmpty(isover)) {
					mIsOverForSearch = 0;
				} else {
					mIsOverForSearch = Integer.valueOf(isover);
				}
				
				mEndDateForSearch = dialog.getM_UserPass().getText().toString().trim();
				mStartDateForSearch = dialog.getM_StartTime().getText().toString().trim();
				
				if (!TextUtils.isEmpty(isover) && (mIsOverForSearch != 0 || mIsOverForSearch != 1)) {
					dialog.setMessage("请输入1/0表示完成/未完成");
					return;
				} 
				
				if (!TextUtils.isEmpty(mStartDateForSearch) && !Util.isValidDate(mStartDateForSearch)) {
					dialog.setMessage("请输入正确格式的开始日期如：2011.11.25");
					return;
				}
				if (!Util.isValidDate(mEndDateForSearch)) {
					dialog.setMessage("请输入正确格式的截止日期如：2011.11.25");
					return;
				}
				if (TextUtils.isEmpty(mEndDateForSearch)) {
					dialog.setMessage("第三个编辑框的截止日必须输入"); 
					return;
				}
				mDataList = null;
				new GetTaskListTask().execute();
				
				dialog.dismiss();
			}
		});
		
		dialog.setCancelOnclick("取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
			}
		});
		dialog.show();
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();	
 
	}
	
    @Override 
    protected void onDestroy() {
        super.onDestroy();       
        unregisterReceiver(mDeleteReceiver);  
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 1, "刷新").setIcon(R.drawable.ic_refresh);
		menu.add(0, 1, 2, "添加议题").setIcon(R.drawable.ic_add);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Logger.getInstance().d(TAG, "TaskListActivity  refresh");	
			new GetTaskListTask().execute();
			break;
		case 1:
			Logger.getInstance().d(TAG, "TaskListActivity  add task");	
			Intent intent = new Intent(this, TaskEditActivity.class);	
			intent.putExtra(Constants.INTENT_EXTRA_MEETING_ID, mMeetingId);
			startActivityForResult(intent, 0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data){
		switch (resultCode){
		case RESULT_OK:
			new GetTaskListTask().execute();
			break;
		}
	}
	
	private class GetTaskListTask extends AsyncTask<Void, Integer, ArrayList<Task>> {		

		@Override
		protected void onPreExecute() {
			Logger.getInstance().d(TAG, "GetTaskListTask onPreExecute");			
			showLoadingView();			
		}		
		
		@Override
		protected ArrayList<Task> doInBackground(Void... params) {
			Logger.getInstance().d(TAG, "GetTaskListTask doInBackground");
			if (mEndDateForSearch == null) {				
				return DataBaseImpl.getInstance().queryTasks(mMeetingId);
			} else {				
				return DataBaseImpl.getInstance().queryOrderTasks(mMeetingId, mIsOverForSearch, mStartDateForSearch, mEndDateForSearch);
			}
			
		
		}		
		@Override
		protected void onPostExecute(ArrayList<Task> list){
			Logger.getInstance().d(TAG, "GetTaskListTask onPostExecute");			
			mDataList = list;
			if (mDataList == null || mDataList.size() == 0) {
				setEmptyView();
				mListView.setVisibility(View.GONE);
			} else {	
				mListView.setVisibility(View.VISIBLE);
				ensureUi();
			}
			
			hideLoadingView();			
			
		}
		
		@Override
		protected void onCancelled() {
			Logger.getInstance().d(TAG, "GetTaskListTask onCancelled");				
		}
	}
}
