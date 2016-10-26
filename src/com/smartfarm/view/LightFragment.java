package com.smartfarm.view;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LightConfig;
import com.smartfarm.bean.LightControlBean;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.UIHelper;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LightFragment extends BaseFragment implements EventHandler {

	private LightControlBean[] lights;
	
	private TextView illValue;
	private TextView illTime;
	private TextView maxIll;
	private TextView minIll;
	private TextView netState;
	private ImageView mode;
	private TextView tCO2;
	
	private boolean isAuto = false;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_light;
	}

	@Override
	protected void initView(View view) {

		lights = new LightControlBean[4];
		
		lights[Constants.LIGHT_ID_1] = new LightControlBean(view.findViewById(R.id.light1), 
				view.findViewById(R.id.light1_dividing), Constants.LIGHT_ID_1);
		lights[Constants.LIGHT_ID_2] = new LightControlBean(view.findViewById(R.id.light2), 
				view.findViewById(R.id.light2_dividing), Constants.LIGHT_ID_2);
		lights[Constants.LIGHT_ID_3] = new LightControlBean(view.findViewById(R.id.light3), 
				view.findViewById(R.id.light3_dividing), Constants.LIGHT_ID_3);
		lights[Constants.LIGHT_ID_4] = new LightControlBean(view.findViewById(R.id.light4), 
				view.findViewById(R.id.light4_dividing), Constants.LIGHT_ID_4);
		
		refreshLightState();
		
		mode = (ImageView) view.findViewById(R.id.toggle_AT);
		netState = (TextView) view.findViewById(R.id.button_startGT);
		maxIll = (TextView) view.findViewById(R.id.maxTempValue);
		minIll = (TextView) view.findViewById(R.id.minTempValue);
		tCO2=(TextView) view.findViewById(R.id.light_time1);
		illTime = (TextView) view.findViewById(R.id.light_time);
		illTime.setVisibility(View.GONE);
		illValue = (TextView) view.findViewById(R.id.light_value);
		illValue.setVisibility(View.GONE);

		mode.setOnClickListener(this);
		netState.setOnClickListener(this);
		view.findViewById(R.id.imageButton_setup).setOnClickListener(this);
	}
	
	private void refreshLightState() {
		
		for(int i = 0; i < 4; i++) 
			lights[i].setState(RunningData.getInstance().getLightState(i));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		EventBus.getDefault().add(this);
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		EventBus.getDefault().remove(this);
	}

	@Override
	public void onResume() {
		
		initData();
		
		super.onResume();
	}

	@Override
	protected void initData() {
		
		LightConfig model = SimpleConfigManager.getInstance().getLightModel();
		
		for(int i = 0; i < 4; i++) {
			
			if(i < model.getCount()) {
				lights[i].setVisibility(true);
			} else {
				lights[i].setVisibility(false);
			}
		}
		
		maxIll.setText(String.format(getResources().getString(R.string.illuminance_max), model.getMaxValue()));
		minIll.setText(String.format(getResources().getString(R.string.illuminance_min), model.getMinValue()));
		
		illTime.setText(String.format(getResources().getString(R.string.illuminance_vaild_time), model.getTimeDescr()));

		modeChange();
		netState.setText(AppContext.context().netIdOk() 
				? R.string.net_connect : R.string.net_disconnect);
		/*if(RunningData.getInstance().getLightValue()<model.getMinValue()){
			SerialHelper.openLightAll(SerialControlRunable.SOURCE_AUTO);
		}*/
	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.toggle_AT) {
			
			if (isAuto) {
				if (MessageBox.DialogResult.OK == MessageBox.Show(
						getActivity(), "控制系统将不能自动进行补光！",
						"请确定需要重进入手动模式？", MessageBoxButtons.OKCancel)) {
					
					SimpleConfigManager.getInstance().getLightModel().setLightMode(false);
					SimpleConfigManager.getInstance().saveLightCfg();
					modeChange();
				}
			} else {
				
				SimpleConfigManager.getInstance().getLightModel().setLightMode(true);
				SimpleConfigManager.getInstance().saveLightCfg();
				modeChange();
			}
		} else if(v.getId() == R.id.imageButton_setup){
			
			UIHelper.showSimpleBack(getActivity(), BackPage.ILLUMINACE);
		} else if(v.getId() == R.id.button_startGT) {
			
			HuanxinHelper.get().relink(getActivity());
		}
	}
	
	private void modeChange() {
		
		isAuto = SimpleConfigManager.getInstance().getLightModel().isLightMode();
		mode.setImageResource(isAuto ? R.drawable.button_auto
				: R.drawable.button_auto_manual);
	}

	@Override
	public void onEvent(LocalEvent event) {
		if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_CHANGE) {

			netState.setText(AppContext.context().netIdOk() ? R.string.net_connect
					: R.string.net_disconnect);
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_RECEIVE_LIGHT) {
			illValue.setText(String.format(getResources().getString(R.string.illuminance_value), RunningData.getInstance().getIllumination()));
			tCO2.setText(String.format(getString(R.string.illuminance_co2), RunningData.getInstance().getValueCO2()+"ppm"));
			initData();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_LIGHT_CHANGE) {
			
			refreshLightState();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_LIGHT_MODE_CHANGE) {
			
			modeChange();
		}
	}
}
