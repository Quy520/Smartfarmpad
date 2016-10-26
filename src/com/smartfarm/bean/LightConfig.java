package com.smartfarm.bean;

import com.smartfarm.view.SimpleConfigManager;

public class LightConfig {

	private int minValue;
	private int maxValue;
	private int timeCount;
	private int needTime;
	private int diyTime;
	private int count;
	private long lastTime;
	private String startTime;
	private boolean lightMode;

	public boolean isLightMode() {
		return lightMode;
	}

	public void setLightMode(boolean lightMode) {
		this.lightMode = lightMode;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getTimeCount() {
		return timeCount;
	}

	public void setTimeCount(int timeCount) {
		this.timeCount = timeCount;
	}

	public int getNeedTime() {
		return needTime;
	}

	public void setNeedTime(int needTime) {
		this.needTime = needTime;
	}

	public int getDiyTime() {
		return diyTime;
	}

	public void setDiyTime(int diyTime) {
		this.diyTime = diyTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public void add() {
		
		timeCount++;
		lastTime = System.currentTimeMillis();

		SimpleConfigManager.getInstance().putInt(SimpleConfigManager.KEY_LIGHT_CURR, timeCount);
		SimpleConfigManager.getInstance().putLong(SimpleConfigManager.KEY_LIGHT_CURR_TIME, lastTime);
	}
	
	public void clear() {
		
		timeCount = 0;
		lastTime = System.currentTimeMillis();

		SimpleConfigManager.getInstance().putInt(SimpleConfigManager.KEY_LIGHT_CURR, timeCount);
		SimpleConfigManager.getInstance().putLong(SimpleConfigManager.KEY_LIGHT_CURR_TIME, lastTime);
	}
	
	public String getTimeDescr() {
		
		StringBuilder res = new StringBuilder();
		int h = timeCount / 60;
		 
		if(h < 10) {
			res.append(0);
			res.append(h);
		} else {
			res.append(h);
		}

		res.append(":");
		int min = timeCount % 60;
		
		if(min < 10) {
			res.append(0);
			res.append(min);
		} else {
			res.append(min);
		}
		
		return res.toString();
	}

}
