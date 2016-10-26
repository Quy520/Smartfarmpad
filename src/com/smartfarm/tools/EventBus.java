package com.smartfarm.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;

/**
 * 软件消息处理中心
 * 
 * @author jeff
 *
 */
public class EventBus {
	
	private static EventBus instance = new EventBus();
	private List<EventHandler> handlers = new ArrayList<EventHandler>();
	private Handler uiThreadHandler;
	
	/**
	 * 获取消息处理器
	 * 
	 * @return
	 */
	public static EventBus getDefault() {
		
		return instance;
	}
	
	private EventBus() {
		
		uiThreadHandler = new Handler();
	}
	
	/**
	 * 添加关注本地广播
	 * 
	 * @param handler
	 */
	public void add(EventHandler handler) {
		
		Log.d("mmsg", " EventBus add handler : " + handler);
		synchronized (handlers) {
			handlers.add(handler);
		}
	}
	
	/**
	 * 移除关注本地广播
	 * 
	 * @param handler
	 */
	public void remove(EventHandler handler) {

		Log.d("mmsg", " EventBus remove handler : " + handler);
		synchronized (handlers) {

			Iterator<EventHandler> it = handlers.iterator();
			
			while(it.hasNext()) {
				
				EventHandler get = it.next();
				if(get.equals(handler))
					it.remove();
			}
		}
	}
	
	/**
	 * 同步发送一个消息广播
	 * 
	 * @param event
	 */
	public void post(LocalEvent event) {
		
		synchronized (handlers) {
			
			Iterator<EventHandler> it = handlers.iterator();
			
			while(it.hasNext())
				it.next().onEvent(event);
		}
	}
	
	/**
	 * 异步发送一个消息广播
	 * 
	 * @param event
	 */
	public void postInOtherThread(final LocalEvent event) {
		
		uiThreadHandler.post(new Runnable() {
			
			@Override
			public void run() {
				
				post(event);
			}
		});
	}
	
	/**
	 * 异步显示一个toast消息
	 * 
	 * @param msg
	 */
	public void noticeMsg(final String msg) {
		
		uiThreadHandler.post(new Runnable() {
			
			@Override
			public void run() {

				ToastTool.showToast(msg);
			}
		});
	}
	
	/**
	 * 异步执行一个任务
	 * 
	 * @param runnable
	 */
	public void runningOnUiThread(Runnable runnable) {
		
		uiThreadHandler.post(runnable);
	}
	
	/**
	 * 异步执行一个任务，带有延迟
	 * 
	 * @param runnable
	 */
	public void runningOnUiThread(Runnable runnable, int delay) {
		
		uiThreadHandler.postDelayed(runnable, delay);
	}
}
