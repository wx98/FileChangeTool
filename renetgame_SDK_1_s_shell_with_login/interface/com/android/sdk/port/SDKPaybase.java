package com.android.sdk.port;


import org.json.JSONException;
import org.json.JSONObject;

import com.android.sdk.shcore.LoginInfo;

import com.android.sdk.shcore.ShellH5Pay;

import com.android.sdk.util.MyUtil;
import com.android.sdk.util.PayVerify;
import com.android.sdk.util.Plugin;
import com.android.sdk.util.ReflectUtil;
import com.android.sdk.util.ResourcesUtil;
import com.android.sdk.util.SDKDebug;
import com.android.sdk.util.UnitUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class SDKPaybase {
	
	//请求服务器支付
	private static final int PAY_REQUEST_ORDER_SUCCESS = 1000;
	private static final int PAY_REQUEST_ORDER_FAILED = 1001;

	//请求服务器检查订单状态次数
	public PayInfo mPayInfo = null;
	public PayListener mPayListener;
	public LoginListener mLogoutListener;
	public RoleBean mRoleBean;
	
	private String userId;

	public RoleBean getmRoleBean() {
		return mRoleBean;
	}

	public void setmRoleBean(RoleBean mRoleBean) {
		this.mRoleBean = mRoleBean;
	}

	public String getUserId() {
		return userId;
	}

	public void requestInitialize(Activity mActivity, InitInfo appInfo, InitListener initListener) {
		initialize(mActivity, appInfo, initListener);
	}
	
	public void requestLogin(Activity activity, final LoginListener listener) {
		show(activity, listener);
	}

	/**
	 * 请求生成订单。
	 * @param mActivity
	 * @param payInfo
	 * @param listener
	 */
	public void requestPay(final Activity mActivity, final PayInfo payInfo, final PayListener listener) {
		mPayInfo = null;

		Plugin.getInstance(mActivity).requestServerOrder(mActivity, payInfo, mRoleBean, new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {

				if (msg.what == PAY_REQUEST_ORDER_SUCCESS) {
					String result = msg.getData().getString("result");
					SDKDebug.log("服务器返回结果==" + result);
					getPayMsg(payInfo, result, listener);
					
					if (SDKPay.getPayment() == SDKPay.PAYMENT_ORIGIN) {
						//渠道支付
						pay(mActivity, mPayInfo);
					} else if (SDKPay.getPayment() == SDKPay.PAYMENT_AB_H5) {
						//爱贝h5支付
						abH5Pay(mActivity, mPayInfo);
					}
					
				} else if (msg.what == PAY_REQUEST_ORDER_FAILED) {
					listener.onCompleted(PayStatusCode.ERROR_PAY_FAILED, payInfo);

				} else {
					listener.onCompleted(PayStatusCode.ERROR_PAY_FAILED, payInfo);
				}

			};
		});
	}
	
	private void abH5Pay(final Activity mActivity, final PayInfo payInfo) {
		ShellH5Pay h5PayWrapper = new ShellH5Pay(mActivity, userId, payInfo, mPayListener);
		h5PayWrapper.pay();
	}
	
	
	public void getPayMsg(PayInfo payInfo, String str, PayListener listener) {
		try {
			JSONObject result = new JSONObject(str);

			payInfo.setOrderid(result.optString("orderid"));
			payInfo.setPrivatekey(result.optString("privatekey"));
			payInfo.setH5privatekey(result.optString("attachedPrivatekey"));
			payInfo.setPublickey(result.optString("publickey"));
			payInfo.setAppid(result.optString("appkey"));
			payInfo.setHaveCheckInterface(result.optInt("haveCheckInterface"));
			payInfo.setNotifyUrl(result.optString("notifyUrl"));

			mPayInfo = payInfo;
			mPayListener = listener;

		} catch (Exception e) {
			e.printStackTrace();
			listener.onCompleted(PayStatusCode.ERROR_PAY_FAILED, payInfo);
			SDKDebug.log("服务器返回结果=11111=Exception==" + e.toString());
		}
	}
	
	/**
	 * 获取用户在鸿延平台上的id。
	 * @param activity
	 * @param userId3rd
	 * @param listener
	 */
	public void getHYUserId(final Activity activity, String userId3rd, final LoginListener listener) {
		InitInfo initInfo = SDKPay.getInitInfo();
		SDKDebug.rlog("SDKPaybase.getHYUserId(): appId = " + initInfo.getAppid());
		SDKDebug.rlog("SDKPaybase.getHYUserId(): userId3rd = " + userId3rd);
		
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setAppId(initInfo.getAppid());
		loginInfo.setUserId(userId3rd);
		Plugin.getInstance(activity).requestHYUserId(activity, loginInfo, new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				SDKDebug.rlog("确认登陆结果");
				try {
					JSONObject json = (JSONObject) msg.obj;
					String account = json.getString("account");
					userId = account;
					SDKDebug.rlog("返回给CP的userId = " + userId);
					listener.onLoginCompleted(PayStatusCode.LOGIN_SUCCESS, account, account);
				} catch (JSONException e) {
					SDKDebug.relog("SDKPaybase.getHYUserId(): json exception");
					listener.onLoginCompleted(PayStatusCode.LOGIN_FAILED, null, null);
				}
			}
		});
	}

	/**
	 * 确认订单状态。
	 * @param activity
	 */
	public void verifytPayResult(final Activity activity) {

		PayVerify payVerify = new PayVerify(mPayInfo, mPayListener);
		payVerify.verifytPayResult(activity);
	}
	
	protected final void exitGameImmly(Activity activity) {
		if (activity != null) activity.finish();
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void prepare(Activity activity) {
		SDKDebug.rlog("SDKPaybase.prepare()");
	}
	
	public void initialize(Activity activity, InitInfo appInfo, InitListener initListener) {
		SDKDebug.elog("SDKPaybase.initialize() hasn't been realized!");
	}
	
	public void show(Activity activity, final LoginListener listener) {
		SDKDebug.elog("SDKPaybase.show() hasn't been realized!");
	}
	
	public void enterGame(RoleBean bean) {

	}

	public void pay(final Activity activity, final PayInfo payInfo) {
		SDKDebug.elog("SDKPaybase.pay() hasn't been realized!");
	}

	public void logout(Activity activity, final LoginListener loginListener) {
		loginListener.onLogoutCompleted(PayStatusCode.LOGIN_SUCCESS, null, null);
	}
	
	public void exitGame(Activity activity, RoleBean roleBean, ExitGameListener exitGameListener) {
		SDKDebug.rlog("SDKPaybase.exitGame(): pending exit");
		showExitDialog(activity, exitGameListener);
	}
	
	public void onCreate(Activity activity){
		SDKDebug.rlog("SDKPaybase.onCreate()");
	}
	
	public void onStart(Activity activity){
		SDKDebug.rlog("SDKPaybase.onStart()");
	}
	
	public void onResume(Activity activity){
		SDKDebug.rlog("SDKPaybase.onResume()");
	}
	
	public void onRestart(Activity activity){
		SDKDebug.rlog("SDKPaybase.onRestart()");
	}
	
	public void onBackPressed(Activity activity){
		SDKDebug.rlog("SDKPaybase.onBackPressed()");
	}
	
	public void onNewIntent(Activity activity, Intent intent){
		SDKDebug.rlog("SDKPaybase.onNewIntent()");
	}
	
	public void onPause(Activity activity){
		SDKDebug.rlog("SDKPaybase.onPause()");
	}
	
	public void onStop(Activity activity){
		SDKDebug.rlog("SDKPaybase.onStop()");
	}
	
	public void onDestroy(Activity activity){
		SDKDebug.rlog("SDKPaybase.onDestroy()");
	}
	
	public void onActivityResult(Activity activity, int requestCode,int resultCode,Intent intent){
		SDKDebug.rlog("SDKPaybase.onActivityResult()");
	}
	
	private void showExitDialog(Activity activity, final ExitGameListener exitGameListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("是否确定退出");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if (exitGameListener != null) 
            		exitGameListener.onPendingExit(PayStatusCode.EXIT_GAME);
                System.exit(0);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if (exitGameListener != null) 
            		exitGameListener.onPendingExit(PayStatusCode.EXIT_CANCEL);
            }
        });
        
        builder.show();
	}
}
