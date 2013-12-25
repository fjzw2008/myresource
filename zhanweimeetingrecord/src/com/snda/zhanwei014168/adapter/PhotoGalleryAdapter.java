package com.snda.zhanwei014168.adapter;


import java.util.ArrayList;
import com.snda.zhanwei014168.R;
import com.snda.zhanwei014168.datatype.Photo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/*****************************************
 * 画廊适配器
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class PhotoGalleryAdapter extends BaseAdapter {

	private ArrayList<Photo> mPhotoList;
	private Context mContext;
	
	public PhotoGalleryAdapter(Context context) {	
		mContext =  context;
	}
	
	public void setPhotoList(ArrayList<Photo> photoList){
		mPhotoList = photoList;
	}
	
	@Override
	public int getCount() {
		if (mPhotoList != null) {
			return mPhotoList.size();
		} 
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mPhotoList == null) {
			return null;
		} else {
			return mPhotoList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
	    	mViewHolder = new ViewHolder();	    	
	    	ImageView mImageView = new ImageView(mContext);
	    	Gallery.LayoutParams mLayoutParams = new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    	mImageView.setLayoutParams(mLayoutParams);
	    	ImageView.ScaleType mScaleType = ImageView.ScaleType.CENTER_INSIDE;
	    	mImageView.setScaleType(mScaleType);
	    	mImageView.setAdjustViewBounds(true);
	    	mViewHolder.m_ivPhoto = mImageView;
	    	convertView = mImageView;
	    	convertView.setTag(mViewHolder);
		} else {
	    	mViewHolder = (ViewHolder)convertView.getTag();
		}	   
		Photo photo = (Photo) getItem(position);
		String photoUrl = photo.mPhotoPath;
		Bitmap bitmap = BitmapFactory.decodeFile(photoUrl);
		if (bitmap != null) {
			mViewHolder.m_ivPhoto.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} else {
			mViewHolder.m_ivPhoto.setImageResource(R.drawable.bg_empty_camera);
		}
		return convertView;
		  
	}
		

	class ViewHolder {
		ImageView m_ivPhoto;
	}	
	
	
}

