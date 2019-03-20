package com.android.sdk.example;

import com.android.sdk.port.InitInfo;
import com.android.sdk.port.InitListener;
import com.android.sdk.port.LoginListener;
import com.android.sdk.port.PayInfo;
import com.android.sdk.port.PayStatusCode;
import com.android.sdk.port.PayListener;
import com.android.sdk.port.RoleBean;
import com.android.sdk.port.SDKPay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class sample extends Activity implements OnClickListener {
	//鸿延SDK参数
	//这里要替换成游戏的参数
	private static final String APPID = "666666";
	private static final String USERID = "abc01#test01";
	
	//CGameSDK参数
	//这里要替换成游戏的参数
	private static long CGAME_APP_ID = 10189;
	private static String CGAME_APP_KEY = "f670358cb814363ff58f5687b87b645f";
	
	//八门SDK参数
	//这里要替换成游戏的参数
	private static final int BM_APP_ID = 8;
	private static final String BM_APP_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN7WBTqiXDJA1pVoPCLi1FSiWDTq/v+GYnIGJaQNrJ+SmwJddMGojspiSbShzLZyT94gZ5JVfM3tvmq11mBLdPVWFd8KPvdl55QM2GumKQaE8QimuH6nTkdI0IW3Prc6U/C7gc+UKbNzOPXSY8EeSQXvFVIcaObeW7Pb3puGSEpnAgMBAAECgYEAlb/qH69Amhzsl9dGooMhAgdfk6Bg4UNkIRB0dz0hRtN1DC/V6NbnrqZCtfsN4YYMmk/YioScrJ8amge9G1VaMMuY+w7h5jHosDhJLf53IwDm9jqykN7aPfPfEreRteF2q1V+eyNN2qNSaq3T7F3ALDT0MHYsN1Ia8R46ZkAqsUECQQD+GZlmRudEqZVCbYlUPd6Ofwtr1TlZYwI5G6B6PadMD3bvdL1QJ8x2g7DsKnlXQYF/DAr7mLQPUT+1iOwlu24xAkEA4ICTTE8VX7wGJ8uO4zRgB/b5xIa8cYhJnigcLPJ5yJkyMjQPDu6sqAYEqxUf6pOIRHZiZiF8O2j4vaD0tPqkFwJAW49f3iHIbc5pkTElHezZSCFxPR7s9k+d2nQhBEs5AEhGRAydtsdQfpf/ZWn8pTtebSgqwPQKVcaiHUjERuhd0QJBAKBh2lkCik95QKB/YJXaHdyyyN5oLwmgho8xMme91djD8MNCw5s0US48FmuuTL3FJe1a8ZLqyxiaNlEqQodkI4MCQGv8ixDsXY5Yt7euTXz7H5XGU1PXNJ1XDpz+mgKAtjqSWMjBxYSmrpxu/9wSrZz4E4NB0//u22qY3XFHg7IU4NA=";
	
	//齐齐乐SDK参数
	//这里要替换成游戏的参数
	private static final String QQL_SID = "10162";

	//支付回调
	private PayListener payListener = new PayListener() {

		@Override
		public void onCompleted(int statusCode, PayInfo payInfo) {
			if (PayStatusCode.PAY_SUCCESS == statusCode) {
				Toast.makeText(getApplicationContext(), "支付成功！", 1 * 1000)
						.show();
			} else if (PayStatusCode.PAY_CANCEL == statusCode) {
				Toast.makeText(getApplicationContext(), "用户取消支付！", 1 * 1000)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "支付失败！", 1 * 1000)
						.show();
			}
		}
	};

	// 登录接口回调
	private LoginListener loginListener = new LoginListener() {

		@Override
		public void onLogoutCompleted(int statusCode, String account,
				String userid) {
			if (PayStatusCode.LOGOUT_SUCCESS == statusCode) {
				Toast.makeText(sample.this,
						"展示:登出成功,用户名:" + account + ", userid:" + userid,
						Toast.LENGTH_LONG).show();
			} else if (PayStatusCode.LOGOUT_FAILED == statusCode) {
				Toast.makeText(sample.this, "展示:登出失败", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onLoginCompleted(int statusCode, String account,
				String userid) {
			if (PayStatusCode.LOGIN_SUCCESS == statusCode) {
				Toast.makeText(sample.this,"!!!!!!!!!!!!!!cp展示:登录成功,用户名:" + account + ", userid:" + userid,
						Toast.LENGTH_LONG).show();
			} else if (PayStatusCode.LOGIN_FAILED == statusCode) {
				Toast.makeText(sample.this, "展示:登录失败", Toast.LENGTH_LONG)
						.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//主Activity.onCreate回调中调用
		SDKPay.getInstance(this).onCreate();
		
		//如果使用八门，需要在主Activity的setContentView之前调用
		//ResourceUtils.setPackageName("com.res.depend");
		
		initView();
	}
	
	private void initView() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		Button initButton = new Button(this);
		initButton.setText("init");
		initButton.setTag("init");
		initButton.setOnClickListener(this);
		
		Button loginButton = new Button(this);
		loginButton.setText("login");
		loginButton.setTag("login");
		loginButton.setOnClickListener(this);

		Button createRoleButton = new Button(this);
		createRoleButton.setText("createRole");
		createRoleButton.setTag("createRole");
		createRoleButton.setOnClickListener(this);

		Button enterGameButton = new Button(this);
		enterGameButton.setText("enterGame");
		enterGameButton.setTag("enterGame");
		enterGameButton.setOnClickListener(this);
		
		Button payButton = new Button(this);
		payButton.setText("pay");
		payButton.setTag("pay");
		payButton.setOnClickListener(this);
		
		Button logoutButton = new Button(this);
		logoutButton.setText("logout");
		logoutButton.setTag("logout");
		logoutButton.setOnClickListener(this);
		
		Button exitGameButton = new Button(this);
		exitGameButton.setText("exitGame");
		exitGameButton.setTag("exitGame");
		exitGameButton.setOnClickListener(this);

		layout.addView(initButton);
		layout.addView(loginButton);
		layout.addView(createRoleButton);
		layout.addView(enterGameButton);
		layout.addView(payButton);
		layout.addView(logoutButton);
		layout.addView(exitGameButton);
		setContentView(layout);

		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		String imsi = tm.getSubscriberId();

		if (!TextUtils.isEmpty(imsi)) {
			TextView tvImsi = new TextView(this);
			tvImsi.setText("\t\tIMSI:\t" + imsi);
			layout.addView(tvImsi);
		}
		if (!TextUtils.isEmpty(imei)) {
			TextView tvImei = new TextView(this);
			tvImei.setText("\t\tIMEI:\t" + imei);
			layout.addView(tvImei);
		}
		setContentView(layout);
	}

	@Override
	public void onClick(View v) {
		if (v.getTag().equals("init")) {
			//鸿延SDK初始化信息
			InitInfo initInfo = new InitInfo();
			initInfo.setAppid(APPID);								//必填 appid 请联系我们获取
			initInfo.setOrientation(SDKPay.PORTRAIT);				// 必填 LANDSCAPE,PORTRAIT
			
			//使用部分渠道，在SDKPay.init()前需要设置渠道信息
			//525、八门、齐齐乐、葫芦侠需要在代码中设置（如下所示）
			//CGame、虫虫需要在AndroidManifest.xml中设置
			SDKPay
				.getInstance(this)
//				.use525(
//					"74", 											//游戏id
//					"游戏名称", 										//游戏名称
//					"9B9E286F51D5E00CF", 							//appid
//					"CgsPEgsLEg8LCg==", 							//appkey
//					"http://a2.vlcms.com/sdk.php")					//联运SDK服务器地址
//				
//				.useBM(
//					8, 												//八门appId
//					BM_APP_KEY, 									//八门appKey
//					getPackageName())								//游戏包名
//				
//				.useQQL(QQL_SID)									//齐齐乐sid
//				
//				.useSix7(
//					8000, 											//葫芦侠appId						
//					"MainActivity", 								//主Activity名称
//					"http://xxxx.cn/server-demo/notify_url.jsp")	//该url由游戏方填写，用于支付，此处提供范例
				.init(initInfo, new InitListener() {

					@Override
					public void initCompleted(int statusCode, InitInfo initInfo) {
						if (PayStatusCode.INIT_SUCCESS == statusCode) {
							Toast.makeText(getApplicationContext(), "SDK初始化成功！",
									1 * 1000).show();// 这里可以不处理
						} else if (PayStatusCode.INIT_FAILED == statusCode) {
							Toast.makeText(getApplicationContext(),
									"SDK初始化失败,请检查sdk参数！", 1 * 1000).show();
						}
					}
				});
		} else if (v.getTag().equals("login")) {
			//登录
			SDKPay.getInstance(this).show(this, loginListener);
		} else if (v.getTag().equals("createRole")) {
			//创建角色
			SDKPay.getInstance(this).createRole(APPID, USERID);
		} else if(v.getTag().equals("enterGame")){
			//正式进入
			RoleBean roleBean = new RoleBean();
			roleBean.setRoleId("001");
			roleBean.setRoleName("player01");
			roleBean.setServerId("1");
			roleBean.setServerName("ceshifu");
			SDKPay.getInstance(this).enterGame(this,roleBean);
		} else if (v.getTag().equals("pay")) {
			//支付
			RoleBean roleBean = new RoleBean();
			roleBean.setRoleId("001");
			roleBean.setRoleName("player01");
			roleBean.setServerId("1");
			roleBean.setServerName("ceshifu");
			
			PayInfo payInfo = new PayInfo();
			payInfo.setOrderid("CP000000");// CP自定义订单号
			payInfo.setWaresname("测试");// 商品名称,需要加上以作标识
			//payInfo.setPrice("0.01");// 单位：元，微信支付时最低1元,支付宝最低2元,测试时请注意
			payInfo.setPrice("1.00");// 单位：元，微信支付时最低1元,支付宝最低2元,测试时请注意
			payInfo.setAppid(APPID);
			payInfo.setUserid(USERID);// userid,如果有区服可传 userid#区服编号
			payInfo.setExt("touchuan#123_ABC@透传");//透传参数(长度控制在64内),服务器回调会返回,如果没有请传""
			//调用支付
			SDKPay.getInstance(this).pay(payInfo, roleBean,payListener);
		} else if (v.getTag().equals("logout")) {
			//登出
			SDKPay.getInstance(this).logout(this, loginListener);
		} else if(v.getTag().equals("exitGame")){
			//退出游戏
			RoleBean roleBean = new RoleBean();
			roleBean.setRoleId("001");
			roleBean.setRoleName("player01");
			roleBean.setServerId("1");
			roleBean.setServerName("ceshifu");
			SDKPay.getInstance(this).exitGame(this, roleBean);
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	RoleBean roleBean = new RoleBean();
			roleBean.setRoleId("001");
			roleBean.setRoleName("player01");
			roleBean.setServerId("1");
			roleBean.setServerName("ceshifu");
        	SDKPay.getInstance(this).exitGame(this/*sample.this*/, roleBean);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		SDKPay.getInstance(this).onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SDKPay.getInstance(this).onResume();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		SDKPay.getInstance(this).onRestart();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		SDKPay.getInstance(this).onBackPressed();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SDKPay.getInstance(this).onNewIntent(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SDKPay.getInstance(this).onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		SDKPay.getInstance(this).onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SDKPay.getInstance(this).onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SDKPay.getInstance(this).onActivityResult(requestCode, resultCode, data);
	}
}