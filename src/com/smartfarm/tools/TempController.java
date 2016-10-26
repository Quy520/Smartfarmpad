package com.smartfarm.tools;

import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.receiver.BatteryBroadcastReceiver;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;

/**
 * 温度管理控制器
 * 
 * @author jeff
 *
 */
public class TempController {
	private int windowId = -1;
	
	private float tempFIFO[] = new float[Constants.FIFO_LENGTH]; 
	private boolean[] okFlag = new boolean[Constants.FIFO_LENGTH];
	private int firstCount = 0;
	
	public TempController(int windowId) {
		this.windowId = windowId;
		
		for(int i = 0; i < Constants.FIFO_LENGTH; i++) {
			okFlag[i] = true;
		}
	}
	
	/**
	 * 获取最近的一个温度值
	 * 
	 * @return
	 */
	public float getTempRecordRecently() {
		return tempFIFO[0];
	}
	
	/**
	 * 获取最近的一个工作状态
	 * 
	 * @return
	 */
	public boolean getWorkingStatus() {
		return okFlag[0];
	}
	
	/**
	 * 获取所有的温度信息
	 * 
	 * @return
	 */
	public float[] getTempRecord() {
		return tempFIFO;
	}
	
	public boolean isFirstError() {
		
		if(firstCount <= 0 || firstCount > 3)
			return false;
		else
			return true;
	}
	
	/**
	 * 设置工作状态
	 * 
	 * @param status
	 */
	public void saveWorkingStatus(boolean status) {
		
		if(!status) {
			firstCount++;
		} else {
			firstCount = 0;
		}
		
		for(int i = (Constants.FIFO_LENGTH - 1); i > 0; i--) {
			okFlag[i] = okFlag[i - 1];
		}
			
		okFlag[0] = status;
	}
	
	/**
	 * 处理温度
	 * 
	 * @param temp 温度值
	 * @param needAutoChange 是否用于自动控制
	 */
	public void dealWithTemp(float temp, boolean needAutoChange) {
		
		for(int i = (Constants.FIFO_LENGTH - 1); i > 0; i--) {
			tempFIFO[i] = tempFIFO[i - 1];
		}
			
		tempFIFO[0] = temp;
		boolean charing = BatteryBroadcastReceiver.isCharging;
		
//		try {
//			
//			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
//			Intent batteryStatus = AppContext.context().registerReceiver(null, ifilter);
//			int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//			charing = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
//		} catch(Exception e) {}

		if (needAutoChange && Alarm.isShouldAlarm() && charing) {
			
			ConfigModel cm = SimpleConfigManager.getInstance().getConfig();
			if(temp > cm.getAlarmMax() && cm.isAutoOpenEnable() && !cm.isAutoOrManual()) {
				
				ProtocolFactory.getNoticeProtocol("autochange").send();
				SimpleConfigManager.getInstance().modeChange(SerialControlRunable.SOURCE_AUTO, true);
			} else if(cm.isAutoOrManual()) {
				
				//根据温度自动控制窗口
				AdjustTempHelper.adjust(getRecentTempIntForCompare(), windowId);
			}
			
		}
	}
	
	/**
	 * 获取最近的三个有效温度值
	 * 
	 * @return 最近的三个有效温度值，有效数目少于三个则带有无效值
	 */
	public float[] getRecentTemp() {
		float[] compareTemp = new float[3]; 

		int index = 0;
		for(int i = 0; i < compareTemp.length; i++) {
			
			while(index < Constants.FIFO_LENGTH) {
				
				if(tempFIFO[index] != 0) {
					compareTemp[i] = tempFIFO[index];
					index++;
					break;
				} 

				index++;
			}
		}
		
		return compareTemp;
	}
	
	/**
	 * 获取最近的三个有效温度值
	 * 
	 * @return 最近的三个有效温度值，有效数目少于三个则返回null
	 */
	public float[] getRecentTempForCompare() {
		float[] compareTemp = getRecentTemp(); 
		
		if(compareTemp[0] > 0 && compareTemp[1] > 0 && compareTemp[2] > 0)
			return compareTemp;
		
		return null;
	}
	
	/**
	 * 获取最近的三个有效整型温度值，数值四舍五入
	 * 
	 * @return 最近的三个有效温度值，有效数目少于三个则带有无效值
	 */
	public int[] getRecentTempIntForCompare() {
		float[] compareTemp = getRecentTemp();
		int[] res = new int[3];
		
		if(compareTemp[0] > 0 && compareTemp[1] > 0 && compareTemp[2] > 0) {
			res[0] = Math.round(compareTemp[0]);
			res[1] = Math.round(compareTemp[1]);
			res[2] = Math.round(compareTemp[2]);
			
			return res;
		}
		
		return null;
	}
	
	/**
	 * 获取最近的三个有效整型温度值，数值四舍五入
	 * 
	 * @return 最近的三个有效温度值，有效数目少于三个则返回null
	 */
	public int[] getRecentTempInt() {
		float[] compareTemp = getRecentTemp();
		int[] res = new int[3];
		
		res[0] = Math.round(compareTemp[0]);
		res[1] = Math.round(compareTemp[1]);
		res[2] = Math.round(compareTemp[2]);
		
		return res;
	}
}
