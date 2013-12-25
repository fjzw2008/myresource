package com.snda.zhanwei014168;

import java.util.ArrayList;
import com.snda.zhanwei014168.adapter.MeetingListAdapter;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Meeting;
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
 * 会议列表
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class MeetingListAcitivity extends BaseListActivity {
	private final static String TAG = "MeetingListAcitivity";
	
	private ListView mListView;
	private MeetingListAdapter mListAdapter;
	private ArrayList<Meeting> mDataList;
	private DeleteReceiver mDeleteReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ensureUi();
		Logger.getInstance().d(TAG, "MeetingListActivity loading...");	
		new GetMeetingListTask().execute();
		mDeleteReceiver = new DeleteReceiver();
		registerReceiver(mDeleteReceiver, new IntentFilter(Constants.INTENT_ACTION_DELETE));
	}
	
	private void ensureUi() {
		m_tvCommonMainTitle.setText("会议列表");
    	mListView = getListView();     
    	mListView.setDivider(new ColorDrawable(0xFFE3E2E3));
    	mListView.setDividerHeight(1);
    	mListAdapter = new MeetingListAdapter(this);		
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
				Intent intent = new Intent(MeetingListAcitivity.this, MeetingEditActivity.class);				
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
		menu.add(0, 1, 2, "添加会议").setIcon(R.drawable.ic_add);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Logger.getInstance().d(TAG, "MeetingListActivity  refresh");	
			new GetMeetingListTask().execute();
			break;
		case 1:
			Logger.getInstance().d(TAG, "MeetingListActivity  come in add meeting");	
			Intent intent = new Intent(this, MeetingEditActivity.class);				
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
			new GetMeetingListTask().execute();
			break;
		}
	}
	
	private class GetMeetingListTask extends AsyncTask<Void, Integer, ArrayList<Meeting>> {		

		@Override
		protected void onPreExecute() {
			Logger.getInstance().d(TAG, "GetMeetingListTask onPreExecute");		
			if (mDataList == null || mDataList.size() == 0) {
				showLoadingView();
			}
		}		
		
		@Override
		protected ArrayList<Meeting> doInBackground(Void... params) {
			Logger.getInstance().d(TAG, "GetMeetingListTask doInBackground");
			return DataBaseImpl.getInstance().queryMeetings();
		
		}		
		@Override
		protected void onPostExecute(ArrayList<Meeting> list){
			Logger.getInstance().d(TAG, "GetMeetingListTask onPostExecute");			
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
			Logger.getInstance().d(TAG, "GetMeetingListTask onCancelled");				
		}
	}
}
