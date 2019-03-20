package com.android.sdk.port;

public interface LoginListener {
	public void onLoginCompleted(int statusCode, String account, String userid);
	public void onLogoutCompleted(int statusCode, String account, String userid);
}
