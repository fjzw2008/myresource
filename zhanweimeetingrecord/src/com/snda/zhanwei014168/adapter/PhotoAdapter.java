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
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;


/*****************************************
 * 图片墙适配器
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.25 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class PhotoAdapter extends BaseAdapter {

	
	private ArrayList<Photo> mPhotoList;
	private Context mContext;
	private final int mCellDimen;	
	BitmapFactory.Options options ;
	
	public PhotoAdapter(Context context) {	
		mContext =  context;		
		mCellDimen = mContext.getResources().getDimensionPixelSize(R.dimen.photo_width_height);	
		options = new BitmapFactory.Options();
		options.inSampleSize = 4;
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
		ViewHolder mViewHolder = null ;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			ImageView m_ivPhoto = new ImageView(mContext);			
			AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(mCellDimen, mCellDimen);
			m_ivPhoto.setLayoutParams(localLayoutParams);
			ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
			m_ivPhoto.setScaleType(localScaleType);
			m_ivPhoto.setBackgroundResource(R.drawable.selector_photo_background);	
			int mPadding = mContext.getResources().getDimensionPixelSize(R.dimen.photo_grid_padding);
			m_ivPhoto.setPadding(mPadding, mPadding, mPadding, mPadding);
			mViewHolder.m_ivPhoto = m_ivPhoto ;
			convertView = m_ivPhoto ;
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder)convertView.getTag();			
		}
		Photo photo = (Photo) getItem(position);
		String photoUrl = photo.mPhotoPath;
		Bitmap bitmap = BitmapFactory.decodeFile(photoUrl, options);
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
