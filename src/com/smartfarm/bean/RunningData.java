package com.smartfarm.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.TempController;
import com.smartfarm.view.SimpleConfigManager;

/**
 * 
 * 程序运行数据管理器
 * 
 * @author jeff
 *
 */
public class RunningData implements EventHandler {

	private static RunningData instance;

	private long[] lastMsg = { 0, 0, 0 };
	private TempController[] temps = new TempController[9];
	private LightDataControler light = new LightDataControler();
	private boolean[] motorWorking = { false, false, false, false, false,
			false, false, false, false };
	private int[] closeState = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int[] openState = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int[] water = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private float[] waterTemp = { 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f };
	private boolean[] waterState = { false, false, false, false, false, false,
			false, false, false };
	private volatile boolean rumpState = false;
	private volatile boolean yaoState = false;
	private volatile boolean oneKeyState = false;
	private long[] windowState = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int valueCO2;
	private float humidity;
	private int illumination;
	// WaterConfig wConfig = SimpleConfigManager.getInstance().getWaterModel();

	public int getIllumination() {
		return illumination;
	}

	public void setIllumination(int illumination) {
		this.illumination = illumination;
	}

	public int getValueCO2() {
		return valueCO2;
	}

	public void setValueCO2(int valueCO2) {
		this.valueCO2 = valueCO2;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	private boolean[] lightState = { false, false, false, false };

	private RunningData() {
		EventBus.getDefault().add(this);

		for (int i = 0; i < 9; i++)
			windowState[i] = SimpleConfigManager.getInstance().getLong(
					SimpleConfigManager.WINDOW_STATE_PREFIX + i);
	}

	/**
	 * 获取数据数据管理器实例
	 * 
	 * @return
	 */
	public static RunningData getInstance() {

		if (instance == null)
			instance = new RunningData();

		return instance;
	}

	public long getWindowState(int id) {

		synchronized (windowState) {
			return windowState[id];
		}
	}

	public void setWindowState(int id, long newState) {

		synchronized (windowState) {
			windowState[id] = newState;
		}

		SimpleConfigManager.getInstance().putLong(
				SimpleConfigManager.WINDOW_STATE_PREFIX + id, newState);
	}

	/**
	 * 检查最后一条消息的时间
	 * 
	 * @param user
	 * @param time
	 * 
	 * @return 消息已经过时返回true，未过时返回false
	 */
	public boolean checkTime(int user, long time) {

		if (user > 2)
			return false;
		else {
			if (lastMsg[user] >= time)
				return true;
			else {
				lastMsg[user] = time;
				return false;
			}
		}
	}

	/**
	 * 储存温度信息
	 * 
	 * @param windowId
	 *            窗口id
	 * @param temp
	 *            温度值
	 * @param needSave
	 *            是否需要用于自动控制和判断工作状态
	 */
	public void saveTemp(int windowId, float temp, boolean needSave) {

		getTempController(windowId);

		synchronized (temps) {

			if (needSave)
				temps[windowId].saveWorkingStatus(temp != 0);

			temps[windowId].dealWithTemp(temp, needSave);
		}
	}

	private TempController getTempController(int windowId) {

		if (temps[windowId] == null)
			temps[windowId] = new TempController(windowId);

		return temps[windowId];
	}

	@Override
	public void onEvent(LocalEvent event) {

		if (event.getEventType() == LocalEvent.EVENT_TYPE_TEMP) {
			saveTemp(event.getEventValue(), event.getEventData() == null ? 0
					: (Float) event.getEventData(), !event.getEventMsg()
					.contains("-1"));
		}
	}

	/**
	 * 获取最新的所有窗口的温度信息的数据
	 * 
	 * @return
	 */
	public String getTempInfo() {

		String AM_push = SimpleConfigManager.getInstance().getConfig()
				.isAutoOrManual() ? "auto" : "no";

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm",
				Locale.getDefault());
		Date curDate = new Date();

		StringBuilder result = new StringBuilder();
		result.append(formatter.format(curDate));
		result.append(":");

		synchronized (temps) {
			for (int i = 0; i < temps.length; i++) {

				if (temps[i] == null)
					break;

				result.append("T");
				result.append(i + 1);
				result.append("=");
				result.append(Math.round(temps[i].getTempRecordRecently()));
				result.append(":");

				long currState = SimpleConfigManager.getInstance().getLong(
						SimpleConfigManager.WINDOW_STATE_PREFIX + i);

				result.append(Math.round((float) (currState)
						/ (float) (Constants.MOTOR_SPEED * 1000)));
				result.append(";");
			}
		}

		result.append(AM_push);

		return result.toString();
	}

