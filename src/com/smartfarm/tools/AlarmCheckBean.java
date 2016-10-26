package com.smartfarm.tools;

public class AlarmCheckBean {
	public final int errorType;
	public final int windowId;
	public final boolean isFirst;
	
	public AlarmCheckBean(int errorType, int windowId, boolean isFirst){
		this.errorType = errorType;
		this.windowId = windowId;
		this.isFirst = isFirst;
	}
}
