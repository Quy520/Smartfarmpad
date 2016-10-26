package com.smartfarm.tools;

public class Constants {

	public static final boolean SHOW_DEBUG = false;
	public static final String TAG = "GreenHouse_Tablet";

	public static final String ZT_SITE = "http://www.shztzn.com/";
	public static final String MORE_SITE = "http://www.shztzn.com/index.php?case=archive&act=list&catid=1";

	public static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; // SPP服务UUID号

	public static final String MSG_PREFIX_DATA = "data_";
	
	public static final int PAGE_SIZE = 10;

	public static final boolean DEBUG_MODE = false;
	// 无电机测试 为true 否则为false
	public static final boolean DEFAULT_STATE = false;

	public static final int UPLOAD_COUNT = 1000;

	public static final int REQUEST_CONNECT_DEVICE = 1; // 宏定义查询设备句柄

	public static final boolean BLUETOOTH = false;

	// 是否为测试模式 true 为测模式 不关机，不重启 10s读取一次温度
	public static boolean IS_DEBUG = false;

	// 是否为生产模式true 为生产模式不主动度温度与放关停

	public static final boolean IS_FACTORY = false;

	public static boolean SUPER_MODE = false;
	/**
	 * 电机运转速度,单位1cm花费秒数
	 */
	public static int MOTOR_SPEED = IS_DEBUG ? 1 : 6;

	/**
	 * 窗口状态阶段等差间隔
	 */
	public static final int WINDOW_STATE_INTERVAL = 5 * MOTOR_SPEED * 1000;

	public static final int WINDOW_STATE_INTERVAL_MAX = 6;

	/**
	 * 温度采集时间 10s读取一次温度 1分钟重启一次测试版本
	 */
	public static int TEMP_PERIOD = IS_DEBUG ? 30 * 1000 : 5 * 60 * 1000;
	// public static final int TEMP_PERIOD = 10 * 1000;

	/**
	 * 温度检查偏移量
	 */
	public static final int TEMP_CHECK_OFFSET = 4;

	/**
	 * 最大报警数
	 */
	public static final int TEMP_ALARM_MAX_COUNT = 3;

	/**
	 * 最大重启数
	 */
	public static final int REBOOT_MAX_COUNT = 3;

	/**
	 * 最大采集温度记录数
	 */
	public static final int FIFO_LENGTH = 10;

	/**
	 * 温度采集控制电机间隔时间
	 */
	public static final int MOTOR_STRP_COUNT_MAX = 3;

	public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	public static final int TEMPMESSAGE = 0x102;

	public static final int MSG_RECELVE_SMS = 0;
	public static final int MSG_BETTERY = MSG_RECELVE_SMS + 1;
	public static final int MSG_PUSH_TEMP = MSG_BETTERY + 1;
	public static final int MSG_RECELVE_PUSH = MSG_PUSH_TEMP + 1;
	public static final int MSG_MOTOR_IS_WORKING = MSG_RECELVE_PUSH + 1;
	public static final int MSG_MOTOR_TIMER_STOP = MSG_MOTOR_IS_WORKING + 1;
	public static final int MSG_WINDOW_COUNT_CHANGE = MSG_MOTOR_TIMER_STOP + 1;
	public static final int MSG_CHECK_ALARM_RES = MSG_WINDOW_COUNT_CHANGE + 1;
	public static final int MSG_RECEIVE_TEST_RESPONSE = MSG_CHECK_ALARM_RES + 1;
	public static final int MSG_VEN_TIME_END = MSG_RECEIVE_TEST_RESPONSE + 1;
	public static final int MSG_OPEN_MAX = MSG_VEN_TIME_END + 1;
	public static final int MSG_CLOSE_MIN = MSG_OPEN_MAX + 1;
	public static final int MSG_ADD_DELAY_STOP = MSG_CLOSE_MIN + 1;
	public static final int MSG_RECEIVE_DELAY_STOP = MSG_ADD_DELAY_STOP + 1;
	public static final int MSG_RECELVE_TEMP = MSG_RECEIVE_DELAY_STOP + 1;
	public static final int MSG_MODE_CHANGE = MSG_RECELVE_TEMP + 1;
	public static final int MSG_RECEIVE_NET_MSG = MSG_MODE_CHANGE + 1;
	public static final int MSG_NET_IS_OK = MSG_RECEIVE_NET_MSG + 1;
	public static final int MSG_NET_IS_DOWN = MSG_NET_IS_OK + 1;
	public static final int MSG_MOTOR_STOP = MSG_NET_IS_DOWN + 1;
	public static final int MSG_REFRESH_MAIN_VIEW = MSG_MOTOR_STOP + 1;
	public static final int MSG_RECEIVE_SERIAL_MSG = MSG_REFRESH_MAIN_VIEW + 1;
	public static final int MSG_LOW_BATTERY = MSG_RECEIVE_SERIAL_MSG + 1;
	public static final int MSG_TOAST_SHOW = MSG_LOW_BATTERY + 1;

