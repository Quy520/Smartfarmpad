package com.smartfarm.db.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@DatabaseTable(tableName = "temp_config")
public class TempConfig implements Serializable {

	private static final long serialVersionUID = -8646975381793381420L;

    @DatabaseField(generatedId = true) 
	private int id;

	@XStreamAlias("window_id")
    @DatabaseField(columnName = "window_id") 
	private int windowId;

	@XStreamAlias("max")
    @DatabaseField(columnName = "max") 
	private int max;

	@XStreamAlias("nor_max")
    @DatabaseField(columnName = "nor_max") 
	private int norMax;

	@XStreamAlias("nor_min")
    @DatabaseField(columnName = "nor_min") 
	private int norMin;

	@XStreamAlias("min")
    @DatabaseField(columnName = "min") 
	private int min;

    @DatabaseField(columnName = "remark") 
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWindowId() {
		return windowId;
	}

	public void setWindowId(int windowId) {
		this.windowId = windowId;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getNorMax() {
		return norMax;
	}

	public void setNorMax(int norMax) {
		this.norMax = norMax;
	}

	public int getNorMin() {
		return norMin;
	}

	public void setNorMin(int norMin) {
		this.norMin = norMin;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
