package com.smartfarm.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.TempConfigModel;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.MessageBox.DialogResult;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

public class TempConfigFragment extends BaseFragment implements OnCheckedChangeListener, OnItemSelectedListener, EventHandler {

	private EditText tempMax;
	private EditText tempMin;
	private EditText tempNorMax;
	private EditText tempNorMin;
	
	private Spinner windowIdSpinner;
	private CheckBox moreModeChecked;
	private TextView selectIdTitle;
	private View selectIdDividing;
	
	private TempConfig tempConfig;
	private String[] windowIds;
	
	private LinearLayout linCalibrateState;

	@Override
	public void onDestroy() {
		
		EventBus.getDefault().remove(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		EventBus.getDefault().add(this);
		super.onCreate(savedInstanceState);
	}

	@Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.fragment_temp_config, container, false);

        initView(view);
        initData();
        
        return view;
    }
    
    @Override
    protected void initView(View view) {

		tempMax = (EditText) view.findViewById(R.id.setting_temp_high);
		tempMin = (EditText) view.findViewById(R.id.setting_temp_low);
		tempNorMax = (EditText) view.findViewById(R.id.setting_temp_nor_max);
		tempNorMin = (EditText) view.findViewById(R.id.setting_temp_nor_min);
		windowIdSpinner = (Spinner) view.findViewById(R.id.edit_window_id);
		moreModeChecked = (CheckBox) view.findViewById(R.id.temp_more_mode);
		selectIdTitle = (TextView) view.findViewById(R.id.select_id_title);
		selectIdDividing = view.findViewById(R.id.select_id_dividing);
		linCalibrateState = (LinearLayout) view.findViewById(R.id.calibrate_state);
		linCalibrateState.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.calibrate_state:
			if (MessageBox.Show(getActivity(), R.string.calibrate_state_tip,
					R.string.operation_sure, MessageBoxButtons.OKCancel) == DialogResult.OK) {
				ToastTool.showToast("正在进行校准，请勿停止！");
				SerialHelper.calibrateState(windowIdSpinner.getSelectedItemPosition());
			}
			break;

		default:
			break;
		}
    }
    
    @Override
    protected void initData() {

    	int count = SimpleConfigManager.getInstance().getConfig().getWindowCount();
    	windowIds = new String[count];
    	
    	for(int i = 1; i <= count; i++)
    		windowIds[i - 1] = String.valueOf(i);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
                android.R.layout.simple_spinner_item, windowIds); 
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        windowIdSpinner.setAdapter(adapter); 
        windowIdSpinner.setOnItemSelectedListener(this);

        moreModeChecked.setOnCheckedChangeListener(this);
		
		refresh();
    }
    
    private void refresh() {
    	
    	tempConfig = SimpleConfigManager.getInstance().getTempModel().getConfig(windowIdSpinner.getSelectedItemPosition());
        
    	TempConfigModel tcm = SimpleConfigManager.getInstance().getTempModel();
        
        moreModeChecked.setChecked(tcm.isTempMoreMode());
        
        int show = tcm.isTempMoreMode() ? View.VISIBLE : View.GONE;
		
		selectIdTitle.setVisibility(show);
		selectIdDividing.setVisibility(show);
		windowIdSpinner.setVisibility(show);
//		linCalibrateState.setVisibility(show);
		
 		tempMax.setText("");
 		tempMin.setText("");
 		tempNorMax.setText("");
 		tempNorMin.setText("");

 		tempMax.setHint("" + tempConfig.getMax());
 		tempMin.setHint("" + tempConfig.getMin());
 		tempNorMax.setHint("" + tempConfig.getNorMax());
 		tempNorMin.setHint("" + tempConfig.getNorMin());
    }
    
    private boolean submit() {
    	
    	int tempMaxValue = ShowUtil.isEmpty(tempMax.getText().toString()) ? Integer
				.valueOf(tempMax.getHint().toString()) : Integer
				.valueOf(tempMax.getText().toString());

		int tempNorMaxValue = ShowUtil.isEmpty(tempNorMax.getText().toString()) ? Integer
				.valueOf(tempNorMax.getHint().toString()) : Integer
				.valueOf(tempNorMax.getText().toString());

		int tempNorMinValue = ShowUtil.isEmpty(tempNorMin.getText().toString()) ? Integer
				.valueOf(tempNorMin.getHint().toString()) : Integer
				.valueOf(tempNorMin.getText().toString());

		int tempMinValue = ShowUtil.isEmpty(tempMin.getText().toString()) ? Integer
				.valueOf(tempMin.getHint().toString()) : Integer
				.valueOf(tempMin.getText().toString());
				
		if (tempNorMaxValue >= tempMaxValue) {

			ToastTool.showToast("输入的适宜温度下限过大，请重新输入！");
			return false;
		}

		if (tempNorMinValue >= tempNorMaxValue) {

			ToastTool.showToast("输入的开风温度过大，请重新输入！");
			return false;
		}

		if (tempMinValue >= tempNorMinValue) {

			ToastTool.showToast("输入的关风温度过大，请重新输入！");
			return false;
		}

		tempConfig.setMax(tempMaxValue);
		tempConfig.setMin(tempMinValue);
		tempConfig.setNorMax(tempNorMaxValue);
		tempConfig.setNorMin(tempNorMinValue);
		SimpleConfigManager.getInstance().getTempModel().updateConfig(tempConfig);
		
		ToastTool.showToast("温度参数保存成功!");
		CommonTool.HideKb(getActivity(), getView());
		
		refresh();
		
		return true;
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		int show = isChecked ? View.VISIBLE : View.GONE;
		
		selectIdTitle.setVisibility(show);
		selectIdDividing.setVisibility(show);
		windowIdSpinner.setVisibility(show);
//		linCalibrateState.setVisibility(show);
		
		SimpleConfigManager.getInstance().putBoolean(SimpleConfigManager.KEY_TEMP_MORE_MODE, isChecked);
		SimpleConfigManager.getInstance().refreshTempConfig();
		
		refresh();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		refresh();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	
        inflater.inflate(R.menu.submit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	submit();
        return true;
    }

	@Override
	public void onEvent(LocalEvent event) {
		
		if(event.getEventType() == LocalEvent.EVENT_TYPE_TEMP_CONFIG_CHANGE)
			refresh();
	}
}
