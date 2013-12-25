package com.snda.zhanwei014168;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/*****************************************
 * 一些列表页的父类,列表共性
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/

public class BaseListActivity extends ListActivity {
	
	private ProgressBar m_tvEmptyProgress;
	private LinearLayout m_lyEmptyTip;  
    protected TextView m_tvCommonMainTitle;    
    protected Button m_btnBack;
    protected Button m_btnAdd;  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.base_list_activity);     

        m_tvEmptyProgress = (ProgressBar)findViewById(R.id.emptyProgress);
        m_lyEmptyTip = (LinearLayout)findViewById(R.id.empty_show); 
      
        
        m_tvCommonMainTitle = (TextView)findViewById(R.id.common_titlebar_name);          
        m_btnBack = (Button)findViewById(R.id.common_titlebar_left);  
        m_btnAdd = (Button)findViewById(R.id.common_titlebar_right);  
        m_btnAdd.setVisibility(View.VISIBLE);
        m_btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				finish();
			}
		});
        
    }
    
    protected void showLoadingView() {
    	m_lyEmptyTip.setVisibility(View.GONE);     
        m_tvEmptyProgress.setVisibility(View.VISIBLE);   
    }
    
    protected void hideLoadingView() {
    	m_tvEmptyProgress.setVisibility(View.GONE); 

    }    
    
    protected void setEmptyView() {
        m_tvEmptyProgress.setVisibility(View.GONE);     
        m_lyEmptyTip.setVisibility(View.VISIBLE);        
    }
}
