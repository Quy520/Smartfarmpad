 package com.smartfarm.tools;

public interface AdjustTempTemplate {

	/**
	 * @param tempRange 温度所在范围，由上到下为0-4
	 * @param trend 温度趋势，-1为下降，0为平稳，1为上升
	 * @param windowId 窗口id
	 * @param percentage 当前窗口的已动作的时间
	 * @param currTemp 当前获取的温度值
	 */
	public void onReceiveTemp(int tempRange, int trend, int windowId, long percentage, int currTemp);
}
