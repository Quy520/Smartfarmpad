package com.smartfarm.control.runable;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;

public class LightCloseRunable extends SerialControlRunable {
	
	private int id;
	private int source = -1;
	
	public LightCloseRunable(int id, int source) {
		this.id = id;
		this.source = source;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		
		int count = 0;
		boolean find = Constants.DEFAULT_STATE;	
		while(count < 10) {
			
			sendSerialMsg(Constants.CMD_LIGHT_CLOSE_ALL[currId]);
			CommonTool.delay(200);
			try {
				while (!msgs.isEmpty()) {

					LocalEvent msg = msgs.take();

					if (msg.getEventMsg().matches("E8:00" + (currId + 1) + "\\s*")) {
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
				Log.d(Constants.TAG, "send close light cmd error, wait 1s");
				CommonTool.delay(1000);
			}
		}
		
		if(count < 10) {
			
			RunningData.getInstance().setLightState(currId, false);
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_LIGHT_CHANGE));
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
		return Constants.MOTOR_CONTROL_TYPE_LIGHT_CLOSE;
	}

	@Override
	public int getSource() {
		return source;
	}
}