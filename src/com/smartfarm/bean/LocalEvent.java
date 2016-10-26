package com.smartfarm.bean;

/**
 * 本地消息事件
 * 
 * @author jeff
 *
 */
public class LocalEvent {

	/**
	 * 温度消息
	 */
	public static final int EVENT_TYPE_TEMP = 101;

	/**
	 * 通风状态
	 */
	public static final int EVENT_TYPE_VEN = EVENT_TYPE_TEMP + 1;

	/**
	 * 模式切换
	 */
	public static final int EVENT_TYPE_MODE_CHANGE = EVENT_TYPE_VEN + 1;

	/**
	 * 配置更改
	 */
	public static final int EVENT_TYPE_CFG_CHANGE = EVENT_TYPE_MODE_CHANGE + 1;

	/**
	 * 接收到网络消息
	 */
	public static final int EVENT_TYPE_RECEIVE_MSG = EVENT_TYPE_CFG_CHANGE + 1;

	/**
	 * 放风长度变化
	 */
	public static final int EVENT_TYPE_LEN_CHANGE = EVENT_TYPE_RECEIVE_MSG + 1;
	
	/**
	 * 窗口数量变化
	 */
	public static final int EVENT_TYPE_WINDOW_CHANGE = EVENT_TYPE_LEN_CHANGE + 1;

	/**
	 * 网络状况变化
	 */
	public static final int EVENT_TYPE_NET_CHANGE = EVENT_TYPE_WINDOW_CHANGE + 1;

	/**
	 * 接收到test消息
	 */
	public static final int EVENT_TYPE_RECEIVE_TEST = EVENT_TYPE_NET_CHANGE + 1;

	/**
	 * 接收到串口消息
	 */
	public static final int EVENT_TYPE_RECEIVE_SERIAL = EVENT_TYPE_RECEIVE_TEST + 1;

	/**
	 * 接收到照度消息
	 */
	public static final int EVENT_TYPE_RECEIVE_LIGHT = EVENT_TYPE_RECEIVE_SERIAL + 1;

	/**
	 * 补光灯状态变化
	 */
	public static final int EVENT_TYPE_LIGHT_CHANGE = EVENT_TYPE_RECEIVE_LIGHT + 1;

	/**
	 * 主界面显示的界面数变更
	 */
	public static final int EVENT_TYPE_PAGER_CHANGE = EVENT_TYPE_LIGHT_CHANGE + 1;

	/**
	 * 电磁阀数量变更
	 */
	public static final int EVENT_TYPE_WATER_COUNT_CHANGE = EVENT_TYPE_PAGER_CHANGE + 1;

	/**
	 * 收到湿度消息
	 */
	public static final int EVENT_TYPE_RECEIVE_WATER = EVENT_TYPE_WATER_COUNT_CHANGE + 1;

	/**
	 * 电磁阀状态变更
	 */
	public static final int EVENT_TYPE_WATER_CHANGE = EVENT_TYPE_RECEIVE_WATER + 1;

	/**
	 * 水肥一体化模式变更
	 */
	public static final int EVENT_TYPE_WATER_MODE_CHANGE = EVENT_TYPE_WATER_CHANGE + 1;

	/**
	 * 自定义补光模式开启
	 */
	public static final int EVENT_TYPE_LIGHT_DIY_MODE = EVENT_TYPE_WATER_MODE_CHANGE + 1;

	/**
	 * 温度配置信息变更
	 */
	public static final int EVENT_TYPE_TEMP_CONFIG_CHANGE = EVENT_TYPE_LIGHT_DIY_MODE + 1;

	/**
	 * 照度模式变更
	 */
	public static final int EVENT_TYPE_LIGHT_MODE_CHANGE = EVENT_TYPE_TEMP_CONFIG_CHANGE + 1;

	/**
	 * 退出
	 */
	public static final int EVENT_TYPE_EXIT = EVENT_TYPE_LIGHT_MODE_CHANGE + 1;

