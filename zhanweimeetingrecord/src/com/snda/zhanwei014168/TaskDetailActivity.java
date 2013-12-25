package com.snda.zhanwei014168;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Task;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.PreferenceKey;


/*****************************************
 * 任务详情，用户可设置任务是否完成
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class TaskDetailActivity extends Activity {
	private final static String TAG = "TaskDetailActivity";	
	
	private TextView m_tvTheme;
	private TextView m_tvIsOver;
	private TextView m_tvPeople;
	private TextView m_tvStartTime;
	private TextView m_tvEndTime;
	private TextView m_tvResult;
	private TextView m_tvStandard;	
	
	private TextView m_tvCommonMainTitle;    
	private Button m_btnBack;
	private Button m_btnAdd; 
	
	private Task mTask;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.task_detail_activity);
		Intent intent = getIntent();
		mTask = (Task) intent.getSerializableExtra(PreferenceKey.PRE_KEY_TASK);	
		if (mTask != null) {
			ensureUi();
			showTask();
		} else {
			finish();
		}
		Logger.getInstance().d(TAG, "TaskDetailActivity  loading");	
		
	}
	
	private void ensureUi() {
		m_tvCommonMainTitle = (TextView)findViewById(R.id.common_titlebar_name);          
	    m_btnBack = (Button)findViewById(R.id.common_titlebar_left);  
		m_tvCommonMainTitle.setText("任务详情");
		m_btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();		
				Logger.getInstance().d(TAG, "TaskDetailActivity return last page ");	
			}
		});		
		
	    m_btnAdd = (Button)findViewById(R.id.common_titlebar_right);  
        m_btnAdd.setVisibility(View.VISIBLE);
        m_btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.getInstance().d(TAG, "TaskDetailActivity  update meeting");
				Intent intent = new Intent(TaskDetailActivity.this, TaskEditActivity.class);	
				intent.putExtra(PreferenceKey.PRE_KEY_TASK, mTask);
				startActivityForResult(intent, 0);		
				
			}
		});
		
		m_tvTheme = (TextView)findViewById(R.id.meeting_theme);		
		m_tvPeople = (TextView)findViewById(R.id.meeting_people);		
		m_tvStartTime = (TextView)findViewById(R.id.meeting_starttime);
		m_tvEndTime = (TextView)findViewById(R.id.meeting_endtime);
		m_tvResult = (TextView)findViewById(R.id.meeting_result);
		m_tvStandard = (TextView)findViewById(R.id.meeting_standard);		
		m_tvIsOver = (TextView)findViewById(R.id.meeting_isover);
	}
	
	
	private void showTask() {
		if (mTask != null) {
			m_tvTheme.setText("任务名称：" + mTask.mName);		
			m_tvPeople.setText("负责人员：" +mTask.mPeople);			
			m_tvStartTime.setText("开始时间：" +mTask.mStartDate);
			m_tvEndTime.setText("结束时间：" +mTask.mEndDate);
			m_tvResult.setText("提交结果：" +mTask.mTaskResult);
			m_tvStandard.setText("审核标准：" +mTask.mCheckStandard);
			if (mTask.mIsOver == 0) {
				m_tvIsOver.setText("完成情况：" + "未完成");
			} else {
				m_tvIsOver.setText("完成情况：" + "完成");
			}
			
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data){	
		switch (resultCode){		
		case RESULT_OK:
			mTask = DataBaseImpl.getInstance().queryTaskDetail(mTask.id);
			showTask();
			break;				
		}		 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 1, "刷新").setIcon(R.drawable.ic_refresh);
		menu.add(0, 1, 2, "编辑").setIcon(R.drawable.ic_add);	
		return super.onCreateOptionsMenu(menu);
	}
	
	public final static int DIALOG_SHOW_LIST = 0;

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Logger.getInstance().d(TAG, "TaskDetailActivity refresh...");	
			mTask = DataBaseImpl.getInstance().queryTaskDetail(mTask.id);
			showTask();
			break;
		case 1:
			Logger.getInstance().d(TAG, "TaskDetailActivity go to TaskEditActivity");	
			Intent intent = new Intent(this, TaskEditActivity.class);	
			intent.putExtra(PreferenceKey.PRE_KEY_TASK, mTask);
			startActivityForResult(intent, 0);
			break;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
