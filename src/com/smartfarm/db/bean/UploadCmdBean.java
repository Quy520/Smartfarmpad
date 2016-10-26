package com.smartfarm.db.bean;

import java.util.List;

public class UploadCmdBean {
	
	private int deviceId;
	private List<CmdRecord> cmds;
	
	public UploadCmdBean() {}
	
	public UploadCmdBean(int deviceId, List<CmdRecord> cmds) {

		this.cmds = cmds;
		this.deviceId = deviceId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public List<CmdRecord> getCmds() {
		return cmds;
	}

	public void setCmds(List<CmdRecord> cmds) {
		this.cmds = cmds;
	}
	
	
}
