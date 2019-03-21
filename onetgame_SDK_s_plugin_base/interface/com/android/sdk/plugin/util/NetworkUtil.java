package com.android.sdk.plugin.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class NetworkUtil {
	
	private static final int PAY_请求生成订单成功 = 1000;
	private static final int PAY_请求生成订单失败 = 1001;
	private static final int PAY_确认订单支付_成功 = 2000;
	private static final int PAY_确认订单支付_失败 = 2001;
	private static final int PAY_确认订单支付_再次请求 = 2002;
	
	
	/**
	 * Returns whether the network is wifi connection.
	 */
	public static boolean isWifiNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}

		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}

		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}

		return false;
	}
	
	// 获取内网ip地址
	public static String getIPAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return StringUtil.EMPTY_STRING;
	}

	// ssid bssid
	public static String[] getWifiInfo(Context mContext) {
		String result[] = new String[2];
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String temp = wifiInfo.getSSID();
		if (temp.startsWith("\"")) {
			temp = temp.replaceFirst("\"", "");
		}
		if (temp.endsWith("\"")) {
			temp = temp.substring(0, temp.length() - 1);
		}
		result[0] = temp;
		// result[0] = wifiInfo.getSSID();
		result[1] = wifiInfo.getBSSID();
		return result;
	}

	public static String getNetInfo(Context var0) {
		NetworkInfo var1 = ((ConnectivityManager) var0
				.getSystemService("connectivity")).getActiveNetworkInfo();
		String netType;
		if (var1 == null) {
			netType = StringUtil.EMPTY_STRING;
		} else {
			netType = var1.getTypeName();
			if (netType.compareTo("MOBILE") == 0
					|| netType.compareTo("mobile") == 0) {
				return "GPRS";
			}
		}
		return netType;
	}
	
	public static void getPosition(final Context mContext) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// String serverURL =
					// "http://ip.chinaz.com/getip.aspx?qq-pf-to=pcqq.c2c";
					// HttpGet httpRequest = new HttpGet(serverURL);//
					// HttpResponse httpResponse = new DefaultHttpClient()
					// .execute(httpRequest);// 发出http请求
					// String uriAPI = "http://127.0.0.1/xxx/xx.jsp"; //声明网址字符串
					HttpPost httpRequest = new HttpPost(
							"http://ip.chinaz.com/getip.aspx?qq-pf-to=pcqq.c2c"); //
					httpRequest.setEntity(new UrlEncodedFormEntity(
							new ArrayList<NameValuePair>(), HTTP.UTF_8)); // 发出http请求
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest); // 取得http响应
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						JSONObject json = new JSONObject(EntityUtils
								.toString(httpResponse.getEntity()));
						PreferenceUtil.saveLocateData(mContext, json.getString("ip"), json.getString("address"));
					}
				} catch (Exception e) {
					//调用第三方api接口
					MyUncaughtExceptionHandler.writeError(e, "third_api");
				}
			}
		}).start();
	}
	
	public static void get525Account(final String url,final String userid,final String token,final Handler requestHandler){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpRequest = new HttpPost(url); //
					// 绑定到请求 Entry  
					JSONObject param = new JSONObject();  
					param.put("user_id", userid);  
					param.put("token", token);  
					StringEntity stringEntity = new StringEntity(param.toString());   
					httpRequest.setEntity(stringEntity);  
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest); // 取得http响应
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						JSONObject json = new JSONObject(EntityUtils
								.toString(httpResponse.getEntity()));
						int result = json.getInt("status");
						if(1 == result){
							String account = json.getString("user_account");
							Bundle bundle = new Bundle();
							bundle.putString("account", account);
							Message message = Message.obtain();
							message.what = 0;
							message.setData(bundle);
							requestHandler.sendMessage(message);
						}else{
							requestHandler.sendEmptyMessage(-1);
						}
						
					}
				} catch (Exception e) {
					//调用第三方api接口
					MyUncaughtExceptionHandler.writeError(e, "get_525_account");
					requestHandler.sendEmptyMessage(-2);
				}
			}
		}).start();
	}
	
	
	//请求服务器生成订单
	public static void requestOrderUtil(final String url, final String token, final Handler requestHandler){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpRequest = new HttpPost(url); //
					// 绑定到请求 Entry  
					//JSONObject param = new JSONObject();  
					
					//param.put("token", MyHttpProtocol.enCodeReqContent(token)); 
					
					StringEntity stringEntity = new StringEntity(MyHttpProtocol.enCodeReqContent(token));   
					httpRequest.setEntity(stringEntity);  
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest); // 取得http响应

					MyDebug.log("请求服务器生成订单结果code"+httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {

					
						
						JSONObject json = new JSONObject(MyHttpProtocol.deCodeRspContent(EntityUtils
								.toString(httpResponse.getEntity()), "utf-8"));

					
						MyDebug.log("请求服务器生成订单结果:result==="+json.toString());
						
						int result = json.getInt("status");
						if(0 == result){
							Bundle bundle = new Bundle();
							bundle.putString("result", json.getJSONObject("order").toString());
							Message message = Message.obtain();
							message.what = PAY_请求生成订单成功;
							message.setData(bundle);
							requestHandler.sendMessage(message);
						}else{
							requestHandler.sendEmptyMessage(PAY_请求生成订单失败);
						}
						
					} else {
						requestHandler.sendEmptyMessage(PAY_请求生成订单失败);
					}
				} catch (Exception e) {
					MyDebug.log("请求服务器生成订单结果Exception"+e.toString());
					//调用第三方api接口
					MyUncaughtExceptionHandler.writeError(e, "get_525_account");
					requestHandler.sendEmptyMessage(PAY_请求生成订单失败);
				}
			}
		}).start();
	}
	
	//确认订单信息
	public static void verifytPayResultUtil(final String url, final String token, final Handler requestHandler){
		MyDebug.log("确认订单信息结果url"+url);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpRequest = new HttpPost(url); //
					StringEntity stringEntity = new StringEntity("content=transdata="+ URLEncoder.encode(token, "utf-8"));   
					httpRequest.setEntity(stringEntity);  
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest); // 取得http响应

					MyDebug.log("确认订单信息结果code"+httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {

						/*MyDebug.log("确认订单信息结果1111111"+MyHttpProtocol.deCodeRspContent(EntityUtils
								.toString(httpResponse.getEntity()), "utf-8"));*/

						String resultStr = EntityUtils
								.toString(httpResponse.getEntity());
						
						MyDebug.log("确认订单信息结果11=" + resultStr);
						
						JSONObject json = new JSONObject(resultStr);
						
						int result = json.getInt("status");

						MyDebug.log("确认订单信息结果"+json.toString());
						
						if(0 == result){
							Message message = Message.obtain();
							message.what = PAY_确认订单支付_成功;
							//message.setData(bundle);
							requestHandler.sendMessage(message);
						}else{
							//TODO: 是否展示失败消息
							requestHandler.sendEmptyMessage(PAY_确认订单支付_失败);
						}
						
					} else {
						requestHandler.sendEmptyMessage(PAY_确认订单支付_再次请求);
					}
				} catch (Exception e) {
					MyDebug.log("确认订单信息结果Exception----"+e.toString());
					//调用第三方api接口
					MyUncaughtExceptionHandler.writeError(e, "get_525_account");
					requestHandler.sendEmptyMessage(PAY_确认订单支付_再次请求);
				}
			}
		}).start();
	}	
}
