package com.snda.zhanwei014168;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Task;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.Constants;
import com.snda.zhanwei014168.util.PreferenceKey;
import com.snda.zhanwei014168.util.Util;



/*****************************************
 * 任务的增加更新
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class TaskEditActivity extends Activity {
	private final static String TAG = "TaskEditActivity";
	
	private EditText m_edtTheme;
	private EditText m_edtResult;
	private EditText m_edtStandard;
	private EditText m_edtPeople;
	private EditText m_edtStartTime;
	private EditText m_edtEndTime;
	private RadioGroup m_rgTask;
	private RadioButton m_rbNonOver;
	private RadioButton m_rbOver;
	private Button m_btnOK;	
	private TextView m_tvCommonMainTitle;    
	private Button m_btnBack;
	
    private Task mTask;
    private int mMeetingId;
    
    private int mIsOver;//1表示完成，0表示未完成
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
	    setContentView(R.layout.task_edit_activity);  
	    Intent intent = getIntent();
		mTask = (Task) intent.getSerializableExtra(PreferenceKey.PRE_KEY_TASK);	
		mMeetingId = intent.getIntExtra(Constants.INTENT_EXTRA_MEETING_ID, -1);
		ensureUi();
		if (mTask != null) {			
			showTask();
		}
		Logger.getInstance().d(TAG, "TaskEditActivity  loading");	
	}

	private void ensureUi() {
		m_tvCommonMainTitle = (TextView)findViewById(R.id.common_titlebar_name);          
	    m_btnBack = (Button)findViewById(R.id.common_titlebar_left);  
		m_tvCommonMainTitle.setText("任务记录");
		m_btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				Logger.getInstance().d(TAG, "TaskEditActivity retrun last page");
			}
		});
		
		
		m_edtTheme = (EditText)findViewById(R.id.meeting_theme);
		m_edtResult = (EditText)findViewById(R.id.meeting_result);
		m_edtStandard = (EditText)findViewById(R.id.meeting_standard);
		m_edtPeople = (EditText)findViewById(R.id.meeting_people);		
		m_edtStartTime = (EditText)findViewById(R.id.meeting_starttime);
		m_edtEndTime = (EditText)findViewById(R.id.meeting_endtime);
		
		m_rgTask = (RadioGroup) findViewById(R.id.task_radiogroup);		
		m_rbNonOver = (RadioButton) findViewById(R.id.nonover_radiobutton);
		m_rbOver = (RadioButton) findViewById(R.id.over_radiobutton);
		m_rgTask.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == m_rbNonOver.getId())
					mIsOver = 0;
				else
					mIsOver = 1;
			}
		});
		
		m_btnOK = (Button)findViewById(R.id.meeting_ok);
		
		m_btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String theme = m_edtTheme.getText().toString().trim();
				String result = m_edtResult.getText().toString().trim();
				String standard = m_edtStandard.getText().toString().trim();
				String people = m_edtPeople.getText().toString().trim();				
				String starttime = m_edtStartTime.getText().toString().trim();
				String endtime = m_edtEndTime.getText().toString().trim();
				
				if (!TextUtils.isEmpty(starttime) && !Util.isValidDate(starttime)) {
					Toast.makeText(TaskEditActivity.this, "请输入正确格式的开始日期如：2011.11.25", 0).show();					
					return;
				}
				if (!Util.isValidDate(endtime)) {
					Toast.makeText(TaskEditActivity.this, "请输入正确格式的截止日期如：2011.11.25", 0).show();				
					return;
				}
				if (TextUtils.isEmpty(endtime)) {
					Toast.makeText(TaskEditActivity.this, "截止日必须输入", 0).show();					
					return;
				}
				
				
				if (theme.length() != 0 ) {
					Task task = new Task();
					task.mName = theme;
					task.mTaskResult = result;				
					task.mCheckStandard = standard;
					task.mPeople = people;
					task.mStartDate = starttime;
					task.mEndDate= endtime;
					task.mIsOver = mIsOver;
					task.meeting_id = mMeetingId;
					boolean result1 = false;
					if (mTask != null) {
						result1 = DataBaseImpl.getInstance().updateTaskDetail(mTask.id, task);
					} else {
						result1 = DataBaseImpl.getInstance().addTask(task);
					}				
						
					if (result1) {
						Logger.getInstance().d(TAG, "TaskEditActivity  add success...");
						Toast.makeText(TaskEditActivity.this, "恭喜您，提交成功", 0).show();
						setResult(RESULT_OK);
						finish();
					} else {
						Logger.getInstance().d(TAG, "TaskEditActivity  add fairlure...");
						Toast.makeText(TaskEditActivity.this, "提交失败，请重试", 0).show();
					}
				} else {
					Toast.makeText(TaskEditActivity.this, "任务名称不能为空", 0).show();
				}
			}
		});
		
	}
	
	private void showTask() {
		if (mTask != null) {
			m_edtTheme.setText(mTask.mName);
			m_edtResult.setText(mTask.mTaskResult);
			m_edtStandard.setText(mTask.mCheckStandard);
			m_edtPeople.setText(mTask.mPeople);			
			m_edtStartTime.setText(mTask.mStartDate);
			m_edtEndTime.setText(mTask.mEndDate);
			if (mTask.mIsOver == 1) {
				m_rbOver.setChecked(true);
				m_rbNonOver.setChecked(false);
			} else {
				m_rbNonOver.setChecked(true);
				m_rbOver.setChecked(false);
			}
			
		}
		
	}
	
}
