package com.smartfarm.control.runable;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;

import android.os.Handler;
import android.util.Log;

public class ModifyDeviceRunable extends SerialControlRunable {
	private int originalID;
	private int newID;

	public ModifyDeviceRunable(int originalID, int newID) {
		this.originalID = originalID;
		this.newID = newID;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		boolean find = false;
		int count = 1;
		/*
		 * while (true) { if (count > 30) break;
		 */
		if (originalID == newID) {
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_DEVICE_CHANGE_ERROR, 1));
			return LINK_ERROR;
		}
		sendSerialMsg(SerialCmd.modifyDevice(originalID, currId));
		CommonTool.delay(500);
		while (!msgs.isEmpty()) {
			try {
				LocalEvent msg = msgs.take();
				if (msg.getEventMsg().matches("IDSuccess\\s*")) {
					EventBus.getDefault().postInOtherThread(
							LocalEvent.getEvent(
									LocalEvent.EVENT_TYPE_DEVICE_CHANGE_SCUESS,
									currId));
					find = true;
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/*
			 * } count++;
			 */
			if (find)
				return SUCCESS;
		}
		Log.d("zqq", "消息格式化失败->" + count);
		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_DEVICE_CHANGE_ERROR,
						1));
		return LINK_ERROR;
	}

	@Override
	public int getId() {
		return newID;
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
