package com.smartfarm.bean;

import java.io.Serializable;

import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;
/**
 * 
 * 手机设备信息对象
 * @author Lei
 *
 */
public class DeviceInfoBean implements Serializable {

	private static final long serialVersionUID = -6230352953647331423L;
	
	private int id;
	private String name;
	private String num;
	private String phone1;
	private String phone2;
	private String phone3;
	private int tempMax;
	private int tempMaxDiff;
	private int tempMinDiff;
	private int tempMin;
	private int lenMax;
	private int len1;
	private int len2;
	private int lenFree;
	private int venLen;
	private String nightStart;
	private String nightEnd;
	private int pushTime;
	private int deviceType;
	private int alarmMax;
	private int alarmMin;
	private int alarmMaxEnable;
	private int alarmMinEnable;
	private String remark;

	public DeviceInfoBean() {
		
		ConfigModel cfg = SimpleConfigManager.getInstance().getConfig();
		TempConfig tcg = SimpleConfigManager.getInstance().getTempModel().getConfig(0);
		
		num = AppContext.context().getPadNum();
		phone1 = cfg.getPhonenumber();
		phone2 = cfg.getPhonenumber2();
		phone3 = cfg.getPhonenumber3();
		lenMax = cfg.getOpenLen();
		len1 = cfg.getOpenLenFirst();
		len2 = cfg.getOpenLenSecond();
		lenFree = cfg.getOpenLenFifth();
		venLen = cfg.getVenTime();
		nightStart = cfg.getMorningOpenTime();
		nightEnd = cfg.getNightCloseTime();
		pushTime = cfg.getPushTime();
		deviceType = 2;
		alarmMax = cfg.getAlarmMax();
		alarmMin = cfg.getAlarmMin();
		alarmMaxEnable = cfg.isHighAlarmEnable() ? 1 : 0;
		alarmMinEnable = cfg.isLowAlarmEnable() ? 1 : 0;
		remark = "null";
		tempMax = tcg.getMax();
		tempMaxDiff = tcg.getNorMax();
		tempMin = tcg.getMin();
		tempMinDiff = tcg.getNorMin();
	}

	public int getId() {
		return id;
	}
	

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public int getTempMax() {
		return tempMax;
	}

	public void setTempMax(int tempMax) {
		this.tempMax = tempMax;
	}

	public int getTempMaxDiff() {
		return tempMaxDiff;
	}

	public void setTempMaxDiff(int tempMaxDiff) {
		this.tempMaxDiff = tempMaxDiff;
	}

	public int getTempMinDiff() {
		return tempMinDiff;
	}

	public void setTempMinDiff(int tempMinDiff) {
		this.tempMinDiff = tempMinDiff;
	}

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public int getLenMax() {
		return lenMax;
	}

	public void setLenMax(int lenMax) {
		this.lenMax = lenMax;
	}


	public int getVenLen() {
		return venLen;
	}

	public void setVenLen(int venLen) {
		this.venLen = venLen;
	}

	public String getNightStart() {
		return nightStart;
	}

	public void setNightStart(String nightStart) {
		this.nightStart = nightStart;
	}

	public String getNightEnd() {
		return nightEnd;
	}

	public void setNightEnd(String nightEnd) {
		this.nightEnd = nightEnd;
	}

	public int getPushTime() {
		return pushTime;
	}

	public void setPushTime(int pushTime) {
		this.pushTime = pushTime;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
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

	public int getAlarmMaxEnable() {
		return alarmMaxEnable;
	}

	public void setAlarmMaxEnable(int alarmMaxEnable) {
		this.alarmMaxEnable = alarmMaxEnable;
	}

	public int getAlarmMinEnable() {
		return alarmMinEnable;
	}

	public void setAlarmMinEnable(int alarmMinEnable) {
		this.alarmMinEnable = alarmMinEnable;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getLen1() {
		return len1;
	}

	public void setLen1(int len1) {
		this.len1 = len1;
	}

	public int getLen2() {
		return len2;
	}

	public void setLen2(int len2) {
		this.len2 = len2;
	}

	public int getLenFree() {
		return lenFree;
	}

	public void setLenFree(int lenFree) {
		this.lenFree = lenFree;
	}
	
	

}
