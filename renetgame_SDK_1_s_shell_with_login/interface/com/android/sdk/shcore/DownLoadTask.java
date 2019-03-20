package com.android.sdk.shcore;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.android.sdk.util.SDKDebug;

class DownLoadTask implements Runnable {

	/** 所请求到链接 */
	private String sUrl;

	/** 超时时间 **/
	private int timeout = 30;

	/** 任务标识 */
	private String taskname;

	/***/
	private byte[] postData;

	/** 本次链接是否自动跳转,not global */
	private boolean isRedirect;

	/** 控制缓存 */
	private boolean isUserCache;

	/** 控制退出任务 */
	private boolean cancel;

	/** 本地缓存数据大小 */
	private final static int BUFFER_SIZE = 8 * 1024;

	private Hashtable<String, String> RequestHeader;

	private Hashtable<String, Map<String, List<String>>> recv;

	/** 代理host */
	private String proxy;
	/** 代理端口 */
	private int proxyport;

	private static final int STATE_CHECK = 0;
	private static final int STATE_SET = 1;
	private static final int STATE_HEADER = 2;
	private static final int STATE_RECVBODY = 3;
	private int state;

	/**
	 * 请求类型
	 * 
	 * @param "GET","POST","HEAD"
	 * */
	private String methodType;

	private static final String CMWAP1 = "application/vnd.wap.wmlc"; //
	// 收费提示1

	private static final String CMWAP2 = "text/vnd.wap.wml";
													// //
													// 收费提示2

	private static final String POST = "POST";// 

	private static final String CONTENT_ENCODING = "Content-Encoding";// 

	private static final String GZIP = "gzip";// 

	private static final String CANCEL_SUCCESS = "cancel success";// 

	private static final String FINISH = "finish";// 

	private static final String HEAD = "HEAD";// 

	protected DownLoadTask() {
		sUrl = null;
		taskname = null;
		isRedirect = true;
		isUserCache = false;
		timeout = 20000;

		cancel = false;
		state = STATE_CHECK;
	}

