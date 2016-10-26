package com.smartfarm.view;

import com.smartfarm.bean.LightConfig;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 照度设置界面
 * 
 * @author jeff
 *
 */
public class LightSettingFragment extends BaseFragment {

	private boolean diyMode = false;
	
	private EditText illTime;
	private EditText illMin;
	private EditText illMax;
	private EditText illDiy;
	private TextView illStart;
	private TextView illDiyStart;
	private Spinner illCount;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_light_setting;
	}

	@Override
	protected void initView(View view) {

		illTime = (EditText) view.findViewById(R.id.ill_setting_time);
		illMin = (EditText) view.findViewById(R.id.ill_setting_vaild);
		illMax = (EditText) view.findViewById(R.id.ill_setting_up);
		illDiy = (EditText) view.findViewById(R.id.ill_setting_diy);
		illStart = (TextView) view.findViewById(R.id.ill_setting_start);
		illDiyStart = (TextView) view.findViewById(R.id.ill_setting_start_diy);
		illCount = (Spinner) view.findViewById(R.id.ill_setting_count);
		
		illStart.setOnClickListener(this);
		illDiyStart.setOnClickListener(this);
		
		diyMode = SimpleConfigManager.getInstance().getBoolean(SimpleConfigManager.KEY_LIGHT_DIY_MODE);
		resetDitBtnTip();
	}
	
	private void resetDitBtnTip() {
		
		if(diyMode)
			illDiyStart.setText(R.string.illuminance_end_diy);
		else
			illDiyStart.setText(R.string.illuminance_start_diy);
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

		if(submit()) {
			CommonTool.HideKb(getActivity(), getView());
			getActivity().finish();
		}
		
		return true;
	}

	@Override
	protected void initData() {

		LightConfig model = SimpleConfigManager.getInstance().getLightModel();
		
		illMin.setText("");
		illMax.setText("");
		illDiy.setText("");
		illTime.setText("");
		
		illMin.setHint(String.valueOf(model.getMinValue()));
		illMax.setHint(String.valueOf(model.getMaxValue()));
		illDiy.setHint(String.valueOf(model.getDiyTime()));
		illTime.setHint(String.valueOf(model.getNeedTime()));
		
		illStart.setText(model.getStartTime());
		setLightCount(model.getCount());
	}

	public int getLightCount() {
		return illCount.getSelectedItemPosition() + 2;
	}

	private void setLightCount(int count) {
		illCount.setSelection(count - 2);
	}
	
	public boolean submit() {
		
		LightConfig model = SimpleConfigManager.getInstance().getLightModel();
		
		int needTimeValue = -1;
		if(!ShowUtil.isEmpty(illTime.getText().toString())) {
			
			needTimeValue = Integer.valueOf(illTime.getText().toString());
			
			if(needTimeValue > 900 || needTimeValue < 60) {

				ToastTool.showToast("每天需要有效的光照时间设置不合理，请重试！");
				illTime.requestFocus();
				return false;
			}
		} else {
			
			needTimeValue = model.getNeedTime();
		}

		int minValue = -1;
		if(!ShowUtil.isEmpty(illMin.getText().toString())) {
			
			minValue = Integer.valueOf(illMin.getText().toString());
			
			if(minValue > 56412) {

				ToastTool.showToast("最小有效照度设置不合理，请重试！");
				illMin.requestFocus();
				return false;
			}
		} else {
			
			minValue = model.getMinValue();
		}

		int maxValue = -1;
		if(!ShowUtil.isEmpty(illMax.getText().toString())) {
			
			maxValue = Integer.valueOf(illMax.getText().toString());
			
			if(maxValue > 56412) {

				ToastTool.showToast("照度上限设置不合理，请重试！");
				illMax.requestFocus();
				return false;
			}
		} else {
			
			maxValue = model.getMaxValue();
		}

		int diyValue = -1;
		if(!ShowUtil.isEmpty(illDiy.getText().toString())) {
			
			diyValue = Integer.valueOf(illDiy.getText().toString());
			
			if(diyValue > 720) {

				ToastTool.showToast("自定义补光时间设置不应该超过10小时，请重试！");
				illDiy.requestFocus();
				return false;
			}
		} else {
			
			diyValue = model.getDiyTime();
		}
		
		model.setCount(getLightCount());
		model.setDiyTime(diyValue);
		model.setMaxValue(maxValue);
		model.setMinValue(minValue);
		model.setNeedTime(needTimeValue);
		model.setStartTime(illStart.getText().toString());
		
		SimpleConfigManager.getInstance().saveLightCfg();
		
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.ill_setting_start) {
			
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
									
									result.append(dialogTimePicker.getCurrentHour());
									result.append(":");

									if (dialogTimePicker.getCurrentMinute() < 10)
										result.append("0");
									result.append(dialogTimePicker.getCurrentMinute());

									illStart.setText(result.toString());
								}

							}).create().show();
		} else if(R.id.ill_setting_start_diy == v.getId()) {
			
			diyMode = !diyMode;
			
			if(diyMode) {
				ToastTool.showToast("开启自定义补光模式补光!");
			} else {
				ToastTool.showToast("结束自定义补光模式补光!");
			}

			resetDitBtnTip();
			SimpleConfigManager.getInstance().putBoolean(SimpleConfigManager.KEY_LIGHT_DIY_MODE, diyMode);
			
			EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_LIGHT_DIY_MODE));
		}
	}
}
