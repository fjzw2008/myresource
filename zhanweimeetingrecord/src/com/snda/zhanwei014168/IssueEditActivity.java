package com.snda.zhanwei014168;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Issue;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.Constants;
import com.snda.zhanwei014168.util.PreferenceKey;


/*****************************************
 * 议题增加或者更新
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class IssueEditActivity extends Activity {
	private final static String TAG = "IssueEditActivity";
	
	private EditText m_edtTheme;
	private EditText m_edtPeople;
	private EditText m_edtSpeakTime;
	private Button m_btnOK;	
	private TextView m_tvCommonMainTitle;    
	private Button m_btnBack;
	
    private Issue mIssue;
    private int mMeetingId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
	    setContentView(R.layout.issue_edit_activity);  
	    Intent intent = getIntent();
		mIssue = (Issue) intent.getSerializableExtra(PreferenceKey.PRE_KEY_ISSUE);
		mMeetingId = intent.getIntExtra(Constants.INTENT_EXTRA_MEETING_ID, -1);
		ensureUi();
		if (mIssue != null) {			
			showIssue();
		}
		Logger.getInstance().d(TAG, "IssueEditActivity  loading");	
	}

	private void ensureUi() {
		m_tvCommonMainTitle = (TextView)findViewById(R.id.common_titlebar_name);          
	    m_btnBack = (Button)findViewById(R.id.common_titlebar_left);  
		m_tvCommonMainTitle.setText("议题记录");
		m_btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				Logger.getInstance().d(TAG, "IssueEditActivity retrun last page");
			}
		});
		
		
		m_edtTheme = (EditText)findViewById(R.id.meeting_theme);
		
		m_edtPeople = (EditText)findViewById(R.id.meeting_people);		
		m_edtSpeakTime = (EditText)findViewById(R.id.meeting_date);		
		
		
		m_btnOK = (Button)findViewById(R.id.meeting_ok);
		
		m_btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String theme = m_edtTheme.getText().toString().trim();			
				String people = m_edtPeople.getText().toString().trim();				
				String speaktime = m_edtSpeakTime.getText().toString().trim();
			
				
				if (theme.length() != 0 ) {
					Issue issue = new Issue();	
					issue.mTheme = theme;
					issue.mPeople = people;
					issue.mSpeakTime = speaktime;	
					issue.meeting_id = mMeetingId;
					boolean result1 = false;
					if (mIssue != null) {
						result1 = DataBaseImpl.getInstance().updateIssueDetail(mIssue.id, issue);
					} else {
						result1 = DataBaseImpl.getInstance().addIssue(issue);
					}				
						
					if (result1) {
						Logger.getInstance().d(TAG, "IssueEditActivity  add success...");
						Toast.makeText(IssueEditActivity.this, "恭喜您，提交成功", 0).show();
						setResult(RESULT_OK);
						finish();
					} else {
						Logger.getInstance().d(TAG, "IssueEditActivity  add fairlure...");
						Toast.makeText(IssueEditActivity.this, "提交失败，请重试", 0).show();
					}
				} else {
					Toast.makeText(IssueEditActivity.this, "议题主题不能为空", 1).show();
				}
			}
		});
		
	}
	
	private void showIssue() {
		if (mIssue != null) {
			m_edtTheme.setText(mIssue.mTheme);		
			m_edtPeople.setText(mIssue.mPeople);			
			m_edtSpeakTime.setText(mIssue.mSpeakTime);				
		}		
	}
	
}