	/**
	 * root权限缺失
	 */
	public static final int EVENT_TYPE_NO_ROOT = EVENT_TYPE_EXIT + 1;
	/**
	 * 蓝牙已连接
	 */
	public static final int EVENT_TYPE_BLUETOOTH_ON = EVENT_TYPE_NO_ROOT + 1;
	/**
	 * 蓝牙未连接
	 */
	public static final int EVENT_TYPE_BLUETOOTH_OFF = EVENT_TYPE_BLUETOOTH_ON + 1;
	/**
	 * 重启服务
	 */
	public static final int EVENT_TYPE_RESTART_SERVICE = EVENT_TYPE_BLUETOOTH_OFF + 1;
	/**
	 * 二氧化碳
	 */
	public static final int EVENT_TYPE_CHECK_OTHER1 = EVENT_TYPE_RESTART_SERVICE + 1;
	/**
	 * 湿度
	 */
	public static final int EVENT_TYPE_CHECK_OTHER2 = EVENT_TYPE_CHECK_OTHER1 + 1;
	/**
	 * 光照
	 */
	public static final int EVENT_TYPE_CHECK_OTHER3 = EVENT_TYPE_CHECK_OTHER2 + 1;
	/**
	 * 测试模式
	 */
	public static final int EVENT_TYPE_IS_DEBUG = EVENT_TYPE_CHECK_OTHER3 + 1;

	public static final int EVENT_TYPE_GET_DEVICE_ID = EVENT_TYPE_IS_DEBUG + 1;
	public static final int EVENT_TYPE_GET_DEVICE_VERSION = EVENT_TYPE_GET_DEVICE_ID + 1;
	public static final int EVENT_TYPE_DEVICE_CHANGE_SCUESS = EVENT_TYPE_GET_DEVICE_VERSION + 1;
	public static final int EVENT_TYPE_DEVICE_CHANGE_ERROR = EVENT_TYPE_DEVICE_CHANGE_SCUESS + 1;
	public static final int EVENT_TYPE_ON_SUPERMODE = EVENT_TYPE_DEVICE_CHANGE_ERROR + 1;
	public static final int EVENT_TYPE_OFF_SUPERMODE = EVENT_TYPE_ON_SUPERMODE + 1;
	/**
	 * 高温切自动
	 */
	public static final int EVENT_TYPE_AUTO_OPEN_ENABLE = EVENT_TYPE_OFF_SUPERMODE + 1;
	public static final int EVENT_TYPE_RAIN_MODE = EVENT_TYPE_AUTO_OPEN_ENABLE + 1;
	public static final int EVENT_TYPE_WIND_MODE = EVENT_TYPE_RAIN_MODE + 1;
	public static final int EVENT_TYPE_AUTO_CHANGE = EVENT_TYPE_WIND_MODE + 1;
	public static final int EVENT_TYPE_READ_ID_ERROR = EVENT_TYPE_AUTO_CHANGE + 1;
	
	/**
	 * 打开水泵
	 */
	public static final int EVENT_TYPE_WATER_OPEN = 0;
	/**
	 * 关闭水泵
	 */
	public static final int EVENT_TYPE_WATER_CLOSE = EVENT_TYPE_WATER_OPEN + 1;

	public static final int EVENT_TYPE_WATER_ONEKEY_OPEN = EVENT_TYPE_WATER_OPEN + 1;
	public static final int EVENT_TYPE_WATER_ONEKEY_CLOSE = EVENT_TYPE_WATER_ONEKEY_OPEN + 1;
	public static final int EVENT_TYPE_WATER_ONEKEY_CHANGE = EVENT_TYPE_WATER_ONEKEY_CLOSE + 1;
	public static final int EVENT_TYPE_WATER_HX_OUT = EVENT_TYPE_WATER_ONEKEY_CHANGE + 1;

	private int eventType;
	private Object eventData;
	private String eventMsg;
	private int eventValue;

	public static LocalEvent getEvent(int eventType) {

		return getEvent(eventType, -1, "", null);
	}

	public static LocalEvent getEvent(int eventType, String eventMsg) {

		return getEvent(eventType, -1, eventMsg, null);
	}

	public static LocalEvent getEvent(int eventType, int eventValue) {

		return getEvent(eventType, eventValue, "", null);
	}

	public static LocalEvent getEvent(int eventType, int eventValue,
			String eventMsg) {

		return getEvent(eventType, eventValue, eventMsg, null);
	}

	public static LocalEvent getEvent(int eventType, int eventValue,
			String eventMsg, Object eventData) {

		LocalEvent event = new LocalEvent();

		event.eventMsg = eventMsg;
		event.eventType = eventType;
		event.eventData = eventData;
		event.eventValue = eventValue;

		return event;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public Object getEventData() {
		return eventData;
	}

	public void setEventData(Object eventData) {
		this.eventData = eventData;
	}

	public String getEventMsg() {
		return eventMsg;
	}

	public void setEventMsg(String eventMsg) {
		this.eventMsg = eventMsg;
	}

	public int getEventValue() {
		return eventValue;
	}

	public void setEventValue(int eventValue) {
		this.eventValue = eventValue;
	}

}
