package com.smartfarm.control.runable;

import java.util.List;

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
import com.smartfarm.view.SimpleConfigManager;
/**
 * 
 * 打开窗口
 * @author QSD
 *
 */
public class TempOpenRunable extends SerialControlRunable {
	
	private int destWindowId;
	private boolean openAll = false;
	private int source = -1;
	private int stalls = -1;

	public void setStalls(int stall) {
		stalls = stall;
	}
	
	public TempOpenRunable(int windowId, int source) {
		this.source = source;
		destWindowId = windowId;
	}
	
	public TempOpenRunable(int windowId, boolean openAll, int source) {
		this.source = source;
		this.openAll = openAll;
		destWindowId = windowId;
	}
	
	public TempOpenRunable(int windowId, int delay, int source) {
		this.source = source;
		destWindowId = windowId;
	}
	
	public TempOpenRunable(int windowId, int delay, boolean openAll, int source) {
		this.source = source;
		this.openAll = openAll;
		destWindowId = windowId;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		
		if(RunningData.getInstance().judge(currId)) {
			
			EventBus.getDefault().noticeMsg("电机0" + (currId + 1) + "正在运转，请稍后操作！");
			return MOTOR_IS_WORKING;
		}
		if(RunningData.getInstance().getOpenCount(currId) >= 3){
			return WINDOW_OPEN_MAX + SUCCESS;
		}
		if(!SimpleConfigManager.getInstance().getConfig().isAutoOpenEnable()){
			if(source != SerialControlRunable.SOURCE_AUTO){
				/*SimpleConfigManager.getInstance().getConfig().setAutoOpenEnable(true);
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_CHANGE));*/
				SimpleConfigManager.getInstance().modeChangeLast(SerialControlRunable.SOURCE_PHONE, false,true);
			}
		}
		
		RunningData.getInstance().setWorkingState(currId, true);
		
		int openTime = SimpleConfigManager.getInstance().getConfig().getOpenLen() * 1000 * Constants.MOTOR_SPEED;
		long nowState = RunningData.getInstance().getWindowState(currId);
		long counterDownOpen = 0;
		int count = 0;
		boolean openMax = false;
		
		List<Integer> allState = SimpleConfigManager.getInstance().getConfig().getAllStalls();
		
		if(stalls < 0) {
			for(int i = 0; i < allState.size(); i++) {
				
				int state = allState.get(i);
				if(CommonTool.compareBound(nowState, state) < 0) {
					counterDownOpen = state - nowState;
					break;
				}
			}
			
			if(counterDownOpen == 0) {
				openMax = true;
				RunningData.getInstance().addOpenCount(currId);
				EventBus.getDefault().noticeMsg("窗口已开到最大！若不准确请重新校准窗口位置。");
			}
			
			Log.d(Constants.TAG, "now state -> " + nowState + ", want to open " + counterDownOpen);
			
		} else if(stalls < allState.size()) {
			
			int want = stalls == 0 ? 0 : allState.get(stalls - 1);
			long diff = want - nowState;

			Log.d(Constants.TAG, "now state -> " + nowState + ", want to change to " + want);
			
			if(diff > 0) 
				counterDownOpen = diff;
			else 
				counterDownOpen = 0;
		} else {
			
			counterDownOpen = 0;
		}
		
		
		if(openAll) {

			counterDownOpen = openTime - nowState;
			
			if(counterDownOpen < 0)
				counterDownOpen = 0;
		}

		Log.d(Constants.TAG, " window " + (currId + 1) + " want to open " + (counterDownOpen / 1000) + "s !");
		
		boolean find = false;	
		while(count < 10) {
			Log.d("zqq", "开被触发了->"+count);
			
			sendSerialMsg(SerialCmd.CMD_OPEN_ALL[currId]);
			CommonTool.delay(300);
			try {
				while (!msgs.isEmpty()) {

					LocalEvent msg = msgs.take();

					if (msg.getEventMsg().matches("(A|B)6:00" + (currId + 1) + ".*\\s*")) {
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
			
			RunningData.getInstance().clearCloseCount(currId);
			DelayRunable delay = new TempDelayRunable(currId,Constants.MOTOR_CONTROL_TYPE_STOP,System.currentTimeMillis(),true,nowState,openTime,false);
			
			Message msg = new Message();
			msg.what = Constants.MSG_ADD_DELAY_STOP;
			msg.arg1 = (int) counterDownOpen;
			msg.obj = delay;
			handler.sendMessage(msg);
			
			return openMax ? WINDOW_OPEN_MAX + SUCCESS : SUCCESS;
		}

		if(Alarm.isShouldAlarm())
			Alarm.getInstance().sendAlarm("风口" + (currId + 1) + "打开风口失败，原因：链路异常！", false);

		RunningData.getInstance().setWorkingState(currId, false);
		
		return openMax ? WINDOW_OPEN_MAX + LINK_ERROR : LINK_ERROR;
	}

	@Override
	public int getId() {
		return destWindowId;
	}

	@Override
	public int getType() {
		return Constants.MOTOR_CONTROL_TYPE_OPEN;
	}

	@Override
	public int getSource() {
		return source;
	}
}
