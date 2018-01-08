package com.example.rh.artlive.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.application.DemoApplication;
import com.example.rh.artlive.db.DemoDBManager;
import com.example.rh.artlive.db.EaseUser;
import com.example.rh.artlive.util.EaseCommonUtils;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mob.tools.utils.UIHandler;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;

import static android.R.attr.action;
import static android.R.attr.breadCrumbShortTitle;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener,PlatformActionListener, Handler.Callback {
	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;

	private static final int MSG_ACTION_CCALLBACK = 0;


	//需要腾讯提供的一个Tencent类
	private Tencent mTencent;
	//还需要一个IUiListener 的实现类（LogInListener implements IUiListener）

	//用来判断当前是否已经授权登录，若为false，点击登录button时进入授权，否则注销
	private boolean isLogIned = false;


	private ImageView mQQView;
	private ImageView mWeiboView;
	private ImageView mWeChartView;
	private TextView mForget;
	private IWXAPI api;

	private EditText phone;
	private EditText passwd;

	private Button mLogin;

	private int currentapiVersion;

	private ProgressDialog progressDialog;
	private String mUid;
	private String mName;
	private String mIconurl;
	private TextView mRegisterView;
	private String mEmName="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_login);

		setupViews();
		setListener();

		if ("".equals(mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN))){

		}else{
			Intent intent=new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}

	}


	private void setupViews() {

		mQQView=(ImageView)findViewById(R.id.login_bottom_iv_qq);
		mWeiboView=(ImageView)findViewById(R.id.login_bottom_iv_weibo);
		mWeChartView=(ImageView)findViewById(R.id.login_bottom_iv_wechat);
		mForget=(TextView)findViewById(R.id.forget);
		mRegisterView=(TextView)findViewById(R.id.register);
		phone=(EditText)findViewById(R.id.login_phone);
		passwd=(EditText)findViewById(R.id.login_password);
		mLogin=(Button)findViewById(R.id.logins_btn);

	}
	private void setListener(){

		mQQView.setOnClickListener(this);
		mWeiboView.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mWeChartView.setOnClickListener(this);
		mForget.setOnClickListener(this);
		mRegisterView.setOnClickListener(this);
	}
	/**
	 * 账号登录
	 */
	private void setAdminLogin(){
		HashMap<String,String> map=new HashMap<>();
		map.put("tel",phone.getText()+"".toString().trim());
		map.put("password",passwd.getText()+"".toString().trim());
		HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.LOGIN, map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
			}
			@Override
			public void onResponse(String response) {
				Log.e("TAG","账号登录"+response);
				JSONObject obj;
				try {
					obj=new JSONObject(response);
					if("1".equals(obj.getString("state"))){
						JSONObject data=obj.getJSONObject("data");
						mSharePreferenceUtil.setString(SharedPerfenceConstant.TOKEN,data.getString("access_token"));
						mSharePreferenceUtil.setString(SharedPerfenceConstant.RONG_TOKEN,data.getString("rong_token"));
					Log.e("TAG","token"+data.getString("rong_token"));
						JSONObject info=data.getJSONObject("info");
						mSharePreferenceUtil.setString(SharedPerfenceConstant.USER_ID,info.getString("user_id"));
						mEmName=info.getString("hxpwd");
//						Intent intent = new Intent(LoginActivity.this,
//								MainActivity.class);
//						startActivity(intent);
//						finish();
						login();
					}else{
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "register", null);
	}
	private void setQQ(String mUid,String mName,String mIconurl){
		HashMap<String,String> map=new HashMap<>();
		map.put("uid",mUid);
		map.put("name",mName);
		map.put("iconurl",mIconurl);
		HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.QQ, map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
			}

			@Override
			public void onResponse(String response) {
				Log.e("TAG","QQ登录"+response);
				JSONObject obj;
				try {
					obj=new JSONObject(response);
					if("1".equals(obj.getString("state"))){
						JSONObject data=obj.getJSONObject("data");
						String Token=data.getString("access_token");
						// 进入主页面
						mSharePreferenceUtil.setString(SharedPerfenceConstant.TOKEN,data.getString("access_token"));
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						startActivity(intent);
						finish();
					}else{
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "register", null);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.forget:
				Intent intent2=new Intent(LoginActivity.this,ForgetActivity.class);
				startActivity(intent2);
				break;
			case R.id.register:
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.logins_btn:
//				login();
				setAdminLogin();
				break;
			case R.id.login_bottom_iv_qq: //qq
				Platform qq = ShareSDK.getPlatform(QQ.NAME);
				qq.setPlatformActionListener(this);
				qq.SSOSetting(false);
				authorize(qq, 2);
				break;
			case R.id.login_bottom_iv_weibo:  //微博
				Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
				sina.setPlatformActionListener(this);
				sina.SSOSetting(false);
				authorize(sina, 3);
				break;
			case R.id.login_bottom_iv_wechat://微信
				Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
				wechat.setPlatformActionListener(this);
				wechat.SSOSetting(false);
				authorize(wechat, 1);
				break;
				}
		}

	//授权
	private void authorize(Platform plat, int type) {
	       Log.e("TAG","微信");
		switch (type) {
			case 1:
				showProgressDialog(getString(R.string.opening_wechat));

				break;
			case 2:
				showProgressDialog(getString(R.string.opening_qq));
				break;
			case 3:
				showProgressDialog(getString(R.string.opening_blog));
				break;
		}
		if (plat.isAuthValid()) { //如果授权就删除授权资料
			plat.removeAccount(true);
		}
		plat.showUser(null);//授权并获取用户信息
	}

	//登陆授权成功的回调
	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
		Log.e("WeChat", " weixin: -->> onComplete: hashMap:" + platform.getDb().exportData());
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);   //发送消息
	}

	//登陆授权错误的回调
	@Override
	public void onError(Platform platform, int i, Throwable t) {
		Log.e("WeChat", " weixin: -->> onComplete: hashMap:" + platform.getDb().exportData());
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);
	}

	//登陆授权取消的回调
	@Override
	public void onCancel(Platform platform, int i) {
		Log.e("WeChat", " weixin: -->> onCancel");
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	//登陆发送的handle消息在这里处理
	@Override
	public boolean handleMessage(Message message) {
		hideProgressDialog();
		switch (message.arg1) {
			case 1: { // 成功
				Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();

				//获取用户资料
				Platform platform = (Platform) message.obj;
				mUid = platform.getDb().getUserId();//获取用户账号
				mName = platform.getDb().getUserName();//获取用户名字
				mIconurl = platform.getDb().getUserIcon();//获取用户头像
				String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
				Toast.makeText(LoginActivity.this, "用户信息为--用户名：" + mName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();
				setQQ(mUid,mName,mIconurl);

				//下面就可以利用获取的用户信息登录自己的服务器或者做自己想做的事啦!
				//。。。

			}
			break;
			case 2: { // 失败
				Toast.makeText(LoginActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
			}
			break;
			case 3: { // 取消
				Toast.makeText(LoginActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return false;
	}

	//显示dialog
	public void showProgressDialog(String message) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	//隐藏dialog
	public void hideProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}


	/**
	 * 登录
	 */
	public void login() {
		if (!EaseCommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = phone.getText()+"".toString().trim();
		currentPassword =mEmName;
	Log.e("TAG","用户名"+phone.getText()+"".toString().trim()+"环信密码"+mEmName);

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		progressShow = true;
		final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Log.e("TAG","登录"+"失败");
				progressShow = false;
			}
		});
		pd.setMessage(getString(R.string.Is_landing));
		pd.show();
 		// close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();
        // reset current user name before login
        DemoApplication.getInstance().setCurrentUserName(currentUsername);
		// 调用sdk登陆方法登陆聊天服务器
		Log.e("TAG", "EMClient.getInstance().login");
		EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.e(TAG, "login: onSuccess");
				Log.e("TAG","登录"+"成功");

				if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
					pd.dismiss();
				}

				// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
				// ** manually load all local groups and
			    EMClient.getInstance().groupManager().loadAllGroups();
			    EMClient.getInstance().chatManager().loadAllConversations();
				getFriends();

				// 进入主页面
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onProgress(int progress, String status) {
				Log.e("TAG","登录"+"失败");
			}

			@Override
			public void onError(final int code, final String message) {
			Log.e("TAG","登录"+"失败");
			Log.e("TAG","登录"+message);
				if (!progressShow) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
    private  void  getFriends(){
    	try {
			List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
		    Map<String ,EaseUser> users=new HashMap<String ,EaseUser>();
			for(String username:usernames){
				EaseUser user=new EaseUser(username);
				users.put(username, user);

			}

			DemoApplication.getInstance().setContactList(users);

		} catch (HyphenateException e) {
			e.printStackTrace();
		}
    }
	
	
//	/**
//	 * 注册
//	 *
//	 * @param view
//	 */
//	public void register(View view) {
//		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (autoLogin) {
//			return;
//		}
//	}
}
