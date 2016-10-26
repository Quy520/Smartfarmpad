package com.smartfarm.tools;

public class AlarmModel {
	private long time;
	private String alarmString;
	private int errorType;
	private int windowId;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	public AlarmModel() {
		time = System.currentTimeMillis();
	}

	public AlarmModel(long time) {
		this.time = time;
	}

	public AlarmModel(int errorType, int windowId) {
		this();
		this.errorType = errorType;
		this.windowId = windowId;
	}

	public String getAlarmString() {
		return alarmString;
	}


	public void setAlarmString(String alarmString) {
		this.alarmString = alarmString;
	}


	public int getErrorType() {
		return errorType;
	}


	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}


	public int getWindowId() {
		return windowId;
	}


	public void setWindowId(int windowId) {
		this.windowId = windowId;
	}

	@Override
	public boolean equals(Object o) {

		if(AlarmModel.class.isInstance(o)) {
			if(((AlarmModel)o).time == time)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "alarm(" + errorType + ") -> " + alarmString;
	}
}
