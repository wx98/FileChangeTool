package com.android.sdk.ext;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.android.sdk.port.SDKPay;
import com.android.sdk.util.ResourcesUtil;
import com.android.sdk.util.SDKDebug;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {

	public static final String EXTRA_RES_ID = "resId";
	public static final String EXTRA_ORIENTATION = "orientation";
	public static final String EXTRA_TIME = "time";
	public static final String EXTRA_MESSENGER = "messenger";
	
	private Messenger messenger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SDKDebug.rlog("SplashActivity: start splash");
		
		String resId = null;
		int orientation = 0;
		long time = 2000;
		
		Intent intent = getIntent();
		orientation = intent.getIntExtra(EXTRA_ORIENTATION, SDKPay.LANDSCAPE);
		resId = orientation == SDKPay.LANDSCAPE ? "hy_splash_land" : "hy_splash_port";
		time = intent.getLongExtra(EXTRA_TIME, 2000);
		messenger = intent.getParcelableExtra(EXTRA_MESSENGER);
		
		setRequestedOrientation(orientation == SDKPay.LANDSCAPE ? 
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : 
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
		);
		
		SDKDebug.rlog("SplashActivity: resId = " + resId);
		SDKDebug.rlog("SplashActivity: orientation = " + orientation);
		SDKDebug.rlog("SplashActivity: time = " + time);
		
		ViewGroup.LayoutParams lpll = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.MATCH_PARENT
		);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(lpll);
		
		ImageView iv = new ImageView(this);
		iv.setScaleType(ScaleType.CENTER_CROP);
		LinearLayout.LayoutParams lpiv = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, 
			ViewGroup.LayoutParams.MATCH_PARENT
		);
		iv.setLayoutParams(lpiv);
		if (!TextUtils.isEmpty(resId)) {
			int drawableId = ResourcesUtil.getDrawableId(this, resId);
			
			if (drawableId > 0) {
				iv.setBackgroundResource(drawableId);
			} else {
				SDKDebug.relog("SplashActivity: drawableId = " + drawableId);
			}
		} else {
			SDKDebug.relog("SplashActivity: resId = " + resId);
		}

		layout.addView(iv);
		setContentView(layout);
		
		//延迟消失
		new DeleyFinishHandler(new WeakReference<Activity>(this), new WeakReference<Messenger>(messenger))
			.sendEmptyMessageDelayed(0, time);
	}
	
	static class DeleyFinishHandler extends Handler {
		
		private WeakReference<Activity> wActivity;
		private WeakReference<Messenger> wMessenger;
		
		private static boolean isPendingSend;
		
		DeleyFinishHandler (WeakReference<Activity> ac, WeakReference<Messenger> m) {
			wActivity = ac;
			wMessenger = m;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what != 0) {
				return;
			}
			Activity ac = wActivity.get();
			final Messenger messenger = wMessenger.get();
			SDKDebug.rlog("isPendingSend = " + isPendingSend);
			if (messenger != null && !isPendingSend) {
				isPendingSend = true;
				new Thread(){
					
					@Override
					public void run() {
						SDKDebug.rlog("SplashActivity: messenger.send() wait");
						try {
							Thread.sleep(500);
							SDKDebug.rlog("SplashActivity: messenger.send() notify");
							messenger.send(new Message());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
			if (ac != null) {
				SDKDebug.rlog("SplashActivity: end splash");
				ac.finish();
			}
			
		}
	}
}
