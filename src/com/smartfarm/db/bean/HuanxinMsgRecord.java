package com.smartfarm.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "huanxin_msg")
public class HuanxinMsgRecord {

    @DatabaseField(generatedId = true) 
	private int id;
	
    @DatabaseField(columnName = "time") 
	private long time = System.currentTimeMillis();
	
    @DatabaseField(columnName = "msg") 
	private String msg;
	
    @DatabaseField(columnName = "remark") 
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
