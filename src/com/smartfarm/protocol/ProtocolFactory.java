package com.smartfarm.protocol;

import com.smartfarm.bean.RunningData;
import com.smartfarm.tools.Constants;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;

public class ProtocolFactory {
	public static Protocol getReadTempProtocol() {
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_READ);

		return protocol;
	}

	public static Protocol getNoticeProtocol(String notice) {
		Protocol protocol = new Protocol();
		
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_NOTICE);
		protocol.setData(notice);                                                                                                                                                                 

		return protocol;
	}

	public static Protocol getPwdErrorProtocol(String receiver) {
		Protocol protocol = new Protocol();

		protocol.setReceiver(receiver);
		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_PWD);

		return protocol;
	}

	public static Protocol getNoticeModeProtocol() {
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_MODE);
		protocol.setData(SimpleConfigManager.getInstance().getConfig()
				.isAutoOrManual() ? "auto" : "no");

		return protocol;
	}
	
	// 高温转自动的消息
	public static Protocol getNoticeHighModeProtocol() {
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_HIGH_MODE);
		protocol.setData(SimpleConfigManager.getInstance().getConfig()
				.isAutoOpenEnable() ? "auto" : "no");
		return protocol;
	}
	
	public static Protocol OpenWindEndProtocol(){
		
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_OPEN_WIND_END);
		protocol.setData("stop");
		return protocol;
	}

	// 下雨关风
	public static Protocol getRainModeProtocol(String state) {

		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_RAIN_MODE);
		protocol.setData(state);
		 
		return protocol;
	}

	// 一键放风
	public static Protocol getOpenWindowsModeProtocol(String state) {
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE);
		protocol.setData(state);
		return protocol;
	}
	//一键获取湿度
	public static Protocol getNoticeWaterModeProtocol() {
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_MODE);
		protocol.setData(SimpleConfigManager.getInstance().getWaterModel()
				.isMode() ? "auto" : "no");

		return protocol;
	}

	public static Protocol getTestRequestProtocol(int phoneId) {
		Protocol protocol = new Protocol();
		ConfigModel config = SimpleConfigManager.getInstance().getConfig();

		switch (phoneId) {
		case 1:
			if (config.getPhonenumber().equals(""))
				return null;
			protocol.setReceiver(config.getPhonenumber());
			break;
		case 2:
			if (config.getPhonenumber2().equals(""))
				return null;
			protocol.setReceiver(config.getPhonenumber2());
			break;
		case 3:
			if (config.getPhonenumber3().equals(""))
				return null;
			protocol.setReceiver(config.getPhonenumber3());
			break;
		}

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_TEST);

		return protocol;
	}
	//获取警告
	public static Protocol getAlarmProtocol(String alarm, String date) {
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_REQUEST);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_ALARM);
		protocol.setData(alarm);
		protocol.setWindowId(date);

		return protocol;
	}

	public static Protocol getTestProtocol() {
		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_TEST);

		return protocol;
	}

	public static Protocol getTestProtocolToSelf() {

		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_TEST);
		protocol.setReceiver(AppContext.context().getPadNum());
		protocol.setPwd(SimpleConfigManager.getInstance().getString("pwd"));

		return protocol;
	}

	public static Protocol getWaterInfoProtocol() {

		Protocol protocol = new Protocol();

		protocol.setTime(System.currentTimeMillis());
		protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
		protocol.setCmdType(Constants.MOTOR_CONTROL_TYPE_WATER_READ);
		protocol.setData(RunningData.getInstance().getWaterInfo());

		return protocol;
	}

	public static Protocol GetProtocol(String protocol) {
		Protocol res = new Protocol();

		if (protocol.contains("-")) {
			String[] data = protocol.split("-", 8);

			if (data.length < 8)
				return null;

			try {
				res.setTime(Long.parseLong(data[0]));
				res.setSender(data[1]);
				res.setReceiver(data[2]);
				res.setProtocolType(Integer.parseInt(data[3]));
				res.setCmdType(Integer.parseInt(data[4]));
				res.setWindowId(data[5]);
				res.setData(data[6]);
				res.setPwd(data[7]);
				

				return res;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}
}
