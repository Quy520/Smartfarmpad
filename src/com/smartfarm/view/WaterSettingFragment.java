package com.smartfarm.view;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.WaterConfig;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;

public class WaterSettingFragment extends BaseFragment implements OnCheckedChangeListener {
	
	private EditText time;
	private EditText value;
	private EditText valueMax;
	private EditText valueMin;
	private CheckBox maxEnable;
	private CheckBox minEnable;
	private Spinner waterCount;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_water_setting;
	}

	@Override
	protected void initView(View view) {

		waterCount = (Spinner) view.findViewById(R.id.water_count);
		time = (EditText) view.findViewById(R.id.water_setting_time);
		value = (EditText) view.findViewById(R.id.water_setting_value);
		maxEnable = (CheckBox) view.findViewById(R.id.water_max_enable);
		minEnable = (CheckBox) view.findViewById(R.id.water_min_enable);
		valueMax = (EditText) view.findViewById(R.id.water_setting_value_max);
		valueMin = (EditText) view.findViewById(R.id.water_setting_value_min);
		
		maxEnable.setOnCheckedChangeListener(this);
		minEnable.setOnCheckedChangeListener(this);
	}

	@Override
	protected void initData() {

		WaterConfig wConfig = SimpleConfigManager.getInstance().getWaterModel();
		
		time.setText("");
		value.setText("");
		valueMax.setText("");
		valueMin.setText("");
		
		time.setHint(String.valueOf(wConfig.getTime()));
		value.setHint(String.valueOf(wConfig.getMin()));
		valueMax.setHint(String.valueOf(wConfig.getAlarmMax()));
		valueMin.setHint(String.valueOf(wConfig.getAlarmMin()));
		
		maxEnable.setChecked(wConfig.isAlarmMaxEnable());
		minEnable.setChecked(wConfig.isAlarmMinEnable());
		
		valueMax.setEnabled(wConfig.isAlarmMaxEnable());
		valueMin.setEnabled(wConfig.isAlarmMinEnable());
		
		setWaterCount(wConfig.getWaterCount());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.submit_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(submit()) {
			CommonTool.HideKb(getActivity(), getView());
			getActivity().finish();
		}
		
		return true;
	}

	public int getWaterCount() {
		return waterCount.getSelectedItemPosition() + 1;
	}

	private void setWaterCount(int count) {
		waterCount.setSelection(count - 1);
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.ill_setting_start) {
			
			
		} else if(R.id.ill_setting_start_diy == v.getId()) {
			
			ToastTool.showToast("开始补光!");
		}
	}
	
	public boolean submit() {
		
		int timeValue = -1;
		if(!ShowUtil.isEmpty(time.getText().toString())) {
			timeValue = Integer.valueOf(time.getText().toString());
		} else {
			timeValue = Integer.valueOf(time.getHint().toString());
		}
		
		if(timeValue == 0 || timeValue > 180) {
			
			ToastTool.showToast("滴灌时间设置不合理，请重新输入！");
			time.requestFocus();
			return false;
		}

		int minValue = -1;
		if(!ShowUtil.isEmpty(value.getText().toString())) {
			minValue = Integer.valueOf(value.getText().toString());
		} else {
			minValue = Integer.valueOf(value.getHint().toString());
		}
		
		if(minValue == 0 || minValue > 100) {
			
			ToastTool.showToast("湿度阈值设置不合理，请重新输入！");
			value.requestFocus();
			return false;
		}

		int alarmMaxValue = -1;
		boolean amaxEnableValue = maxEnable.isChecked();
		
		if(!amaxEnableValue) {
			alarmMaxValue = Integer.valueOf(valueMax.getHint().toString());
		} else {
			if(!ShowUtil.isEmpty(valueMax.getText().toString())) {
				alarmMaxValue = Integer.valueOf(valueMax.getText().toString());
			} else {
				alarmMaxValue = Integer.valueOf(valueMax.getHint().toString());
			}
			
			if(alarmMaxValue == 0 || alarmMaxValue > 100) {
				
				ToastTool.showToast("湿度报警最大值设置不合理，请重新输入！");
				valueMax.requestFocus();
				return false;
			}
		}

		int alarmMinValue = -1;
		boolean aminEnableValue = minEnable.isChecked();
		
		if(!aminEnableValue) {
			alarmMinValue = Integer.valueOf(valueMin.getHint().toString());
		} else {
			if(!ShowUtil.isEmpty(valueMin.getText().toString())) {
				alarmMinValue = Integer.valueOf(valueMin.getText().toString());
			} else {
				alarmMinValue = Integer.valueOf(valueMin.getHint().toString());
			}
			
			if(alarmMinValue == 0 || alarmMinValue > 100) {
				
				ToastTool.showToast("湿度报警最小值设置不合理，请重新输入！");
				valueMin.requestFocus();
				return false;
			}
		}
		
		WaterConfig wConfig = SimpleConfigManager.getInstance().getWaterModel();
		boolean countChange = getWaterCount() != wConfig.getWaterCount();
		
		wConfig.setMin(minValue);
		wConfig.setTime(timeValue);
		wConfig.setAlarmMax(alarmMaxValue);
		wConfig.setAlarmMin(alarmMinValue);
		wConfig.setWaterCount(getWaterCount());
		wConfig.setAlarmMaxEnable(amaxEnableValue);
		wConfig.setAlarmMinEnable(aminEnableValue);
		
		SimpleConfigManager.getInstance().saveWaterCfg();
		
		if(countChange)
			EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_COUNT_CHANGE));
			
		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(buttonView.getId() == R.id.water_max_enable) {
			valueMax.setEnabled(isChecked);
		} else {
			valueMin.setEnabled(isChecked);
		}
	}
}
