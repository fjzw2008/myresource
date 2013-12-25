package com.snda.zhanwei014168;

import java.io.File;

import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.RequestToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Meeting;
import com.snda.zhanwei014168.datatype.Photo;
import com.snda.zhanwei014168.log.Logger;
import com.snda.zhanwei014168.sina.OAuthActivity;
import com.snda.zhanwei014168.sina.OAuthConstant;
import com.snda.zhanwei014168.sina.WeiBoConstants;
import com.snda.zhanwei014168.util.Constants;
import com.snda.zhanwei014168.util.PreferenceKey;
import com.snda.zhanwei014168.util.Util;
import com.snda.zhanwei014168.widget.CustomDialog;

/*****************************************
 * 会议详情，包含拍照功能，目前拍照功能只针对1.6+中高端机器，没有做适配
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class MeetingDetailActivity extends Activity {
	private final static String TAG = "MeetingDetailActivity";
	
	public final static int RESULT_CODE_FOR_TAKE_PIC = 200;
	public final static int RESULT_CODE_FOR_GET_PHOTO_FROM_ABULM = 201;
	public final static int RESULT_CODE_FOR_TAKE_MEETING = 202;
	
	public static final String MIME_TYPE_IMAGE = "image/*";
	public static final String EXTRA_TEMP_PHOTO_NAME="meeting_camera.jpg";
	
	private TextView m_tvTheme;
	private TextView m_tvPlace;
	private TextView m_tvHost;
	private TextView m_tvPeople;
	private TextView m_tvDate;
	private TextView m_tvStartTime;
	private TextView m_tvEndTime;
	
	private TextView m_tvIssueList;
	private TextView m_tvTaskList;
	private TextView m_tvPhotoList;
	
	private TextView m_tvCommonMainTitle;    
	private Button m_btnBack;
	private Button m_btnAdd; 
	
	private Meeting mMeeting;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.meeting_detail_activity);
		Intent intent = getIntent();
		mMeeting = (Meeting) intent.getSerializableExtra(PreferenceKey.PRE_KEY_MEETING);	
		if (mMeeting != null) {
			ensureUi();
			showMeeting();
		} else {
			finish();
		}
		Logger.getInstance().d(TAG, "MeetingDetailActivity  loading");	
		
	}
	
	private void ensureUi() {
		m_tvCommonMainTitle = (TextView)findViewById(R.id.common_titlebar_name);          
	    m_btnBack = (Button)findViewById(R.id.common_titlebar_left);  
		m_tvCommonMainTitle.setText("会议详情");
		m_btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();		
				Logger.getInstance().d(TAG, "MeetingDetailActivity return last page ");	
			}
		});		
		
	    m_btnAdd = (Button)findViewById(R.id.common_titlebar_right);  
        m_btnAdd.setVisibility(View.VISIBLE);
        m_btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.getInstance().d(TAG, "MeetingDetailActivity  update meeting");
				Intent intent = new Intent(MeetingDetailActivity.this, MeetingEditActivity.class);	
				intent.putExtra(PreferenceKey.PRE_KEY_MEETING, mMeeting);
				startActivityForResult(intent, 0);		
				
			}
		});
		
		m_tvTheme = (TextView)findViewById(R.id.meeting_theme);
		m_tvPlace = (TextView)findViewById(R.id.meeting_place);
		m_tvHost = (TextView)findViewById(R.id.meeting_host);
		m_tvPeople = (TextView)findViewById(R.id.meeting_people);
		m_tvDate = (TextView)findViewById(R.id.meeting_date);
		m_tvStartTime = (TextView)findViewById(R.id.meeting_starttime);
		m_tvEndTime = (TextView)findViewById(R.id.meeting_endtime);
		
		m_tvIssueList = (TextView)findViewById(R.id.meeting_issuelist);
		m_tvTaskList = (TextView)findViewById(R.id.meeting_tasklist);
		m_tvPhotoList = (TextView)findViewById(R.id.meeting_photolist);
		
		m_tvIssueList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.getInstance().d(TAG, "go to IssueListActivity");	
				Intent intent = new Intent(MeetingDetailActivity.this,IssueListActivity.class);
				intent.putExtra(Constants.INTENT_EXTRA_MEETING_ID, mMeeting.id);
				startActivity(intent);
				
			}
		});
		
		m_tvTaskList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Logger.getInstance().d(TAG, "go to TaskListActivity");	
				Intent intent = new Intent(MeetingDetailActivity.this,TaskListActivity.class);
				intent.putExtra(Constants.INTENT_EXTRA_MEETING_ID, mMeeting.id);
				startActivity(intent);
				
			}
		});
	
		m_tvPhotoList.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Logger.getInstance().d(TAG, "go to PhotoGridViewActivity");	
				Intent intent = new Intent(MeetingDetailActivity.this,PhotoGridViewActivity.class);
				intent.putExtra(Constants.INTENT_EXTRA_MEETING_ID, mMeeting.id);
				startActivity(intent);
				
			}
		});
	}
	
	
	private void showMeeting() {
		if (mMeeting != null) {
			m_tvTheme.setText("主题：" + mMeeting.mTheme);
			m_tvPlace.setText("地点：" +mMeeting.mPlace);
			m_tvHost.setText("主持：" +mMeeting.mHost);
			m_tvPeople.setText("人员：" +mMeeting.mParticipants);
			m_tvDate.setText("日期：" +mMeeting.mDate);
			m_tvStartTime.setText("开始时间：" +mMeeting.mStartTime);
			m_tvEndTime.setText("结束时间：" +mMeeting.mEndTime);
		}
		
	}
	
	private Uri imageUri = null;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data){
		
		if (requestCode == RESULT_CODE_FOR_TAKE_MEETING) {
				switch (resultCode){		
				case RESULT_OK:
					mMeeting = DataBaseImpl.getInstance().queryMeetingDetail(mMeeting.id);
					showMeeting();
					break;				
				}
		} else {
			if (data != null) {				
				imageUri = data.getData();
			} 
			String filePath = null;
			File m_bitmapfile = Util.convertImageUriToFile(imageUri,this);    
    		if(m_bitmapfile != null){
    			filePath = m_bitmapfile.getAbsolutePath();
    		}
			Photo photo = new Photo();
			photo.mPhotoPath = filePath;
			photo.meeting_id = mMeeting.id;
			boolean result = DataBaseImpl.getInstance().addPhoto(photo);
			if (result) {
				Logger.getInstance().d(TAG, "add photo success");	
				Toast.makeText(this, "添加照片成功", 0).show();
			} else {
				Logger.getInstance().d(TAG, "add photo failure");	
				Toast.makeText(this, "添加照片失败", 0).show();
			}					
		} 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 1, "刷新").setIcon(R.drawable.ic_refresh);
		menu.add(0, 1, 2, "编辑").setIcon(R.drawable.ic_add);
		menu.add(0, 2, 3, "添加图片").setIcon(R.drawable.ic_add_photo);
		menu.add(0, 3, 4, "分享").setIcon(R.drawable.logo);
		return super.onCreateOptionsMenu(menu);
	}
	
	public final static int DIALOG_SHOW_LIST = 0;

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Logger.getInstance().d(TAG, "MeetDetailActivity refresh...");	
			mMeeting = DataBaseImpl.getInstance().queryMeetingDetail(mMeeting.id);
			showMeeting();
			break;
		case 1:
			Logger.getInstance().d(TAG, "MeetDetailActivity go to MeetingEditActivity");	
			Intent intent = new Intent(this, MeetingEditActivity.class);				
			startActivityForResult(intent, 0);
			break;
		case 2:
			Logger.getInstance().d(TAG, "MeetDetailActivity go to take photo");	
			showDialog(DIALOG_SHOW_LIST);
			break;
		case 3:
			showWeiBoDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void showWeiBoDialog() {
		final CustomDialog dialog = new CustomDialog(this);
		dialog.setTitle("新浪微博分享");
		EditText m_edtTitle = dialog.getM_UserName();
		m_edtTitle.setText(mMeeting.mTheme);
		m_edtTitle.setHint("");
		EditText m_edtPeople = dialog.getM_UserPass();
		m_edtPeople.setText(mMeeting.mParticipants);
		m_edtPeople.setHint("");
		EditText m_edtPlace = dialog.getM_StartTime();
		m_edtPlace.setText(mMeeting.mPlace);
		m_edtPlace.setHint("");
		dialog.setConfirmOnclick("确定", new OnClickListener() {			
			@Override
			public void onClick(View v) {	
				doShareWeibo(dialog);		
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
	
	private void doShareWeibo(CustomDialog dialog) {
		
		String title = dialog.getM_UserName().getText().toString().trim();				
		String mEndDateForSearch = dialog.getM_UserPass().getText().toString().trim();
		String mStartDateForSearch = dialog.getM_StartTime().getText().toString().trim();	
		
		
		System.setProperty("weibo4j.oauth.consumerKey",WeiBoConstants.SINACUSTOMERID);
	    System.setProperty("weibo4j.oauth.consumerSecret",WeiBoConstants.SINACUSTOMERSECRET);		      
		Weibo weibo = OAuthConstant.getInstance().getWeibo();	
		String token = OAuthActivity.getSinaToken(this);
		String secretToken = OAuthActivity.getSinaTokenSecret(this);
		if (TextUtils.isEmpty(token) || TextUtils.isEmpty(secretToken)) {
			 AuthWeibo();
			 return;
		}
		
		weibo.setToken(token, secretToken);
		try {
			//传内容的地方
			weibo.updateStatus("会议主题:" + title + "会议定点:" + mStartDateForSearch + "会议人员:" + mEndDateForSearch);			  
			Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
			
		} catch (WeiboException e) {				
			Logger.getInstance().e("sina pusn====>", e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 跳转登陆,获取token
	 */
	private void AuthWeibo() {	
        System.setProperty("weibo4j.oauth.consumerKey",WeiBoConstants.SINACUSTOMERID);
		System.setProperty("weibo4j.oauth.consumerSecret",WeiBoConstants.SINACUSTOMERSECRET);		 
		
    	Weibo weibo = OAuthConstant.getInstance().getWeibo();
    	RequestToken requestToken;
    	try {
			requestToken = weibo.getOAuthRequestToken("weibo4android://OAuthActivity");
			Uri uri = Uri.parse(requestToken.getAuthenticationURL()+ "&from=xweibo");
			OAuthConstant.getInstance().setRequestToken(requestToken);
			startActivity(new Intent(Intent.ACTION_VIEW, uri));
		} 
		catch (WeiboException e) {		 
			e.printStackTrace();			 
		}
				
	}

	
	
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_SHOW_LIST) {
			return new AlertDialog.Builder(this)
	        	.setTitle("选择图片")
	        	.setItems(R.array.choose_photo_items, new DialogInterface.OnClickListener() {
	        		public void onClick(DialogInterface dialog, int which) {
	            	 //take the photo
	            	 if (which == 0) {	      
	            		 Logger.getInstance().d(TAG, "MeetDetailActivity go to take photo capture");
	            		 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	            		 if (MeetingApplication.isSDCardAvailable()) {	            			
	            			long now = System.currentTimeMillis();
	            			String fileName = "meeting"+String.valueOf(now)+".jpg";	 	         
		    				ContentValues values = new ContentValues(); 
		    				values.put(MediaStore.Images.Media.TITLE, fileName); 
		    				values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
		    				values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
		    				values.put(MediaStore.Images.Media.DATE_TAKEN, now);
		    				values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		    				imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
		            		
		    				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		    				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);		    			
		    				startActivityForResult(intent, RESULT_CODE_FOR_TAKE_PIC);
	            		 } else {
	            			 Toast.makeText(MeetingDetailActivity.this, R.string.str_err_nosd, Toast.LENGTH_LONG).show();
	            		 }	            		 
	            	 } else {//
	            		 Logger.getInstance().d(TAG, "MeetDetailActivity choose a photo from album");
	            		 File out = new File(Util.getPhotoPath(), EXTRA_TEMP_PHOTO_NAME);
          				 Uri imageUri = Uri.fromFile(out);
	            		 Intent mIntent= new Intent(Intent.ACTION_GET_CONTENT);
	            		 mIntent.addCategory(Intent.CATEGORY_OPENABLE);
	            		 mIntent.setType(MIME_TYPE_IMAGE);
	            		 mIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	            		 mIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	            		 mIntent.putExtra("return-data", 0);                   		 
	            		
	            		 startActivityForResult(mIntent, RESULT_CODE_FOR_GET_PHOTO_FROM_ABULM);
	            	 }
	             }
	         })
	         .create();
		}
		return super.onCreateDialog(id);
	}
	
}
