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
import com.snda.zhanwei014168.datatype.Meeting;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.PreferenceKey;



/*****************************************
 * 会议增加更新
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class MeetingEditActivity extends Activity {
	private final static String TAG = "MeetingEditActivity";
	
	private EditText m_edtTheme;
	private EditText m_edtPlace;
	private EditText m_edtHost;
	private EditText m_edtPeople;
	private EditText m_edtDate;
	private EditText m_edtStartTime;
	private EditText m_edtEndTime;
	private Button m_btnOK;	
	private TextView m_tvCommonMainTitle;    
	private Button m_btnBack;
	
    private Meeting mMeeting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
	    setContentView(R.layout.meeting_edit_activity);  
	    Intent intent = getIntent();
		mMeeting = (Meeting) intent.getSerializableExtra(PreferenceKey.PRE_KEY_MEETING);	
		ensureUi();
		if (mMeeting != null) {			
			showMeeting();
		}
		Logger.getInstance().d(TAG, "MeetingEditActivity  loading");	
	}

	private void ensureUi() {
		m_tvCommonMainTitle = (TextView)findViewById(R.id.common_titlebar_name);          
	    m_btnBack = (Button)findViewById(R.id.common_titlebar_left);  
		m_tvCommonMainTitle.setText("会议记录");
		m_btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				Logger.getInstance().d(TAG, "MeetingEditActivity  return last page");	
			}
		});
		
		
		m_edtTheme = (EditText)findViewById(R.id.meeting_theme);
		m_edtPlace = (EditText)findViewById(R.id.meeting_place);
		m_edtHost = (EditText)findViewById(R.id.meeting_host);
		m_edtPeople = (EditText)findViewById(R.id.meeting_people);
		m_edtDate = (EditText)findViewById(R.id.meeting_date);
		m_edtStartTime = (EditText)findViewById(R.id.meeting_starttime);
		m_edtEndTime = (EditText)findViewById(R.id.meeting_endtime);
		
		m_btnOK = (Button)findViewById(R.id.meeting_ok);
		
		m_btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String theme = m_edtTheme.getText().toString().trim();
				String place = m_edtPlace.getText().toString().trim();
				String host = m_edtHost.getText().toString().trim();
				String people = m_edtPeople.getText().toString().trim();
				String date = m_edtDate.getText().toString().trim();
				String starttime = m_edtStartTime.getText().toString().trim();
				String endtime = m_edtEndTime.getText().toString().trim();
				
				if (theme.length() != 0 && place.length() != 0 && date.length() != 0) {
					Meeting meeting = new Meeting();
					meeting.mTheme = theme;
					meeting.mPlace = place;
					meeting.mDate = date;
					meeting.mHost = host;
					meeting.mParticipants = people;
					meeting.mStartTime = starttime;
					meeting.mEndTime = endtime;
					boolean result = false;
					if (mMeeting != null) {
						result = DataBaseImpl.getInstance().updateMeetingDetail(mMeeting.id, meeting);
					} else {
						result = DataBaseImpl.getInstance().addMeeting(meeting);
					}				
						
					if (result) {
						Logger.getInstance().d(TAG, "MeetingEditActivity  add success...");	
						Toast.makeText(MeetingEditActivity.this, "恭喜您，提交成功", 0).show();
						setResult(RESULT_OK);
						finish();
					} else {
						Logger.getInstance().d(TAG, "MeetingEditActivity  add fairlure...");	
						Toast.makeText(MeetingEditActivity.this, "提交失败，请重试", 0).show();
					}
				} else {
					Toast.makeText(MeetingEditActivity.this, "会议主题，地点，日期不能为空", 1).show();
				}
			}
		});
		
	}
	
	private void showMeeting() {
		if (mMeeting != null) {
			m_edtTheme.setText(mMeeting.mTheme);
			m_edtPlace.setText(mMeeting.mPlace);
			m_edtHost.setText(mMeeting.mHost);
			m_edtPeople.setText(mMeeting.mParticipants);
			m_edtDate.setText(mMeeting.mDate);
			m_edtStartTime.setText(mMeeting.mStartTime);
			m_edtEndTime.setText(mMeeting.mEndTime);
		}
		
	}
	
}
