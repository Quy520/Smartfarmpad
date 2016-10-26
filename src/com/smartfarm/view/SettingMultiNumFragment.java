package com.smartfarm.view;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ToastTool;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingMultiNumFragment extends BaseFragment implements
		EventHandler {

	private boolean success = false;
	private TextView readNum;
	private TextView needReadSum;

	private Button submitModify;
	private Button btnStop;

	private int needModify = 0;

	private int isOK = 0;
	private boolean isStop = false;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_multi_num,
				container, false);
		initView(view);
		return view;
	}

	@Override
	protected void initView(View view) {
		readNum = (TextView) view.findViewById(R.id.read_in_num);
		needReadSum = (TextView) view.findViewById(R.id.need_read_sum);
		submitModify = (Button) view.findViewById(R.id.submit_write);
		btnStop = (Button) view.findViewById(R.id.btn_stop);
		submitModify.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		view.findViewById(R.id.btn_one).setOnClickListener(this);
		view.findViewById(R.id.btn_two).setOnClickListener(this);
		view.findViewById(R.id.btn_three).setOnClickListener(this);
		view.findViewById(R.id.btn_four).setOnClickListener(this);
		view.findViewById(R.id.btn_five).setOnClickListener(this);
		view.findViewById(R.id.btn_six).setOnClickListener(this);
		view.findViewById(R.id.btn_seven).setOnClickListener(this);
		view.findViewById(R.id.btn_eight).setOnClickListener(this);
		view.findViewById(R.id.btn_nine).setOnClickListener(this);
		view.findViewById(R.id.btn_clear).setOnClickListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().add(this);
		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_ON_SUPERMODE));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_write:
			submitModify.setEnabled(false);
			btnStop.setEnabled(true);
			ToastTool.showToast("正在执行操作，请等待");
			SerialHelper.modifyDeviceID(needModify);
			break;
		case R.id.btn_stop:
			SerialHelper.clearRunnable();
			isStop = true;
			ToastTool.showToast("程序中断中");
			break;
		case R.id.btn_one:
			needModify = 1;
			needReadSum.setText(1 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_two:
			needModify = 2;
			needReadSum.setText(2 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_three:
			needModify = 3;
			needReadSum.setText(3 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_four:
			needModify = 4;
			needReadSum.setText(4 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_five:
			needModify = 5;
			needReadSum.setText(5 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_six:
			needModify = 6;
			needReadSum.setText(6 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_seven:
			needModify = 7;
			needReadSum.setText(7 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_eight:
			needModify = 8;
			needReadSum.setText(8 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_nine:
			needModify = 9;
			needReadSum.setText(9 + "");
			submitModify.setEnabled(true);
			break;
		case R.id.btn_clear:
			SerialHelper.clearDeviceRunable();
			break;
		default:
			break;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isStop = true;
		SerialHelper.clearRunnable();
		EventBus.getDefault().remove(this);
		EventBus.getDefault().postInOtherThread(
				LocalEvent.getEvent(LocalEvent.EVENT_TYPE_OFF_SUPERMODE));
	}

	@Override
	public void onEvent(LocalEvent event) {
		if (event.getEventType() == LocalEvent.EVENT_TYPE_DEVICE_CHANGE_SCUESS) {
			Log.d("zqq", "修改成功success->" + success);
			isOK++;
			readNum.setText("第" + isOK + "台修改完成");
			if (event.getEventValue() == needModify) {
				ToastTool.showToast("全部修改完成");
				return;
			}
			ToastTool.showToast("第" + isOK + "台修改完成，请拔掉串口插入下一台设备");
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_DEVICE_CHANGE_ERROR) {
			if (isStop) {
				ToastTool.showToast("中断成功");
				submitModify.setEnabled(true);
				btnStop.setEnabled(false);
			} else if (event.getEventValue() == 1) {
				ToastTool.showToast("多台修改失败,运行已被中断");
				submitModify.setEnabled(true);
				btnStop.setEnabled(false);
			}
		}
	}
}
