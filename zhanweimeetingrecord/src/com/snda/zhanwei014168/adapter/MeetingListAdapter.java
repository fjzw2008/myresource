package com.snda.zhanwei014168.adapter;



import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.snda.zhanwei014168.MeetingDetailActivity;
import com.snda.zhanwei014168.MeetingEditActivity;
import com.snda.zhanwei014168.R;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Meeting;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.util.Constants;
import com.snda.zhanwei014168.util.PreferenceKey;


/*****************************************
 * 会议列表数据适配器
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class MeetingListAdapter extends BaseAdapter{		
		
	private final static String TAG = "MeetingListAdapter";
		
		private ArrayList<Meeting> mDataList;	
		private Context mContext;
		private LayoutInflater m_inflater;	
		
        public MeetingListAdapter(Context c) {
            mContext = c;         
            m_inflater = LayoutInflater.from(mContext);
    		
        }

        public void setDataList(ArrayList<Meeting> dataList){
        	mDataList = dataList;        
        }
        public int getCount() {
        	if (mDataList == null) {
    			return 0;    		
    		} else {
    			return mDataList.size();
    		}
        }

        public Object getItem(int position) {
        	if (mDataList == null)
    			return null;
    		else{    		
    			return mDataList.get(position);
    		}
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	ViewHolder holder;
        	if (convertView == null) {
				holder = new ViewHolder();
				convertView = m_inflater.inflate(R.layout.item_list,
						null);
				holder.m_tvTitle = (TextView) convertView.findViewById(R.id.item_list_title);
				holder.m_tvBody = (TextView) convertView.findViewById(R.id.item_list_body);
				holder.m_tvBottom = (TextView) convertView.findViewById(R.id.item_list_bottom);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
        	
        	
        	final Meeting meeting = (Meeting) getItem(position);
    		holder.m_tvTitle.setText(meeting.mTheme);
    		holder.m_tvBody.setText(meeting.mPlace);
    		holder.m_tvBottom.setText(meeting.mDate);       
        	
        	convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {									  
					Intent intent = new Intent(mContext,MeetingDetailActivity.class);		
		        	intent.putExtra(PreferenceKey.PRE_KEY_MEETING, meeting);
				   	mContext.startActivity(intent);
				   	Logger.getInstance().d(TAG, "go to MeetingDetailActivity");	
				}
			});    
        	
        	convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					ArrayList<String> contentList = new ArrayList<String>();
					contentList.add("编辑");
					contentList.add("删除");
					String [] title = new String[contentList.size()];
					contentList.toArray(title);
					AlertDialog dlgAlertDialog =  new AlertDialog.Builder(mContext)
			         .setTitle(meeting.mTheme)
			         .setItems(title, getDialogClickRunnalbe(meeting))
				        .create();
					dlgAlertDialog.show();	
					return true;
					
				}
			});
        	
			return convertView;         
           
        }
       
        
    private final DialogInterface.OnClickListener getDialogClickRunnalbe(final Meeting meeting) {
    	return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	switch(which) {
            	case 0:
            		Logger.getInstance().d(TAG, "go to MeetingEditActivity...");	
            		Intent intent = new Intent(mContext,MeetingEditActivity.class);		
		        	intent.putExtra(PreferenceKey.PRE_KEY_MEETING, meeting);
				   	mContext.startActivity(intent);
            		break;
            	case 1:
            		Logger.getInstance().d(TAG, "deleting meeting...");	
            		boolean result = DataBaseImpl.getInstance().deleteMeetingDetail(meeting.id);
            		if (result) {
            			Intent i = new Intent();
            			i.putExtra(Constants.INTENT_EXTRA_MEETING_ID, meeting.id);
            			i.setAction(Constants.INTENT_ACTION_DELETE);
            			mContext.sendBroadcast(i);
            			Toast.makeText(mContext, "删除成功", 0).show();
            		} else {
            			Toast.makeText(mContext, "删除失败", 0).show();
            		}
            		break;
            	}
            }
    	};
            	
            
    }
    static class ViewHolder {
		TextView m_tvTitle;
		TextView m_tvBody;
		TextView m_tvBottom;    	
	}

}
