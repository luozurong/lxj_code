package com.hori.lxjsdk.utils;

public class AsyncTaskExecutor {

	
	/**
	 * 异步任务线程管理器
	 */
	private static AsyncTaskManager asyncTaskManager;
	

	static{
		asyncTaskManager=AsyncTaskManager.newAsyncTaskManager(3,Integer.MAX_VALUE);
	}
	
	private AsyncTaskExecutor(){}
	
	
	public static void addAsyncTaskToQueue(Runnable runnable){
		asyncTaskManager.executeTask(runnable);
	}
}
