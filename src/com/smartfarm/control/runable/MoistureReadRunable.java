package com.smartfarm.control.runable;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.control.SerialDriver;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.tencent.bugly.crashreport.CrashReport;

import android.os.Handler;
import android.util.Log;

public class MoistureReadRunable extends SerialControlRunable {
	private int windowsId;
	private int source;
	// 光照
	private int illumination;
	// 湿度
	private float humidity;
	// 二氧化碳浓度
	private int valueCO2;

	public MoistureReadRunable(int windowsId, int source) {
		this.windowsId = windowsId;
		this.source = source;
	}

	@Override
	public int onExecute(Handler handler, int currId) {
		int count = 0;
		while (true) {
			if (count >= 10) {
				break;
			}
			sendSerialMsg(SerialCmd.CMD_AIR_MISTURE);
			CommonTool.delay(300);
			boolean find = false;
			while (!msgs.isEmpty()) {
				try {
					LocalEvent msg = msgs.take();
					if (msg.getEventValue() == SerialDriver.SERIAL_TYPE_OTHER) {

						byte[] data = (byte[]) msg.getEventData();

						if(data[0] != 2 || CommonTool.getValidCount(data) < 11) {
							Log.d("zqq", " check vhi data error!");
							continue;
						}

						int value1 = data[3] < 0 ? data[3] + 128 : data[3];
						int value2 = data[4] < 0 ? data[4] + 128 : data[4];
						valueCO2 = ((value1 << 8) | value2);
						value1 = data[7] < 0 ? data[7] + 128 : data[7];
						value2 = data[8] < 0 ? data[8] + 128 : data[8];
						humidity = ((value1 << 8) | value2) / 100;
						value1 = data[9] < 0 ? data[9] + 128 : data[9];
						value2 = data[10] < 0 ? data[10] + 128 : data[10];
						illumination = ((value1 << 8) | value2);

						find = true;
						break;
					}
				} catch (Exception e) {
					CrashReport.postCatchedException(e);
					e.printStackTrace();
				}
			}
			if (find)
				break;

			count++;

			if (count >= 5 && count < 10) {
				Log.d(Constants.TAG, "read water error, wait 1s! ");
				CommonTool.delay(1000);
			}
			continue;
		}
		
		if(count > 10)
			return LINK_ERROR;
		
		Log.d("zqq", "CO2=" + valueCO2 + ";humidity=" + humidity
				+ ";illumination=" + illumination);
		RunningData.getInstance().setValueCO2(valueCO2);
		RunningData.getInstance().setHumidity(humidity);
		RunningData.getInstance().saveLightData(illumination);
		RunningData.getInstance().setIllumination(illumination);

		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_CHECK_OTHER2));

		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_LIGHT));

		return SUCCESS;
	}

	@Override
	public int getId() {
		return windowsId;
	}

	@Override
	public int getType() {
		return Constants.MOTOR_CONTROL_TYPE_READ;
	}

	@Override
	public int getSource() {
		return source;
	}
}