	public static final int WINDOW_ID_1 = 0;
	public static final int WINDOW_ID_2 = WINDOW_ID_1 + 1;
	public static final int WINDOW_ID_3 = WINDOW_ID_2 + 1;
	public static final int WINDOW_ID_4 = WINDOW_ID_3 + 1;
	public static final int WINDOW_ID_5 = WINDOW_ID_4 + 1;
	public static final int WINDOW_ID_6 = WINDOW_ID_5 + 1;
	public static final int WINDOW_ID_7 = WINDOW_ID_6 + 1;
	public static final int WINDOW_ID_8 = WINDOW_ID_7 + 1;
	public static final int WINDOW_ID_9 = WINDOW_ID_8 + 1;

	public static final int LIGHT_ID_1 = 0;
	public static final int LIGHT_ID_2 = LIGHT_ID_1 + 1;
	public static final int LIGHT_ID_3 = LIGHT_ID_2 + 1;
	public static final int LIGHT_ID_4 = LIGHT_ID_3 + 1;

	public static final int WATER_ID_1 = 0;
	public static final int WATER_ID_2 = WATER_ID_1 + 1;
	public static final int WATER_ID_3 = WATER_ID_2 + 1;
	public static final int WATER_ID_4 = WATER_ID_3 + 1;
	public static final int WATER_ID_5 = WATER_ID_4 + 1;
	public static final int WATER_ID_6 = WATER_ID_5 + 1;
	public static final int[] WATER_ID_ALL = { WATER_ID_1, WATER_ID_2,
			WATER_ID_3, WATER_ID_4, WATER_ID_5, WATER_ID_6 };

	public static final int MOTOR_CONTROL_TYPE_OPEN = 10;
	public static final int MOTOR_CONTROL_TYPE_CLOSE = MOTOR_CONTROL_TYPE_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_STOP = MOTOR_CONTROL_TYPE_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_READ = MOTOR_CONTROL_TYPE_STOP + 10;
	public static final int MOTOR_CONTROL_TYPE_MODE = MOTOR_CONTROL_TYPE_READ + 10;
	public static final int MOTOR_CONTROL_TYPE_SYN = MOTOR_CONTROL_TYPE_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_ALARM = MOTOR_CONTROL_TYPE_SYN + 10;
	public static final int MOTOR_CONTROL_TYPE_TEST = MOTOR_CONTROL_TYPE_ALARM + 10;
	public static final int MOTOR_CONTROL_TYPE_PWD = MOTOR_CONTROL_TYPE_TEST + 10;
	public static final int MOTOR_CONTROL_TYPE_NOTICE = MOTOR_CONTROL_TYPE_PWD + 10;
	public static final int MOTOR_CONTROL_TYPE_SUPER_TEST = MOTOR_CONTROL_TYPE_NOTICE + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_READ = MOTOR_CONTROL_TYPE_SUPER_TEST + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_OPEN = MOTOR_CONTROL_TYPE_LIGHT_READ + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_CLOSE = MOTOR_CONTROL_TYPE_LIGHT_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_CONFIG = MOTOR_CONTROL_TYPE_LIGHT_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_READ = MOTOR_CONTROL_TYPE_WATER_CONFIG + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_OPEN = MOTOR_CONTROL_TYPE_WATER_READ + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_CLOSE = MOTOR_CONTROL_TYPE_WATER_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_MODE = MOTOR_CONTROL_TYPE_WATER_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_WATER_STATE = MOTOR_CONTROL_TYPE_WATER_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_SYN_TEMP = MOTOR_CONTROL_TYPE_WATER_STATE + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_CONFIG = MOTOR_CONTROL_TYPE_SYN_TEMP + 10;
	public static final int MOTOR_CONTROL_TYPE_BENG_OPEN = MOTOR_CONTROL_TYPE_LIGHT_CONFIG + 10;
	public static final int MOTOR_CONTROL_TYPE_BENG_CLOSE = MOTOR_CONTROL_TYPE_BENG_OPEN + 10;
	public static final int MOTOR_CONTROL_TYPE_LIGHT_MODE = MOTOR_CONTROL_TYPE_BENG_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_YAO_OPEN = MOTOR_CONTROL_TYPE_LIGHT_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_YAO_CLOSE = MOTOR_CONTROL_TYPE_YAO_OPEN + 10;

	public static final int MOTOR_CONTROL_TYPE_ONEKEY_OPEN = MOTOR_CONTROL_TYPE_YAO_CLOSE + 10;
	public static final int MOTOR_CONTROL_TYPE_ONEKEY_CLOSE = MOTOR_CONTROL_TYPE_ONEKEY_OPEN + 10;

