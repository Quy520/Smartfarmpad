package com.smartfarm.control;

public class DelayStopTuple {
	public final int windowId;
	public final long time;
	public final boolean open;
	public final long state;
	public final int runtime;
	private boolean calibrate = false;
	
	public DelayStopTuple(int windowId, long currTime, boolean open, long state, int runtime) {
		this.windowId = windowId;
		this.runtime = runtime;
		this.state = state;
		this.open = open;
		time = currTime;
	}

	@Override
	public boolean equals(Object o) {
		return DelayStopTuple.class.isInstance(o) && ((DelayStopTuple)o).time == this.time;
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
