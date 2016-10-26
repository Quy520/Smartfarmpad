package com.smartfarm.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.smartfarm.bean.LightConfig;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.TempConfigModel;
import com.smartfarm.bean.WaterConfig;
import com.smartfarm.db.access.ControlDao;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.StringUtils;
/**
 * 
 * 
 * @author QSD
 *条件初始化控制管理
 */
public class SimpleConfigManager {

	public static final String KEY_INIT_FLAG = "init_flag2";
	public static final String KEY_PHONE_NUMBER = "phoneNumber";
	public static final String KEY_VEN_TIME = "venTimes";
	public static final String KEY_MORNING_OPEN = "MorningOpenTime";
	public static final String KEY_NIGHT_CLOSE = "NightCloseTime";
	public static final String KEY_OPEN_LEN = "openLen";
	public static final String KEY_OPEN_FIRST = "openLenFirst";
	public static final String KEY_OPEN_SECOND = "openLenSecond";
	public static final String KEY_OPEN_THIRD = "openLenThird";
	public static final String KEY_OPEN_FOURTH = "openLenFourth";
	public static final String KEY_OPEN_FIFTH = "openLenFifth";
	public static final String KEY_HIGH_ALARM_ENABLE = "highAlarmEnable";
	public static final String KEY_LOW_ALARM_ENABLE = "lowAlarmEnable";
	public static final String KEY_ALARM_ENABLE = "msgAlarmEnable";
	public static final String KEY_PHONE2 = "phoneNumber2";
	public static final String KEY_PHONE3 = "phoneNumber3";
	public static final String KEY_WIN_COUNT = "windowCount";
	public static final String KEY_MINUTES_COUNT = "minutesCount";
	public static final String KEY_TEMP_MAX = "maxTemp";
	public static final String KEY_TEMP_NOR_MAX = "maxTempNor";
	public static final String KEY_TEMP_NOR_MIN = "minTempNor";
	public static final String KEY_TEMP_MIN = "minTemp";
	public static final String KEY_TEMP_MORE_MODE = "tempMoreMode";
	public static final String KEY_TEMP_ALARM_MAX = "alarm_max";
	public static final String KEY_TEMP_ALARM_MIN = "alarm_min";
	public static final String KEY_PUSH_TIME = "push_time";
	public static final String KEY_MODE = "AutoOrManual";
	public static final String KEY_ONE_Water_MODE = "WaterAutoOrManual";
	public static final String KEY_PWD = "pwd";
	public static final String KEY_WATER_ENABLE = "water_enable";
	public static final String KEY_LIGHT_ENABLE = "light_enable";
	public static final String KEY_LIGHT_DIY_MODE = "light_diy_mode";
	public static final String KEY_AUTO_OPEN = "auto_open";

	public static final String KEY_NEED_LIGHT = "lightNeed";
	public static final String KEY_MIN_LIGHT = "lightMin";
	public static final String KEY_MAX_LIGHT = "lightMax";
	public static final String KEY_DIY_LIGHT = "lightDiy";
	public static final String KEY_LIGHT_START = "lightStart";
	public static final String KEY_LIGHT_COUNT = "lightCount";
	public static final String KEY_LIGHT_CURR = "lightCurr";
	public static final String KEY_LIGHT_CURR_TIME = "lightCurrTime";
	public static final String KEY_LIGHT_MODE = "lightMode";

	public static final String KEY_WATER_TIME = "water_time";
	public static final String KEY_WATER_MIN = "water_min";
	public static final String KEY_WATER_COUNT = "water_count";
	public static final String KEY_WATER_ALARM_MAX = "water_alarm_max";
	public static final String KEY_WATER_ALARM_MIN = "water_alarm_min";
	public static final String KEY_WATER_AMAX_ENABLE = "water_amax_enable";
	public static final String KEY_WATER_AMIN_ENABLE = "water_amin_enable";
	public static final String KEY_WATER_MODE = "water_mode";

	public static final String CONFIG_FILE_NAME = "smart_farm_cfg";
	public static final String WINDOW_STATE_PREFIX = "window_state_";
	public static final String KEY_BLUETOOTH_MAC = "bluetoothmac";
	public static final int SEND_BY_YUNBA = 100;
	public static final int SEND_BY_HUANXIN = 102;

