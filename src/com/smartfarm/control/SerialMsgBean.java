package com.smartfarm.control;

public class SerialMsgBean {
	private int type;
	private int windowId;
	private String data;
	private boolean vaild = false;
	
	public SerialMsgBean(int type, int windowId, String data) {
		this.type = type;
		this.data = data;
		this.windowId = windowId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getWindowId() {
		return windowId;
	}

	public void setWindowId(int windowId) {
		this.windowId = windowId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isVaild() {
		return vaild;
	}

	public void setVaild(boolean vaild) {
		this.vaild = vaild;
	}
	
	
}
