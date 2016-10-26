package com.smartfarm.control.runable;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.BaseLinkDriver;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.tencent.bugly.crashreport.CrashReport;

public class LightReadRunable extends SerialControlRunable {
	
	private int source = -1;
//	private float lastLight = -1;
	private int currLight = 0;
	
	public LightReadRunable(int source) {
		
		this.source = source;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		
		currLight = 0;
		int count = 0;
		String res = null;
//		lastLight = RunningData.getInstance().getLightValue();
		
		while(true) {
			
			if(count >= 10) 
				break;
			
			sendSerialMsg(Constants.CMD_LIGHT_READ);
			CommonTool.delay(300);
			
			boolean find = false;
			while(!msgs.isEmpty()) {
				
				try {
					LocalEvent msg = msgs.take();

					if(msg.getEventValue() == BaseLinkDriver.SERIAL_TYPE_LIGHT) {

						res = msg.getEventMsg();
						int index = res.indexOf("Lux");
						String temp = res.substring(0, index);
						
						currLight = Integer.valueOf(temp, 16);
						
						currLight /= 1.2;
						
						find = true;
						break;
					}
				} catch (Exception e) {
					CrashReport.postCatchedException(e);
					e.printStackTrace();
				}
			}
			
			if(find)
				break;

			count++;

			if(count >= 5 && count < 10) {
				Log.d(Constants.TAG, "read light error, wait 1s! ");
				CommonTool.delay(1000);
			}
			continue;
		}
		
		RunningData.getInstance().saveLightData(currLight);
		
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(
					LocalEvent.EVENT_TYPE_RECEIVE_LIGHT, currLight));
		
		if(currLight <= 0) 
			return LINK_ERROR;
		
		return SUCCESS;
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public int getType() {
		return Constants.MOTOR_CONTROL_TYPE_LIGHT_READ;
	}

	@Override
	public int getSource() {
		return source;
	}
}
