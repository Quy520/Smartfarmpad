package com.smartfarm.control.runable;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;

import android.os.Handler;
import android.util.Log;

public class ReadDeviceRunable extends SerialControlRunable {

	@Override
	public int onExecute(Handler handler, int currId) {
		sendSerialMsg(SerialCmd.COM_READ_DEVICE);
		CommonTool.delay(300);

		while (!msgs.isEmpty()) {
			try {
				LocalEvent msg = msgs.take();
				String _msg = msg.getEventMsg() + "V1.50";
				if(_msg.length()<10){
					EventBus.getDefault().postInOtherThread(
							LocalEvent.getEvent(LocalEvent.EVENT_TYPE_READ_ID_ERROR));
					return LINK_ERROR;
				}
				String msgs1 = _msg.substring(0, 5);
				String msg2 = _msg.substring(5,10);
				if(msg2.matches("V.*")){
					EventBus.getDefault().postInOtherThread(
							LocalEvent.getEvent(LocalEvent.EVENT_TYPE_GET_DEVICE_VERSION, msg2));
				}else {
					msg2 = "V1.50";
					EventBus.getDefault().postInOtherThread(
							LocalEvent.getEvent(LocalEvent.EVENT_TYPE_GET_DEVICE_VERSION, msg2));
				}
				if (msgs1.matches("ID:(\\d{2})")) {
					int index = msgs1.indexOf(":");
					String temp = msgs1.substring(index + 1, msgs1.length());
					Log.d("cxy", "temp->"+temp);
					EventBus.getDefault().postInOtherThread(
							LocalEvent.getEvent(LocalEvent.EVENT_TYPE_GET_DEVICE_ID, Integer.parseInt(temp)));
					return SUCCESS;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return 0;
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
