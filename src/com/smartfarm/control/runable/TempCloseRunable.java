package com.smartfarm.control.runable;

import java.util.List;

import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.DelayRunable;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.tools.Alarm;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;
/**
 * 
 * 关窗口操作
 * @author QSD
 *
 */
public class TempCloseRunable extends SerialControlRunable {

	private int destWindowId;
	private boolean closeAll = false;
	private boolean calibrate = false;
	private int source = -1;
	private int stalls = -1;
	

	public TempCloseRunable(int windowId, boolean isCloseAll, int source) {
		destWindowId = windowId;
		closeAll = isCloseAll;
		
		this.source = source;
	}

	public void setStalls(int stall) {
		stalls = stall;
	}

	public void runCalibrate() {
		calibrate = true;
		closeAll = true;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		if (RunningData.getInstance().judge(currId)) {
			EventBus.getDefault().noticeMsg(
					"电机0" + (currId + 1) + "正在运转，请稍后操作！");
			return MOTOR_IS_WORKING;
		}
		if(!SimpleConfigManager.getInstance().getConfig().isAutoOpenEnable()){
			if(source != SerialControlRunable.SOURCE_AUTO){
				/*SimpleConfigManager.getInstance().getConfig().setAutoOpenEnable(true);
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_CHANGE));*/
				SimpleConfigManager.getInstance().modeChangeLast(SerialControlRunable.SOURCE_PHONE, false,true);
			}
		}
		RunningData.getInstance().setWorkingState(currId, true);

		int closeTime = SimpleConfigManager.getInstance().getConfig()
				.getOpenLen() * 1000 * Constants.MOTOR_SPEED;

		long nowState = RunningData.getInstance().getWindowState(currId);
		long counterDownOpen = 0;
		int count = 0;
		boolean isCloseMin = false;
		List<Integer> allState = SimpleConfigManager.getInstance().getConfig()
				.getAllStalls();

		if (stalls < 0) {

			boolean find = false;
			for (int i = allState.size() - 1; i >= 0; i--) {
				if (CommonTool.compareBound(nowState, allState.get(i)) > 0) {

					find = true;
					counterDownOpen = nowState - allState.get(i);
					break;
				}
			}

			if (!find) {
				counterDownOpen = nowState * 2;
			}

			if (counterDownOpen <= 0) {
				counterDownOpen = allState.get(0);
				isCloseMin = true;
				RunningData.getInstance().addCloseCount(currId);
				EventBus.getDefault().noticeMsg("窗口已关到最小！若不准确请重新校准窗口位置。");
			}

			Log.d(Constants.TAG, "now state -> " + nowState
					+ ", want to close " + counterDownOpen);

		} else if (stalls < allState.size()) {

			long want = stalls == 0 ? 0 : allState.get(stalls - 1);
			long diff = nowState - want;

			Log.d(Constants.TAG, "now state -> " + nowState
					+ ", want to change to " + want);
			if (diff > 0)
				counterDownOpen = diff;
			else if (stalls == 0)
				counterDownOpen = allState.get(1);
			else
				counterDownOpen = 0;

		} else {

			counterDownOpen = 0;
		}

		if(closeAll)
			counterDownOpen = closeTime * 2;

		Log.d(Constants.TAG, " window " + (currId + 1) + " want to close "
				+ (counterDownOpen / 1000) + "s !");

		while (count < 10) {
			RunningData.getInstance().clearOpenCount(currId);
			sendSerialMsg(SerialCmd.CMD_CLOSE_ALL[currId]);
			CommonTool.delay(300);

			boolean find = false;
			while (!msgs.isEmpty()) {

				try {
					LocalEvent msg = msgs.take();

					Log.e("zqq", msg.getEventMsg());
					if (msg.getEventMsg().matches("(A|B)7:00" + (currId + 1) + ".*\\s*")) {
						find = true;
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (find)
				break;

			count++;
			if (count >= 5 && count < 10) {
				Log.d(Constants.TAG, "send close cmd error, wait 1s");
				CommonTool.delay(1000);
			}
		}

		DelayRunable delay = new TempDelayRunable(currId,
				Constants.MOTOR_CONTROL_TYPE_STOP, System.currentTimeMillis(),
				false, nowState, closeTime, calibrate);
		if (count < 10) {
			Message msg = new Message();
			msg.what = Constants.MSG_ADD_DELAY_STOP;
			msg.arg1 = (int) counterDownOpen;
			msg.obj = delay;
			handler.sendMessage(msg);

			return isCloseMin ? WINDOW_CLOASE_MIN + SUCCESS : SUCCESS;
		}

		if (Alarm.isShouldAlarm())
			Alarm.getInstance().sendAlarm("风口" + (currId + 1) + "关闭风口失败，原因：链路异常！", false);

		RunningData.getInstance().setWorkingState(currId, false);
		return isCloseMin ? WINDOW_CLOASE_MIN + LINK_ERROR : LINK_ERROR;
	}

	@Override
	public int getId() {
		return destWindowId;
	}

	@Override
	public int getType() {
		return Constants.MOTOR_CONTROL_TYPE_CLOSE;
	}

	@Override
	public int getSource() {
		return source;
	}

}
