package com.smartfarm.control;

public abstract class DelayRunable {

	protected int type;
	protected int id;
	protected long time;

	public abstract void onCancle();

	public abstract void onExecute();

	@Override
	public boolean equals(Object o) {

		if (DelayRunable.class.isInstance(o)) {

			DelayRunable delay = (DelayRunable) o;

			if (delay.time == time)
				return true;
		}

		return false;
	}

	public boolean cancleable(int type, int id) {
		return type == this.type && id == this.id;
	}

	public boolean executeable(long time) {

		return time == this.time;
	}
}
