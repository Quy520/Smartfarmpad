package com.smartfarm.control.runable;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.DelayRunable;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;

public class WaterBengOpenRunable extends SerialControlRunable {
	
	private int id;
	private int source = -1;
	private boolean isAuto=false;
	
	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public WaterBengOpenRunable(int source,boolean isAuto) {
		id = 0;
		this.source = source;
		this.isAuto=isAuto;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		
		int count = 0;
		boolean find = Constants.DEFAULT_STATE;	
		while(count < 10) {
			
			sendSerialMsg(SerialCmd.CMD_WATER_BENG_OPEN);
			CommonTool.delay(200);
			try {
				while (!msgs.isEmpty()) {

					LocalEvent msg = msgs.take();

					if (msg.getEventMsg().matches("H2:00" + (currId + 1) + ".*\\s*")) {
						find = true;
						break;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(find)
				break;
			
			count++;
			
			if(count >= 5 && count < 10) {
				Log.d(Constants.TAG, "send open water beng cmd error, wait 1s");
				CommonTool.delay(1000);
			}
		}
		
		if(count < 10) {

			RunningData.getInstance().setRumpState(true);
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_CHANGE));
			
			DelayRunable delay = new WaterBengDelayRunable(true);
			Message msg = new Message();
			msg.what = Constants.MSG_ADD_DELAY_STOP;
			msg.arg1 = 30000;
			msg.obj = delay;
			handler.sendMessage(msg);
			
			return SUCCESS;
		}

		return LINK_ERROR;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getType() {
		return Constants.MOTOR_CONTROL_TYPE_BENG_OPEN;
	}

	@Override
	public int getSource() {
		return source;
	}
}