	public static final boolean DEFAULT_WATER_ENABLE = false;
	public static final boolean DEFAULT_LIGHT_ENABLE = false;

	public static final int DEFAULT_WATER_MIN = 60;
	public static final int DEFAULT_WATER_TIME = 30;
	public static final int DEFAULT_WATER_COUNT = 2;
	public static final int DEFAULT_WATER_ALARM_MAX = 80;
	public static final int DEFAULT_WATER_ALARM_MIN = 50;
	public static final boolean DEFAULT_WATER_AMAX_ENABLE = false;
	public static final boolean DEFAULT_WATER_AMIN_ENABLE = false;
	public static final boolean DEFAULT_WATER_MODE = false;
	public static final boolean DEFAULT_ONE_WATER_MODE = false;

	public static final int DEFAULT_LIGHT_NEED = 480;
	public static final int DEFAULT_LIGHT_MIN = 100;
	public static final int DEFAULT_LIGHT_MAX = 10000;
	public static final int DEFAULT_LIGHT_DIY = 120;
	public static final int DEFAULT_LIGHT_COUNT = 2;
	public static final String DEFAULT_LIGHT_START = "20:00";

	private static SimpleConfigManager cfgManager;
	private SharedPreferences configurationInfo;
	private Editor save;
	private ConfigModel mModel;
	private LightConfig lightModel;
	private WaterConfig waterModel;
	private TempConfigModel tempModel;

	public static SimpleConfigManager getInstance() {

		if (cfgManager == null)
			cfgManager = new SimpleConfigManager(AppContext.context());

		return cfgManager;
	}

	public Editor getEditor() {

		return save;
	}

	public LightConfig getLightModel() {
		return lightModel;
	}

	public WaterConfig getWaterModel() {
		return waterModel;
	}

	public TempConfigModel getTempModel() {

		if (tempModel == null)
			refreshTempConfig();

		return tempModel;
	}

	public void refreshTempConfig() {
		tempModel = new TempConfigModel(
				getBoolean(SimpleConfigManager.KEY_TEMP_MORE_MODE));
	}

	private SimpleConfigManager(Context context) {
		configurationInfo = context.getSharedPreferences(CONFIG_FILE_NAME,
				Context.MODE_PRIVATE);
		save = configurationInfo.edit();

		int initialFlag = configurationInfo.getInt(KEY_INIT_FLAG, -1);
		if (initialFlag == -1) {
			initAppCfg();
		}

		mModel = new ConfigModel();
		lightModel = new LightConfig();
		waterModel = new WaterConfig();

		updateApplicationCfg();

		updateLightCfg();

		updateWaterConfig();
	}

