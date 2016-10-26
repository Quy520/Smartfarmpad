package com.smartfarm.control.runable;

import com.smartfarm.control.DelayRunable;

public class CompareDelayRunable extends DelayRunable {

	public CompareDelayRunable(int type, int id) {
		this.type = type;
		this.id = id;
	}
	
	@Override
	public void onCancle() {}

	@Override
	public void onExecute() {}

}
