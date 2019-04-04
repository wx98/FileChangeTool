package com.android.sdk.util;

import java.io.File;

import com.android.sdk.lg.port.HYSDK;
import com.android.sdk.lg.port.UserBean;
import com.android.sdk.lg.util.LodingDialogUtil;
import com.android.sdk.lg.util.ResultListener;
import com.android.sdk.port.InitInfo;
import com.android.sdk.port.PayInfo;
import com.android.sdk.port.RoleBean;
import com.android.sdk.shcore.LoginInfo;

import android.content.Context;
import android.os.Handler;

public class Plugin {
	private static Plugin instance = null;

	/**
	 * @Description: 复制jar包到内部存储(sd卡)
	 * @param mContext
	 */
	private Plugin(final Context mContext) {
		// 初始化相关操作
		/*new Thread(new Runnable() {

			@Override
			public void run() {
				// mkdir
				MyFileUtil.mkPluginDir(mContext);

				// copyfile
				MyFileUtil.copyFile(MyFileUtil.getPluginDexPath(mContext)
						+ File.separator + Constant.PLUGIN_JAR_NAME,
						MyFileUtil.getPluginPath(mContext) + File.separator
								+ Constant.PLUGIN_JAR_NAME, true);
			}
		}).run();*/
	}

	public static Plugin getInstance(Context mContext) {
		if (null == instance) {
			instance = new Plugin(mContext);
		}
		return instance;
	}

	/**
	 * @Description: 上传应用和设备信息到服务器,以便统计新增和日活等数据,并判断appid是否存在(合法)
	 * @param mContext
	 * @param appInfo
	 * @param appkey
	 * @param mHandler
	 * @return void
	 */
	public void initMethod(Context mContext, InitInfo appInfo, String appkey,
			Handler mHandler) {
		RemotePort.getInstance(mContext).RemoteSdk_sdkInit(appInfo, appkey,
				mHandler);
	}

	/**
	 * @Description: 上传信息至服务器以便统计创角信息
	 * @param mContext
	 * @param appid
	 * @param userid
	 * @param channelid
	 * @return void
	 */
	public void statisticsInCreateRole(Context mContext, String appid,
			String userid, String channelid) {
		RemotePort.getInstance(mContext).RemoteSdk_uploadIncreaseInfoToServer(
				appid, userid, channelid);
	}

	/**
	 * @Description: 上传计费结果至服务器(成功,失败,取消)
	 * @param mContext
	 * @param payInfo
	 * @param uuid
	 * @param appkey
	 * @param payResult
	 * @return void
	 */
	public void sendResultToServer(Context mContext, PayInfo payInfo,
			String uuid, String appkey, int payResult,int payType) {
		RemotePort.getInstance(mContext).RemoteSdk_uploadPayResultToServer(
				MyUtil.payInfoToString(payInfo, uuid, MyUtil
						.getStringFromMetaData(mContext, UploadTag.channelTag),
						payResult, payInfo.getUserid(), appkey,payInfo.getExt(),payType));
	}
	
	//根据第三方渠道userId返回本渠道userId
	public void requestHYUserId(Context mContext, LoginInfo loginInfo, Handler handler) {
		RemotePort.getInstance(mContext).RemoteSdk_getHYUserId(loginInfo, handler);
	}
	
	//请求生成订单
	public void requestServerOrder(Context mContext, PayInfo payInfo, RoleBean roleBean, Handler mHandler) {
		RemotePort.getInstance(mContext).RemoteSdkRequestServerOrder(payInfo, roleBean, mHandler);
	}
	
	//h5支付专用，请求支付网关地址
	public void requestH5Pay(Context mContext, String userId, PayInfo payInfo, Handler mHandler) {
		RemotePort.getInstance(mContext).RemoteSdkRequestH5Pay(userId, payInfo, mHandler);
	}
	
	public void submitRoleInfo(Context context, HYSDK sdk, String appId, RoleBean roleBean, Handler mHandler) {
		UserBean currentUser = sdk.getCurrentUser();
		if (currentUser == null) {
			mHandler.sendEmptyMessage(-1);
			return;
		}
		RemotePort.getInstance(context).RemoteSdkSubmitRoleInfo(
				roleBean, 
				currentUser.getAccount(), 
				currentUser.getPasswd(), 
				appId, 
				MyUtil.getStringFromMetaData(context, "chal"), 
				DeviceMsgUtil.getInstance(context).getImei(), 
				mHandler);
	}
	
	public void platformCoinPay(final Context context, final HYSDK sdk, final String orderId, final Handler mHandler) {
		UserBean currentUser = sdk.getCurrentUser();
		if (currentUser == null) {
			mHandler.sendEmptyMessage(-1);
			return;
		}
		LodingDialogUtil.createLodingDialog(context, "请求中,请耐心等待...", new ResultListener() {
			
			@Override
			public void onComplete(int code) {
				if (-1  == code) {
					platformCoinPay(context, sdk, orderId, mHandler);
				}
			}
		});
		RemotePort.getInstance(context).RemoteSdkPlatformCoinPay(currentUser, orderId, mHandler);
	}
	
	//确认订单状态（第三方查询）
	public void verifytPayResult(Context mContext, PayInfo payInfo, Handler mHandler) {
		RemotePort.getInstance(mContext).RemoteSdkVerifytPayResult(payInfo,
				mHandler);
	}
	
	//确认订单状态（向本后端查询）
	public void nativeVerifyPayResult(Context mContext, PayInfo payInfo, Handler mHandler) {
		RemotePort.getInstance(mContext).RemoteSdkNativeVerifytPayResult(payInfo,
				mHandler);
	}
	
	//啪啪游请求本地服务端同步订单状态
	public void syncPayPPY(Context mContext, String request, Handler mHandler) {
		
	}
}
