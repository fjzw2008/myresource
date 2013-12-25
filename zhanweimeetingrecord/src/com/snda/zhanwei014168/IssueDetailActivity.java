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
import com.snda.zhanwei014168.datatype.Issue;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.PreferenceKey;

/*****************************************
 * 议题详情
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class IssueDetailActivity extends Activity {
	private final static String TAG = "IssueDetailActivity";
	
	public final static int RESULT_CODE_FOR_TAKE_PIC = 200;
	public final static int RESULT_CODE_FOR_GET_PHOTO_FROM_ABULM = 201;
	public final static int RESULT_CODE_FOR_TAKE_MEETING = 202;
	
	public static final String MIME_TYPE_IMAGE = "image/*";
	public static final String EXTRA_TEMP_PHOTO_NAME="meeting_camera.jpg";
	
	private TextView m_tvTheme;
	private TextView m_tvPeople;
	private TextView m_tvSpeakTime;
	
	
	private TextView m_tvCommonMainTitle;    
	private Button m_btnBack;
	private Button m_btnAdd; 
	
	private Issue mIssue;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.issue_detail_activity);
		Intent intent = getIntent();
		mIssue = (Issue) intent.getSerializableExtra(PreferenceKey.PRE_KEY_ISSUE);	
		if (mIssue != null) {
			ensureUi();
			showIssue();
		} else {
			finish();
		}
		Logger.getInstance().d(TAG, "IssueDetailActivity  loading");	
		
	}
	
	private void ensureUi() {
		m_tvCommonMainTitle = (TextView)findViewById(R.id.common_titlebar_name);          
	    m_btnBack = (Button)findViewById(R.id.common_titlebar_left);  
		m_tvCommonMainTitle.setText("议题详情");
		m_btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();		
				Logger.getInstance().d(TAG, "IssueDetailActivity return last page ");	
			}
		});		
		
	    m_btnAdd = (Button)findViewById(R.id.common_titlebar_right);  
        m_btnAdd.setVisibility(View.VISIBLE);
        m_btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.getInstance().d(TAG, "IssueDetailActivity  update meeting");
				Intent intent = new Intent(IssueDetailActivity.this, IssueEditActivity.class);	
				intent.putExtra(PreferenceKey.PRE_KEY_ISSUE, mIssue);
				startActivityForResult(intent, 0);		
				
			}
		});
		
		m_tvTheme = (TextView)findViewById(R.id.meeting_theme);		
		m_tvPeople = (TextView)findViewById(R.id.meeting_people);		
		m_tvSpeakTime = (TextView)findViewById(R.id.meeting_date);	
		
	}
	
	
	private void showIssue() {
		if (mIssue != null) {
			m_tvTheme.setText("任务名称：" + mIssue.mTheme);		
			m_tvPeople.setText("负责人员：" +mIssue.mPeople);			
			m_tvSpeakTime.setText("发言时间：" +mIssue.mSpeakTime);
		
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data){	
		switch (resultCode){		
		case RESULT_OK:
			mIssue = DataBaseImpl.getInstance().queryIssueDetail(mIssue.id);
			showIssue();
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
			Logger.getInstance().d(TAG, "MeetDetailActivity refresh...");	
			mIssue = DataBaseImpl.getInstance().queryIssueDetail(mIssue.id);
			showIssue();
			break;
		case 1:
			Logger.getInstance().d(TAG, "MeetDetailActivity go to IssueEditActivity");	
			Intent intent = new Intent(this, IssueEditActivity.class);				
			startActivityForResult(intent, 0);
			break;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
