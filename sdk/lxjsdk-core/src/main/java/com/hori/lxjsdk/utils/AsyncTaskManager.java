package com.hori.lxjsdk.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;




/**
 * 异步任务调度管理器
 * @author sucs
 *
 */
public class AsyncTaskManager{

	protected AsyncTaskHandler handler;
	
	
	private AsyncTaskManager(int threadNum,int maxTaskNum){
		
		handler=new AsyncTaskHandler(threadNum,maxTaskNum);
		
		handler.start();
	}
	
	/**
	 * 创建一个异步任务调度管理器
	 * @param threadNum   线程数量
	 * @param maxTaskNum  任务队列最大任务数
	 * @return
	 */
	public static AsyncTaskManager newAsyncTaskManager(int threadNum,int maxTaskNum){
		return new AsyncTaskManager(threadNum,maxTaskNum);
	} 

	/**
	 * 创建一个异步任务调度管理器
	 * @param threadNum   线程数量
	 * @return
	 */
	public static AsyncTaskManager newAsyncTaskManager(int threadNum){
		return new AsyncTaskManager(threadNum,Integer.MAX_VALUE);
	} 
	
	
	public void executeTask(Runnable task){
		handler.getAsyncTaskQueue().addTask(task);
	}
	
	static class AsyncTaskHandler  extends Thread{
		
		private AsyncTaskQueue asyncTaskQueue;

		private static ExecutorService pushJobThreadPool; 
		
		private int threadNum;
		private int maxTaskNum;
		
		public AsyncTaskHandler(int threadNum,int maxTaskNum) {
			this.maxTaskNum=maxTaskNum;
			this.threadNum=threadNum;
			
			asyncTaskQueue=new AsyncTaskQueue(maxTaskNum);
			
			if(threadNum>1){
				pushJobThreadPool = Executors.newFixedThreadPool(threadNum); 
			}
			
		}
		
		@Override
		public void run() {
			

			while (true) {
				
				try {
						
					//从任务队列中取出某个任务
					Runnable task = asyncTaskQueue.getTask();
					
					if(threadNum>1){
						
						pushJobThreadPool.execute(task);
					}else{
						
						task.run();
					}
					
				//	Thread.sleep(50);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			
		}

		public int getThreadNum() {
			return threadNum;
		}

		public int getMaxTaskNum() {
			return maxTaskNum;
		}

		public AsyncTaskQueue getAsyncTaskQueue() {
			return asyncTaskQueue;
		}
	}
	
	
	
	static class AsyncTaskQueue {
		
		/**
		 * 任务队列
		 */
		private LinkedBlockingQueue<Runnable> asyncTaskQueue;
		
		public AsyncTaskQueue() {
			asyncTaskQueue = new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE);
		}

		public AsyncTaskQueue(int capacity) {
			asyncTaskQueue = new LinkedBlockingQueue<Runnable>(capacity);
		}
		
		/**
		 * 取出某个长时间任务
		 * @return
		 */
		public Runnable getTask(){
			
			Runnable task = null;
			
			try {
				task = asyncTaskQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return task;
		}
		
		/**
		 * 向长时间任务队列添加任务
		 * @param pushJob
		 */
		public void addTask(Runnable task){
			try{
				asyncTaskQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 取任务队列的任务数量
		 * @return
		 */
		public int getAsyncTaskQueueSize() {
			return asyncTaskQueue.size();
		}
		
	}
}
