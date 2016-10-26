package com.smartfarm.control.runable;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.bean.WaterConfig;
import com.smartfarm.control.DelayRunable;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.SimpleConfigManager;

public class WaterOpenRunable extends SerialControlRunable {

	private int id;
	private int source = -1;
	private boolean isAuto = false;

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public WaterOpenRunable(int id, int source, boolean isAuto) {
		this.id = id;
		this.source = source;
		this.isAuto = isAuto;
		Log.d("zqq", "id -> " + id);
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		Log.d("zqq", "currId -> " + currId);
		int count = 0;
		boolean find = Constants.DEFAULT_STATE;
		while (count < 10) {

			sendSerialMsg(SerialCmd.CMD_WATER_OPEN_ALL[currId]);
			System.out.println("message:"
					+ SerialCmd.CMD_WATER_OPEN_ALL[currId]);

			// 1号为a1，2号为a3
			// sendSerialMsg(new byte[] { (byte) 0x41, currId == 1 ? (byte) 0x33
			// : (byte) 0x31, (byte) 0x3A,
			// (byte) 0x30, (byte) 0x30, (byte) 0x32, (byte) 0x0d, (byte) 0x0a
			// });
			CommonTool.delay(200);
			try {
				while (!msgs.isEmpty()) {

					LocalEvent msg = msgs.take();
					if (msg.getEventMsg().matches(
							"(H5|B1|B3):00" + Constants.WATER_ID_ALL[currId] + ".*\\s*")) {
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
				Log.d(SerialCmd.TAG, "send open water cmd error, wait 1s");
				CommonTool.delay(1000);
			}
		}

		if (count < 10) {
			if (isAuto) {
				WaterConfig wConfig = SimpleConfigManager.getInstance()
						.getWaterModel();
				DelayRunable delay = new WaterDelayRunable(currId,System.currentTimeMillis(), isAuto);
				long counterDownOpen = wConfig.getTime() * 1000 * 60;
				Message msg = new Message();
				msg.what = Constants.MSG_ADD_DELAY_STOP;
				msg.arg1 = (int) counterDownOpen;
				msg.obj = delay;
				handler.sendMessage(msg);
			}
			RunningData.getInstance().setWaterState(Constants.WATER_ID_ALL[currId], true);
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
		return Constants.MOTOR_CONTROL_TYPE_WATER_OPEN;
	}

	@Override
	public int getSource() {
		return source;
	}
}
