package com.android.sdk.port;

/**
 * 退出游戏监听。
 * @author Synaric
 *
 */
public interface ExitGameListener {

	/**
	 * 退出游戏回调。
	 * @param code 是否退出游戏
	 * @author Synaric
	 */
	public void onPendingExit(int code);
}