	public void initAppCfg() {
		save.putInt(KEY_INIT_FLAG, 1);
		save.putString(KEY_PHONE_NUMBER, ""); // 主手机号
		save.putInt(KEY_VEN_TIME, 30); // 清晨通风时间长度
		save.putString(KEY_MORNING_OPEN, "18:00"); // 早晨开窗时间
		save.putString(KEY_NIGHT_CLOSE, "06:00"); // 晚上关窗时间
		save.putInt(KEY_OPEN_LEN, 120);
		save.putInt(KEY_OPEN_FIRST, 30);
		save.putInt(KEY_OPEN_SECOND, 5);
		save.putInt(KEY_OPEN_THIRD, 10);
		save.putInt(KEY_OPEN_FOURTH, 15);
		save.putInt(KEY_OPEN_FIFTH, 20);
		save.putBoolean(KEY_MODE, false); // 是否自动控制
		save.putBoolean(KEY_ONE_Water_MODE, false); // 是否自动灌水
		save.putBoolean(KEY_HIGH_ALARM_ENABLE, true);
		save.putBoolean(KEY_LOW_ALARM_ENABLE, false);
		save.putBoolean(KEY_ALARM_ENABLE, true);
		save.putString(KEY_PHONE2, ""); // 从2手机号码
		save.putString(KEY_PHONE3, ""); // 从3手机号码
		save.putInt(KEY_WIN_COUNT, 3); // 窗口数量
		save.putInt(KEY_MINUTES_COUNT, 60);
		save.putInt(KEY_TEMP_MAX, 32);
		save.putInt(KEY_TEMP_NOR_MAX, 28);
		save.putInt(KEY_TEMP_NOR_MIN, 24);
		save.putInt(KEY_TEMP_MIN, 20);
		save.putInt("send_way", SEND_BY_HUANXIN);
		save.putLong("window_state_0", 0);
		save.putLong("window_state_1", 0);
		save.putLong("window_state_2", 0);
		save.putLong("window_state_3", 0);
		save.putLong("window_state_4", 0);
		save.putLong("window_state_5", 0);
		save.putInt(KEY_TEMP_ALARM_MAX, 35);
		save.putInt(KEY_TEMP_ALARM_MIN, 15);
		save.putInt(KEY_PUSH_TIME, 10);
		save.putString("date", "2008/8/8");
		save.putLong("lastAlarm", System.currentTimeMillis() - 3600000);
		save.putString(KEY_PWD, Md5Utils.encode("123456"));

		save.putInt(KEY_NEED_LIGHT, DEFAULT_LIGHT_NEED);
		save.putInt(KEY_MIN_LIGHT, DEFAULT_LIGHT_MIN);
		save.putInt(KEY_MAX_LIGHT, DEFAULT_LIGHT_MAX);
		save.putInt(KEY_DIY_LIGHT, DEFAULT_LIGHT_DIY);
		save.putInt(KEY_LIGHT_COUNT, DEFAULT_LIGHT_COUNT);
		save.putString(KEY_LIGHT_START, DEFAULT_LIGHT_START);

		save.putInt(KEY_WATER_ALARM_MAX, DEFAULT_WATER_ALARM_MAX);
		save.putInt(KEY_WATER_ALARM_MIN, DEFAULT_WATER_ALARM_MIN);
		save.putInt(KEY_WATER_TIME, DEFAULT_WATER_TIME);
		save.putInt(KEY_WATER_MIN, DEFAULT_WATER_MIN);
		save.putInt(KEY_WATER_COUNT, DEFAULT_WATER_COUNT);
		save.putBoolean(KEY_WATER_AMAX_ENABLE, DEFAULT_WATER_AMAX_ENABLE);
		save.putBoolean(KEY_WATER_AMIN_ENABLE, DEFAULT_WATER_AMIN_ENABLE);
		save.putBoolean(KEY_WATER_MODE, DEFAULT_WATER_MODE);
		save.putBoolean(KEY_ONE_Water_MODE, DEFAULT_ONE_WATER_MODE);

		save.putBoolean(KEY_WATER_ENABLE, DEFAULT_WATER_ENABLE);
		save.putBoolean(KEY_LIGHT_ENABLE, DEFAULT_LIGHT_ENABLE);

		save.putString(KEY_BLUETOOTH_MAC, "");

		save.commit();
	}

	public int getSendWay() {
		return configurationInfo.getInt("send_way", SEND_BY_HUANXIN);
	}

	public void setSendWay(int way) {

		if (way != SEND_BY_HUANXIN && way != SEND_BY_YUNBA)
			return;

		save.putInt("send_way", way);
		save.commit();
	}

	public ConfigModel getConfig() {
		return mModel;
	}

