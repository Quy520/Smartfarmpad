package com.smartfarm.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.SimpleConfigManager;

public class Protocol {
	public static final String SEPARATOR = "-";

	private long time = 0;
	private String sender = "null";
	private String receiver = "null";
	private int protocolType = 0;
	private int cmdType = 0;
	private String windowId = "null";
	private String data = "null";
	private String pwd = "null";

	public Protocol() {

		sender = AppContext.context().getPadNum();
		receiver = SimpleConfigManager.getInstance().getReceiver();
	}

	public Protocol(Protocol protocol) {

		time = protocol.getTime();
		sender = protocol.getSender();
		receiver = protocol.getReceiver();
		protocolType = protocol.getProtocolType();
		cmdType = protocol.getCmdType();
		windowId = protocol.getWindowId();
		data = protocol.getData();
		pwd = protocol.getPwd();
	}

	public void send() {

		if (ShowUtil.isEmpty(sender) || ShowUtil.isEmpty(receiver))
			return;

		MsgSendHelper.send(this);
	}

	public String getProtocolString() {
		StringBuilder res = new StringBuilder();
		res.append(time);
		res.append(SEPARATOR);
		res.append(sender);
		res.append(SEPARATOR);
		res.append(receiver);
		res.append(SEPARATOR);
		res.append(protocolType);
		res.append(SEPARATOR);
		res.append(cmdType);
		res.append(SEPARATOR);
		res.append(windowId);
		res.append(SEPARATOR);
		res.append(data);
		res.append(SEPARATOR);
		res.append(pwd);

		return res.toString();
	}

	@Override
	public boolean equals(Object o) {

		if (Protocol.class.isInstance(o)) {
			Protocol other = (Protocol) o;

			if (other.getTime() == time)
				return true;
			else
				return false;

		} else
			return false;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	public int getCmdType() {
		return cmdType;
	}

	public void setCmdType(int cmdType) {
		this.cmdType = cmdType;
	}

	public String getWindowId() {
		return windowId;
	}

	public List<Integer> getWindowIds() {
		String[] ids = windowId.split(";");

		List<Integer> res = new ArrayList<Integer>(ids.length);

		try {
			for (int i = 0; i < ids.length; i++) {
				res.add(Integer.parseInt(ids[i]));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return res;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDescr() {
		StringBuilder result = new StringBuilder();

		result.append("用户");
		result.append(sender);
		switch (cmdType) {
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_CLOSE:
			result.append("关闭补光灯");
			result.append(windowId);
			break;
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_OPEN:
			result.append("打开补光灯");
			result.append(windowId);
			break;
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_READ:
			result.append("读取照度信息");
			break;
		case Constants.MOTOR_CONTROL_TYPE_CLOSE:
			result.append("关闭窗口");
			result.append(windowId);
			break;
		case Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE:
			result.append("关闭电磁阀");
			result.append(windowId);
			break;
		case Constants.MOTOR_CONTROL_TYPE_WATER_OPEN:
			result.append("打开电磁阀");
			result.append(windowId);
			break;
		case Constants.MOTOR_CONTROL_TYPE_WATER_CONFIG:
			result.append("同步水肥一体化设置参数");
			break;
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_CONFIG:
			result.append("同步照度管理设置参数");
			break;
		case Constants.MOTOR_CONTROL_TYPE_WATER_MODE:
			result.append("变更水肥一体化控制模式为");
			result.append(data.contains("auto") ? "自动模式" : "手动模式");
			break;
		case Constants.MOTOR_CONTROL_TYPE_LIGHT_MODE:
			result.append("变更照度管理控制模式为");
			result.append(data.contains("auto") ? "自动模式" : "手动模式");
			break;
		case Constants.MOTOR_CONTROL_TYPE_WATER_READ:
			result.append("读取湿度信息");
			break;
		case Constants.MOTOR_CONTROL_TYPE_MODE:
			result.append("调整模式");
			result.append(data.equals("auto") ? "为自动" : "为手动");
			break;
		case Constants.MOTOR_CONTROL_TYPE_HIGH_MODE:
			result.append("调整高温切自动模式");
			result.append(data.equals("auto") ? "自动切换" : "不切换");
			break;
		case Constants.MOTOR_CONTROL_TYPE_OPEN:
			if (windowId.contains("ven")) {
				result.append("开窗通风");
			} else {
				result.append("打开窗口");
				result.append(windowId);
			}
			break;
		case Constants.MOTOR_CONTROL_TYPE_READ:
			result.append("读取温度");
			break;
		case Constants.MOTOR_CONTROL_TYPE_STOP:
			result.append("停止窗口");
			result.append(windowId);
			break;
		case Constants.MOTOR_CONTROL_TYPE_TEST:
			result.append("测试连接");
			break;
		case Constants.MOTOR_CONTROL_TYPE_SYN:
			result.append("同步数据");
			break;
		case Constants.MOTOR_CONTROL_TYPE_ALARM:
			result.append("收到温度报警");
			break;

		}

		result.append(" --- ");
		result.append(CommonTool.getDateString(new Date(time)));

		return result.toString();
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
}
