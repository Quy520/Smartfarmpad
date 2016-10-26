package com.smartfarm.bean;

public class WaterConfig {

	private int min;	//湿度阀值
	private int time;	//滴灌时间
	private int alarmMax;	//湿度最大值警报
	private int alarmMin;	//湿度最小值警报
	private int waterCount;	//电磁阀数量
	private boolean alarmMaxEnable;	//是否开启最大值警报
	private boolean alarmMinEnable;	//是否开启最小值警报
	private boolean mode;	//自动模式是否启用

	public boolean isMode() {
		return mode;
	}

	public void setMode(boolean mode) {
		this.mode = mode;
	}

	public int getWaterCount() {
		return waterCount;
	}

	public void setWaterCount(int waterCount) {
		this.waterCount = waterCount;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getAlarmMax() {
		return alarmMax;
	}

	public void setAlarmMax(int alarmMax) {
		this.alarmMax = alarmMax;
	}

	public int getAlarmMin() {
		return alarmMin;
	}
	
	
	public void setAlarmMin(int alarmMin) {
		this.alarmMin = alarmMin;
	}

	public boolean isAlarmMaxEnable() {
		return alarmMaxEnable;
	}

	public void setAlarmMaxEnable(boolean alarmMaxEnable) {
		this.alarmMaxEnable = alarmMaxEnable;
	}
	

	public boolean isAlarmMinEnable() {
		return alarmMinEnable;
	}

	public void setAlarmMinEnable(boolean alarmMinEnable) {
		this.alarmMinEnable = alarmMinEnable;
	}

}
