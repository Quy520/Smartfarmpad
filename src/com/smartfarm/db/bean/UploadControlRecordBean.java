package com.smartfarm.db.bean;

import java.util.List;

public class UploadControlRecordBean {
	
	private int deviceId;
	private List<ControlRecord> controls;
	
	public UploadControlRecordBean() {}
	
	public UploadControlRecordBean(int deviceId, List<ControlRecord> controls) {

		this.controls = controls;
		this.deviceId = deviceId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public List<ControlRecord> getControls() {
		return controls;
	}

	public void setControls(List<ControlRecord> controls) {
		this.controls = controls;
	}

	
}
