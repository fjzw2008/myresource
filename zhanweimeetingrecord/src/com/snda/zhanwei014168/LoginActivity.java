package com.snda.zhanwei014168;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.snda.sdw.woa.callback.LoginCallBack;
import com.snda.sdw.woa.interfaces.OpenAPI;

/*****************************************
 * 无线OA的登陆 测试merge
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
public class LoginActivity extends Activity {

	private final static String TAG = "LoginActivity";

	private EditText m_edtUserAccount;
	private EditText m_edtUserPwd;
	private Button m_btnLogin;
	private Button m_btnActivate;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
	
		if(!MeetingApplication.isOAStarted()){
			MeetingApplication.startOA(this);
		}
		ensureUi();
		
	}

	private void ensureUi() {

		Button m_btnShelfOrBack = (Button) findViewById(R.id.common_titlebar_left);
		m_btnShelfOrBack.setVisibility(View.GONE);

		m_edtUserAccount = (EditText) findViewById(R.id.user_account);
		m_edtUserPwd = (EditText) findViewById(R.id.user_password);
		m_btnLogin = (Button) findViewById(R.id.user_login);
		m_btnActivate = (Button) findViewById(R.id.user_activate);
	

		// 登陆
		m_btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String userAccount = m_edtUserAccount.getText().toString().trim();
				String userPwd = m_edtUserPwd.getText().toString().trim();
				if (userAccount.length() > 0 && userPwd.length() > 0) {										
					OpenAPI.login(userAccount, userPwd, false, LoginActivity.this, new	LoginCallBack(){

						@Override
						public void onFailure(String arg0) {
							// TODO Auto-generated method stub
							super.onFailure(arg0);
							Toast.makeText(LoginActivity.this, "登陆失败，请重试",0).show();
						}

						@Override
						public void onHTTPException(String arg0) {
							// TODO Auto-generated method stub
							super.onHTTPException(arg0);
							Toast.makeText(LoginActivity.this, "网络异常",0).show();
						}

						@Override
						public void onSuccess(String arg0) {
						
							Toast.makeText(LoginActivity.this, "登陆成功",0).show();
							Intent intent = new Intent(LoginActivity.this,MeetingListAcitivity.class);
							startActivity(intent);
						}
						
					});
				} else {
					Toast.makeText(LoginActivity.this, "用户名密码不能为空",0).show();
				}
			}
		
		});

		
		// 注册
		m_btnActivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "登陆成功",0).show();
				Intent intent = new Intent(LoginActivity.this,MeetingListAcitivity.class);
				startActivity(intent);
			}
		});

		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
