package com.android.sdk.plugin.port;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.sdk.plugin.util.Constant;
import com.android.sdk.plugin.util.DeviceUtil;
import com.android.sdk.plugin.util.MyDebug;
import com.android.sdk.plugin.util.MyHttpListener;
import com.android.sdk.plugin.util.MyHttpPost;
import com.android.sdk.plugin.util.MyHttpProtocol;
import com.android.sdk.plugin.util.MyUncaughtExceptionHandler;
import com.android.sdk.plugin.util.UploadTag;

public class Upload {
	private static MyHttpPost httpPost = null;

	/**
	 * @Description: 初始化信息
	 * @param info
	 * @param mHandler
	 * @return void
	 */
	public static void netInitSdk(String info, final Handler mHandler) {
		try {
			JSONObject postValues = new JSONObject(info);
			MyDebug.log("ip:"+MyHttpProtocol.getHost()
					+ Constant.SRC_PAY_INIT);
			MyDebug.log("2:" + postValues);
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_PAY_INIT, postValues, new MyHttpListener() {

				@Override
				public void onCompleted(String recvStr, int eventCode) {
					MyDebug.log("eventCode:" + eventCode);
					MyDebug.log("recvStr:" + recvStr);

					if (eventCode == MyHttpListener.FINISH && recvStr != null) {
						try {
							JSONObject json = new JSONObject(recvStr);
							String contentStr = json.optString("content");
							String abAppId = json.optString("abAppId");
							String payment = json.optString("payment");
							int enablePltCoinPay = json.optInt("enablePltCoinPay", 1);
							if (json.optString("status").equals("3")) {
								Message message = Message.obtain();
								message.what = Integer.valueOf(UploadTag.APPID_UNLEGAL);
								message.arg1 = Integer.valueOf(UploadTag.ZY_DEFAULT);
								Bundle b = new Bundle();
								b.putString("contentStr", contentStr);
								b.putString("abAppId", abAppId);
								b.putString("payment", payment);
								b.putInt("enablePltCoinPay", enablePltCoinPay);
								message.obj = b;
								mHandler.sendMessage(message);
//								mHandler.sendEmptyMessage(3);// appid不存在
							} else {
								//正常
								Message message = Message.obtain();
								message.what = Integer.valueOf(UploadTag.APPID_NORMAL);
								String type = json.optString("type");
								message.arg1 = Integer.valueOf("".equals(type)?"0":type);//切换第三方支付
								Bundle b = new Bundle();
								b.putString("contentStr", contentStr);
								b.putString("abAppId", abAppId);
								b.putString("payment", payment);
								b.putInt("enablePltCoinPay", enablePltCoinPay);
								message.obj = b;
								mHandler.sendMessage(message);
//								mHandler.sendEmptyMessage(0);// appid正常
							}
						} catch (JSONException e) {
							e.printStackTrace();
							mHandler.sendEmptyMessage(Integer.valueOf(UploadTag.APPID_INIT_UNSUCCESS));
						}
					} else {
					}
					httpPost = null;
				}
			});
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "netInitSdk_upload_initialize_info");
			e.printStackTrace();
			mHandler.sendEmptyMessage(Integer.valueOf(UploadTag.APPID_INIT_UNSUCCESS));//初始化
		}
	}

	/**
	 * @Description: 创角统计
	 * @param info
	 * @return void
	 */
	public static void uploadIncrementInfoToServer(final String info) {
		try {
			JSONObject postValues = new JSONObject(info);
			MyDebug.log("4:" + postValues.toString());
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_PAY_UPLOAD_INCREMENT, postValues,
					new MyHttpListener() {

						@Override
						public void onCompleted(String recvStr, int eventCode) {
							MyDebug.log("eventCode:" + eventCode);
							MyDebug.log("recvStr:" + recvStr);
							if (eventCode == MyHttpListener.FINISH
									&& recvStr != null) {
							} else {

							}
							httpPost = null;
						}
					});
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadIncrementInfoToServer_upload");
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 根据渠道uid返回本平台uid。
	 * @param info
	 */
	public static void getHYUserId(final String info, final Handler mHandler) {
		try {
			JSONObject postValues = new JSONObject(info);
			MyDebug.log("14:" + postValues.toString());
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_GET_HY_USERID, postValues,
					new MyHttpListener() {

						@Override
						public void onCompleted(String recvStr, int eventCode) {
							MyDebug.log("eventCode:" + eventCode);
							MyDebug.log("recvStr:" + recvStr);
							if (eventCode == MyHttpListener.FINISH
									&& recvStr != null) {
								try {
									JSONObject json = new JSONObject(recvStr);
									String result = json.optString("result");
									if ("0".equals(result)) {								
										Message message = Message.obtain();
										message.obj = json;
										mHandler.sendMessage(message);
									} else {
										MyDebug.log("getHYUserId(): result = " + result);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							httpPost = null;
						}
					});
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadIncrementInfoToServer_upload");
			e.printStackTrace();
		}
	}
	
	/**
	 * 请求h5支付网关。
	 * @param info
	 * @param mHandler
	 */
	public static void requestH5Pay(final String info, final Handler mHandler) {
		try {
			JSONObject postValues = new JSONObject(info);
			MyDebug.log("15:" + postValues.toString());
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_REQUEST_H5_PAY, postValues,
					new MyHttpListener() {

						@Override
						public void onCompleted(String recvStr, int eventCode) {
							MyDebug.log("eventCode:" + eventCode);
							MyDebug.log("recvStr:" + recvStr);
							if (eventCode == MyHttpListener.FINISH
									&& recvStr != null) {
								try {
									JSONObject json = new JSONObject(recvStr);
									String result = json.optString("status");
									if ("0".equals(result)) {
										Bundle bundle = new Bundle();
										bundle.putString("url", json.getString("url"));
										Message message = Message.obtain();
										message.obj = bundle;
										mHandler.sendMessage(message);
									} else {
										MyDebug.log("requestH5Pay(): result = " + result);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							httpPost = null;
						}
					});
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadIncrementInfoToServer_upload");
			e.printStackTrace();
		}
	}
	
	public static void submitRoleInfo(String info, final Handler mHandler) {
		try {
			JSONObject postValues = new JSONObject(info);
			MyDebug.log("28:" + postValues.toString());
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_SUBMIT_ROLE_INFO, postValues,
					new MyHttpListener() {

						@Override
						public void onCompleted(String recvStr, int eventCode) {
							MyDebug.log("eventCode:" + eventCode);
							MyDebug.log("recvStr:" + recvStr);
							if (eventCode == MyHttpListener.FINISH
									&& recvStr != null) {
								try {
									JSONObject json = new JSONObject(recvStr);
									String result = json.optString("result");
									Message msg = Message.obtain();
									msg.what = 0;
									msg.obj = result;
									mHandler.sendMessage(msg);
								} catch (JSONException e) {
									mHandler.sendEmptyMessage(1);
									e.printStackTrace();
								}
							}
							httpPost = null;
						}
					});
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadIncrementInfoToServer_upload");
			e.printStackTrace();
		}
	}
	
	public static void platformCoinPay(String info, final Handler mHandler) {
		try {
			JSONObject postValues = new JSONObject(info);
			MyDebug.log("29:" + postValues.toString());
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_PLATFORM_COIN_PAY, postValues,
					new MyHttpListener() {

						@Override
						public void onCompleted(String recvStr, int eventCode) {
							MyDebug.log("eventCode:" + eventCode);
							MyDebug.log("recvStr:" + recvStr);
							if (eventCode == MyHttpListener.FINISH
									&& recvStr != null) {
								try {
									JSONObject json = new JSONObject(recvStr);
									int result = json.optInt("status", -1);
									Message msg = Message.obtain();
									msg.what = result;
									msg.obj = json;
									mHandler.sendMessage(msg);
								} catch (JSONException e) {
									mHandler.sendEmptyMessage(-1);
									e.printStackTrace();
								}
							}
							httpPost = null;
						}
					});
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadplatformCoinPay_upload");
			e.printStackTrace();
		}
	}
	
	public static void nativeVerifytPayResultUtil(Context mContext, String request, final Handler handler) {
		try {
			JSONObject postValues = new JSONObject(request);
			MyDebug.log("16:" + postValues.toString());
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_NATIVE_PAY_RESULT, postValues,
					new MyHttpListener() {

						@Override
						public void onCompleted(String recvStr, int eventCode) {
							MyDebug.log("eventCode:" + eventCode);
							MyDebug.log("recvStr:" + recvStr);
							if (eventCode == MyHttpListener.FINISH
									&& recvStr != null) {
								try {
									JSONObject json = new JSONObject(recvStr);
									Bundle bundle = new Bundle();
									bundle.putString("url", json.getString("url"));
									Message message = Message.obtain();
									message.obj = bundle;
									handler.sendMessage(message);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							httpPost = null;
						}
					});
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadIncrementInfoToServer_upload");
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 付费结果
	 * @param payInfo
	 * @return void
	 */
	public static void uploadPayResultToServer(String payInfo) {
		try {
			JSONObject postValues = new JSONObject(payInfo);
			MyDebug.log("3:" + postValues);
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_PAY_REPORT_RESULT, postValues,
					uploadPayResultHttpListener);
			httpPost.post();
		} catch (JSONException e) {
			MyUncaughtExceptionHandler.writeError(e, "uploadPayResultToServer_upload");
			e.printStackTrace();
		}
	}

	private static MyHttpListener uploadPayResultHttpListener = new MyHttpListener() {

		@Override
		public void onCompleted(String recvStr, int eventCode) {
			MyDebug.log("eventCode:" + eventCode);
			MyDebug.log("recvStr:" + recvStr);
			if (eventCode == MyHttpListener.FINISH && recvStr != null) {
			} else {
			}
			httpPost = null;
		}
	};

	public static void uploadException(String exception) {
		/*try {
			MyDebug.log("exception:" + exception);
			JSONObject postValues = new JSONObject();

			Context mContext = getContext();
			postValues.put(UploadTag.deviceid, DeviceUtil.getDeviceid(mContext));// 设备识别码
			postValues.put(UploadTag.imsi, DeviceUtil.getImsi(mContext));
			postValues.put(UploadTag.iccid, DeviceUtil.getIccid(mContext));
			postValues.put(UploadTag.coreversion, Constant.VERSION);// jar
			postValues.put(UploadTag.pkgname, mContext.getPackageName());
			
			postValues.put(UploadTag.exceptionTag, exception);
			
			// JSONObject postValues = new JSONObject(exception);
			MyDebug.log("5:" + postValues);
			httpPost = new MyHttpPost(MyHttpProtocol.getHost()
					+ Constant.SRC_PAY_FEEDBACK, postValues, dummyHttpListener);
			httpPost.post();
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
	}

	private static Context getContext() {
		Context a = null;
		try {
			a = (Context) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// if (null == a) {
		// a = mContext;
		// }
		return a;
	}

	private static MyHttpListener dummyHttpListener = new MyHttpListener() {

		@Override
		public void onCompleted(String recvStr, int eventCode) {
			// 不做处理
			MyDebug.log("eventCode:" + eventCode);
			MyDebug.log("recvStr:" + recvStr);
		}
	};

}
