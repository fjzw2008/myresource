package com.snda.zhanwei014168;

import java.util.ArrayList;

import com.snda.zhanwei014168.adapter.PhotoAdapter;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Photo;
import com.snda.zhanwei014168.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

/*****************************************
 * 图片墙
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class PhotoGridViewActivity extends Activity {
	
	private int mMeetingId;
	private ArrayList<Photo> mPhotoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.meeting_photos);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
        	finish();
        }
        mMeetingId = bundle.getInt(Constants.INTENT_EXTRA_MEETING_ID);
        mPhotoList = DataBaseImpl.getInstance().queryMeetingPhoto(mMeetingId);       
        ensureUi();
	}
	
	private void ensureUi() {
	  	GridView mPhotosGridView = (GridView) findViewById(R.id.gridview);
        PhotoAdapter mPhotoAdaptter = new PhotoAdapter(this);
        mPhotoAdaptter.setPhotoList(mPhotoList);
        mPhotosGridView.setAdapter(mPhotoAdaptter);
        mPhotoAdaptter.notifyDataSetChanged();
        mPhotosGridView.setSmoothScrollbarEnabled(true);     
        mPhotosGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {	      
            	Intent intent = new Intent(PhotoGridViewActivity.this,PhotoGalleryActivity.class);
            	intent.putExtra(Constants.INTENT_EXTRA_PHOTO_POSITION, position);   
            	intent.putExtra(Constants.INTENT_EXTRA_MEETING_ID, mMeetingId);
            	startActivity(intent);
			}
        	
		}); 
		
	}
}
