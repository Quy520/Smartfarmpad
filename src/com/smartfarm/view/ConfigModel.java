package com.smartfarm.view;

import java.util.List;
/**
 * 
 * @author QSD
 * 配置控制信息
 *
 */
public class ConfigModel {
	private boolean firstTime; // 是否第一运行程序
	private String name = "GreenHouse";
	
	private int windowCount = 0;//窗口数量
	private int mintuesCount=0; //发短信时间间隔
	private boolean AutoOrManual; // 自动开窗 还是 手动开窗
	private boolean highAlarmEnable;
	private boolean lowAlarmEnable;
	private boolean autoOpenEnable;
	private boolean isAlarmEnable;	//短信报警是否开启
	private String morningOpenTime; // 早晨放风时间
	private String nightCloseTime; // 晚上关窗时间
	private String phonenumber; // 主电话号码
	private String phonenumber2; // 从2电话号码
	private String phonenumber3; // 从3电话号码
	private int alarmMax;
	private int alarmMin;
	private int pushTime;
	private int openLen;
	private int openLenFirst;
	private int openLenSecond;
	private int openLenThird;
	private int openLenFourth;
	
/*	private boolean isRain;
	private boolean isWindows;*/
	
	public boolean isAlarmEnable() {
		return isAlarmEnable;
	}

	public void setAlarmEnable(boolean isAlarmEnable) {
		this.isAlarmEnable = isAlarmEnable;
	}
	
	public int getMintuesCount() {
		return mintuesCount;
	}

	public void setMintuesCount(int mintuesCount) {
		this.mintuesCount = mintuesCount;
	}

	private int openLenFifth;
	private int venTime;
	private List<Integer> allStalls;
	
	private boolean waterEnable;
	private boolean lightEnable;
	
	private String bluetoothmac;	//控制机 蓝牙的mac地址

	public String getBluetoothmac() {
		return bluetoothmac;
	}

	public void setBluetoothmac(String bluetoothmac) {
		this.bluetoothmac = bluetoothmac;
	}

	public boolean isAutoOpenEnable() {
		return autoOpenEnable;
	}

	public void setAutoOpenEnable(boolean autoOpenEnable) {
		this.autoOpenEnable = autoOpenEnable;
	}

	public boolean isWaterEnable() {
		return waterEnable;
	}

	public void setWaterEnable(boolean waterEnable) {
		this.waterEnable = waterEnable;
	}

	public boolean isLightEnable() {
		return lightEnable;
	}

	public void setLightEnable(boolean lightEnable) {
		this.lightEnable = lightEnable;
	}

	public int getVenTime() {
		return venTime;
	}

	public void setVenTime(int venTime) {
		this.venTime = venTime;
	}

	public int getOpenLenSecond() {
		return openLenSecond;
	}

	public void setOpenLenSecond(int openLenSecond) {
		this.openLenSecond = openLenSecond;
	}

	public int getOpenLenThird() {
		return openLenThird;
	}

	public void setOpenLenThird(int openLenThird) {
		this.openLenThird = openLenThird;
	}

	public int getOpenLenFourth() {
		return openLenFourth;
	}

	public void setOpenLenFourth(int openLenFourth) {
		this.openLenFourth = openLenFourth;
	}

	public int getOpenLenFifth() {
		return openLenFifth;
	}

	public void setOpenLenFifth(int openLenFifth) {
		this.openLenFifth = openLenFifth;
	}

	public boolean isHighAlarmEnable() {
		return highAlarmEnable;
	}

	public void setHighAlarmEnable(boolean highAlarmEnable) {
		this.highAlarmEnable = highAlarmEnable;
	}

	public boolean isLowAlarmEnable() {
		return lowAlarmEnable;
	}

	public void setLowAlarmEnable(boolean lowAlarmEnable) {
		this.lowAlarmEnable = lowAlarmEnable;
	}

	public List<Integer> getAllStalls() {
		return allStalls;
	}

	public void setAllStalls(List<Integer> allStalls) {
		this.allStalls = allStalls;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
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

	public int getPushTime() {
		return pushTime;
	}

	public void setPushTime(int pushTime) {
		this.pushTime = pushTime;
	}

	public String getName() {
		return name;
	}

	public int getOpenLen() {
		return openLen;
	}

	public void setOpenLen(int openLen) {
		this.openLen = openLen;
	}

	public int getOpenLenFirst() {
		return openLenFirst;
	}

	public void setOpenLenFirst(int openLenFirst) {
		this.openLenFirst = openLenFirst;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAutoOrManual() {
		return AutoOrManual;
	}

	public void setModeChange(boolean isAuto) {
		AutoOrManual = isAuto;
	}

	public String getMorningOpenTime() {
		return morningOpenTime;
	}

	public void setMorningOpenTime(String morningOpenTime) {
		this.morningOpenTime = morningOpenTime;
	}

	public String getNightCloseTime() {
		return nightCloseTime;
	}

	public void setNightCloseTime(String nightCloseTime) {
		this.nightCloseTime = nightCloseTime;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPhonenumber2() {
		return phonenumber2;
	}

	public void setPhonenumber2(String phonenumber2) {
		this.phonenumber2 = phonenumber2;
	}

	public String getPhonenumber3() {
		return phonenumber3;
	}

	public void setPhonenumber3(String phonenumber3) {
		this.phonenumber3 = phonenumber3;
	}

	public int getWindowCount() {
		return windowCount;
	}

	public void setWindowCount(int windowCount) {
		this.windowCount = windowCount;
	}
}
