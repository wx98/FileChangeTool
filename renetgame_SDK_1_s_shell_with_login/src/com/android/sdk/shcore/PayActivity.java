package com.android.sdk.shcore;

import com.android.sdk.port.PayInfo;
import com.android.sdk.port.PayListener;
import com.android.sdk.port.PayStatusCode;
import com.android.sdk.util.PayVerify;
import com.android.sdk.util.Plugin;
import com.android.sdk.util.ResourcesUtil;
import com.android.sdk.util.SDKDebug;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class PayActivity extends Activity {

	public static final String EXTRA_PAY_INFO = "extraPayInfo";
	public static final String EXTRA_MESSENGER = "extraMessenger";
	
	private WebView mWebView;

	private PayInfo payInfo;
	private Messenger messenger;
	
	private int payResult = PayStatusCode.PAY_CANCEL;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initWebView();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SDKDebug.rlog("PayActivity.onNewIntent()");
		mWebView.loadUrl("");
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {

		FrameLayout container = new FrameLayout(this);
		mWebView = new WebView(this);
		container.addView(mWebView);
		setContentView(container);

		//启用javascript
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		//内容的渲染需要webviewChromClient去实现
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!TextUtils.isEmpty(url)) {
					if (url.startsWith("weixin") || url.startsWith("alipays")) {
						SDKDebug.rlog("weixin url = " + url);
						//弹出确认框
						//showConfirmDialog(PayActivity.this, url);
						//加载手机内置支付
						toLoadInnerApp(url);
						return true;
					}
				}
				return false;
			}
				
		});
		mWebView.addJavascriptInterface(new JSInterface(this), "JSInterface");
		
		Bundle bundle = getIntent().getExtras();
		//获取请求网关地址
		String url = bundle.getString("url");
		//获取支付回调
		messenger = bundle.getParcelable(EXTRA_MESSENGER);
		payInfo = (PayInfo) bundle.getSerializable(EXTRA_PAY_INFO);
		if (messenger == null) {
			SDKDebug.relog("PayActivity: messenger == null");
			return;
		}
		if (payInfo == null) {
			SDKDebug.relog("PayActivity: payInfo == null");
			sendCallback(PayStatusCode.ERROR_PAY_FAILED);
			return;
		}
		if (TextUtils.isEmpty(url)) {
			SDKDebug.relog("PayActivity: url is empty");
			sendCallback(PayStatusCode.ERROR_PAY_FAILED);
			return;
		}
		
		//加载支付页面
		mWebView.loadUrl(url);
	}
	
	private void sendCallback(int code) {
		Message msg = Message.obtain();
		msg.what = code;
		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		finish();
	}

	/**
	 * 加载手机内置支付app。
	 * @param url
	 */
	private void toLoadInnerApp(String url) {
		try {
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setData(Uri.parse(url));
			startActivity(it);
		} catch (Exception e) {
			//这里需要处理 发生异常的情况
			//可能情况： 手机没有安装支付宝或者微信。或者安装支付宝或者微信但是版本过低
			showUpgradeDialog(this);
		}
	}
	
	private void showUpgradeDialog(Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("请安装最新的微信app");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	sendCallback(PayStatusCode.ERROR_PAY_FAILED);
            }
        });
        
        builder.show();
	}
	
	//向本地后端验证支付结果
	//将会返回一个url，这个url是支付结果展示页面
//	private void nativeVerifyPayResult() {
//		Plugin.getInstance(this).nativeVerifyPayResult(this, payInfo, new Handler(getMainLooper()) {
//			
//			@Override
//			public void handleMessage(Message msg) {
//				if (msg == null || msg.obj == null) {
//					SDKDebug.relog("PayActivity.nativeVerifyPayResult(): no response bundle found");
//					return;
//				}
//				
//				Bundle bundle = (Bundle) msg.obj;
//				String url = bundle.getString("url");
//				SDKDebug.rlog("PayActivity.nativeVerifyPayResult(): url = " + url);
//				mWebView.loadUrl(url);
//			}
//		});
//	}
	
	/**
	 * JS交互接口。
	 * @author Synaric
	 *
	 */
	public class JSInterface {
		
		Activity context;
		
		public JSInterface(Activity context) {
			this.context = context;
		}
		
		@JavascriptInterface
		public void onPayResult(final String url) {
			SDKDebug.rlog("JSInterface.onPayResult(): url = " + url);
			loadUrl(url);
		}
		
		@JavascriptInterface
		public void onPaySuccess() {
			SDKDebug.rlog("JSInterface.onPaySuccess()");
			sendCallback(PayStatusCode.PAY_SUCCESS);
		}
		
		@JavascriptInterface
		public void onPayFailed() {
			SDKDebug.relog("JSInterface.onPayFailed()");
			sendCallback(PayStatusCode.ERROR_PAY_FAILED);
		}
		
		@JavascriptInterface
		public void onPayCancel() {
			SDKDebug.rlog("JSInterface.onPayCancel()");
			sendCallback(PayStatusCode.PAY_CANCEL);
		}
		
		private void loadUrl(final String url) {
			if (TextUtils.isEmpty(url)) {
				SDKDebug.relog("JSInterface.loadUrl(): url is empty");
				sendCallback(PayStatusCode.PAY_UNKNOWN);
				return;
			}
			mWebView.post(new Runnable() {
				
				@Override
				public void run() {
					mWebView.loadUrl(url);
					
				}
			});
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	sendCallback(payResult);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.removeAllViews();
			mWebView.destroy();
			mWebView = null;
		}
	}
}
