package com.smartfarm.control.runable;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;

public class WaterCloseRunable extends SerialControlRunable {

	private int id;
	private int source = -1;
	private boolean isAuto = false;

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public WaterCloseRunable(int id, int source, boolean isAuto) {
		this.id = id;
		this.source = source;
		this.isAuto = isAuto;
	}

	@Override
	public int onExecute(Handler handler, int currId) {

		int count = 0;
		boolean find = Constants.DEFAULT_STATE;
		while (count < 10) {

			sendSerialMsg(SerialCmd.CMD_WATER_CLOSE_ALL[currId]);

			// 1号为a2，2号为a9
			// sendSerialMsg(new byte[] { (byte) 0x41, currId == 1 ? (byte) 0x39
			// : (byte) 0x32, (byte) 0x3A,
			// (byte) 0x30, (byte) 0x30, (byte) 0x32, (byte) 0x0d, (byte) 0x0a
			// });
			CommonTool.delay(200);
			//CommonTool.delay(1000);
			try {
				while (!msgs.isEmpty()) {

					LocalEvent msg = msgs.take();

					// if (msg.getEventMsg().matches("(H8|B2|B9):002\\s*")) {
					if (msg.getEventMsg().matches(
							"(H6|B2|B9):00" + Constants.WATER_ID_ALL[currId] + ".*\\s*")) {
						find = true;
						break;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (find)
				break;

			count++;

			if (count >= 5 && count < 10) {
				Log.d(Constants.TAG, "send close water cmd error, wait 1s");
				CommonTool.delay(1000);
			}
		}

		if (count < 10) {
			RunningData.getInstance().setWaterState(Constants.WATER_ID_ALL[currId], false);
			EventBus.getDefault().postInOtherThread(
					LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_CHANGE));
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
		return Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE;
	}

	@Override
	public int getSource() {
		return source;
	}
}