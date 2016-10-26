package com.smartfarm.control.runable;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;

import android.os.Handler;

public class WaterOnekeyOpenRunable extends SerialControlRunable  {
	private int source;
	public WaterOnekeyOpenRunable(int source){
		this.source=source;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		RunningData.getInstance().setOneKeyWaterState(true);
		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_ONEKEY_CHANGE));
		return SUCCESS;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return Constants.MOTOR_CONTROL_TYPE_ONEKEY_OPEN;
	}

	@Override
	public int getSource() {
		// TODO Auto-generated method stub
		return source;
	}

}
