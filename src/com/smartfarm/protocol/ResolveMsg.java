package com.smartfarm.protocol;

import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.smartfarm.bean.LightConfig;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.bean.TempConfigModel;
import com.smartfarm.bean.VenBean;
import com.smartfarm.bean.WaterConfig;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.SerialHelper.OnTaskFinishListener;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.tools.Alarm;
import com.smartfarm.tools.AlarmModel;
import com.smartfarm.tools.AssistThread;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.XmlUtils;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;

public class ResolveMsg {

	public static void resolve(final Protocol receProtocol) {

		ConfigModel config = SimpleConfigManager.getInstance().getConfig();

		if (receProtocol == null)
			return;

		int user = 0;
		if (receProtocol.getSender().equals(config.getPhonenumber()))
			user = 0;
		else if (receProtocol.getSender().equals(config.getPhonenumber2()))
			user = 1;
		else if (receProtocol.getSender().equals(config.getPhonenumber3()))
			user = 2;
		else
			user = 3;

		final Protocol feedback = new Protocol(receProtocol);

		feedback.setReceiver(receProtocol.getSender());
		feedback.setSender(receProtocol.getReceiver());
		feedback.setPwd("");

		switch (feedback.getProtocolType()) {
		case Constants.PROTOCOL_TYPE_REQUEST:

			switch (feedback.getCmdType()) {
			case Constants.MOTOR_CONTROL_TYPE_TEST:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				feedback.send();
				break;
			case Constants.MOTOR_CONTROL_TYPE_NOTICE:

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				feedback.send();

				if (feedback.getData().contains("close"))
					CommonTool.ToReboot();
				break;
			case Constants.MOTOR_CONTROL_TYPE_MODE:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (AppContext.context().inVenMode()) {
					AppContext.context().getVenBean().endVen();
					SerialHelper.stopAll(SerialControlRunable.SOURCE_PAD_BUTTON);
				}

				String newMode = feedback.getData();
				feedback.setData("");

				if (newMode.contains("auto")) {
					SimpleConfigManager.getInstance().modeChange(SerialControlRunable.SOURCE_PHONE, true);

				} else if (newMode.contains("no")) {
					SimpleConfigManager.getInstance().modeChange(SerialControlRunable.SOURCE_PHONE, false);

				} else {
					feedback.setData(config.isAutoOrManual() ? "auto" : "no");
				}

				feedback.send();
				EventBus.getDefault().postInOtherThread(
						LocalEvent.getEvent(LocalEvent.EVENT_TYPE_MODE_CHANGE));

				break;

			case Constants.MOTOR_CONTROL_TYPE_HIGH_MODE:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				/*
				 * if (AppContext.context().inVenMode()) {
				 * AppContext.context().getVenBean().endVen(); }
				 */
				String highMode = feedback.getData();
				feedback.setData("");

				if (highMode.contains("auto")) {
					SimpleConfigManager.getInstance().modeOpenChange(SerialControlRunable.SOURCE_PHONE, true);

				} else if (highMode.contains("no")) {
					SimpleConfigManager.getInstance().modeOpenChange(SerialControlRunable.SOURCE_PHONE, false);

				} else {
					feedback.setData(config.isAutoOrManual() ? "auto" : "no");
				}
				// feedback.send();
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_OPEN_ENABLE));
				break;

			case Constants.MOTOR_CONTROL_TYPE_RAIN_MODE:
				if (AppContext.context().inVenMode()) {
					AppContext.context().getVenBean().endVen();
				}
				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				String rainMode = feedback.getData();
				feedback.setData("");
				if (rainMode.contains("rain")) {
					SerialHelper.stopAll(SerialControlRunable.SOURCE_AUTO);
					SerialHelper.rainCloseAll(SerialControlRunable.SOURCE_AUTO);
					SimpleConfigManager.getInstance().modeChangeAll(SerialControlRunable.SOURCE_PHONE, false);
				} else if (rainMode.contains("stop")) {
					SerialHelper.stopAll(SerialControlRunable.SOURCE_AUTO);
					SimpleConfigManager.getInstance().modeChangeLast(SerialControlRunable.SOURCE_AUTO, false,true);
				}
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RAIN_MODE,rainMode));
				break;

			case Constants.MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE:
				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				String windMode = feedback.getData();
				feedback.setData("");
				if (windMode.contains("wind")) {
					SerialHelper.stopAll(SerialControlRunable.SOURCE_AUTO);
					SerialHelper.openAll(SerialControlRunable.SOURCE_AUTO);
					new VenBean().start();
				} else if (windMode.contains("stop")) {
					if (AppContext.context().inVenMode())
						AppContext.context().getVenBean().endVen();
				}
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WIND_MODE,windMode));
				break;

			case Constants.MOTOR_CONTROL_TYPE_OPEN:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (!Alarm.isShouldAlarm()) {

					feedback.setData("night");
					feedback.send();
					return;
				}

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				if (AppContext.context().inVenMode()) {
					feedback.setData("error");
					feedback.send();
					return;
				}

				if (feedback.getWindowId().contains("ven")) {

					SerialHelper.openAll(SerialControlRunable.SOURCE_PHONE,
							new OnControlFinishListener(feedback));

					new VenBean().start();
				} else {
					SerialHelper.exeOpenMore(SerialControlRunable.SOURCE_PHONE,
							feedback.getWindowIds(),
							new OnControlFinishListener(feedback));
					/*SimpleConfigManager.getInstance().modeChange(
							SerialControlRunable.SOURCE_PHONE, false);
					SimpleConfigManager.getInstance().modeOpenChange(
							SerialControlRunable.SOURCE_PHONE, false);*/
				}
				break;
			case Constants.MOTOR_CONTROL_TYPE_CLOSE:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (!Alarm.isShouldAlarm()) {

					feedback.setData("night");
					feedback.send();
					return;
				}

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				if (AppContext.context().inVenMode()
						&& !feedback.getWindowId().contains("all")) {
					feedback.setData("error");
					feedback.send();
					return;
				}

				if (feedback.getWindowId().contains("all")) {

					VenBean venBean = AppContext.context().getVenBean();
					if (venBean != null
							&& System.currentTimeMillis() > venBean
									.getStartTime()) {
						venBean.endVen();

						SerialHelper
								.rainCloseAll(SerialControlRunable.SOURCE_PHONE);

						feedback.setData("end");
						SimpleConfigManager.getInstance().modeChange(
								SerialControlRunable.SOURCE_PHONE, false);
						SimpleConfigManager.getInstance().modeOpenChange(
								SerialControlRunable.SOURCE_PHONE, false);
						feedback.send();

					} else if (venBean != null) {

						feedback.setData("error");
						feedback.send();
						return;
					} else {
						SerialHelper.rainCloseAll(
								SerialControlRunable.SOURCE_PHONE,
								new OnControlFinishListener(feedback));
						SimpleConfigManager.getInstance().modeChange(
								SerialControlRunable.SOURCE_PHONE, false);
						SimpleConfigManager.getInstance().modeOpenChange(
								SerialControlRunable.SOURCE_PHONE, false);
					}

				} else {

					SerialHelper.exeCloseMore(
							SerialControlRunable.SOURCE_PHONE,
							feedback.getWindowIds(),
							new OnControlFinishListener(feedback));
				}

				break;
			case Constants.MOTOR_CONTROL_TYPE_READ:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				SerialHelper.readAll(SerialControlRunable.SOURCE_PHONE,
						new OnTaskFinishListener() {
							@Override
							public void onTaskFinish(String res) {
								feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
								feedback.setData(RunningData.getInstance()
										.getTempInfo());
								feedback.send();
							}
						});
				break;
			case Constants.MOTOR_CONTROL_TYPE_STOP:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (!Alarm.isShouldAlarm()) {

					feedback.setData("night");
					feedback.send();
					return;
				}

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				if (AppContext.context().inVenMode()) {
					feedback.setData("error");
					feedback.send();
					return;
				}

				SerialHelper.ExeStopMore(SerialControlRunable.SOURCE_PHONE,
						feedback.getWindowIds(), new OnControlFinishListener(
								feedback));

				break;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_CONFIG:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (feedback.getWindowId().equals("get")) {

					StringBuilder result = new StringBuilder();
					LightConfig light = SimpleConfigManager.getInstance()
							.getLightModel();

					result.append("*setMax:");
					result.append(light.getMaxValue());
					result.append("*setMin:");
					result.append(light.getMinValue());
					result.append("*setNeed:");
					result.append(light.getNeedTime());
					result.append("*setDiy:");
					result.append(light.getDiyTime());
					result.append("*setStart:");
					result.append(light.getStartTime());

					feedback.setData(result.toString());
					feedback.send();
				} else {

					String[] synData = feedback.getData().split("\\*");
					Editor save = SimpleConfigManager.getInstance().getEditor();

					for (String s : synData) {
						try {
							if (s.contains("setMax")) {

								save.putInt(SimpleConfigManager.KEY_MAX_LIGHT,
										Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setMin")) {

								save.putInt(SimpleConfigManager.KEY_MIN_LIGHT,
										Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setNeed")) {

								save.putInt(SimpleConfigManager.KEY_NEED_LIGHT,
										Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setDiy")) {

								save.putInt(SimpleConfigManager.KEY_DIY_LIGHT,
										Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setStart")) {

								save.putString(
										SimpleConfigManager.KEY_LIGHT_START,
										s.split(":", 2)[1]);
							}

						} catch (Exception e) {
							// ingore dirty data
						}
					}

					feedback.setData("");
					feedback.send();
					save.commit();
					SimpleConfigManager.getInstance().updateLightCfg();
				}

				break;
			case Constants.MOTOR_CONTROL_TYPE_WATER_CONFIG:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (feedback.getWindowId().equals("get")) {

					StringBuilder result = new StringBuilder();
					WaterConfig wc = SimpleConfigManager.getInstance()
							.getWaterModel();

					result.append("*setMin:");
					result.append(wc.getMin());
					result.append("*setAlarmMax:");
					result.append(wc.getAlarmMax());
					result.append("*setAlarmMin:");
					result.append(wc.getAlarmMin());
					result.append("*setTime:");
					result.append(wc.getTime());
					result.append("*setAlarmMaxEnable:");
					result.append(wc.isAlarmMaxEnable());
					result.append("*setAlarmMinEnable:");
					result.append(wc.isAlarmMinEnable());

					feedback.setData(result.toString());
					feedback.send();
				} else {

					String[] synData = feedback.getData().split("\\*");
					Editor save = SimpleConfigManager.getInstance().getEditor();

					for (String s : synData) {
						try {
							if (s.contains("setMin")) {

								save.putInt(SimpleConfigManager.KEY_WATER_MIN,
										Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setAlarmMinEnable")) {

								save.putBoolean(
										SimpleConfigManager.KEY_WATER_AMIN_ENABLE,
										s.contains("true"));
							} else if (s.contains("setAlarmMaxEnable")) {

								save.putBoolean(
										SimpleConfigManager.KEY_WATER_AMAX_ENABLE,
										s.contains("true"));
							} else if (s.contains("setAlarmMax")) {

								save.putInt(
										SimpleConfigManager.KEY_WATER_ALARM_MAX,
										Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setAlarmMin")) {

								save.putInt(
										SimpleConfigManager.KEY_WATER_ALARM_MIN,
										Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setTime")) {

								save.putInt(SimpleConfigManager.KEY_WATER_TIME,
										Integer.valueOf(s.split(":", 2)[1]));
							}

						} catch (Exception e) {
							// ingore dirty data
						}
					}

					feedback.setData("");
					feedback.send();
					save.commit();
					SimpleConfigManager.getInstance().updateWaterConfig();
				}

				break;
			case Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				if (feedback.getWindowId().contains("rump"))
					SerialHelper.closeBeng(SerialControlRunable.SOURCE_PHONE,
							new OnControlFinishListener(feedback), false);
				else if (feedback.getWindowId().contains("yao"))
					SerialHelper.closeYao(SerialControlRunable.SOURCE_PHONE,
							new OnControlFinishListener(feedback));
				else if (feedback.getWindowId().contains("onekey"))
					SerialHelper.closeKeyWater(
							SerialControlRunable.SOURCE_PHONE,
							new OnControlFinishListener(feedback));
				else
					SerialHelper.closeWater(SerialControlRunable.SOURCE_PHONE,
							feedback.getWindowIds(),
							new OnControlFinishListener(feedback), false);

				break;
			case Constants.MOTOR_CONTROL_TYPE_WATER_OPEN:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				if (feedback.getWindowId().contains("rump")) {
					if (RunningData.getInstance().judgeWaterState())
						SerialHelper.openBeng(
								SerialControlRunable.SOURCE_PHONE,
								new OnControlFinishListener(feedback), false);
					else {
						feedback.setData("no");
						feedback.send();
					}
				} else if (feedback.getWindowId().contains("yao")) {

					SerialHelper.openYao(SerialControlRunable.SOURCE_PHONE,
							new OnControlFinishListener(feedback));
				} else if (feedback.getWindowId().contains("onekey"))
					SerialHelper.openKeyWater(
							SerialControlRunable.SOURCE_PHONE,
							new OnControlFinishListener(feedback));
				else {
					SerialHelper.openWater(SerialControlRunable.SOURCE_PHONE,
							feedback.getWindowIds(),
							new OnControlFinishListener(feedback), false);
				}
				break;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_OPEN:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				SerialHelper.openLight(SerialControlRunable.SOURCE_PHONE,
						feedback.getWindowIds(), new OnControlFinishListener(
								feedback));

				break;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_CLOSE:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);

				if (RunningData.getInstance().checkTime(user,
						feedback.getTime()))
					return;

				SerialHelper.closeLight(SerialControlRunable.SOURCE_PHONE,
						feedback.getWindowIds(), new OnControlFinishListener(
								feedback));

				break;
			case Constants.MOTOR_CONTROL_TYPE_WATER_READ:

				if (!feedback.getWindowId().contains("btn")) {

					feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
					feedback.setData(RunningData.getInstance().getWaterInfo());
					feedback.send();
				} else {
					SerialHelper.readWater(SerialControlRunable.SOURCE_PHONE,
							new OnTaskFinishListener() {

								@Override
								public void onTaskFinish(String res) {

									feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
									feedback.setData(RunningData.getInstance()
											.getWaterInfo());
									feedback.send();
								}
							});
				}
				break;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_READ:

				if (!feedback.getWindowId().contains("btn")) {

					feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
					feedback.setData(RunningData.getInstance().getLightInfo());
					feedback.send();
				} else {
					SerialHelper.readLight(SerialControlRunable.SOURCE_PHONE,
							new OnTaskFinishListener() {

								@Override
								public void onTaskFinish(String res) {

									feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
									feedback.setData(RunningData.getInstance()
											.getLightInfo());
									feedback.send();
								}
							});
				}
				break;
			/*
			 * case Constants.MOTOR_CONTROL_TYPE_READ_OTHERS: if
			 * (!feedback.getWindowId().contains("btn")) {
			 * 
			 * feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
			 * feedback.setData(RunningData.getInstance().getOthers());
			 * feedback.send(); } else {
			 * SerialHelper.readOthers(SerialControlRunable.SOURCE_PHONE, new
			 * OnTaskFinishListener() {
			 * 
			 * @Override public void onTaskFinish(String res) {
			 * feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
			 * feedback.setData(RunningData.getInstance() .getOthers());
			 * feedback.send(); } }); } break;
			 */
			case Constants.MOTOR_CONTROL_TYPE_WATER_STATE:

				break;
			case Constants.MOTOR_CONTROL_TYPE_WATER_MODE:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);

				SimpleConfigManager.getInstance().changeWaterMode(
						feedback.getData().contains("auto"));

				feedback.setData(SimpleConfigManager.getInstance()
						.getWaterModel().isMode() ? "auto" : "no");
				feedback.send();

				break;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_MODE:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);

				SimpleConfigManager.getInstance().changeLightMode(
						feedback.getData().contains("auto"));

				feedback.setData(SimpleConfigManager.getInstance()
						.getLightModel().isLightMode() ? "auto" : "no");
				feedback.send();

				break;
			case Constants.MOTOR_CONTROL_TYPE_SYN_TEMP:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);

				TempConfigModel model = XmlUtils.toBean(TempConfigModel.class,
						feedback.getData().getBytes());
				SimpleConfigManager.getInstance().getTempModel().synTemp(model);

				feedback.setData("");
				feedback.send();

				break;
			case Constants.MOTOR_CONTROL_TYPE_SYN:

				feedback.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				if (feedback.getWindowId().equals("get")) {

					AssistThread.getInstance().setThreadWork(
							new AssistThreadWork() {

								@Override
								public void working() {
									StringBuilder result = new StringBuilder();
									ConfigModel config = SimpleConfigManager.getInstance().getConfig();

									result.append("*setOpenLen:");
									result.append(config.getOpenLen());
									result.append("*alarmMax:");
									result.append(config.getAlarmMax());
									result.append("*alarmMin:");
									result.append(config.getAlarmMin());
									result.append("*isAlarmEnable:");
									result.append(config.isAlarmEnable());
									result.append("*pushTime:");
									result.append(config.getPushTime());
									result.append("*o1:");
									result.append(config.getOpenLenFirst());
									result.append("*o2:");
									result.append(config.getOpenLenSecond());
									result.append("*o3:");
									result.append(config.getOpenLenThird());
									result.append("*o4:");
									result.append(config.getOpenLenFourth());
									result.append("*o5:");
									result.append(config.getOpenLenFifth());
									result.append("*ha:");
									result.append(config.isHighAlarmEnable());
									result.append("*la:");
									result.append(config.isLowAlarmEnable());
									result.append("*MorningTimePeriod:");
									result.append(config.getVenTime());

									if (!config.getMorningOpenTime().equals("")) {
										result.append("*MorningOpenTime:");
										result.append(config
												.getMorningOpenTime());
									}

									if (!config.getNightCloseTime().equals("")) {

										result.append("*NightCloseTime:");
										result.append(config
												.getNightCloseTime());

									}
									
									feedback.setData(result.toString());
									feedback.send();

									feedback.setCmdType(Constants.MOTOR_CONTROL_TYPE_SYN_TEMP);
									feedback.setData(XmlUtils.toXml(SimpleConfigManager.getInstance().getTempModel()));
									feedback.send();
								}

								@Override
								public void ok() {
								}

							});

				} else {

					AssistThread.getInstance().setThreadWork(
							new AssistThreadWork() {

								@Override
								public void working() {
									String[] synData = feedback.getData()
											.split("\\*");
									Editor save = SimpleConfigManager
											.getInstance().getEditor();

									for (String s : synData) {
										try {
											if (s.contains("setOpenLen")) {

												save.putInt(
														SimpleConfigManager.KEY_OPEN_LEN,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s
													.contains("MorningTimePeriod")) {

												save.putInt(
														SimpleConfigManager.KEY_VEN_TIME,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s
													.contains("MorningOpenTime")) {

												save.putString(
														SimpleConfigManager.KEY_MORNING_OPEN,
														s.split(":", 2)[1]);
											} else if (s
													.contains("NightCloseTime")) {

												save.putString(
														SimpleConfigManager.KEY_NIGHT_CLOSE,
														s.split(":", 2)[1]);
											} else if (s.contains("isAlarmEnable")) {
												save.putBoolean(SimpleConfigManager.KEY_ALARM_ENABLE,s.split(":", 2)[1].contains("true"));
											}else if (s.contains("alarmMax")) {

												save.putInt(
														SimpleConfigManager.KEY_TEMP_ALARM_MAX,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("alarmMin")) {

												save.putInt(
														SimpleConfigManager.KEY_TEMP_ALARM_MIN,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("pushTime")) {

												save.putInt(
														SimpleConfigManager.KEY_PUSH_TIME,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("o1")) {

												save.putInt(
														SimpleConfigManager.KEY_OPEN_FIRST,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("o2")) {

												save.putInt(
														SimpleConfigManager.KEY_OPEN_SECOND,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("o3")) {

												save.putInt(
														SimpleConfigManager.KEY_OPEN_THIRD,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("o4")) {

												save.putInt(
														SimpleConfigManager.KEY_OPEN_FOURTH,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("o5")) {

												save.putInt(
														SimpleConfigManager.KEY_OPEN_FIFTH,
														Integer.valueOf(s
																.split(":", 2)[1]));
											} else if (s.contains("ha")) {

												save.putBoolean(
														SimpleConfigManager.KEY_HIGH_ALARM_ENABLE,
														s.split(":", 2)[1].contains("true"));
											} else if (s.contains("la")) {

												save.putBoolean(SimpleConfigManager.KEY_LOW_ALARM_ENABLE,
														s.split(":", 2)[1].contains("true"));
											} 

										} catch (Exception e) {
											// ingore dirty data
										}
									}

									feedback.setData("");
									feedback.send();
									save.commit();
									SimpleConfigManager.getInstance()
											.updateApplicationCfg();
								}

								@Override
								public void ok() {

									EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_CFG_CHANGE));
								}
							});
				}

				break;
			}

			break;
		case Constants.PROTOCOL_TYPE_RESPONSE:

			switch (feedback.getCmdType()) {
			case Constants.MOTOR_CONTROL_TYPE_ALARM:

				AlarmModel alarm = new AlarmModel(Long.parseLong(feedback
						.getWindowId()));

				Log.d(Constants.TAG,
						"receive response to remove alarm , res -> "
								+ Alarm.getInstance().remove(alarm));
				break;
			case Constants.MOTOR_CONTROL_TYPE_TEST:

				if (user == 3) {
					EventBus.getDefault().postInOtherThread(
							LocalEvent.getEvent(
									LocalEvent.EVENT_TYPE_NET_CHANGE, 1));
				}

				EventBus.getDefault().postInOtherThread(
						LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_TEST,
								user));
				break;
			}
			break;
		}
	}

	static class OnControlFinishListener implements OnTaskFinishListener {

		Protocol protocol;

		OnControlFinishListener(Protocol protocol) {
			this.protocol = protocol;
		}

		@Override
		public void onTaskFinish(String res) {

			protocol.setData(res);
			protocol.send();
		}
	}
}
