package com.android.sdk.shcore;

import java.lang.ref.WeakReference;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.sdk.port.PayInfo;
import com.android.sdk.port.PayListener;
import com.android.sdk.port.PayStatusCode;
import com.android.sdk.util.Plugin;
import com.android.sdk.util.SDKDebug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

/**
 * 封装爱贝H5支付。
 * @author Synaric
 *
 */
public class ShellH5Pay {
	
	private Activity activity;
	private String userId;
	private PayInfo payInfo;
	private PayListener payListener;
	
	public ShellH5Pay(Activity activity, String userId, PayInfo payInfo, PayListener payListener) {
		this.activity = activity;
		this.userId = userId;
		this.payInfo = payInfo;
		this.payListener = payListener;
	}
	
	/**
	 * 发起支付。
	 */
	public void pay() {
		Plugin.getInstance(activity).requestH5Pay(
			activity, 
			userId, 
			payInfo, 
			new GetH5PayAddressCallback(this)
		);
	}
	
	public Activity getActivity() {
		return activity;
	}

	public String getUserId() {
		return userId;
	}

	public PayInfo getPayInfo() {
		return payInfo;
	}

	public PayListener getPayListener() {
		return payListener;
	}

	/**
	 * 获取h5支付网关。
	 * @author Synaric
	 *
	 */
	static class GetH5PayAddressCallback extends Handler {
		
		private ShellH5Pay shell;
		
		public GetH5PayAddressCallback(ShellH5Pay shell) {
			super(Looper.getMainLooper());
			this.shell = shell;
		}
		
		@Override
		public void handleMessage(Message msg) {
			SDKDebug.rlog("GetH5PayAddressCallback.handleMessage()");
			if (shell == null) {
				SDKDebug.rlog("GetH5PayAddressCallback.handleMessage() shell == null");
				return;
			}
			PayListener payListener = shell.getPayListener();
			PayInfo payInfo = shell.getPayInfo();
			Activity activity = shell.getActivity();
			
			if (msg.obj == null) {
				payListener.onCompleted(PayStatusCode.ERROR_PAY_FAILED, payInfo);
				return;
			}
			Bundle result = (Bundle) msg.obj;
			SDKDebug.rlog("ShellH5Pay.pay(): result = " + result);
			result.putSerializable(PayActivity.EXTRA_PAY_INFO, payInfo);
			result.putParcelable(PayActivity.EXTRA_MESSENGER, new Messenger(new PayCallback(shell)));
			
			Intent intent = new Intent(activity, PayActivity.class);
			intent.putExtras(result);
			activity.startActivity(intent);
		}
	}

	/**
	 * 处理支付结果。
	 * @author Synaric
	 *
	 */
	static class PayCallback extends Handler {
		
		private ShellH5Pay shell;
		
		public PayCallback(ShellH5Pay shell) {
			super(Looper.getMainLooper());
			this.shell = shell;
		}
		
		@Override
		public void handleMessage(Message msg) {
			SDKDebug.rlog("PayCallback.handleMessage(): callback");
			if (shell == null) {
				SDKDebug.rlog("PayCallback.handleMessage() shell == null");
				return;
			}
			
			PayListener payListener = shell.getPayListener();
			PayInfo payInfo = shell.getPayInfo();
			int result = msg.what;
			SDKDebug.rlog("PayCallback.handleMessage(): result = " + result);
			payListener.onCompleted(result, payInfo);
		}
	}
}
