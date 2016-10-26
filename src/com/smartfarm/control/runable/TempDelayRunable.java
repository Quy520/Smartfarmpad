package com.smartfarm.control.runable;

import com.smartfarm.bean.RunningData;
import com.smartfarm.control.DelayRunable;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.view.SimpleConfigManager;

public class TempDelayRunable extends DelayRunable {
	private boolean open;
	private long state;
	private boolean calibrate = false;

	public TempDelayRunable(int id, int type, long currTime, boolean open,
			long state, int runtime, boolean calibrate) {
		this.id = id;
		this.type = type;
		this.open = open;
		this.state = state;
		time = currTime;
		this.calibrate = calibrate;
	}
	
	
	@Override
	public void onCancle() {
		long currState = 0;

		if (open)
			currState = state + System.currentTimeMillis() - time;
		else
			currState = state - System.currentTimeMillis() + time;

		setWindowState(currState);
	}
	
	private void setWindowState(long curr) {
		
		if (curr < 0)
			curr = 0;

		long bound = SimpleConfigManager.getInstance().getConfig().getOpenLen() * 6000 + 1000;

		if (curr > bound)
			curr = bound - 1000;

		RunningData.getInstance().setWindowState(id, curr);
	}

	@Override
	public void onExecute() {
		long currState = 0;

		if (!isCalibrate()) {
			if (open)
				currState = state + System.currentTimeMillis() - time;
			else
				currState = state - System.currentTimeMillis() + time;
		}

		setWindowState(currState);
		SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, id);
	}

	@Override
	public String toString() {
		return "" + time;
	}

	public boolean isCalibrate() {
		return calibrate;
	}

	public void setCalibrate(boolean calibrate) {
		this.calibrate = calibrate;
	}
}
