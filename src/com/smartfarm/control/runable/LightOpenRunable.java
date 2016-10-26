package com.smartfarm.control.runable;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;

public class LightOpenRunable extends SerialControlRunable {
	
	private int id;
	private int source = -1;
	
	public LightOpenRunable(int id, int source) {
		this.id = id;
		this.source = source;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		
		int count = 0;
		boolean find = Constants.DEFAULT_STATE;	
		while(count < 10) {
			
			sendSerialMsg(Constants.CMD_LIGHT_OPEN_ALL[currId]);
			if(currId > 0){
				return SUCCESS;
			}
			CommonTool.delay(200);
			try {
				while (!msgs.isEmpty()) {

					LocalEvent msg = msgs.take();
					
					if (msg.getEventMsg().matches("E6:00" + (currId + 1) + "\\s*")) {
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
				Log.d(Constants.TAG, "send open cmd error, wait 1s");
				CommonTool.delay(1000);
			}
		}
		
		if(count < 10) {
			
			RunningData.getInstance().setLightState(currId, true);
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
		return Constants.MOTOR_CONTROL_TYPE_LIGHT_OPEN;
	}

	@Override
	public int getSource() {
		return source;
	}
}
