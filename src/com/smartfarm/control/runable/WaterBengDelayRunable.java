package com.smartfarm.control.runable;

import com.smartfarm.bean.RunningData;
import com.smartfarm.bean.WaterConfig;
import com.smartfarm.control.DelayRunable;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.tools.Constants;
import com.smartfarm.view.SimpleConfigManager;

public class WaterBengDelayRunable extends DelayRunable {

	// true:开, false:关
	private boolean isOnOff;

	public WaterBengDelayRunable(boolean isOnOff) {

		type = Constants.MOTOR_CONTROL_TYPE_BENG_CLOSE;
		this.isOnOff = isOnOff;
	}

	@Override
	public void onCancle() {}

	@Override
	public void onExecute() {

		WaterConfig wConfig = SimpleConfigManager.getInstance().getWaterModel();
		
		if (!isOnOff) {
			int waterNum = wConfig.getWaterCount();
			for (int i = 0; i < waterNum; i++) {
				if (RunningData.getInstance().getWaterState(Constants.WATER_ID_ALL[i]))
					SerialHelper.closeWater(Constants.WATER_ID_ALL[i],
							SerialControlRunable.SOURCE_PAD_BUTTON, false);
			}
		} else {

		}
		
	}

}