	/**
	 * 获取最新的温度值
	 * 
	 * @param windowId
	 * @return
	 */
	public float getTempRecordRecently(int windowId) {

		float temp = 0;

		synchronized (temps) {
			temp = getTempController(windowId).getTempRecordRecently();
		}

		return temp;
	}

	/**
	 * 获取最新的工作状态
	 * 
	 * @param windowId
	 * @return
	 */
	public boolean getWorkingState(int windowId) {

		boolean res = false;

		synchronized (temps) {
			res = getTempController(windowId).getWorkingStatus();
		}

		return res;
	}

	/**
	 * 判断窗口电机的工作状态
	 * 
	 * @param windowId
	 * @return
	 */
	public boolean judge(int windowId) {

		synchronized (motorWorking) {
			return motorWorking[windowId];
		}
	}

	/**
	 * 设置窗口电机的工作状态
	 * 
	 * @param windowId
	 * @param state
	 */
	public void setWorkingState(int windowId, boolean state) {

		synchronized (motorWorking) {
			motorWorking[windowId] = state;
		}
	}

	public int getCloseCount(int id) {
		return closeState[id];
	}

	public void clearCloseCount(int id) {
		closeState[id] = 0;
	}
	
	public void addCloseCount(int id) {
		closeState[id]++;
	}
	
	public int getOpenCount(int id){
		return openState[id];
	}
	
	public void clearOpenCount(int id){
		openState[id] = 0;
	}
	
	public void addOpenCount(int id){
		openState[id]++;
	}

	/**
	 * 判断补光灯的工作状态
	 * 
	 * @param windowId
	 * @return
	 */
	public boolean getLightState(int id) {
		return lightState[id];
	}

	/**
	 * 设置补光灯的工作状态
	 * 
	 * @param windowId
	 * @param state
	 */
	public void setLightState(int id, boolean state) {

		lightState[id] = state;
	}

	/**
	 * 保存照度信息
	 * 
	 * @param value
	 */
	public void saveLightData(int value) {

		synchronized (light) {
			light.save(value);

			LightConfig model = SimpleConfigManager.getInstance().getLightModel();
			if (value > model.getMinValue())
				model.add();
		}
		
		LightConfig lightConfig = SimpleConfigManager.getInstance().getLightModel();
		if(lightConfig.isLightMode() && value < lightConfig.getMinValue())
			SerialHelper.openLightAll(SerialControlRunable.SOURCE_AUTO);
		else if(lightConfig.isLightMode() && value >= lightConfig.getMinValue())
			SerialHelper.closeLightAll(SerialControlRunable.SOURCE_AUTO);
	}

	/**
	 * 获取照度值
	 * 
	 * @return
	 */
	public int getLightValue() {

		int res = -1;

		synchronized (light) {
			res = light.getRecently();
		}

		return res;
	}

	public void saveWaterTemp(int id, float temp) {

		if (id >= this.waterTemp.length)
			return;

		synchronized (this.waterTemp) {
			this.waterTemp[id] = temp;
		}
	}

	public float getWaterTemp(int id) {

		if (id >= this.waterTemp.length)
			return -1;

		synchronized (waterTemp) {
			return this.waterTemp[id];
		}
	}

