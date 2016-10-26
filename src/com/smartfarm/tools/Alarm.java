package com.smartfarm.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.util.Log;

import com.smartfarm.bean.RunningData;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;

/**
 * 报警管理
 * 
 * @author jeff
 *
 */
public class Alarm {
	
	private static final int FIRST_ALARM = 1;
	private static final int SECONDE_ALARM = 2;
	private static final int LAST_ALARM = 6;
	
	private Vector<AlarmModel> alarmList;
	private static Alarm mAlarm;
	
	private int alarmState = SECONDE_ALARM;
	private int nowAlarmState = 1;
	private int alarmCount = 1;
	
	private Alarm() {
		alarmList = new Vector<AlarmModel>();
	}
	
	public static Alarm getInstance() {
		
		if(mAlarm == null)
			mAlarm = new Alarm();
		
		return mAlarm;
	}
	
	/**
	 * 移除一个报警信息
	 * 
	 * @param model
	 * @return
	 */
	public boolean remove(AlarmModel model) {
		
		synchronized (this) {
			if(alarmList.contains(model)) {
				alarmList.remove(model);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 将异常转化为自然语言文字
	 * 
	 * @param err 异常信息
	 * @param windowCount 问题窗口id
	 * @return
	 */
	@SuppressLint("UseSparseArrays")
	public String getAlarmText(List<AlarmModel> err, int windowCount) {
		
		Log.d("mmsg", "err -> " + err);
		Log.d("mmsg", "windowCount -> " + windowCount);
		if(err.get(0).getErrorType() == Constants.ALARM_LINE_ERROR && err.size() >= windowCount) 
			return "检测到所有线路都无法连通，可能已断电。	";
		Map<Integer, StringBuilder> results = new HashMap<Integer, StringBuilder>();
		
		for(AlarmModel bean : err) {
			
			if(results.containsKey(bean.getErrorType())) {
				
				StringBuilder result = results.get(bean.getErrorType());
				
				result.append("窗口");
				result.append(bean.getWindowId() + 1);
				result.append(";");
			} else {
				
				StringBuilder result = new StringBuilder();
				
				if(bean.getErrorType() == Constants.ALARM_LINE_ERROR)
					result.append("检测到线路不连通：");
				else if(bean.getErrorType() == Constants.ALARM_CAN_NOT_READ_TEMP)
					result.append("无法读取温度：");
//				else if(bean.getErrorType() == Constants.ALARM_TEMP_TOO_HIGH)
//					result.append("当前模式："+(config.isAutoOrManual()?"自动模式":"手动模式"+"；")
//							+(config.isAutoOpenEnable()?"":"下雨关风被或者一键通风被打开，无法转入自动模式；"));
				else if(bean.getErrorType() == Constants.ALARM_TEMP_TOO_HIGH)
					result.append("检测到温度过高：");
				else 
					result.append("检测到温度过低：");
				
				result.append("窗口");
				result.append(bean.getWindowId() + 1);
				result.append(";");
				
				results.put(bean.getErrorType(), result);
			}
		}
		
		StringBuilder result = new StringBuilder();
		for(StringBuilder sb : results.values()) {
			result.append(sb.toString());
		}
		
		return result.toString();
	}
	
	/**
	 * 检查异常
	 * 
	 * @param activity
	 */
	public void Check() {
		List<AlarmModel> err = new ArrayList<AlarmModel>();
		
		int len = SimpleConfigManager.getInstance().getConfig().getWindowCount();
		
		for(int id = Constants.WINDOW_ID_1; id < len; id++) {

			boolean linkState = RunningData.getInstance().getWorkingState(id);
			
			if(!linkState) {
				err.add(new AlarmModel(Constants.ALARM_LINE_ERROR, id));
				continue;
			}
			
			tempCheck(Math.round(RunningData.getInstance().getTempRecordRecently(id)), id, err);
		}
		
		if(err.isEmpty()) {
			alarmState = SECONDE_ALARM;
			alarmCount = 1;
			nowAlarmState = 1;
		}
		if(isShouldAlarm() && !err.isEmpty()) {
			
			if(alarmState == FIRST_ALARM) {
				sendAlarm(err, len);
				
				if(alarmCount++ >= 3) {
					alarmState = SECONDE_ALARM;
					alarmCount = 1;
				}
			} else if(alarmState == SECONDE_ALARM) {
				
				if(nowAlarmState++ >= alarmState) {
					sendAlarm(err, len);
					
					if(alarmCount++ >= 3) {
						alarmState = LAST_ALARM;
						alarmCount = 1;
						nowAlarmState = 1;
					}
					nowAlarmState = 1;
				}
			} else {
				if(nowAlarmState++ >= alarmState) {
					sendAlarm(err, len);
					nowAlarmState = 1;
				}
			}
		}
	}

	/**
	 * 报告异常
	 * 
	 * @param err
	 */
	public void sendAlarm(String err, boolean needSendSMS) {
		AlarmModel alarm = new AlarmModel();
		alarm.setAlarmString(err);

		sendAlarm(alarm, needSendSMS);
	}
	
	/**
	 * 报告异常
	 * 
	 * @param err
	 * @param windowCount
	 */
	private void sendAlarm(List<AlarmModel> err, int windowCount) {
		AlarmModel alarm = new AlarmModel();
		alarm.setAlarmString(getAlarmText(err, windowCount));

		sendAlarm(alarm, true);
	}
	
	/**
	 * 报告异常
	 * 
	 * @param alarm
	 */
	private void sendAlarm(final AlarmModel alarm, boolean needSendSMS) {
		Log.d(Constants.TAG, " alarm -> " + alarm.getAlarmString());
		ProtocolFactory.getAlarmProtocol(alarm.getAlarmString(), "" + alarm.getTime()).send();
		
		if(!needSendSMS)
			return;
		
		if(alarm.getAlarmString().contains("检测到所有线路都无法连通"))
			CommonTool.ToReboot();
		
		String phone = SimpleConfigManager.getInstance().getConfig().getPhonenumber();
		
		if(phone.equals("")) {
			
			EventBus.getDefault().noticeMsg("未设置主手机号码,报警(" + alarm + ")发送失败.");
			return;
		}
		
		synchronized (this) {
			alarmList.add(alarm);
		}

		Log.d(Constants.TAG, "add alarm delay check!");
		EventBus.getDefault().runningOnUiThread(new Runnable() {
			
			@Override
			public void run() {

				if(Alarm.getInstance().remove(alarm)) {
					if(SimpleConfigManager.getInstance().getConfig().isAlarmEnable())
						Alarm.getInstance().smsAlarm(alarm.getAlarmString());
					else 
						return;
				}
			}
		}, 35000);
	}
	
	/**
	 * 通过短信报告异常
	 * 
	 * @param alarm
	 */
	public void smsAlarm(String alarm) {
		Date lastAlarm = new Date(SimpleConfigManager.getInstance().getLong("lastAlarm"));
		int sendMsgCount =  SimpleConfigManager.getInstance().getInt("sendMsgCount");
		long tiemDifference = new Date().getTime() - lastAlarm.getTime();
		
		boolean shouldSend = tiemDifference > SimpleConfigManager.getInstance().getConfig().getMintuesCount() * 60 * 1000;
		Log.e(Constants.TAG, "should send sms msg alarm -> " + shouldSend );
		if(shouldSend) {
			SimpleConfigManager.getInstance().putInt("sendMsgCount", ++sendMsgCount);
			SimpleConfigManager.getInstance().putLong("lastAlarm", System.currentTimeMillis());
			
			CommonTool.SendSMS(SimpleConfigManager.getInstance()
					.getConfig().getPhonenumber(), alarm);
		}
	}
	
	/**
	 * 温度检查
	 * 
	 * @param controller 温度管理控制器
	 * @param windowId 窗口id
	 * @param err 异常容器
	 */
	public void tempCheck(int temp, int windowId, List<AlarmModel> err) {

		ConfigModel config = SimpleConfigManager.getInstance().getConfig();
		
		if (temp <= 0) {
			
			err.add(new AlarmModel(Constants.ALARM_CAN_NOT_READ_TEMP, windowId));
			
		} else {
			
			if (temp >= config.getAlarmMax() && config.isHighAlarmEnable()) 
				err.add(new AlarmModel(Constants.ALARM_TEMP_TOO_HIGH, windowId));

			if (temp <= config.getAlarmMin() && config.isLowAlarmEnable()) 
				err.add(new AlarmModel(Constants.ALARM_TEMP_TOO_LOW, windowId));
		}
	}
	
	/**
	 * 
	 * 是否应该报警
	 * 
	 * @return
	 */
	public static boolean isShouldAlarm() {
		try {
			Date nowdate = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(nowdate);
			
			String startTimeString = SimpleConfigManager.getInstance().getConfig().getMorningOpenTime();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(sdf.parse(startTimeString));
			
			calendar.set(Calendar.HOUR_OF_DAY, startCalendar.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, startCalendar.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, startCalendar.get(Calendar.SECOND));
			Date startDate = calendar.getTime();
			
			if(startDate.getTime() > nowdate.getTime()) {
				calendar.setTime(nowdate);
				calendar.add(Calendar.DATE, 1);
				nowdate = calendar.getTime();
			}
			
			if (startDate.getTime() <= nowdate.getTime()) {

				calendar.setTime(startDate);
				String endTimeString = SimpleConfigManager.getInstance().getConfig().getNightCloseTime();
				startCalendar.setTime(sdf.parse(endTimeString));
				
				calendar.set(Calendar.HOUR_OF_DAY, startCalendar.get(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, startCalendar.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, startCalendar.get(Calendar.SECOND));
				Date endDate = calendar.getTime();

				if (endDate.getTime() <= startDate.getTime()) {
					calendar.add(Calendar.DATE, 1);
					endDate = calendar.getTime(); 
				}

				if (endDate.getTime() >= nowdate.getTime()) {
					return false;
				}
			}
			return true;

		} catch (Exception e) {
			
			e.printStackTrace();
			return true;
		}
	}
}
