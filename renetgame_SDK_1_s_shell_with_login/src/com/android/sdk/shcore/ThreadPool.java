package com.android.sdk.shcore;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class ThreadPool {

	/** singlaton模式 */
	private static ThreadPool instance;

	/** 监听器 */
//    private DownLoadListener dlListener = null;
    
    /** 默认池中线程数 */
    private static final int DEFAULT_NUM = 5;
    
    private static final int TASK_MAXNUM = 10;
    
    /** 储存每个任务所获得到Header */
    private Hashtable<String, Map<String, List<String>>> headersRecv;
    
    /***/
    private LinkedList<WeakReference<DownLoadTask>>	dlList;
    
    /**系统的线程池*/
    private ExecutorService executorService;
    
    private String proxy;
    
	private int proxyport;
    
    private ThreadPool(int pool_worker_num) {
    	headersRecv = new Hashtable<String, Map<String,List<String>>>();
    	executorService = Executors.newFixedThreadPool(pool_worker_num);
    	dlList = new LinkedList<WeakReference<DownLoadTask>>();
    }

    static synchronized ThreadPool getInstance(int maxNum) {
    	if (instance == null){
    		if(maxNum <= 0){
    			instance = new ThreadPool(DEFAULT_NUM);
    			return instance;
    		}
    		else{
    			if(maxNum>TASK_MAXNUM){
    				maxNum = TASK_MAXNUM;
    			}
    			instance = new ThreadPool(maxNum);
    			return instance;
    		}
    	}
    	return instance;
    }
    
    void registerDownLoadListener(DownLoadListener listener){
//    	if(this.dlListener != null){
//    		return;
//    	}
//    	this.dlListener = listener;
    	DownLoadTask.addCallBack(listener);
    }
    
    /**
     * 增加新的任务,将任务压入队列
     */
    synchronized void addTask(DownLoadTask newTask) {
    	if(newTask == null||dlList==null){
    		return;
    	}
    	
    	newTask.setHeaderRecv(headersRecv);
    	newTask.setProxy(proxy, proxyport);
    	
    	dlList.add(new WeakReference<DownLoadTask>(newTask));
    	
    	executorService.submit(newTask);
    }
    
    String getHeader(String tskName,String key){
    	
    	if(tskName == null || key == null){
    		//System.out.println("ID OR KEY NULL");
    		return null;
    	}
    	
    	Map<String,List<String>> map = headersRecv.get(tskName);
    	
    	if(map == null){
    		//System.out.println("Can't find id in map");
    		return null;
    	}
    	
		StringBuffer headbuffer = new StringBuffer();
	
		List<String> values = map.get(key);
		if(values==null) {
		    headbuffer.append("");
		}
		else {
		    headbuffer.append(values.toString());
		}
		
		return headbuffer.toString();
	}
	
	Map<String, List<String>> getHeader(String tskName){
		if(tskName == null){
		    //System.out.println("ID NULL");
    		return null;
    	}
    	
    	Map<String,List<String>> map = headersRecv.get(tskName);
		
		return map;
	}
	
	/**退出指定任务*/
	void cancelTask(String tskName){
	    if(dlList!=null) {
	        try {
	            for(WeakReference<DownLoadTask> dl:dlList){
	                if(dl==null)
	                    return;

	                DownLoadTask downLoadTask = dl.get();
	                if(downLoadTask!=null) {
	                    if(downLoadTask.getTaskName().equals(tskName)){
	                        downLoadTask.Cancel();
	                        dlList.remove(dl);
	                        return;
	                    }
	                }
	            }
	        } catch (Exception e) {
	        }
	    }
	}
	
	///////////////////////////////////////////////////////////////////////////
	void removeEachCache(String tskName) {
	    if (headersRecv != null && headersRecv.get(tskName) != null) {
	        headersRecv.remove(tskName);
	    }
	}
	///////////////////////////////////////////////////////////////////////////
	
	/**退出所有到任务*/
	void cancelAllTasks(){
	    if(dlList!=null) {
	        try {
	            for(WeakReference<DownLoadTask> dl:dlList){

	                if(dl==null)
	                    return;

	                DownLoadTask downLoadTask = dl.get();
	                if(downLoadTask!=null) {
	                	downLoadTask.Cancel();
	                }
	            }
	        } catch (Exception e) {
	        }
	        dlList.clear();
	    }
	}
	
	void setProxy(String p,int pp){
		this.proxy = p;
		this.proxyport = pp;
	}
	
	void destroy(){
	    
	    if(executorService!=null) 
	        executorService.shutdown();
	    
	    if(headersRecv!=null)
	        headersRecv.clear();
		
	    if(dlList!=null)
		    dlList.clear();
		
	    instance = null;
		
		////////////////////
		DownLoadTask.clear();
		///////////////////
	}
}
