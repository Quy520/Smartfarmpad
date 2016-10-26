package com.smartfarm.control.runable;

import com.smartfarm.bean.RunningData;
import com.smartfarm.bean.WaterConfig;
import com.smartfarm.control.DelayRunable;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.tools.Constants;
import com.smartfarm.view.SimpleConfigManager;

public class WaterDelayRunable extends DelayRunable {
	
	private final boolean isAuto;
	

	public WaterDelayRunable(int id, long currTime, boolean isAuto) {
		this.id = id;
		type = Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE;
		time = currTime;
		this.isAuto = isAuto;
	}

	@Override
	public void onCancle() {
		RunningData rd = RunningData.getInstance();
		if(isAuto){
			//检查一键灌水是否开启
//			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_ONE_KEY_MODE_OVER));
			if (rd.getRumpState()) {
				SerialHelper.closeBeng(
						SerialControlRunable.SOURCE_PAD_BUTTON, true);
			}
		}
	}
	@Override
	public void onExecute() {
		WaterConfig wConfig = SimpleConfigManager.getInstance().getWaterModel();
		if (isAuto) {
			//发送响应给手机接收
			if(id < Constants.WATER_ID_ALL[ wConfig.getWaterCount()-1]  ) {
				if (RunningData.getInstance().getWaterState(id + 1))
					SerialHelper.closeWater(id + 1, SerialControlRunable.SOURCE_PAD_BUTTON, true);
				SerialHelper.openWater(id + 1, SerialControlRunable.SOURCE_PAD_BUTTON, true);
			} else {
				SerialHelper.closeBeng(SerialControlRunable.SOURCE_PAD_BUTTON, true);
				RunningData.getInstance().setOneKeyWaterState(false);
				SerialHelper.closeKeyWater(SerialControlRunable.SOURCE_PAD_BUTTON);
			}
			SerialHelper.closeWater(id, SerialControlRunable.SOURCE_PAD_BUTTON, true);
		}
	}
}
