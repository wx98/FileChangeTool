package com.android.sdk.util;

import java.util.Timer;
import java.util.TimerTask;

import com.android.sdk.port.LoginListener;
import com.android.sdk.port.PayInfo;
import com.android.sdk.port.PayListener;
import com.android.sdk.port.PayStatusCode;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 验证支付结果。
 * @author Synaric
 *
 */
public class PayVerify {

	private static final int PAY_CONFIRM_SUCCESS = 2000;
	private static final int PAY_CONFIRM_FAILED = 2001;
	private static final int PAY_CONFIRM_REPEAT = 2002; 
	
	public int checkTimes = 1;
	private PayInfo payInfo;
	private PayListener payListener;
	public Timer timer;
	public TimerTask timerTask;
	
	public PayVerify(PayInfo payInfo, PayListener payListener) {
		this.payInfo = payInfo;
		this.payListener = payListener;
	}
	
	/**
	 * 主动查询支付结果。
	 * @param activity
	 */
	public void verifytPayResult(final Activity activity) {
		if (payInfo.getHaveCheckInterface() != 1) {
			payListener.onCompleted(PayStatusCode.PAY_SUCCESS, payInfo);
			SDKDebug.log("不需要确认订单状态 getNotifyUrl = " + payInfo.getNotifyUrl());
			return;
		}
		
		SDKDebug.log("开始请求verifytPay");

		Plugin.getInstance(activity).verifytPayResult(activity, payInfo, new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				if (timerTask != null) {
					timerTask.cancel();
				}
				SDKDebug.log("确认订单状态ssss" + msg.what);
				if (msg.what == PAY_CONFIRM_SUCCESS) {
					payListener.onCompleted(PayStatusCode.PAY_SUCCESS, payInfo);
					SDKDebug.log("确认订单支付_成功");
				} else if (msg.what == PAY_CONFIRM_REPEAT) {
					// 多次通知
					repeatVerifytPayResult(activity, payInfo);
					SDKDebug.log("确认订单支付，再次请求");
				} else if (msg.what == PAY_CONFIRM_FAILED) {
					payListener.onCompleted(PayStatusCode.ERROR_PAY_FAILED, payInfo);
					SDKDebug.log("确认订单支付，失败");
				} else {
					payListener.onCompleted(PayStatusCode.PAY_SUCCESS, payInfo);
					SDKDebug.log("不需要确认订单状态");
				}
			};
		});
	}
	
	private void creatTimerTask(final Activity activity, int time) {
		SDKDebug.log("creatTimerTask:times====" + time);
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				// 再次去请求
				verifytPayResult(activity);
			}
		};

		timer.schedule(timerTask, time * 10 * 1000);
	}

	// 再次确认订单信息
	private void repeatVerifytPayResult(Activity activity, PayInfo payInfo) {
		switch (checkTimes) {
		case 1:
		case 2:
		case 3:
			creatTimerTask(activity, checkTimes++);
			break;

		default:
			if (timerTask != null) {
				timerTask.cancel(); 
			}
			break;
		}

	}

	public void setCheckTimes(int checkTimes) {
		this.checkTimes = checkTimes;
	}

	public void setPayInfo(PayInfo payInfo) {
		this.payInfo = payInfo;
	}

	public void setPayListener(PayListener payListener) {
		this.payListener = payListener;
	}
}