	public void run() {

		URL url = null;
		HttpURLConnection conn = null;
		try {
			while (!cancel) {
				SDKDebug.rlog("taskstate: " + state);
				switch (state) {
				case STATE_CHECK:

					// 不需要过多解释
					if (sUrl == null || taskname == null) {
						String error = "Error:You must input http url and taskname";
						HttpTransfersEvent(DownLoadListener.ERROR, error);
						return;
					}

					state = STATE_SET;
					break;

				case STATE_SET:

					url = new URL(sUrl);

					// 设置代理
					if ((proxy != null) && (proxy.length() >= 7)) {
						// 连接系统属性
						Properties prop = System.getProperties();
						// 通知系统要通过代理连接
						System.getProperties().put(
								"proxySet",
										"true");
						// 指定代理服务地址
						prop.setProperty(
								"http.proxyHost",
								proxy);
						// Log.e("proxy", proxy);
						// 指定代理监听端口
						prop.setProperty(
								"http.proxyPort",
								"" + proxyport);
						// java.net.Proxy p = new
						// Proxy(java.net.Proxy.Type.HTTP, new
						// InetSocketAddress(proxy, proxyport));
						conn = (HttpURLConnection) url.openConnection();
					} else {
						conn = (HttpURLConnection) url.openConnection();
					}

					if (RequestHeader != null) {
						if (RequestHeader.size() > 0) {
							Enumeration<?> enums = RequestHeader.keys();
							while (enums.hasMoreElements()) {
								String key = (String) enums.nextElement();
								String value = RequestHeader.get(key);
								conn.addRequestProperty(key, value);
							}
						}
					}

					// 设置此个单例跳转
					conn.setInstanceFollowRedirects(isRedirect);
					// 设置连接超时时间
					conn.setConnectTimeout(timeout);
					// 设置是否用户缓存
					conn.setUseCaches(isUserCache);
					// 设置读取超时时间
					conn.setReadTimeout(timeout);
					// 设置请求方法
					conn.setRequestMethod(methodType);

					if (methodType.equals(POST)) {// "POST"
						if (postData == null) {
							String error = "Error:Upload data is null";
							HttpTransfersEvent(DownLoadListener.ERROR, error);
							return;
						}

						String encoding = RequestHeader.get(CONTENT_ENCODING);// Content-Encoding
						if (encoding != null) {
							if (encoding.toLowerCase().contains(GZIP)) {// gzip
								ByteArrayOutputStream arr = new ByteArrayOutputStream();
								OutputStream zipper = new GZIPOutputStream(arr);
								zipper.write(postData);
								zipper.close();
								postData = arr.toByteArray();
							}
						}

						conn.setDoOutput(true);
						conn.setFixedLengthStreamingMode(postData.length);

						OutputStream os = conn.getOutputStream();
						// 上传数据
						os.write(postData);
						os.flush();
						os.close();

						postData = null;
						// if(uploadFile == null){
						// }
						// else{
						// FileInputStream fis = new
						// FileInputStream(uploadFile);
						// DataInputStream dis = new DataInputStream(fis);
						// int bytes = 0;
						// byte[] bufferOut = new byte[BUFFER_SIZE];
						// while ((bytes = dis.read(bufferOut)) != -1) {
						// os.write(bufferOut, 0, bytes);
						// }
						// os.flush();
						// os.close();
						// dis.close();
						// fis.close();
						// dis = null;
						// fis = null;
						// state = STATE_HEADER;
						// }
					}
					state = STATE_HEADER;
					break;

				case STATE_HEADER:

					Map<String, List<String>> fields = conn.getHeaderFields();
					if (fields != null) {
						if (recv != null)
							recv.put(taskname, fields);
					}

					int responseCode = conn.getResponseCode();
					if (responseCode >= 400 || responseCode <= -1) {
						String error = "ERROR!ResponseCode:"
								+ responseCode;
						// /////////////////////////
						// if(dlLstener != null){
						// if(responseCode == -1){
						// error =
						// error+"you can try to reduce the num of cocurrent task!";
						// }
						// dlLstener.HttpTransfersEvent(taskname, 4, error);
						// }
						HttpTransfersEvent(DownLoadListener.ERROR, error);
						// /////////////////////////
						conn.disconnect();
						conn = null;
						return;
					} else if (responseCode == 200) {
						if ((conn.getContentType() != null)
								&& (conn.getContentType().startsWith(CMWAP1) || conn
										.getContentType().startsWith(CMWAP2))) {
							HttpTransfersEvent(
									DownLoadListener.ERROR,
									"receive wap charge page!");
							conn.disconnect();
							conn = null;
							return;
						}
					}

					// "HEAD"请求方法返回到数据中不会包含body，因此无需再向下读取
					if (methodType.equals(HEAD)) {// "HEAD"
						state = -1;
						HttpTransfersEvent(DownLoadListener.FINISH, FINISH);// "finish"
						return;
					}

					state = STATE_RECVBODY;
					break;

				case STATE_RECVBODY:
					SDKDebug.rlog("STATE_RECVBODY");
					InputStream bis = null;
					String encoding = conn.getHeaderField(CONTENT_ENCODING);// "Content-Encoding"
					if (encoding != null) {
						if (encoding.toLowerCase().contains(GZIP)) {// "gzip"
							bis = new GZIPInputStream(conn.getInputStream());
						}
					} else {
						bis = new BufferedInputStream(conn.getInputStream());
					}
					int size;
					byte buf[] = new byte[BUFFER_SIZE];

					if (bis == null) {
						SDKDebug.relog("bis == null");
						HttpTransfersEvent(
								DownLoadListener.ERROR,
								"BufferedInputStream  null");
						return;
					}
					SDKDebug.relog("start loop");
					while ((size = bis.read(buf)) != -1) {

						if (!cancel) {
							HttpTransfersBodyReceived(taskname, conn.getURL()
									.toString(), buf, size);
						} else {
							// 如果是退出则会直接中断任务，关闭流，否则只是中断循环
							if (cancel == true) {
								HttpTransfersEvent(DownLoadListener.CANCEL,
										CANCEL_SUCCESS);// "cancel success"

								bis.close();
								bis = null;
								buf = null;
								conn.disconnect();
								conn = null;

								state = -1;
								return;
							}
							break;
						}
					}
					// 给予一个-1可以让对方网络流已经读完
					HttpTransfersBodyReceived(taskname, conn.getURL()
							.toString(), buf, -1);

					bis.close();
					bis = null;
					buf = null;

					conn.disconnect();
					conn = null;

					HttpTransfersEvent(DownLoadListener.FINISH, FINISH);// "finish"

					state = -1;
					return;
				}
			}

			// 如果是正常退出不会走到这里
			if (cancel == true) {
				HttpTransfersEvent(DownLoadListener.CANCEL, CANCEL_SUCCESS);// "cancel success"
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}

				state = -1;
			}

		} catch (Exception e3) {
			StringWriter sw = new StringWriter();
			e3.printStackTrace(new PrintWriter(sw, true));
			String error = getTaskName()
					+ "Error" + sw.toString();
			HttpTransfersEvent(DownLoadListener.ERROR, error);

			e3.printStackTrace();
		}
	}

	void issueHttpGet(String id, String url) {
		taskname = id;
		sUrl = url;
		methodType = "GET";
	}

	/** 如果事先设置了path，那么在设置postbody将无效 **/
	void issueHttpPost(String id, String url, byte[] postBody/* ,String path */) {
		taskname = id;
		sUrl = url;
		methodType = POST;// "POST"
		postData = postBody;
	}

	void issueHttpHead(String id, String url) {
		taskname = id;
		sUrl = url;
		methodType = HEAD;// "HEAD";
	}

	void setRequestProperty(Hashtable<String, String> hash) {
		this.RequestHeader = hash;
	}

	// //////////test/////////////////////////////////////////////
	private static ArrayList<WeakReference<DownLoadListener>> queue = null;

	static void addCallBack(DownLoadListener listener) {
		if (queue == null) {
			queue = new ArrayList<WeakReference<DownLoadListener>>();
		}
		synchronized (queue) {
			queue.add(new WeakReference<DownLoadListener>(listener));
		}
	}

	protected static void clear() {
		if (queue != null) {
			synchronized (queue) {
				queue.clear();
				queue = null;
			}
		}
	}

	private void HttpTransfersEvent(int eventCode, String error) {
		if (queue != null) {
			List<DownLoadListener> copylist = new ArrayList<DownLoadListener>();
			synchronized (queue) {
				if (queue.size() == 0)
					return;
				for (WeakReference<DownLoadListener> q : queue) {
					DownLoadListener d = q.get();
					if (d != null) {
						// d.HttpTransfersEvent(taskname, eventCode, error);
						// just copy
						copylist.add(d);
					}
				}
			}

			// perform
			Iterator<DownLoadListener> it = copylist.iterator();
			while (it.hasNext()) {
				DownLoadListener d = it.next();
				if (d != null) {
					d.HttpTransfersEvent(taskname, eventCode, error);
				}
			}
			copylist.clear();
		}
	}

	private void HttpTransfersBodyReceived(String taskname, String url,
			byte[] buf, int size) {
		if (queue != null) {
			List<DownLoadListener> copylist = new ArrayList<DownLoadListener>();
			synchronized (queue) {
				if (queue.size() == 0)
					return;
				for (WeakReference<DownLoadListener> q : queue) {
					DownLoadListener d = q.get();
					if (d != null) {
						// d.HttpTransfersBodyReceived(taskname, url, buf,size);
						// just copy
						copylist.add(d);
					}
				}
			}

			// perform
			Iterator<DownLoadListener> it = copylist.iterator();
			while (it.hasNext()) {
				DownLoadListener d = it.next();
				if (d != null) {
					d.HttpTransfersBodyReceived(taskname, url, buf, size);
				}
			}
			copylist.clear();
		}
	}

	// /////////////////////////////////////////////////////////////

	protected String getRequestMethod() {
		return methodType;
	}

	protected boolean getRedirect() {
		return isRedirect;
	}

	protected String getUrl() {
		return sUrl;
	}

	protected int getTimeout() {
		return timeout;
	}

	protected void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	String getTaskName() {
		return taskname;
	}

	protected void setHeaderRecv(
			Hashtable<String, Map<String, List<String>>> hash) {
		recv = hash;
	}

	protected void Cancel() {
		cancel = true;
	}

	protected void setProxy(String proxy, int proxyport) {
		this.proxy = proxy;
		this.proxyport = proxyport;
	}
}

