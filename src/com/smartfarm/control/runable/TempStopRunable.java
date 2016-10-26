package com.smartfarm.control.runable;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.Alarm;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.SimpleConfigManager;

public class TempStopRunable extends SerialControlRunable {

	private int destWindowId;
	private int source = -1;
	private boolean clearWorkState = false;

	public TempStopRunable(int windowId, int source) {
		this.source = source;
		destWindowId = windowId;
	}

	public void setClearWorkState(boolean clearWorkState) {
		this.clearWorkState = clearWorkState;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		/*if(Constants.SUPER_MODE)
			return SUCCESS;*/

		if (clearWorkState)
			return SUCCESS;
		
		if(!SimpleConfigManager.getInstance().getConfig().isAutoOpenEnable()){
			if(source != SerialControlRunable.SOURCE_AUTO){
				/*SimpleConfigManager.getInstance().getConfig().setAutoOpenEnable(true);
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_CHANGE));*/
				SimpleConfigManager.getInstance().modeChangeLast(SerialControlRunable.SOURCE_PHONE, false,true);
			}
		}

		int count = 0;
		boolean find = false;
		while (count < 10) {
			sendSerialMsg(SerialCmd.CMD_STOP_ALL[currId]);
			CommonTool.delay(300);
			try {
				while (!msgs.isEmpty()) {

					LocalEvent msg = msgs.take();

					if (msg.getEventMsg().matches("(A|B)8:00" + (currId + 1) + ".*\\s*")) {
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
				Log.d(Constants.TAG, "send stop cmd error, wait 1s");
				CommonTool.delay(1000);
			}
		}

		RunningData.getInstance().setWorkingState(currId, false);

		if (count < 10) {

			return SUCCESS;
		} else {

			if (Alarm.isShouldAlarm())
				Alarm.getInstance().sendAlarm(
						"风口" + (currId + 1) + "停止风口失败，原因：链路异常！", false);

			return LINK_ERROR;
		}
	}

	@Override
	public void afterExecute(String res) {

		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_LEN_CHANGE));
		super.afterExecute(res);
	}

	@Override
	public int getId() {

		return destWindowId;
	}

	@Override
	public int getType() {

		return Constants.MOTOR_CONTROL_TYPE_STOP;
	}

	@Override
	public int getSource() {

		return source;
	}
}
