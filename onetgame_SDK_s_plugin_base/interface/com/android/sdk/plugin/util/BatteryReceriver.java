package com.android.sdk.plugin.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReceriver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
			int status = intent.getIntExtra("status", 0);
			int level = intent.getIntExtra("level", 0);
			int scale = intent.getIntExtra("scale", 0);
			
			PreferenceUtil.saveBatteryData(context, status, level, scale);
			context.unregisterReceiver(this);
		}
	}

}
