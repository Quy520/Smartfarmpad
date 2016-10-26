package com.smartfarm.view;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.BlueToothHelper;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.UIHelper;
import com.smartfarm.view.MessageBox.DialogResult;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

/**
 * 设置界面
 * 
 * @author jeff
 *
 */
public class SettingFragment extends BaseFragment implements
		OnCheckedChangeListener,EventHandler {

	private EditText phoneNumberEdit;
	private EditText phoneNumberEdit2;
	private EditText phoneNumberEdit3;
	private EditText morningTimeEdit;
	private EditText pwdEdit;
	private EditText alarmMax;
	private EditText alarmMin;
	private EditText pushTime;

	private TextView nightModeStartTime;
	private TextView nightModeEndTime;
	
	private TextView txtLinkState;

	private CheckBox highTempAlarmEnable;
	private CheckBox lowTempAlarmEnable;
	private CheckBox waterEnable;
	private CheckBox lightEnable;
	private CheckBox autoOpenEnable;
	private CheckBox alarmEnable;
	private Spinner windowCount;
	private Spinner minutesCount;
	private BlueToothHelper blueHelper;
	
	@Override
	protected int getLayoutId() {
		return R.layout.setting_view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.REQUEST_CONNECT_DEVICE: // 连接结果，由DeviceListActivity设置返回
			// 响应返回结果
			if (resultCode == getActivity().RESULT_OK) { // 连接成功，由DeviceListActivity设置返回
			new Thread(){
					public void run() {
						blueHelper.initSocket();
					}
				};
			}
			break;
		}
	}

	@Override
	public void initData() {
		blueHelper=BlueToothHelper.getBlueToothHelper();
		ConfigModel config = SimpleConfigManager.getInstance().getConfig();

		pwdEdit.setText("");
		pwdEdit.setHint(R.string.edit_pwd);

		alarmMax.setText("");
		alarmMin.setText("");
		pushTime.setText("");
		morningTimeEdit.setText("");
		
		if(blueHelper.get_socket()!= null)
			txtLinkState.setText(SimpleConfigManager.getInstance().getConfig().getBluetoothmac());

		alarmMax.setHint("" + config.getAlarmMax());
		alarmMin.setHint("" + config.getAlarmMin());
		pushTime.setHint("" + config.getPushTime());
		morningTimeEdit.setHint("" + config.getVenTime());

		phoneNumberEdit.setText(config.getPhonenumber());
		nightModeStartTime.setText(config.getMorningOpenTime());
		nightModeEndTime.setText(config.getNightCloseTime());
		phoneNumberEdit2.setText(config.getPhonenumber2());
		phoneNumberEdit3.setText(config.getPhonenumber3());
		lowTempAlarmEnable.setChecked(config.isLowAlarmEnable());
		highTempAlarmEnable.setChecked(config.isHighAlarmEnable());
		alarmEnable.setChecked(config.isAlarmEnable());
		waterEnable.setChecked(config.isWaterEnable());
		lightEnable.setChecked(config.isLightEnable());
		autoOpenEnable.setChecked(config.isAutoOpenEnable());

		alarmMax.setEnabled(highTempAlarmEnable.isChecked());
		alarmMin.setEnabled(lowTempAlarmEnable.isChecked());

		setWindowCount(config.getWindowCount());
		setMintuesCount(config.getMintuesCount());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {

		initData();

		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.submit_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (submit()) {
			CommonTool.HideKb(getActivity(), getView());
			getActivity().finish();
		}

		return true;
	}

	public int getWindowCount() {
		return windowCount.getSelectedItemPosition() + 2;
	}

	private void setWindowCount(int count) {
		windowCount.setSelection(count - 2);
	}
	
	public int getMintuesCount(){
		return (minutesCount.getSelectedItemPosition()+1)*10;
	}
	
	private void setMintuesCount(int count){
		minutesCount.setSelection(count/10 - 1);
	}

	@Override
	public void initView(View view) {

		waterEnable = (CheckBox) view.findViewById(R.id.water_enable);
		lightEnable = (CheckBox) view.findViewById(R.id.light_enable);
		autoOpenEnable = (CheckBox) view.findViewById(R.id.auto_open_enable);
		highTempAlarmEnable = (CheckBox) view.findViewById(R.id.temp_max_enable);
		lowTempAlarmEnable = (CheckBox) view.findViewById(R.id.temp_min_enable);
		alarmEnable = (CheckBox) view.findViewById(R.id.msg_alarm_enable);

		phoneNumberEdit = (EditText) view.findViewById(R.id.edit_phoneNumber);
		phoneNumberEdit2 = (EditText) view.findViewById(R.id.edit_phoneNumber2);
		phoneNumberEdit3 = (EditText) view.findViewById(R.id.edit_phoneNumber3);

		pwdEdit = (EditText) view.findViewById(R.id.edit_pad_pwd);

		morningTimeEdit = (EditText) view.findViewById(R.id.edit_MorningTime);
		nightModeStartTime = (TextView) view
				.findViewById(R.id.timePickerMorning);
		nightModeEndTime = (TextView) view.findViewById(R.id.timePickerNight);
		windowCount = (Spinner) view.findViewById(R.id.edit_window_count);
		minutesCount=(Spinner) view.findViewById(R.id.edit_minutes_count);
		pushTime = (EditText) view.findViewById(R.id.edit_push_time);
		alarmMax = (EditText) view.findViewById(R.id.edit_alarm_max);
		alarmMin = (EditText) view.findViewById(R.id.edit_alarm_min);
		
		txtLinkState=(TextView) view.findViewById(R.id.link_state);
		lowTempAlarmEnable.setOnCheckedChangeListener(this);
		highTempAlarmEnable.setOnCheckedChangeListener(this);
		alarmEnable.setOnCheckedChangeListener(this);
		view.findViewById(R.id.calibrate_state).setOnClickListener(this);
		view.findViewById(R.id.setting_stalls).setOnClickListener(this);
		view.findViewById(R.id.setting_temps).setOnClickListener(this);
		view.findViewById(R.id.clear_state).setOnClickListener(this);
		view.findViewById(R.id.timePickerMorning).setOnClickListener(this);
		view.findViewById(R.id.timePickerNight).setOnClickListener(this);
		view.findViewById(R.id.bluetooth_state).setOnClickListener(this);
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.clear_state:

			if (MessageBox.Show(getActivity(), "确定要清除风口所有的状态信息吗？请确保风口"
					+ "已经全部关死再使用此功能，错误的状态信息可能导致控制不准确，"
					+ "影响使用效果，丢失的信息也不可再恢复，请慎重操作。", "注意",
					MessageBoxButtons.OKCancel) == DialogResult.OK) {

				for (int i = 0; i < 9; i++)
					RunningData.getInstance().setWindowState(i, 0);
			}
			break;
		case R.id.calibrate_state:

			if (MessageBox.Show(getActivity(), R.string.calibrate_state_tip,
					R.string.operation_sure, MessageBoxButtons.OKCancel) == DialogResult.OK) {

				ToastTool.showToast("正在进行校准，请勿停止！");
				SerialHelper.calibrateState();
				SimpleConfigManager.getInstance().modeChange(
						SerialControlRunable.SOURCE_PAD_BUTTON, false);
				SimpleConfigManager.getInstance().modeOpenChange(
						SerialControlRunable.SOURCE_PAD_BUTTON, false);
			}
			break;
		case R.id.setting_stalls:

			UIHelper.showSimpleBack(getActivity(), BackPage.STALLS);
			break;
		case R.id.setting_temps:

			UIHelper.showSimpleBack(getActivity(), BackPage.TEMP_CONFIG);
			break;
		case R.id.timePickerMorning:

			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.timepicker_dialog_view, null);
			final TimePicker dialogTimePicker = (TimePicker) view
					.findViewById(R.id.tp_dialog_picker);
			dialogTimePicker.setIs24HourView(true);

			new AlertDialog.Builder(getActivity())
					.setView(view)
					.setTitle(R.string.set_time_title)
					.setNegativeButton(R.string.cancle, null)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									StringBuilder result = new StringBuilder();

									if (dialogTimePicker.getCurrentHour() < 10)
										result.append("0");
									result.append(dialogTimePicker
											.getCurrentHour());
									result.append(":");

									if (dialogTimePicker.getCurrentMinute() < 10)
										result.append("0");
									result.append(dialogTimePicker
											.getCurrentMinute());

									nightModeStartTime.setText(result
											.toString());
								}

							}).create().show();
			break;
		case R.id.timePickerNight:

			View nightTimePickerView = LayoutInflater.from(getActivity())
					.inflate(R.layout.timepicker_dialog_view, null);
			final TimePicker nihtTimePicker = (TimePicker) nightTimePickerView
					.findViewById(R.id.tp_dialog_picker);
			nihtTimePicker.setIs24HourView(true);

			new AlertDialog.Builder(getActivity())
					.setView(nightTimePickerView)
					.setTitle(R.string.set_time_title)
					.setNegativeButton(R.string.cancle, null)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									StringBuilder result = new StringBuilder();

									if (nihtTimePicker.getCurrentHour() < 10)
										result.append("0");
									result.append(nihtTimePicker
											.getCurrentHour());
									result.append(":");

									if (nihtTimePicker.getCurrentMinute() < 10)
										result.append("0");
									result.append(nihtTimePicker
											.getCurrentMinute());

									nightModeEndTime.setText(result.toString());
								}

							}).create().show();
			break;
		case R.id.bluetooth_state:
			if(blueHelper.get_socket()!=null){
				try {
					blueHelper.get_socket().getInputStream();
				} catch (IOException e) {
					e.printStackTrace();
					Intent serverIntent = new Intent(getActivity(),
							DeviceListActivity.class); // 跳转程序设置
					startActivityForResult(serverIntent,
							Constants.REQUEST_CONNECT_DEVICE); // 设置返回宏定义
				}
			}
			Intent serverIntent = new Intent(getActivity(),
					DeviceListActivity.class); // 跳转程序设置
			startActivityForResult(serverIntent,
					Constants.REQUEST_CONNECT_DEVICE); // 设置返回宏定义
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (buttonView.getId() == R.id.temp_max_enable)
			alarmMax.setEnabled(isChecked);
		else
			alarmMin.setEnabled(isChecked);
	}

	public boolean checkMobile(String mobile) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobile))
			return false;
		else
			return mobile.matches(telRegex);
	}

	/**
	 * 提交用户填写数据
	 * 
	 * @return 检查通过返回true，不通过返回false
	 */
	private boolean submit() {

		int pushTimeValue = ShowUtil.isEmpty(pushTime.getText().toString()) ? Integer
				.valueOf(pushTime.getHint().toString()) : Integer
				.valueOf(pushTime.getText().toString());

		if (pushTimeValue <= 0) {

			ToastTool.showToast("温度推送时间过小，请重新输入！");
			pushTime.requestFocus();
			return false;
		} else if (pushTimeValue > 120) {

			ToastTool.showToast("温度推送时间过大，建议不超过两小时，请重新输入！");
			pushTime.requestFocus();
			return false;
		}

		int alarmMaxValue = ShowUtil.isEmpty(alarmMax.getText().toString()) ? Integer
				.valueOf(alarmMax.getHint().toString()) : Integer
				.valueOf(alarmMax.getText().toString());

		if (alarmMaxValue <= 0) {

			ToastTool.showToast("最高报警温度过小，请重新输入！");
			alarmMax.requestFocus();
			return false;
		} else if (alarmMaxValue > 55) {

			ToastTool.showToast("最高报警温度过大，建议不超过55，请重新输入！");
			alarmMax.requestFocus();
			return false;
		}

		int alarmMinValue = ShowUtil.isEmpty(alarmMin.getText().toString()) ? Integer
				.valueOf(alarmMin.getHint().toString()) : Integer
				.valueOf(alarmMin.getText().toString());

		if (alarmMinValue <= -10) {

			ToastTool.showToast("最高报警温度过小，请重新输入！");
			alarmMin.requestFocus();
			return false;
		} else if (alarmMinValue > 55) {

			ToastTool.showToast("最高报警温度过大，建议不超过55，请重新输入！");
			alarmMin.requestFocus();
			return false;
		}

		int venTime = ShowUtil.isEmpty(morningTimeEdit.getText().toString()) ? Integer
				.valueOf(morningTimeEdit.getHint().toString()) : Integer
				.valueOf(morningTimeEdit.getText().toString());

		if (venTime <= 0) {

			ToastTool.showToast("通风时间过小，请重新输入！");
			morningTimeEdit.requestFocus();
			return false;
		} else if (venTime > 300) {

			ToastTool.showToast("通风时间过大，建议不超过两5个小时,请重新输入！");
			alarmMin.requestFocus();
			return false;
		}

		// phone number confirm
		if (!checkMobile(phoneNumberEdit.getText().toString())) {

			ToastTool.showToast("主电话号码错误，请重新输入！");
			phoneNumberEdit.requestFocus();

			return false;
		}

		String phoneNumber2 = phoneNumberEdit2.getText().toString();
		if (phoneNumber2.length() != 0) {
			if (!checkMobile(phoneNumber2)) {

				ToastTool.showToast("从2电话号码错误，请重新输入！");
				phoneNumberEdit2.requestFocus();

				return false;
			}
		}

		String phoneNumber3 = phoneNumberEdit3.getText().toString();
		if (phoneNumber3.length() != 0) {
			if (!checkMobile(phoneNumber3)) {

				ToastTool.showToast("从3电话号码错误，请重新输入！");
				phoneNumberEdit3.requestFocus();

				return false;
			}
		}

		ToastTool.showToast("保存成功!");

		SimpleConfigManager scManager = SimpleConfigManager.getInstance();

		boolean showChange = false;
		ConfigModel cm = scManager.getConfig();

		if (cm.isWaterEnable() ^ waterEnable.isChecked()) {
			showChange = true;
			scManager.putBoolean(SimpleConfigManager.KEY_WATER_ENABLE,
					waterEnable.isChecked());
		}

		if (cm.isLightEnable() ^ lightEnable.isChecked()) {
			showChange = true;
			scManager.putBoolean(SimpleConfigManager.KEY_LIGHT_ENABLE,
					lightEnable.isChecked());
		}

		scManager.putBoolean(SimpleConfigManager.KEY_AUTO_OPEN,
				autoOpenEnable.isChecked());
		scManager.putString(SimpleConfigManager.KEY_PHONE_NUMBER,
				phoneNumberEdit.getText().toString());
		scManager.putString(SimpleConfigManager.KEY_PHONE2, phoneNumberEdit2
				.getText().toString());
		scManager.putString(SimpleConfigManager.KEY_PHONE3, phoneNumberEdit3
				.getText().toString());
		scManager.putInt(SimpleConfigManager.KEY_VEN_TIME, venTime);
		scManager.putString(SimpleConfigManager.KEY_MORNING_OPEN,
				nightModeStartTime.getText().toString());
		scManager.putString(SimpleConfigManager.KEY_NIGHT_CLOSE,
				nightModeEndTime.getText().toString());
		scManager.putInt(SimpleConfigManager.KEY_TEMP_ALARM_MAX, alarmMaxValue);
		scManager.putInt(SimpleConfigManager.KEY_TEMP_ALARM_MIN, alarmMinValue);
		scManager.putInt(SimpleConfigManager.KEY_PUSH_TIME, pushTimeValue);
		scManager.putBoolean(SimpleConfigManager.KEY_HIGH_ALARM_ENABLE,highTempAlarmEnable.isChecked());
		scManager.putBoolean(SimpleConfigManager.KEY_LOW_ALARM_ENABLE,lowTempAlarmEnable.isChecked());
		scManager.putBoolean(SimpleConfigManager.KEY_ALARM_ENABLE, alarmEnable.isChecked());

		scManager.updateApplicationCfg();

		if (scManager.getConfig().getWindowCount() != getWindowCount()) {

			scManager.putInt(SimpleConfigManager.KEY_WIN_COUNT,
					getWindowCount());
			scManager.updateApplicationCfg();

			EventBus.getDefault().post(
					LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WINDOW_CHANGE));

			SerialHelper.readAll(SerialControlRunable.SOURCE_PAD_BUTTON);
		}
		if(scManager.getConfig().getMintuesCount()!=getMintuesCount()){
			scManager.putInt(SimpleConfigManager.KEY_MINUTES_COUNT, getMintuesCount());
			scManager.updateApplicationCfg();
		}

		if (!ShowUtil.isEmpty(pwdEdit.getText().toString()))
			scManager.putString(SimpleConfigManager.KEY_PWD,
					Md5Utils.encode(pwdEdit.getText().toString()));

		if (showChange)
			EventBus.getDefault().post(
					LocalEvent.getEvent(LocalEvent.EVENT_TYPE_PAGER_CHANGE));

		return true;
	}

	@Override
	public void onEvent(LocalEvent event) {
		switch (event.getEventType()) {
		case LocalEvent.EVENT_TYPE_BLUETOOTH_OFF:
			txtLinkState.setText("未连接");
			break;
		case LocalEvent.EVENT_TYPE_BLUETOOTH_ON:
			ToastTool.showToast("连接成功");
			txtLinkState.setText(SimpleConfigManager.getInstance().getConfig().getBluetoothmac());
			break;
		default:
			break;
		}
	}
}
