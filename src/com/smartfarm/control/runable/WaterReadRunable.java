package com.smartfarm.control.runable;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialDriver;
import com.smartfarm.tools.CRC16;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.tencent.bugly.crashreport.CrashReport;

public class WaterReadRunable extends SerialControlRunable {
	
	private int source = -1;
	private int id;
	private int currWater = 0;
	
	public WaterReadRunable(int source, int id) {
		
		this.id = id;
		this.source = source;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		
		currWater = 0;
		float currTemp = 0;
		int count = 0;
		
		while(true) {
			
			if(count >= 10) 
				break;
			
			sendSerialMsg(Constants.CMD_WATER_485_READ);
			CommonTool.delay(300);
			
			boolean find =false;	
			while(!msgs.isEmpty()) {
				
				try {
					LocalEvent msg = msgs.take();

					if(msg.getEventValue() == SerialDriver.SERIAL_TYPE_OTHER) {

						byte[] data = (byte[]) msg.getEventData();
						
						int crc = CRC16.calcCrc16(data);
						if(crc != 0) {
							
							continue;
						}
						
						int value1 = data[3] < 0 ? data[3] + 128 : data[3];
						int value2 = data[4] < 0 ? data[4] + 128 : data[4];
						
						currTemp = ((value1 << 8) | value2) / 100;
						
						Log.d("mmsg", "receive water temp -> " + currTemp);

						value1 = data[5] < 0 ? data[5] + 128 : data[5];
						value2 = data[6] < 0 ? data[6] + 128 : data[6];
						
						float value = ((value1 << 8) | value2) / 100;
						Log.d("mmsg", "receive water value -> " + value);
						
						currWater = Math.round(value);
						
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
				Log.d(Constants.TAG, "read water error, wait 1s! ");
				CommonTool.delay(1000);
			}
			continue;
		}
		
		RunningData.getInstance().saveWater(currWater, currId);
		RunningData.getInstance().saveWaterTemp(currId, currTemp);
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(
					LocalEvent.EVENT_TYPE_RECEIVE_WATER, currWater, String.valueOf(currTemp)));
		
		if(currWater == 0) 
			return LINK_ERROR;
		
		return SUCCESS;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getType() {
		return Constants.MOTOR_CONTROL_TYPE_WATER_READ;
	}

	@Override
	public int getSource() {
		return source;
	}
}
