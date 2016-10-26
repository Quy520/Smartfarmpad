package com.smartfarm.protocol;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.bean.VenBean;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.SimpleConfigManager;

public class SmsMsgResolve {
	
	public static String CLOSE = "关闭";
	public static String OPEN = "打开";
	public static String STOP = "停止";
	public static String TEMP = "查看";
	public static String RAIN_CLOSE = "下雨";
	public static String MODE_AUTO = "温度自动";
	public static String MODE_UNAUTO = "温度手动";
	public static String MODE_AUTO_OPEN = "高温切自动";
	public static String MODE_AUTO_CLOSE = "高温不变";
	public static String OPEN_MAX = "开至最大";
	public static String VENTILATION_END = "结束放晨风";
	public static String VENTILATION = "放晨风";
	public static String VENTILATION_REGULAR = "放晨风\\d+";
	public static String OPEN_FIRST = "设置第一档";
	public static String OPEN_FIRST_REGULAR = "设置第一档\\d+";
	public static String OPEN_SECOND = "设置第二档";
	public static String OPEN_SECOND_REGULAR = "设置第二档\\d+";
	public static String OPEN_FREE = "设置自由档";
	public static String OPEN_FREE_REGULAR = "设置自由档\\d+";
	public static String OPEN_LEN = "设置总长";
	public static String OPEN_LEN_REGULAR = "设置总长\\d+";
	
	public static String TEMP_MAX = "上限温度";
	public static String TEMP_MAX_REGULAR = "上限温度\\d+";
	public static String TEMP_MAX_TH = "下限温度";
	public static String TEMP_MAX_TH_REGULAR = "下限温度\\d+";
	public static String TEMP_MIN_TH = "开风温度";
	public static String TEMP_MIN_TH_REGULAR = "开风温度\\d+";
	public static String TEMP_MIN = "关风温度";
	public static String TEMP_MIN_REGULAR = "关风温度\\d+";
	public static String TEMP_ALARM_MAX = "报警高温";
	public static String TEMP_ALARM_MAX_REGULAR = "报警高温\\d+";
	public static String TEMP_ALARM_MIN = "报警低温";
	public static String TEMP_ALARM_MIN_REGULAR = "报警低温\\d+";
	
	public static void resolve(String smsData, String phoneNum) {
		
		if(smsData.contains("&")) {
			String[] smsDatas = smsData.split("&");
			
			for(String sms : smsDatas)
				resolveSingle(sms, phoneNum);
		} else {
			
			resolveSingle(smsData, phoneNum);
		}
	}
	
