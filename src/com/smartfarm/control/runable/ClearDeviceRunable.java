package com.smartfarm.control.runable;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.CommonTool;

import android.os.Handler;

public class ClearDeviceRunable extends SerialControlRunable{

	@Override
	public int onExecute(Handler handler, int currId) {
		sendSerialMsg(SerialCmd.COM_CLEAR_DEVICE);
		CommonTool.delay(500);
		while (!msgs.isEmpty()) {
			try {
				LocalEvent msg = msgs.take();
				if (msg.getEventMsg().matches("ID:00\\s*")){
//					EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_DEVICE_CHANGE_SCUESS));
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	@Override
	public int getId() {
		return 0;
	}
	

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public int getSource() {
		return 0;
	}

}