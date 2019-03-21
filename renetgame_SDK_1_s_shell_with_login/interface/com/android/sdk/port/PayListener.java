package com.android.sdk.port;

public interface PayListener {//ZhPayListener
	public void onCompleted(int statusCode, PayInfo payInfo);
}
