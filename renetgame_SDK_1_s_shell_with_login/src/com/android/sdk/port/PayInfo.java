package com.android.sdk.port;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.sdk.lg.port.HYSDK;
import com.android.sdk.lg.port.UserBean;
import com.android.sdk.util.SDKDebug;

import android.text.TextUtils;

public class PayInfo implements Serializable {
	
	private String cpOrderId;// 订单号! cp订单号

	private String orderId;// 订单号! 服务器产生的订单号

	private String price;// chargePrice;//元!
	private String appid;
	private String userid;
	private String mobile;
	private String ext = "";// 透传参数

	// 产品名(计费点)
	private String waresname;
	// 上传爱贝云方的appid
	private String appkey;
	// 运行平台(ios:1,android:0)
	private String os = "0";
	// 识别码(android:imei;ios:idfa)
	private String deviceid;
	private String imsi;
	private String iccid;
	// jar包版本号(没有传空)
	private String coreversion;
	// SDK版本号
	private String sdkver;
	// 客户端上传包名
	private String pkgname;
	// 客户端上传计费方式（0：默认525，1：爱贝，2：525 ,3:狐狸，4：葫芦侠 5：CGAME）
	private int type;
	// 应用名(可以考虑不传)
	private String appname;
	// 我们自己的渠道号
	private String channelid;

	//私钥（Android）
	private String privatekey;
	//私钥（H5）
	private String h5privatekey;
	//公钥
	private String publickey;

	// 平台是否有查询接口 0没有 1有
	private int haveCheckInterface;

	// 请求服务器订单情况
	private String notifyUrl;

	public String getCpOrderId() {
		return cpOrderId;
	}

	public void setCpOrderId(String cpOrderId) {
		this.cpOrderId = cpOrderId;
	}

	/** @hide */
	public int getHaveCheckInterface() {
		return haveCheckInterface;
	}

	/** @hide */
	public void setHaveCheckInterface(int haveCheckInterface) {
		this.haveCheckInterface = haveCheckInterface;
	}

	/**
	 * {@hide}
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/**
	 * {@hide}
	 */
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	/**
	 * {@hide}
	 */
	public String getPrivatekey() {
		return privatekey;
	}

	/**
	 * {@hide}
	 */
	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	/**
	 * {@hide}
	 */
	public String getPublickey() {
		return publickey;
	}

	/**
	 * {@hide}
	 */
	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	/**
	 * {@hide}
	 */
	public String getOrderid() {
		return orderId;
	}

	/**
	 * {@hide}
	 */
	public void setOrderid(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * {@hide}
	 */
	public String getChannelid() {
		return channelid;
	}

	/**
	 * {@hide}
	 */
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getWaresname() {
		return waresname;
	}

	public void setWaresname(String waresname) {
		this.waresname = waresname;
	}

	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * {@hide}
	 */
	public String getAppkey() {
		return appkey;
	}

	/**
	 * {@hide}
	 */
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	/**
	 * {@hide}
	 */
	public String getOs() {
		return os;
	}

	/**
	 * {@hide}
	 */
	public String getDeviceid() {
		return deviceid;
	}

	/**
	 * {@hide}
	 */
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	/**
	 * {@hide}
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * {@hide}
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * {@hide}
	 */
	public String getIccid() {
		return iccid;
	}

	/**
	 * {@hide}
	 */
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	/**
	 * {@hide}
	 */
	public String getCoreversion() {
		return coreversion;
	}

	/**
	 * {@hide}
	 */
	public void setCoreversion(String coreversion) {
		this.coreversion = coreversion;
	}

	/**
	 * {@hide}
	 */
	public String getSdkver() {
		return sdkver;
	}

	/**
	 * {@hide}
	 */
	public void setSdkver(String sdkver) {
		this.sdkver = sdkver;
	}

	/**
	 * {@hide}
	 */
	public String getPkgname() {
		return pkgname;
	}

	/**
	 * {@hide}
	 */
	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}

	/**
	 * {@hide}
	 */
	public int getType() {
		return type;
	}

	/**
	 * {@hide}
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * {@hide}
	 */
	public String getAppname() {
		return appname;
	}

	/**
	 * {@hide}
	 */
	public void setAppname(String appname) {
		this.appname = appname;
	}

	/**
	 * {@hide}
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * {@hide}
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getExt() {
		return ext + getStyleExt();
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	/**
	 * {@hide}
	 */
	public String getH5privatekey() {
		return h5privatekey;
	}

	/**
	 * {@hide}
	 */
	public void setH5privatekey(String h5privatekey) {
		this.h5privatekey = h5privatekey;
	}

	// 拼接请求生成订单参数
	public String getRequestServerOrderMsg(RoleBean roleBean) {
		JSONObject json = new JSONObject();
		try {
			JSONObject role = new JSONObject();
			role.put("roleId", roleBean.getRoleId());
			role.put("roleName", roleBean.getRoleName());
			role.put("serverId", roleBean.getServerId());
			role.put("serverName", roleBean.getServerName());
			json.put("cporderid", cpOrderId);
			json.put("waresname", waresname);
			json.put("price", price);
			json.put("appid", appid);
			json.put("userid", userid);
			json.put("role", role);
			json.put("os", os);
			json.put("deviceid", deviceid);
			json.put("imsi", "111111111111");
			json.put("iccid", "111111111111");
			json.put("coreversion", coreversion);
			json.put("sdkver", sdkver);
			json.put("pkgname", pkgname);
			json.put("type", type);
			json.put("appname", appname);
			json.put("channelid", channelid);
			json.put("ext", ext);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public String getRequestH5PayMsg(String userId) {
		JSONObject json = new JSONObject();
		try {
			json.put("appid", appid);
			json.put("waresname", waresname);
			json.put("cporderid", orderId);
			json.put("price", price);
			json.put("userid", userId);
			json.put("privatekey", h5privatekey);
			json.put("publickey", publickey);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public String getStyleExt() {
//		String cpoid = "";
//		
//		if (TextUtils.isEmpty(ext)) {
//			ext = "";
//			return "cporderid=" + cpOrderId + "&orderid=" + orderId;
//		}
//		if (!ext.contains("cporderid")) cpoid = "&cporderid=" + cpOrderId;
//		return cpoid + "&orderid=" + orderId;
		return "cporderid=" + cpOrderId + "&orderid=" + orderId;
	}
	
	public String getRawExt() {
		return ext;
	}

	//生成确认订单参数（第三方）
	public String getCheckOrderMsgStr() {
		JSONObject json = new JSONObject();
		try {

			json.put("cporderid", orderId);
			json.put("isSdkRequest", "yes");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	//生成确认订单参数（本地后端）
	public String getNativeCheckOrderMsgStr() {
		JSONObject json = new JSONObject();
		try {

			json.put("orderid", orderId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}