	public static void resolveSingle(String smsData, String phoneNum) {
		
		
			SimpleConfigManager scManager = SimpleConfigManager.getInstance();
			
			if (smsData.contains(CLOSE)) {
				
				List<Integer> ids = resolveWindowId(smsData.replace(CLOSE, ""));
				
				if(ids.isEmpty()) {

					SerialHelper.closeAll(SerialControlRunable.SOURCE_PHONE);
					Log.d("mmsg", "close all !");
				} else {
					for(int id : ids)
						SerialHelper.exeClose(SerialControlRunable.SOURCE_PHONE, id);
					Log.d("mmsg", "close : " + ids);
				}
				
			} else if (smsData.contains(OPEN)) {
				
				List<Integer> ids = resolveWindowId(smsData.replace(OPEN, ""));
				
				if(ids.isEmpty()) {

					Log.d("mmsg", "open all !");
					SerialHelper.openAll(SerialControlRunable.SOURCE_PHONE);
				}
				else {
					for(int id : ids)
						SerialHelper.exeOpen(SerialControlRunable.SOURCE_PHONE, id);
					Log.d("mmsg", "open : " + ids);
				}
				
			} else if (smsData.contains(STOP)) {
				
				List<Integer> ids = resolveWindowId(smsData.replace(STOP, ""));
				
				if(ids.isEmpty()) {

					Log.d("mmsg", "stop all !");
					SerialHelper.stopAll(SerialControlRunable.SOURCE_PHONE);
				} else {
					for(int id : ids)
						SerialHelper.exeStop(SerialControlRunable.SOURCE_PHONE, id);
					Log.d("mmsg", "stop : " + ids);
				}
				
			} else if (smsData.contains(RAIN_CLOSE)) {

				SerialHelper.rainCloseAll(SerialControlRunable.SOURCE_PHONE);

				scManager.modeChange(SerialControlRunable.SOURCE_PHONE, false);
				scManager.modeOpenChange(SerialControlRunable.SOURCE_PHONE, false);
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_MODE_CHANGE));
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_OPEN_ENABLE));
				Log.d("mmsg", "rain close");
				
			} else if (smsData.contains(MODE_UNAUTO)) {

				scManager.modeChange(SerialControlRunable.SOURCE_PHONE, false);
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_MODE_CHANGE));
				Log.d("mmsg", "change shoudong");

			} else if (smsData.contains(MODE_AUTO)) {

				scManager.modeChange(SerialControlRunable.SOURCE_PHONE, true);
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_MODE_CHANGE));
				Log.d("mmsg", "change auto");
				
			} else if(smsData.contains(MODE_AUTO_OPEN)){
				
				scManager.modeOpenChange(SerialControlRunable.SOURCE_PHONE, true);
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_OPEN_ENABLE));
			} else if(smsData.contains(MODE_AUTO_CLOSE)){
				
				scManager.modeOpenChange(SerialControlRunable.SOURCE_PHONE, false);
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_OPEN_ENABLE));
			}
			else if (smsData.contains(TEMP)) {

				CommonTool.SendSMS(phoneNum, RunningData.getInstance().getTempInfo());
				Log.d("mmsg", "gettemp");
				
			} else if (smsData.contains(OPEN_MAX)) {
				
				SerialHelper.openAllMax(SerialControlRunable.SOURCE_PHONE);
				Log.d("mmsg", "open max");
				
			} else if (smsData.matches(OPEN_FIRST_REGULAR)) {
				
				int openFirst = Integer.valueOf(smsData.replace(OPEN_FIRST, ""));
				
				if(openFirst > 50)
					return;
				
				scManager.putInt(SimpleConfigManager.KEY_OPEN_FIRST, openFirst);
				scManager.updateApplicationCfg();
				Log.d("mmsg", "set open first -> " + openFirst);
				
			} else if (smsData.matches(OPEN_SECOND_REGULAR)) {
				
				int len = Integer.valueOf(smsData.replace(OPEN_SECOND, ""));
				
				scManager.putInt(SimpleConfigManager.KEY_OPEN_SECOND, len);
				scManager.updateApplicationCfg();
				Log.d("mmsg", "set open second -> " + len);
				
			} else if (smsData.matches(OPEN_FREE_REGULAR)) {
				
				int len = Integer.valueOf(smsData.replace(OPEN_FREE, ""));
				
				scManager.putInt(SimpleConfigManager.KEY_OPEN_FIFTH, len);
				scManager.updateApplicationCfg();
				Log.d("mmsg", "set open free -> " + len);
				
			} else if (smsData.matches(OPEN_LEN_REGULAR)) {

				int openLen = Integer.valueOf(smsData.replace(OPEN_LEN, ""));

				if(openLen < 50)
					return;
				
				scManager.putInt(SimpleConfigManager.KEY_OPEN_LEN, openLen);
				scManager.updateApplicationCfg();
				Log.d("mmsg", "set open len -> " + openLen);
				
			} else if (smsData.matches(TEMP_MAX_REGULAR)) {
				
				//短信设置适宜温度上限
				
//				int tempMax = Integer.valueOf(smsData.replace(TEMP_MAX, ""));

//				if(tempMax <= scManager.getConfig().getTempNorMax())
//					return;
				
//				scManager.putInt(SimpleConfigManager.KEY_TEMP_MAX, tempMax);
//				scManager.updateApplicationCfg();
//				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_CFG_CHANGE));
//				Log.d("mmsg", "set temp max -> " + tempMax);
				
			} else if (smsData.matches(TEMP_MIN_REGULAR)) {

				//短信设置关风温度
				
//				int tempMin = Integer.valueOf(smsData.replace(TEMP_MIN, ""));
//
//				if(tempMin >= scManager.getConfig().getTempNorMin())
//					return;
//				
//				scManager.putInt(SimpleConfigManager.KEY_TEMP_MIN, tempMin);
//				scManager.updateApplicationCfg();
//				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_CFG_CHANGE));
//				Log.d("mmsg", "set temp min -> " + tempMin);
				
			} else if (smsData.matches(TEMP_MAX_TH_REGULAR)) {

				//短信设置开风温度
				
//				int tempMaxTh = Integer.valueOf(smsData.replace(TEMP_MAX_TH, ""));
//
//				if(tempMaxTh >= scManager.getConfig().getTempMax() || tempMaxTh <= scManager.getConfig().getTempMin())
//					return;
//				
//				scManager.putInt(SimpleConfigManager.KEY_TEMP_NOR_MAX, tempMaxTh);
//				scManager.updateApplicationCfg();
//				Log.d("mmsg", "set tempMaxTh -> " + tempMaxTh);
				
			} else if (smsData.matches(TEMP_MIN_TH_REGULAR)) {

				//短信设置适宜温度下限
				
//				int tempMinTh = Integer.valueOf(smsData.replace(TEMP_MIN_TH, ""));
//
//				if(tempMinTh >= scManager.getConfig().getTempNorMax() || tempMinTh <= scManager.getConfig().getTempMin())
//					return;
//				
//				scManager.putInt(SimpleConfigManager.KEY_TEMP_NOR_MIN, tempMinTh);
//				scManager.updateApplicationCfg();
//				Log.d("mmsg", "set tempMinTh -> " + tempMinTh);
				
			} else if (smsData.matches(TEMP_ALARM_MAX_REGULAR)) {

				int temp = Integer.valueOf(smsData.replace(TEMP_ALARM_MAX, ""));

				if(temp <= scManager.getConfig().getAlarmMin())
					return;
				
				scManager.putInt(SimpleConfigManager.KEY_TEMP_ALARM_MAX, temp);
				scManager.putBoolean(SimpleConfigManager.KEY_HIGH_ALARM_ENABLE, true);
				scManager.updateApplicationCfg();
				Log.d("mmsg", "set alarm max -> " + temp);
				
			} else if (smsData.matches(TEMP_ALARM_MIN_REGULAR)) {

				int temp = Integer.valueOf(smsData.replace(TEMP_ALARM_MIN, ""));

				if(temp >= scManager.getConfig().getAlarmMax())
					return;
				
				scManager.putInt(SimpleConfigManager.KEY_TEMP_ALARM_MIN, temp);
				scManager.putBoolean(SimpleConfigManager.KEY_LOW_ALARM_ENABLE, true);
				scManager.updateApplicationCfg();
				Log.d("mmsg", "set alarm min -> " + temp);
				
			} else if(smsData.matches(VENTILATION_REGULAR)) {

				SerialHelper.openAll(SerialControlRunable.SOURCE_PHONE);
				int ven = Integer.valueOf(smsData.replace(VENTILATION, "")) * 60000 
						+ SimpleConfigManager.getInstance().getConfig().getOpenLenFirst() * Constants.MOTOR_SPEED * 1000;
				Log.d("mmsg", "ven -> " + ven);
				
				VenBean bean = new VenBean(ven);
				bean.start();
				
			} else if(smsData.contains(VENTILATION_END)) {
				
				VenBean bean = AppContext.context().getVenBean();
				
				if(bean != null) 
					bean.endVen();
			} else if(smsData.contains(VENTILATION)) {

				SerialHelper.openAll(SerialControlRunable.SOURCE_PHONE);
				Log.d("mmsg", "ven use default ventime ");
				
				VenBean bean = new VenBean();
				bean.start();
				
			}
		
	}
	
	private static List<Integer> resolveWindowId(String windowId) {
		List<Integer> ids = new ArrayList<Integer>();
		
		String[] idStrings = windowId.split("");
		
		for(String s : idStrings) 
			if(s.matches("\\d")) 
				ids.add(Integer.valueOf(s) - 1);
		
		return ids;
	}
}
