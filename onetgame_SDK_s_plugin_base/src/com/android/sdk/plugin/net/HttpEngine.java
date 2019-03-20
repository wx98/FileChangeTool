package com.android.sdk.plugin.net;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HttpEngine{
	
	private ThreadPool pool;
	
	public HttpEngine(int maxNum) {
		if(pool == null){
			pool = ThreadPool.getInstance(maxNum);
		}
	}
	
	/**
	 * @param tskName   任务标识
	 * @param url		请求地址
	 * @param header	自定义的协议
	 * @param timeout	超时时间
	 * **/
	public  void issueHttpGet(String tskName, String url, Hashtable<String, String> header, int timeout){
		DownLoadTask task = new DownLoadTask();
		
		task.setRequestProperty(header);
		
		task.setTimeout(timeout);
		
        task.issueHttpGet(tskName, url);
		
        pool.addTask(task);
	}
	
	/**
	 * 如果设置了上传文件路径filePath，那么再设置postData则无效,要么只能传递postData，要么只能上传文件
	 * 
	 * @param tskName   任务标识
	 * @param url		请求地址
	 * @param header	自定义的协议
	 * @param postData	上传的数据
	 * @param filePath  上传到文件地址
	 * @param timeout	超时时间
	 * **/
	public  void issueHttpPost(String tskName, String url, Hashtable<String, String> header, byte[] postData/*,String filePath*/,int timeout){
		DownLoadTask task = new DownLoadTask();
		
		task.setRequestProperty(header);
		
		/*byte[] data = null; 
		if(postData != null){
			data = postData.getBytes();
		}*/
		
		task.setTimeout(timeout);
		
        task.issueHttpPost(tskName, url, postData/*, filePath*/);
		
        pool.addTask(task);
	}
	
	/**
	 * @param tskName   任务标识
	 * @param url		请求地址
	 * @param header	自定义的协议
	 * @param timeout	超时时间
	 * **/
	public  void issueHttpHead(String tskName, String url, Hashtable<String, String> header, int timeout){
		DownLoadTask task = new DownLoadTask();
		
		task.setRequestProperty(header);
		
		task.setTimeout(timeout);
		
		task.issueHttpHead(tskName, url);
		
		pool.addTask(task);
	}
	

	public  void setDownLoadListener(DownLoadListener dl){
		pool.registerDownLoadListener(dl);
	}

	/***退出指定的任务**/
	public  void cancel(String tskName){
		pool.cancelTask(tskName);
	}
	
	public  void removeCache(String tskName){
	    pool.removeEachCache(tskName);
	}
	
	/***退出所有任务*/
	public  void cancelAll(){
		pool.cancelAllTasks();
	}
	
	public  void setProxy(String Proxy,int ProxyPort){
		pool.setProxy(Proxy, ProxyPort);
	}
	
	public  String getHeader(String id,String key){
		return pool.getHeader(id, key);
	}
	
	public  Map<String, List<String>> getHeader(String id){
		return pool.getHeader(id);
	}
	
	//销毁线程池
	public  void destroy(){
		pool.destroy();
		pool = null;
	}
	
}

