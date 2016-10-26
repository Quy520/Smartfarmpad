package com.smartfarm.bean;

/**
 * 消息处理者evenBus
 * 
 * @author jeff
 *
 */
public interface EventHandler {

	/**
	 * 当接收到消息时回调此方法
	 * 
	 * @param event 发生的事件
	 */
	public void onEvent(LocalEvent event);
}
