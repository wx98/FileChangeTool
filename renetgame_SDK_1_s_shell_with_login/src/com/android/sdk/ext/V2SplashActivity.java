package com.android.sdk.ext;

import java.util.Timer;
import java.util.TimerTask;

import com.android.sdk.util.MyFileUtil;
import com.android.sdk.util.ResourcesUtil;
import com.android.sdk.util.SDKDebug;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

/**
 * v2版闪屏。
 * @author Synaric
 *
 */
public class V2SplashActivity extends Activity {
	
	public static String MAIN_ACTIVITY_PKG = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new LinearLayout(this));
		
		try {
			String info = MyFileUtil.readInitInfoFromAssets(this, "hyinfo.store");
			JsonParser parser = new JsonParser();
			JsonObject initData = (JsonObject) parser.parse(info);
			MAIN_ACTIVITY_PKG = initData.get("launcherActivity").getAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SDKDebug.rlog("SplashActivity: start splash");
		
		String resId = null;
		int orientation = 0;
		long time = 2000;
		
		try {
			ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
			orientation = activityInfo.screenOrientation;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		
		resId = orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? "hy_splash_land" : "hy_splash_port";
		time = 2000;
		
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
				SDKDebug.rlog("V2SplashActivity: back to activity " + MAIN_ACTIVITY_PKG);
				
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					
					@Override
					public void run() {
						V2SplashActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								toMain();
							}
						});
					}
				};
				timer.schedule(task, time);
			} else {
				SDKDebug.relog("SplashActivity: drawableId = " + drawableId);
				toMain();
			}
		} else {
			SDKDebug.relog("SplashActivity: resId = " + resId);
			toMain();
		}

		layout.addView(iv);
		setContentView(layout);
	}
	
	private void toMain() {
		Intent intent = new Intent();
		try {
			intent.setClass(this.getApplicationContext(), Class.forName(MAIN_ACTIVITY_PKG));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
	}
}
