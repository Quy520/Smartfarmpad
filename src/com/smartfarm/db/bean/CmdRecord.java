package com.smartfarm.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "cmd_record")
public class CmdRecord {

    @DatabaseField(generatedId = true) 
	private int id;
	
    @DatabaseField(columnName = "control_type") 
	private int controlType;
	
    @DatabaseField(columnName = "driver_res") 
	private int driverRes;
	
    @DatabaseField(columnName = "window_id") 
	private int windowId;
	
    @DatabaseField(columnName = "time") 
	private long time;
	
    @DatabaseField(columnName = "source") 
	private int source;
	
    @DatabaseField(columnName = "syn") 
	private boolean syn = false;
	
    @DatabaseField(columnName = "remark") 
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getControlType() {
		return controlType;
	}

	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	public int getDriverRes() {
		return driverRes;
	}

	public void setDriverRes(int driverRes) {
		this.driverRes = driverRes;
	}

	public int getWindowId() {
		return windowId;
	}

	public void setWindowId(int windowId) {
		this.windowId = windowId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public boolean isSyn() {
		return syn;
	}

	public void setSyn(boolean syn) {
		this.syn = syn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
