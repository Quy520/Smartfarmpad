package com.smartfarm.db.bean;

import java.util.List;

public class UploadTempBean {
	private int deviceId;
	private List<TempRecord> temps;

	public UploadTempBean() {}

	public UploadTempBean(int deviceId, List<TempRecord> temps) {
		
		this.deviceId = deviceId;
		this.temps = temps;
	}
	
	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public List<TempRecord> getTemps() {
		return temps;
	}

	public void setTemps(List<TempRecord> temps) {
		this.temps = temps;
	}
}
