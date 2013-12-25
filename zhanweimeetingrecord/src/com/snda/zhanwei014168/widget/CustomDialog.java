package com.snda.zhanwei014168.widget;

import com.snda.zhanwei014168.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*****************************************
 * 搜索筛选弹出框
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class CustomDialog extends Dialog {
	private LayoutParams p;
	private Context mContext;
	private float mDialogWidth = 0.85f;
	private LayoutInflater m_inflate;
	private View m_DialogView;
	private TextView m_Title;
	private TextView m_Message;
	private Button m_Confirm;
	private Button m_Cancel;
	private	EditText m_UserName;
	private EditText m_UserPass;
	private EditText m_StartTime;
	
	
   	public TextView getM_Title() {
		return m_Title;
	}

	public EditText getM_UserName() {
		return m_UserName;
	}

	public EditText getM_UserPass() {
		return m_UserPass;
	}
	
	public EditText getM_StartTime() {
		return m_StartTime;
	}
	
	public CustomDialog(Context context) {
   		super(context,R.style.CustomDialogTheme);   		
   		mContext = context;
   		init();
   	}
   	/**
   	 * 
   	 * @param context 
   	 * @param view 自定义的layout
   	 * @param dialogWidth 表示当前dialog的宽占屏幕的宽的百分比
   	 */
   	public CustomDialog(Context context,float dialogWidth) {
   		super(context,R.style.CustomDialogTheme);   		
   		mContext = context;
   		mDialogWidth = dialogWidth;
   		init();
   	}
   	
   	private void init(){
   		m_inflate = LayoutInflater.from(mContext);
		m_DialogView = m_inflate.inflate(R.layout.dlg_login, null);
		m_Title = (TextView)m_DialogView.findViewById(R.id.login_dialog_title);	
		m_Message = (TextView)m_DialogView.findViewById(R.id.login_dialog_body_content);		
		m_Confirm = (Button)m_DialogView.findViewById(R.id.login_dialog_confirm);	
		m_Cancel = (Button)m_DialogView.findViewById(R.id.login_dialog_cancel);	
		m_UserName = (EditText)m_DialogView.findViewById(R.id.login_dialog_body_name);
		m_UserPass = (EditText) m_DialogView.findViewById(R.id.login_dialog_body_pass);
		m_StartTime= (EditText) m_DialogView.findViewById(R.id.login_dialog_body_starttime);
		
   	}
   	
   	 protected void onCreate(Bundle savedInstanceState){
   		 super.onCreate(savedInstanceState);    		 
   		 setContentView(m_DialogView); 
   		 m_DialogView.setBackgroundResource(R.drawable.shape_rounded_corners_view);
   		 if(mContext != null){
   			 WindowManager m = ((Activity) mContext).getWindowManager();
    		 Display d = m.getDefaultDisplay();	
    		 p = getWindow().getAttributes();
    		 p.width = (int) (d.getWidth() * mDialogWidth);  		
    		 getWindow().setAttributes(p); 
   		 }
   	 }
	/**
	 * 设置Dialog Message
	 * @param message
	 */
	public void setMessage(CharSequence message) {
		m_Message.setVisibility(View.VISIBLE);
		m_Message.setText(message);
	}
	/**
	 * 设置Dialog标题头
	 */
	public void setTitle(CharSequence title) {
		m_Title.setText(title);
	}  
    /**
     * Set the title text for this dialog's window. The text is retrieved
     * from the resources with the supplied identifier.
     *
     * @param titleId the title's text resource identifier
     */
    public void setTitle(int titleId) {
		m_Title.setText(mContext.getText(titleId));
    }
   	
  	/**
   	 * 设置确定按钮显示字符和监听事件
   	 * @param confirm
   	 * @param listener
   	 */
   	public void setConfirmOnclick(String confirm,View.OnClickListener listener){ 
   		m_Confirm.setText(confirm);
   		m_Confirm.setOnClickListener(listener);		
   	}
   	/**
   	 * 设置取消按钮显示字符和监听事件
   	 * @param cancel
   	 * @param listener
   	 */
   	public void setCancelOnclick(String cancel,View.OnClickListener listener){
   		m_Cancel.setText(cancel);
   		m_Cancel.setOnClickListener(listener);		
   	}

   }