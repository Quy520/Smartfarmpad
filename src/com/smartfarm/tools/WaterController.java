package com.smartfarm.tools;

public class WaterController {
	
	private int windowId = -1;
	private int firstCount = 0;
	private float tempFIFO[] = new float[Constants.FIFO_LENGTH]; 
	private boolean[] okFlag = new boolean[Constants.FIFO_LENGTH];
	
	public WaterController(int windowId) {
		this.windowId = windowId;
		
		for(int i = 0; i < Constants.FIFO_LENGTH; i++) {
			okFlag[i] = true;
		}
	}
	
}
