package com.snda.zhanwei014168;

import java.util.ArrayList;

import com.snda.zhanwei014168.adapter.PhotoGalleryAdapter;
import com.snda.zhanwei014168.dao.DataBaseImpl;
import com.snda.zhanwei014168.datatype.Photo;
import com.snda.zhanwei014168.util.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Gallery;

/*****************************************
 * 图片画廊，目前没有做图片显示优化，图片内存处理也没有，有可能会导致CRASH
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class PhotoGalleryActivity extends Activity {

	
	private int mMeetingId;
	private int mCurrentIndex;
	private ArrayList<Photo> mPhotoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.meeting_photo_gallery);
	    Bundle bundle = getIntent().getExtras();
	    if (bundle == null) {
	    	finish();
	    }
	    mMeetingId = bundle.getInt(Constants.INTENT_EXTRA_MEETING_ID);
	    mCurrentIndex = bundle.getInt(Constants.INTENT_EXTRA_PHOTO_POSITION, 0);
	    mPhotoList = DataBaseImpl.getInstance().queryMeetingPhoto(mMeetingId);
	    ensureUi();
	}
	
	private void ensureUi() {
	    Gallery mGallery = (Gallery) findViewById(R.id.gallery);
	    PhotoGalleryAdapter mAdapter = new PhotoGalleryAdapter(this);
	    mAdapter.setPhotoList(mPhotoList);
	    mGallery.setAdapter(mAdapter);
	    mAdapter.notifyDataSetChanged();
	    mGallery.setSelection(mCurrentIndex,true);	
	}
	

}
