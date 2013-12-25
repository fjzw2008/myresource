package com.snda.zhanwei014168.sina;

import com.snda.zhanwei014168.MeetingDetailActivity;
import com.snda.zhanwei014168.R;
import com.snda.zhanwei014168.log.Logger;

import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*****************************************
 * 新浪微博授权界面
 * 
 * @author 姓名：zhanwei 工号：014168 公司：盛大文学云中书城 部门：技术开发部
 * @since 2011.9.24
 * @sina sdk
 * @version 1.0 ©2011,盛大网络
 *****************************************/
public class OAuthActivity extends Activity {

	private Button btn_send;
	private Button btn_back;
	private EditText txt_content;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sinalayout);
		btn_back = (Button) findViewById(R.id.btn_sinasback);
		btn_send = (Button) findViewById(R.id.btn_sinasend);
		txt_content = (EditText) findViewById(R.id.txt_siancontent);
		RegistEventListen();

		if (getSinaToken(OAuthActivity.this).length() <= 0
				&& getSinaTokenSecret(OAuthActivity.this)
						.length() <= 0) {

			// need to get token
			Uri uri = this.getIntent().getData();
			try {
				RequestToken requestToken = OAuthConstant.getInstance()
						.getRequestToken();
				AccessToken accessToken = requestToken.getAccessToken(uri
						.getQueryParameter("oauth_verifier"));
				OAuthConstant.getInstance().setAccessToken(accessToken);

				if (accessToken.getToken().length() > 0
						&& accessToken.getTokenSecret().length() > 0) {
					setSinaToken(OAuthActivity.this, accessToken
							.getToken());
					setSinaTokenSecret(OAuthActivity.this,
							accessToken.getTokenSecret());
				}

			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}
	}

	void RegistEventListen() {
		btn_send.setOnClickListener(SendMessageToSina_OnClick);
		btn_back.setOnClickListener(back_OnClick);
	}

	private OnClickListener back_OnClick = new OnClickListener() {

		public void onClick(View v) {
			startActivity(new Intent(OAuthActivity.this, MeetingDetailActivity.class));			

		}

	};
	private OnClickListener SendMessageToSina_OnClick = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.setProperty("weibo4j.oauth.consumerKey",
					WeiBoConstants.SINACUSTOMERID);
			System.setProperty("weibo4j.oauth.consumerSecret",
					WeiBoConstants.SINACUSTOMERSECRET);

			Weibo weibo = OAuthConstant.getInstance().getWeibo();
			Logger.getInstance().d("getSinaToken=======>", getSinaToken(OAuthActivity.this));
			Logger.getInstance().d("getSinaTokenSecret=======>", getSinaTokenSecret(OAuthActivity.this));
			weibo.setToken(getSinaToken(OAuthActivity.this),
					getSinaTokenSecret(OAuthActivity.this));
			try {
				String content = txt_content.getText().toString();
				weibo.updateStatus(content.length() > 100 ? content.substring(
						0, 80) : content);
				Toast.makeText(OAuthActivity.this, "发送成功", Toast.LENGTH_SHORT)
						.show();
			} catch (WeiboException e) {				
				Logger.getInstance().e("sina pusn====>", e.getMessage());
				e.printStackTrace();
			}
		}
	};

	public static String getSinaToken(Context context) {
		return context.getSharedPreferences("sina_token",
				context.MODE_WORLD_READABLE).getString("sina_token", "");
	}

	public static String getSinaTokenSecret(Context context) {
		return context.getSharedPreferences("sina_secret",
				context.MODE_WORLD_READABLE).getString("sina_secret", "");
	}
	
	//保存sina 授权TOKEN
	public static void  setSinaToken(Context context ,String token) {
		SharedPreferences.Editor shardSina = context.getSharedPreferences("sina_token",context.MODE_WORLD_WRITEABLE).edit();
		shardSina.putString("sina_token", token).commit();
	}
  
	//保存sina 授权密钥
	public static void  setSinaTokenSecret(Context context ,String tokenSecret) {
	  SharedPreferences.Editor shardSina = context.getSharedPreferences("sina_secret",context.MODE_WORLD_WRITEABLE).edit();
	  shardSina.putString("sina_secret", tokenSecret).commit();
	}

}
