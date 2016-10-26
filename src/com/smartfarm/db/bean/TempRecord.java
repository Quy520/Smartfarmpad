package com.smartfarm.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "temp_record")
public class TempRecord {

    @DatabaseField(generatedId = true) 
	private int id;
	
    @DatabaseField(columnName = "temp") 
	private float temp;
	
    @DatabaseField(columnName = "window_id") 
	private int windowId;
	
    @DatabaseField(columnName = "time") 
	private long time;
	
    @DatabaseField(columnName = "source") 
	private int source;
	
    @DatabaseField(columnName = "syn") 
	private boolean syn = false;
	
    @DatabaseField(columnName = "count") 
	private int count;

    @DatabaseField(columnName = "consum") 
    private int consum;
	
    @DatabaseField(columnName = "remark") 
	private String remark;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getConsum() {
		return consum;
	}

	public void setConsum(int consum) {
		this.consum = consum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		this.temp = temp;
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
