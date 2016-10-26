package com.smartfarm.view;

import java.util.concurrent.LinkedBlockingDeque;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ToastTool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingNumFragment extends BaseFragment implements EventHandler {
	private TextView editChangeNum;
	private Button btnChange;
	private Button btnReadNum;
	private int originalID = 0;
	private TextView deviceNum;
	private int newDeviceID = 0;
	private TextView deviceVersion;
	
	private boolean is_click = true;

	protected LinkedBlockingDeque<LocalEvent> msgs = new LinkedBlockingDeque<LocalEvent>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().add(this);
		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_ON_SUPERMODE));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().remove(this);
		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_OFF_SUPERMODE));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_num, container,
				false);

		initView(view);
		initData();
		return view;
	}

	@Override
	protected void initData() {
		SerialHelper.readDevice();
	}

	@Override
	protected void initView(View view) {
		editChangeNum = (TextView) view.findViewById(R.id.device_change_num);
		btnReadNum = (Button) view.findViewById(R.id.btn_readding_num);
		deviceNum = (TextView) view.findViewById(R.id.device_num);
		deviceVersion = (TextView) view.findViewById(R.id.device_version);
		view.findViewById(R.id.btn_one).setOnClickListener(this);
		view.findViewById(R.id.btn_two).setOnClickListener(this);
		view.findViewById(R.id.btn_three).setOnClickListener(this);
		view.findViewById(R.id.btn_four).setOnClickListener(this);
		view.findViewById(R.id.btn_five).setOnClickListener(this);
		view.findViewById(R.id.btn_six).setOnClickListener(this);
		view.findViewById(R.id.btn_seven).setOnClickListener(this);
		view.findViewById(R.id.btn_eight).setOnClickListener(this);
		view.findViewById(R.id.btn_nine).setOnClickListener(this);
		view.findViewById(R.id.btn_zero).setOnClickListener(this);

		btnReadNum.setOnClickListener(this);
		btnChange = (Button) view.findViewById(R.id.btn_change_num);
		btnChange.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_num:
			if (is_click) {
				SerialHelper.modifyDeviceID(originalID, newDeviceID);
				is_click = false;
			}
			break;
		case R.id.btn_readding_num:
			SerialHelper.readDevice();

			break;
		case R.id.btn_one:
			newDeviceID = 1;
			editChangeNum.setText(1 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_two:
			newDeviceID = 2;
			editChangeNum.setText(2 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_three:
			newDeviceID = 3;
			editChangeNum.setText(3 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_four:
			newDeviceID = 4;
			editChangeNum.setText(4 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_five:
			newDeviceID = 5;
			editChangeNum.setText(5 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_six:
			newDeviceID = 6;
			editChangeNum.setText(6 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_seven:
			newDeviceID = 7;
			editChangeNum.setText(7 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_eight:
			newDeviceID = 8;
			editChangeNum.setText(8 + "");
			btnChange.setEnabled(true);
			break;
		case R.id.btn_nine:
			newDeviceID = 9;
			editChangeNum.setText(9 + "");
			btnChange.setEnabled(true);
			break;

		case R.id.btn_zero:
			newDeviceID = 0;
			editChangeNum.setText(0 + "");
			btnChange.setEnabled(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onEvent(LocalEvent event) {
		if (event.getEventType() == LocalEvent.EVENT_TYPE_GET_DEVICE_ID) {
			deviceNum.setText(event.getEventValue() + "");
			ToastTool.showToast("读取设备号成功");
			originalID = event.getEventValue();

		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_DEVICE_CHANGE_SCUESS) {
			SerialHelper.readDevice();
			ToastTool.showToast("设备号修改成功");
			is_click = true;
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_DEVICE_CHANGE_ERROR) {
//			SerialHelper.readDevice();
			is_click = true;
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_GET_DEVICE_VERSION){
			deviceVersion.setText(event.getEventMsg());
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_READ_ID_ERROR){
			ToastTool.showToast("设备号读取失败，请重试");
		}
	}
}