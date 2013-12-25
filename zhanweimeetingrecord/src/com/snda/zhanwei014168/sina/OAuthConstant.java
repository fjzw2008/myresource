package com.snda.zhanwei014168.sina;


/*****************************************
 * 新浪微博调用
 * @author 姓名：zhanwei 工号：014168  公司：盛大文学云中书城      部门：技术开发部 
 * @since 2011.9.24 
 * @sina sdk 
 * @version 1.0  ©2011,盛大网络 
 *****************************************/
import weibo4android.Weibo;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;

public class OAuthConstant {
	private static Weibo weibo=null;
	private static OAuthConstant instance=null;
	private RequestToken requestToken;
	private AccessToken accessToken;
	private String token;
	private String tokenSecret;
	private OAuthConstant(){};
	public static synchronized OAuthConstant getInstance(){
		if(instance==null)
			instance= new OAuthConstant();
		return instance;
	}
	public Weibo getWeibo(){
		if(weibo==null)
			weibo= new Weibo();
		return weibo;
	}
	
	public AccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
		this.token=accessToken.getToken();
		this.tokenSecret=accessToken.getTokenSecret();
	}
	public RequestToken getRequestToken() {
		return requestToken;
	}
	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	
}
