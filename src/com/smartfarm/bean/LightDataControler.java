package com.smartfarm.bean;

import com.smartfarm.tools.Constants;

/**
 * 照度管理
 * 
 * @author jeff
 *
 */
public class LightDataControler {
	
	private int fifo[] = new int[Constants.FIFO_LENGTH]; 
	private boolean[] okFlag = new boolean[Constants.FIFO_LENGTH];

	public LightDataControler() {
		
		for(int i = 0; i < Constants.FIFO_LENGTH; i++) {
			okFlag[i] = true;
		}
		
		for(int i = 0; i < Constants.FIFO_LENGTH; i++) {
			fifo[i] = 0;
		}
	}
	
	/**
	 * 获取最近的一个照度
	 * 
	 * @return
	 */
	public int getRecently() {
		return fifo[0];
	}
	
	/**
	 * 获取最近的一个工作状态
	 * 
	 * @return
	 */
	public boolean getRecentlyState() {
		return okFlag[0];
	}
	
	/**
	 * 获取全部
	 * 
	 * @return
	 */
	public int[] getAll() {
		return fifo;
	}
	
	/**
	 * 保存
	 * 
	 * @param value
	 */
	public void save(int value) {
		
		for(int i = (Constants.FIFO_LENGTH - 1); i > 0; i--) {
			okFlag[i] = okFlag[i - 1];
			fifo[i] = fifo[i - 1];
		}
		
		if(value > 0) {

			fifo[0] = value;
			okFlag[0] = true;
		} else {
			
			fifo[0] = value;
			okFlag[0] = false;
		}
	}
}