	public static final int MOTOR_CONTROL_TYPE_READ_OTHERS = MOTOR_CONTROL_TYPE_ONEKEY_CLOSE + 10;
	/**
	 * 高温切自动
	 */
	public static final int MOTOR_CONTROL_TYPE_HIGH_MODE = MOTOR_CONTROL_TYPE_READ_OTHERS + 10;
	public static final int MOTOR_CONTROL_TYPE_RAIN_MODE = MOTOR_CONTROL_TYPE_HIGH_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE = MOTOR_CONTROL_TYPE_RAIN_MODE + 10;
	public static final int MOTOR_CONTROL_TYPE_OPEN_WIND_END = MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE + 10;

	public static final int MSG_TYPE_DATA = 0;
	public static final int MSG_TYPE_CID = MSG_TYPE_DATA + 1;

	public static final String LOCAL_DATA_ROOT_PATH = "/SmartFarm/";

	public static final int ALARM_CAN_NOT_READ_TEMP = 0;
	public static final int ALARM_TEMP_TOO_HIGH = ALARM_CAN_NOT_READ_TEMP + 1;
	public static final int ALARM_TEMP_TOO_LOW = ALARM_TEMP_TOO_HIGH + 1;
	public static final int ALARM_LINE_ERROR = ALARM_TEMP_TOO_LOW + 1;

	public static final int PROTOCOL_TYPE_REQUEST = 100;
	public static final int PROTOCOL_TYPE_RESPONSE = PROTOCOL_TYPE_REQUEST + 1;

	/**
	 * 操作：读取照度 明文协议：　“D5:001\r\n”
	 */
	public static final byte[] CMD_LIGHT_READ = new byte[] { (byte) 0x44,
			(byte) 0x35, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x31,
			(byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯1 明文协议：　“D2:001\r\n”
	 */
	/*
	 * public static final byte[] CMD_LIGHT_OPEN1 = new byte[] { (byte) 0x44,
	 * (byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte)
	 * 0x0d, (byte) 0x0a };
	 */
	public static final byte[] CMD_LIGHT_OPEN1 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯2 明文协议：　“D2:002\r\n”
	 */
	public static final byte[] CMD_LIGHT_OPEN2 = new byte[] { (byte) 0x44,
			(byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x32,
			(byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯3 明文协议：　“D2:003\r\n”
	 */
	public static final byte[] CMD_LIGHT_OPEN3 = new byte[] { (byte) 0x44,
			(byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x33,
			(byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯4 明文协议：　“D2:004\r\n”
	 */
	public static final byte[] CMD_LIGHT_OPEN4 = new byte[] { (byte) 0x44,
			(byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x34,
			(byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_LIGHT_OPEN_ALL = { CMD_LIGHT_OPEN1,
			CMD_LIGHT_OPEN2, CMD_LIGHT_OPEN3, CMD_LIGHT_OPEN4 };

	/**
	 * 操作：关闭补光灯1 明文协议：　“D3:001\r\n”
	 */
	/*
	 * public static final byte[] CMD_LIGHT_CLOSE1 = new byte[] { (byte) 0x44,
	 * (byte) 0x33, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte)
	 * 0x0d, (byte) 0x0a };
	 */
	public static final byte[] CMD_LIGHT_CLOSE1 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭补光灯2 明文协议：　“D3:002\r\n”
	 */
	public static final byte[] CMD_LIGHT_CLOSE2 = new byte[] { (byte) 0x44,
			(byte) 0x33, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x32,
			(byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭补光灯3 明文协议：　“D3:003\r\n”
	 */
	public static final byte[] CMD_LIGHT_CLOSE3 = new byte[] { (byte) 0x44,
			(byte) 0x33, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x33,
			(byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭补光灯4 明文协议：　“D3:004\r\n”
	 */
	public static final byte[] CMD_LIGHT_CLOSE4 = new byte[] { (byte) 0x44,
			(byte) 0x33, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x34,
			(byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_LIGHT_CLOSE_ALL = { CMD_LIGHT_CLOSE1,
			CMD_LIGHT_CLOSE2, CMD_LIGHT_CLOSE3, CMD_LIGHT_CLOSE4 };

	/**
	 * 操作：读取湿度 明文协议：　“G4:001\r\n”
	 */
	public static final byte[] CMD_WATER_READ1 = new byte[] { (byte) 0x47,
			(byte) 0x34, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x31,
			(byte) 0x0d, (byte) 0x0a };

	public static final byte[] CMD_WATER_485_READ = new byte[] { (byte) 0x01,
			(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02,
			(byte) 0xC4, (byte) 0x0B };

}