	/**
	 * 保存湿度信息
	 * 
	 * @param water
	 * @param id
	 */
	public void saveWater(int water, int id) {

		if (id >= this.water.length)
			return;

		synchronized (this.water) {
			this.water[id] = water;
		}
	}
	/**
	 * 保存二氧化碳信息
	 * 
	 * @param CO2
	 */
	public void saveOther(int CO2,float humidity){
			this.valueCO2=CO2;
			this.humidity=humidity;
	}

	/**
	 * 获取湿度信息
	 * 
	 * @param id
	 * @return
	 */
	public int getWater(int id) {

		if (id >= this.water.length)
			return -1;

		synchronized (water) {
			return this.water[id];
		}
	}

	/**
	 * 设置电磁阀状态
	 * 
	 * @param id
	 * @param state
	 */
	public void setWaterState(int id, boolean state) {

		if (id >= this.waterState.length)
			return;

		synchronized (waterState) {
			waterState[id] = state;
		}
	}

	/**
	 * 判定电磁阀状态
	 * 
	 * @return
	 */
	public boolean judgeWaterState() {

		synchronized (waterState) {

			for (boolean state : waterState)
				if (state)
					return true;

			return false;
		}
	}

	/**
	 * 获取水泵状态
	 * 
	 * @return
	 */
	public boolean getRumpState() {
		return rumpState;
	}

	/**
	 * 设置水泵状态
	 * 
	 * @param rumpState
	 */
	public void setRumpState(boolean rumpState) {
		this.rumpState = rumpState;
	}

	/**
	 * 设置一键灌水的状态
	 * 
	 * @param oneKeyState
	 */
	public void setOneKeyWaterState(boolean oneKeyState) {
		this.oneKeyState = oneKeyState;
	}
	/**
	 * 获取一键灌水的状态
	 * 
	 * @return
	 */
	public boolean getOneKeyWaterState() {
		return oneKeyState;
	}

	public boolean getYaoState() {
		return yaoState;
	}

	public void setYaoState(boolean yaoState) {
		this.yaoState = yaoState;
	}

	/**
	 * 获取电磁阀状态
	 * 
	 * @param id
	 * @return
	 */
	public boolean getWaterState(int id) {

		if (id >= this.waterState.length)
			return false;

		synchronized (waterState) {
			return waterState[id];
		}
	}

	/**
	 * 获取湿度信息
	 * 
	 * @return
	 */
	public String getWaterInfo() {

		StringBuilder result = new StringBuilder();

		synchronized (water) {
			synchronized (waterState) {
				for (int i = 0; i < water.length; i++) {

					result.append("W");
					result.append(i + 1);
					result.append("=");
					result.append(water[i]);
					result.append(":");
					result.append(waterState[i]);
					result.append("&");
					result.append(waterTemp[i]);
					result.append(";");
				}
			}
		}

		result.append("Wmode=");
		result.append(SimpleConfigManager.getInstance().getWaterModel()
				.isMode() ? "auto" : "no");
		result.append(";");
		result.append("Wrump=");
		result.append(rumpState);
		result.append(";");
		result.append("Wyao=");
		result.append(yaoState);
		result.append(";");
		result.append("Wonekey=");
		result.append(oneKeyState);
		result.append(";");

		return result.toString();
	}
	
	public String getOthers(){
		StringBuilder result = new StringBuilder();

		result.append("CO2=");
		result.append(valueCO2);
		result.append(";");
		result.append("Humidity=");
		result.append(humidity);
		result.append(";");
		result.append("Light=");
		result.append(illumination);
		result.append(";");

		return result.toString();
	}

	public String getLightInfo() {

		StringBuilder result = new StringBuilder();

		synchronized (lightState) {

			for (int i = 0; i < lightState.length; i++) {

				result.append("L");
				result.append(i + 1);
				result.append("=");
				result.append(lightState[i]);
				result.append(";");
			}
		}

		result.append("Lvalue=");
		result.append(light.getRecently());
		result.append(";");
		result.append("Lmode=");
		result.append(SimpleConfigManager.getInstance().getLightModel()
				.isLightMode());
		result.append(";");

		return result.toString();
	}
}
