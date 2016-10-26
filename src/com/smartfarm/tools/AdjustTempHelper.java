package com.smartfarm.tools;

import com.smartfarm.bean.RunningData;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.view.SimpleConfigManager;


/**
 * 自动控制温度辅助类
 * 
 * @author jeff
 *
 */
public class AdjustTempHelper {
	
	private static AdjustTempTemplate curr = new AdjustTempByInsulation();
	
	/**
	 * 设置自动温度控制调整策略
	 * 
	 * @param newTemplate
	 */
	public static void setAdjustTempTemplate(AdjustTempTemplate newTemplate) {
		
		curr = newTemplate;
	}

	/**
	 * 根据温度对窗口进行自动控制
	 * 
	 * @param recentTemp 该窗口的最近三次温度信息
	 * @param windowId 窗口id
	 */
	public static void adjust(int[] recentTemp, int windowId) {
		
		if (recentTemp == null || recentTemp.length < 3)
			return;
		
		curr.onReceiveTemp(getTempRange(recentTemp[0], windowId), getTrent(recentTemp), windowId, 
				RunningData.getInstance().getWindowState(windowId), recentTemp[0]);
	}
	
	/**
	 * 获取温度所在温度区间
	 * 
	 * @param temp 温度值
	 * @return 区间编号
	 */
	private static int getTempRange(int temp, int windowId) {
		
		TempConfig config = SimpleConfigManager.getInstance().getTempModel().getConfig(windowId);
		
		if(temp >= config.getMax()) 
			return 0;
		else if(temp >= config.getNorMax())
			return 1;
		else if(temp >= config.getNorMin())
			return 2;
		else if(temp >= config.getMin())
			return 3;
		else
			return 4;
	}
	
	/**
	 * 根据三次温度获取温度趋势
	 * 
	 * @param recentTemp 温度信息
	 * @return 1表示上升，0表示不变，-1表示下降
	 */
	private static int getTrent(int[] recentTemp) {
		
		int res = 0;
		
		if(recentTemp[2] < recentTemp[1])
			res += 20;
		else if(recentTemp[2] == recentTemp[1])
			res += 10;

		if(recentTemp[1] < recentTemp[0])
			res += 2;
		else if(recentTemp[1] == recentTemp[0])
			res += 1;
		
		if(res == 11 || res == 2 || res == 20)
			return 0;
		else if(res == 12 || res == 22 || res == 21)
			return 1;
		else
			return -1;
	}
}
