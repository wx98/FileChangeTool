package com.android.sdk.plugin.util;

import java.util.Hashtable;

import org.json.JSONObject;

import com.android.sdk.plugin.net.DownLoadListener;
import com.android.sdk.plugin.net.HttpEngine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MyHttpPost {
	private static final String CHARSET = "UTF-8";
	private static final int RETRY_TIMES = 3;
	private HttpEngine mHttpEngine = null;
	private MyHttpListener mListener = null;
	private String mTaskName;
	private StringBuffer mRspBody;
	private int mRetryTimes;
	private String mUrl;
	private JSONObject mPostValues;

	private DownLoadListener mHttpListener = new DownLoadListener() {

		@Override
		public void HttpTransfersEvent(String taskname, int eventCode,
				String eventDescription) {
			if (mTaskName.equals(taskname) == false) {
				return;
			}

			switch (eventCode) {
			case DownLoadListener.CANCEL:
				mHandler.sendEmptyMessage(MyHttpListener.ERROR);
				mHttpEngine.removeCache(mTaskName);
				break;
			case DownLoadListener.ERROR:
				if (mRetryTimes < RETRY_TIMES) {
					mRetryTimes += 1;
					mHttpEngine.removeCache(mTaskName);
					// retry
					post();
				} else {
					mHandler.sendEmptyMessage(MyHttpListener.ERROR);
					mHttpEngine.removeCache(mTaskName);
				}
				break;
			case DownLoadListener.FINISH:
				if (mRspBody != null && mRspBody.length() != 0) {
					mHandler.sendEmptyMessage(MyHttpListener.FINISH);
				} else {
					mHandler.sendEmptyMessage(MyHttpListener.ERROR);
				}
				mHttpEngine.removeCache(mTaskName);
				break;
			}
		}

		@Override
		public void HttpTransfersBodyReceived(String taskname,
				String receivedUrl, byte[] bodyData, int size) {
			if (mTaskName.equals(taskname) == false) {
				return;
			}

			if (size != -1) {
				if (mRspBody == null) {
					mRspBody = new StringBuffer();
				}
				String s = new String(bodyData, 0, size);
				mRspBody.append(s);
			}
		}
	};

	// 在主线程上回调结果
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MyHttpListener.ERROR:
				if (mListener != null) {
					mListener.onCompleted(null, MyHttpListener.ERROR);
				}
				break;
			case MyHttpListener.FINISH:
				int status = MyHttpListener.FINISH;
				String recvStr = MyHttpProtocol.deCodeRspContent(
						mRspBody.toString(), "utf-8");
				if (mListener != null) {
					mListener.onCompleted(recvStr, status);
				}
				break;
			default:
				break;
			}
		}
	};

	public MyHttpPost(String url, JSONObject postValues, MyHttpListener listener) {
		this.mUrl = url;
		this.mPostValues = postValues;
		this.mListener = listener;
		this.mRetryTimes = 0;

		this.mHttpEngine = new HttpEngine(0);
		this.mHttpEngine.setDownLoadListener(mHttpListener);
	}

	public void post() {
		try {
			int random = (int) (Math.random() * 100);
			mTaskName = "SysPayHttpPost-" + hashCode() + random;

			Hashtable<String, String> header = new Hashtable<String, String>();
			header.put("Content-Type", "text/plain;charset=" + CHARSET);
			header.put("Accept-Encoding", "gzip");
			header.put("Content-Encoding", "gzip");

			mHttpEngine.issueHttpPost(mTaskName, mUrl, header, getPostString()
					.getBytes(), 1 * 60 * 1000);
		} catch (Exception e) {
			MyUncaughtExceptionHandler.writeError(e, "post_method");
			mHandler.sendEmptyMessage(MyHttpListener.ERROR);
		}
	}

	private String getPostString() {
		try {
			String str = mPostValues.toString();
			return MyHttpProtocol.enCodeReqContent(str);
		} catch (Exception e) {
			e.printStackTrace();
			MyUncaughtExceptionHandler.writeError(e, "getPostString");
		}
		return "getPostString decryptDDESString exception";
	}
}