	public void modeChange(int source, boolean isAuto) {
		mModel.setModeChange(isAuto);
		if(isAuto){
			mModel.setAutoOpenEnable(isAuto);
			SimpleConfigManager.getInstance().putBoolean(KEY_AUTO_OPEN, isAuto);
		}
		SimpleConfigManager.getInstance().putBoolean(KEY_MODE, isAuto);
		ControlDao.add(0, source, 0, Constants.MOTOR_CONTROL_TYPE_MODE, 0);
		EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_MODE_CHANGE));
	}
	
	public void modeOpenChange(int source, boolean isAuto) {
		mModel.setAutoOpenEnable(isAuto);
		mModel.setModeChange(isAuto);
		SimpleConfigManager.getInstance().putBoolean(KEY_AUTO_OPEN, isAuto);
		SimpleConfigManager.getInstance().putBoolean(KEY_MODE,isAuto);
		ControlDao.add(0, source, 0, Constants.MOTOR_CONTROL_TYPE_HIGH_MODE, 0);
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_CHANGE));
	}
	
	public void modeChangeLast(int source,boolean modeAuto,boolean openModeAuto){
		mModel.setModeChange(modeAuto);
		mModel.setAutoOpenEnable(openModeAuto);
		SimpleConfigManager.getInstance().putBoolean(KEY_MODE, modeAuto);
		SimpleConfigManager.getInstance().putBoolean(KEY_AUTO_OPEN, openModeAuto);
		ControlDao.add(0, source, 0, Constants.MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE, 0);
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_CHANGE));
	}
	
	public void modeChangeAll(int source,boolean isAuto){
		mModel.setAutoOpenEnable(isAuto);
		mModel.setModeChange(isAuto);
		SimpleConfigManager.getInstance().putBoolean(KEY_MODE, isAuto);
		SimpleConfigManager.getInstance().putBoolean(KEY_AUTO_OPEN, isAuto);
		ControlDao.add(0, source, 0, Constants.MOTOR_CONTROL_TYPE_RAIN_MODE, 0);
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_CHANGE));
	}
	
	public void oneKeyWaterChange(int source, boolean isAuto) {
		mModel.setModeChange(isAuto);
		SimpleConfigManager.getInstance().putBoolean(KEY_MODE, isAuto);
		ControlDao.add(0, source, 0, Constants.MOTOR_CONTROL_TYPE_MODE, 0);
	}

	public String getReceiver() {
		StringBuilder result = new StringBuilder();
		result.append(mModel.getPhonenumber());

		if (!mModel.getPhonenumber2().equals("")) {
			result.append(";");
			result.append(mModel.getPhonenumber2());
		}

		if (!mModel.getPhonenumber3().equals("")) {
			result.append(";");
			result.append(mModel.getPhonenumber3());
		}

		return result.toString();
	}

	public List<String> getReceivers() {
		List<String> result = new ArrayList<String>();

		if (!ShowUtil.isEmpty(mModel.getPhonenumber()))
			result.add(mModel.getPhonenumber());

		if (!ShowUtil.isEmpty(mModel.getPhonenumber2()))
			result.add(mModel.getPhonenumber2());

		if (!ShowUtil.isEmpty(mModel.getPhonenumber3()))
			result.add(mModel.getPhonenumber3());

		return result;
	}

	public void updateLightCfg() {

		lightModel.setLastTime(getLong(KEY_LIGHT_CURR_TIME, 0));
		lightModel.setTimeCount(getInt(KEY_LIGHT_CURR, 0));
		if (!StringUtils.isToday(new Date(lightModel.getLastTime()))) {

			Log.d("mmsg",
					" save yesterday light count : "
							+ lightModel.getTimeCount());

			save.putInt(KEY_LIGHT_CURR, 0);
			save.putLong(KEY_LIGHT_CURR_TIME, System.currentTimeMillis());
			save.commit();
			lightModel.setTimeCount(0);
			lightModel.setLastTime(System.currentTimeMillis());
		}

		lightModel.setLightMode(getBoolean(KEY_LIGHT_MODE));
		lightModel.setCount(getInt(KEY_LIGHT_COUNT, DEFAULT_LIGHT_COUNT));
		lightModel.setDiyTime(getInt(KEY_DIY_LIGHT, DEFAULT_LIGHT_DIY));
		lightModel.setMaxValue(getInt(KEY_MAX_LIGHT, DEFAULT_LIGHT_MAX));
		lightModel.setMinValue(getInt(KEY_MIN_LIGHT, DEFAULT_LIGHT_MIN));
		lightModel.setNeedTime(getInt(KEY_NEED_LIGHT, DEFAULT_LIGHT_NEED));
		lightModel
				.setStartTime(getString(KEY_LIGHT_START, DEFAULT_LIGHT_START));
	}

	public void updateWaterConfig() {

		waterModel.setMin(getInt(KEY_WATER_MIN, DEFAULT_WATER_MIN));
		waterModel.setTime(getInt(KEY_WATER_TIME, DEFAULT_WATER_TIME));
		waterModel.setWaterCount(getInt(KEY_WATER_COUNT, DEFAULT_WATER_COUNT));
		waterModel.setAlarmMax(getInt(KEY_WATER_ALARM_MAX,
				DEFAULT_WATER_ALARM_MAX));
		waterModel.setAlarmMin(getInt(KEY_WATER_ALARM_MIN,
				DEFAULT_WATER_ALARM_MIN));
		waterModel.setAlarmMaxEnable(getBoolean(KEY_WATER_AMAX_ENABLE,
				DEFAULT_WATER_AMAX_ENABLE));
		waterModel.setAlarmMinEnable(getBoolean(KEY_WATER_AMIN_ENABLE,
				DEFAULT_WATER_AMIN_ENABLE));
		waterModel.setMode(getBoolean(KEY_WATER_MODE, DEFAULT_WATER_MODE));
	}

	public void changeWaterMode(boolean mode) {

		save.putBoolean(KEY_WATER_MODE, mode);
		save.commit();

		waterModel.setMode(mode);

		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_MODE_CHANGE));
	}

	public void changeLightMode(boolean mode) {

		save.putBoolean(KEY_LIGHT_MODE, mode);
		save.commit();

		lightModel.setLightMode(mode);

		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_LIGHT_MODE_CHANGE));
	}

	public void saveWaterCfg() {

		save.putInt(KEY_WATER_ALARM_MAX, waterModel.getAlarmMax());
		save.putInt(KEY_WATER_ALARM_MIN, waterModel.getAlarmMin());
		save.putInt(KEY_WATER_TIME, waterModel.getTime());
		save.putInt(KEY_WATER_MIN, waterModel.getMin());
		save.putInt(KEY_WATER_COUNT, waterModel.getWaterCount());
		save.putBoolean(KEY_WATER_AMAX_ENABLE, waterModel.isAlarmMaxEnable());
		save.putBoolean(KEY_WATER_AMIN_ENABLE, waterModel.isAlarmMinEnable());
		save.putBoolean(KEY_WATER_MODE, waterModel.isMode());

		save.commit();
	}

	public void saveLightCfg() {

		save.putInt(KEY_NEED_LIGHT, lightModel.getNeedTime());
		save.putInt(KEY_MIN_LIGHT, lightModel.getMinValue());
		save.putInt(KEY_MAX_LIGHT, lightModel.getMaxValue());
		save.putInt(KEY_DIY_LIGHT, lightModel.getDiyTime());
		save.putInt(KEY_LIGHT_COUNT, lightModel.getCount());
		save.putString(KEY_LIGHT_START, lightModel.getStartTime());
		save.putInt(KEY_LIGHT_CURR, lightModel.getTimeCount());
		save.putLong(KEY_LIGHT_CURR_TIME, lightModel.getLastTime());
		save.putBoolean(KEY_LIGHT_MODE, lightModel.isLightMode());

		save.commit();
	}

	public void updateApplicationCfg() {

		mModel.setModeChange(getBoolean(KEY_MODE));
		mModel.setMorningOpenTime(getString(KEY_MORNING_OPEN));
		mModel.setVenTime(getInt(KEY_VEN_TIME, 30));
		mModel.setNightCloseTime(getString(KEY_NIGHT_CLOSE));
		mModel.setOpenLen(getInt(KEY_OPEN_LEN));
		mModel.setOpenLenFirst(getInt(KEY_OPEN_FIRST));
		mModel.setOpenLenSecond(getInt(KEY_OPEN_SECOND, 5));
		mModel.setOpenLenThird(getInt(KEY_OPEN_THIRD, 10));
		mModel.setOpenLenFourth(getInt(KEY_OPEN_FOURTH, 15));
		mModel.setOpenLenFifth(getInt(KEY_OPEN_FIFTH, 20));
		mModel.setPhonenumber(getString(KEY_PHONE_NUMBER));
		mModel.setPhonenumber2(getString(KEY_PHONE2));
		mModel.setPhonenumber3(getString(KEY_PHONE3));
		mModel.setWindowCount(getInt(KEY_WIN_COUNT));
		mModel.setMintuesCount(getInt(KEY_MINUTES_COUNT));
		mModel.setAlarmMax(getInt(KEY_TEMP_ALARM_MAX, 35));
		mModel.setAlarmMin(getInt(KEY_TEMP_ALARM_MIN, 15));
		mModel.setAlarmEnable(getBoolean(KEY_ALARM_ENABLE, true));
		mModel.setPushTime(getInt(KEY_PUSH_TIME, 10));
		mModel.setHighAlarmEnable(getBoolean(KEY_HIGH_ALARM_ENABLE, true));
		mModel.setLowAlarmEnable(getBoolean(KEY_LOW_ALARM_ENABLE, false));
		mModel.setWaterEnable(getBoolean(KEY_WATER_ENABLE, DEFAULT_WATER_ENABLE));
		mModel.setLightEnable(getBoolean(KEY_LIGHT_ENABLE, DEFAULT_LIGHT_ENABLE));
		mModel.setAutoOpenEnable(getBoolean(KEY_AUTO_OPEN, true));
		mModel.setBluetoothmac(getString(KEY_BLUETOOTH_MAC));

		int sum = 0;
		int five = mModel.getOpenLenFifth() * 1000 * Constants.MOTOR_SPEED;
		int exeTime = mModel.getOpenLen() * 1000 * Constants.MOTOR_SPEED;
		List<Integer> allStalls = new ArrayList<Integer>();

		sum += mModel.getOpenLenFirst() * 1000 * Constants.MOTOR_SPEED;
		allStalls.add(sum);
		sum += mModel.getOpenLenSecond() * 1000 * Constants.MOTOR_SPEED;
		allStalls.add(sum);
		// sum += mModel.getOpenLenThird() * 1000 * Constants.MOTOR_SPEED;
		// allStalls.add(sum);
		// sum += mModel.getOpenLenFourth() * 1000 * Constants.MOTOR_SPEED;
		// allStalls.add(sum);

		while (true) {

			sum += five;
			if (sum >= exeTime)
				break;

			allStalls.add(sum);
		}

		if (sum >= exeTime)
			allStalls.add(exeTime);
		Log.d("mmsg", "new all stalls -> " + allStalls);

		mModel.setAllStalls(allStalls);
	}

	/**
	 * Description 从配置文件中写入String型值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public void putString(String key, String value) {
		save.putString(key, value);
		save.commit();
	}

	/**
	 * Description 从配置文件中写入int型值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public void putInt(String key, int value) {
		save.putInt(key, value);
		save.commit();
	}

	/**
	 * Description 从配置文件中写入long型值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public void putLong(String key, long value) {
		save.putLong(key, value);
		save.commit();
	}

	/**
	 * Description 从配置文件中写入boolean型值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public void putBoolean(String key, boolean value) {
		save.putBoolean(key, value);
		save.commit();
	}

	/**
	 * Description 从配置文件中获取int型值
	 * 
	 * @param key
	 *            键
	 * @return 返回键对应的值，没有对应值则返回-1
	 */
	public int getInt(String key) {
		return configurationInfo.getInt(key, -1);
	}

	/**
	 * Description 从配置文件中获取int型值
	 * 
	 * @param key
	 *            键
	 * @return 返回键对应的值，没有对应值则返回defaultValue
	 */
	public int getInt(String key, int defaultValue) {
		return configurationInfo.getInt(key, defaultValue);
	}

	public static int getInt(Context context, String key, int defaultValue) {

		SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	/**
	 * Description 从配置文件中获取long型值
	 * 
	 * @param key
	 *            键
	 * @return 返回键对应的值，没有对应值则返回-1
	 */
	public long getLong(String key) {
		return configurationInfo.getLong(key, -1);
	}

	/**
	 * Description 从配置文件中获取long型值
	 * 
	 * @param key
	 *            键
	 * @return 返回键对应的值，没有对应值则返回-1
	 */
	public long getLong(String key, long defaultValue) {
		return configurationInfo.getLong(key, defaultValue);
	}

	/**
	 * Description 从配置文件中获取String型值
	 * 
	 * @param key
	 *            键
	 * @return 返回键对应的值，没有对应值则返回空字符串
	 */
	public String getString(String key) {
		return configurationInfo.getString(key, "");
	}

	public String getString(String key, String defaultValue) {
		return configurationInfo.getString(key, defaultValue);
	}

	public Boolean getBoolean(String key) {
		return configurationInfo.getBoolean(key, false);
	}

	public Boolean getBoolean(String key, boolean defaultValue) {
		return configurationInfo.getBoolean(key, defaultValue);
	}

	/**
	 * Description 从配置文件中移除某个键值对
	 * 
	 * @param key
	 *            键名
	 */
	public void Remove(String key) {
		save.remove(key);
		save.commit();
	}
}
