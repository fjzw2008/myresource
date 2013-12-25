package com.snda.zhanwei014168;

import java.util.ArrayList;
import com.snda.zhanwei014168.adapter.IssueListAdapter;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Issue;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.Constants;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/*****************************************
 * 议题列表
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class IssueListActivity extends BaseListActivity {
	private final static String TAG = "IssueListAcitivity";
	
	private ListView mListView;
	private IssueListAdapter mListAdapter;
	private ArrayList<Issue> mDataList;
	private DeleteReceiver mDeleteReceiver;
	private int mMeetingId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
        	finish();
        }
        mMeetingId = bundle.getInt(Constants.INTENT_EXTRA_MEETING_ID);
		ensureUi();
		new GetIssueListTask().execute();
		Logger.getInstance().d(TAG, "IssueListActivity  loading");	
		mDeleteReceiver = new DeleteReceiver();
		registerReceiver(mDeleteReceiver, new IntentFilter(Constants.INTENT_ACTION_DELETE));
	}
	
	private void ensureUi() {
		m_tvCommonMainTitle.setText("议题列表");
    	mListView = getListView();     
    	mListView.setDivider(new ColorDrawable(0xFFE3E2E3));
    	mListView.setDividerHeight(1);
    	mListAdapter = new IssueListAdapter(this,mMeetingId);		
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
				Intent intent = new Intent(IssueListActivity.this, IssueEditActivity.class);
				intent.putExtra(Constants.INTENT_EXTRA_MEETING_ID, mMeetingId);
				startActivityForResult(intent, 0);				
			}
		});
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
			Logger.getInstance().d(TAG, "IssueListActivity  refresh");	
			new GetIssueListTask().execute();
			break;
		case 1:
			Logger.getInstance().d(TAG, "IssueListActivity  add issue");	
			Intent intent = new Intent(this, IssueEditActivity.class);		
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
			new GetIssueListTask().execute();
			break;
		}
	}
	
	private class GetIssueListTask extends AsyncTask<Void, Integer, ArrayList<Issue>> {		

		@Override
		protected void onPreExecute() {
			Logger.getInstance().d(TAG, "GetIssueListTask onPreExecute");		
			if (mDataList == null || mDataList.size() == 0) {
				showLoadingView();
			}
		}		
		
		@Override
		protected ArrayList<Issue> doInBackground(Void... params) {
			Logger.getInstance().d(TAG, "GetIssueListTask doInBackground");
			return DataBaseImpl.getInstance().queryIssue(mMeetingId);
		
		}		
		@Override
		protected void onPostExecute(ArrayList<Issue> list){
			Logger.getInstance().d(TAG, "GetIssueListTask onPostExecute");			
			mDataList = list;
			if (mDataList == null || mDataList.size() == 0) {
				setEmptyView();
			} else {				
				ensureUi();
			}
			
			hideLoadingView();			
			
		}
		
		@Override
		protected void onCancelled() {
			Logger.getInstance().d(TAG, "GetIssueListTask onCancelled");				
		}
	}
}
