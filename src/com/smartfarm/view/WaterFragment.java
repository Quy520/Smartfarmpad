package com.smartfarm.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.bean.WaterConfig;
import com.smartfarm.bean.WaterControlBean;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.UIHelper;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

public class WaterFragment extends BaseFragment implements EventHandler {

	private boolean isAuto = false;
	private boolean isOneKeyMode = false;

	private WaterControlBean[] waters;
	private TextView netState;
	private TextView maxWater;
	private TextView minWater;
	private TextView rumpState;
	private TextView yaoState;
	private ImageView mode;
	private ImageView oneKeyMode;
	
	private TextView tValue;
	private TextView tTemp;

	private LinearLayout tabOneKey;
	private LinearLayout tabValueTemp;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_water;
	}

	@Override
	protected void initView(View view) {

		waters = new WaterControlBean[6];
		waters[Constants.WATER_ID_1] = new WaterControlBean(
				view.findViewById(R.id.water1),
				view.findViewById(R.id.water1_dividing), Constants.WATER_ID_1);
		waters[Constants.WATER_ID_2] = new WaterControlBean(
				view.findViewById(R.id.water2),
				view.findViewById(R.id.water2_dividing), Constants.WATER_ID_2);
		waters[Constants.WATER_ID_3] = new WaterControlBean(
				view.findViewById(R.id.water3),
				view.findViewById(R.id.water3_dividing), Constants.WATER_ID_3);
		waters[Constants.WATER_ID_4] = new WaterControlBean(
				view.findViewById(R.id.water4),
				view.findViewById(R.id.water4_dividing), Constants.WATER_ID_4);
		waters[Constants.WATER_ID_5] = new WaterControlBean(
				view.findViewById(R.id.water5),
				view.findViewById(R.id.water5_dividing), Constants.WATER_ID_5);
		waters[Constants.WATER_ID_6] = new WaterControlBean(
				view.findViewById(R.id.water6),
				view.findViewById(R.id.water5_dividing), Constants.WATER_ID_6);

		mode = (ImageView) view.findViewById(R.id.toggle_AT);
		maxWater = (TextView) view.findViewById(R.id.maxTempValue);
		minWater = (TextView) view.findViewById(R.id.minTempValue);
		netState = (TextView) view.findViewById(R.id.button_startGT);

		View rumpView = view.findViewById(R.id.water_rump);
		rumpState = (TextView) rumpView.findViewById(R.id.water_rump_state);
		
		View yaoView=view.findViewById(R.id.water_yao);
		yaoState = (TextView) yaoView.findViewById(R.id.water_rump_state);
		tValue=(TextView) view.findViewById(R.id.water_value);
		tTemp =(TextView)view.findViewById(R.id.water_temp);
		tabValueTemp=(LinearLayout) view.findViewById(R.id.tab_value_temp);
		tabOneKey = (LinearLayout) rumpView.findViewById(R.id.tab_one_key);
		
		oneKeyMode = (ImageView) rumpView.findViewById(R.id.toggle_one_key);
		oneKeyMode.setOnClickListener(this);

		TextView yaoTitle = (TextView) yaoView
				.findViewById(R.id.water_rump_title);
		yaoTitle.setText(R.string.yao);

		mode.setOnClickListener(this);
		netState.setOnClickListener(this);
		rumpView.findViewById(R.id.water_rump_close).setOnClickListener(this);
		rumpView.findViewById(R.id.water_rump_open).setOnClickListener(this);
		view.findViewById(R.id.imageButton_setup).setOnClickListener(this);

		yaoView.findViewById(R.id.water_rump_close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SerialHelper.closeYao(SerialControlRunable.SOURCE_PAD_BUTTON);
			}
		});
		yaoView.findViewById(R.id.water_rump_open).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						SerialHelper.openYao(SerialControlRunable.SOURCE_PAD_BUTTON);
					}
				});
	}

	@Override
	protected void initData() {

//		for (WaterControlBean bean : waters)
//			bean.setValue(0);
		tValue.setText(0+"%RH");

		refreshWaterCount();
	}

	@Override
	public void onResume() {

		refreshData();

		super.onResume();
	}

	private void refreshData() {

		WaterConfig wConfig = SimpleConfigManager.getInstance().getWaterModel();
		RunningData runningData = RunningData.getInstance();

		setYaoState(runningData.getYaoState());
		setRumpState(runningData.getRumpState());

		maxWater.setText("滴灌时间:" + wConfig.getTime());
		minWater.setText("湿度阈值:" + wConfig.getMin());
		tabOneKey.setVisibility(View.GONE);
		tabValueTemp.setVisibility(View.VISIBLE);

		netState.setText(AppContext.context().netIdOk() ? R.string.net_connect
				: R.string.net_disconnect);

		for (WaterControlBean bean : waters) {

//			bean.setValue(runningData.getWater(0));
			bean.setState(runningData.getWaterState(bean.getWaterId()));
		}
		tValue.setText(runningData.getWater(0)+"%RH");

		modeChange();
	}
	//一键灌水状态变更
	private void refreshOneKey() {
		RunningData rd = RunningData.getInstance();
		boolean _onekey=rd.getOneKeyWaterState();
		isOneKeyMode=_onekey;
		if(_onekey){
			if (rd.getWaterState(Constants.WATER_ID_1)) {
				SerialHelper.closeWater(Constants.WATER_ID_1,
						SerialControlRunable.SOURCE_PAD_BUTTON, true);
			}
			SerialHelper.openWater(Constants.WATER_ID_1,
					SerialControlRunable.SOURCE_PAD_BUTTON, true);
			if (!rd.getRumpState()) {
				SerialHelper.openBeng(
						SerialControlRunable.SOURCE_PAD_BUTTON, true);
			}
		}else{
			WaterConfig wConfig = SimpleConfigManager.getInstance()
					.getWaterModel();
			int nLimit = wConfig.getWaterCount();
			if (rd.getRumpState()) {
				SerialHelper.closeBeng(
						SerialControlRunable.SOURCE_PAD_BUTTON, true);
			}
			for (int i = 1; Constants.WATER_ID_ALL[i] < Constants.WATER_ID_ALL[nLimit]; i++) {
				if (rd.getWaterState(Constants.WATER_ID_ALL[i])) {
					SerialHelper.closeWater(Constants.WATER_ID_ALL[i],
							SerialControlRunable.SOURCE_PAD_BUTTON,
							true);
				}
			}
		}
		oneKeyMode.setImageResource(_onekey?  R.drawable.button_auto: R.drawable.button_auto_manual);
		
	}

	private void modeChange() {

		isAuto = SimpleConfigManager.getInstance().getWaterModel().isMode();
		mode.setImageResource(isAuto ? R.drawable.button_auto
				: R.drawable.button_auto_manual);
	}

	private void refreshWaterCount() {

		WaterConfig wConfig = SimpleConfigManager.getInstance().getWaterModel();

		for (int i = 0; i < 6; i++) {

			if (i < wConfig.getWaterCount()) {
				waters[i].setVisibility(true);
			} else {
				waters[i].setVisibility(false);
			}
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.toggle_AT) {

			if (isAuto) {
				if (MessageBox.DialogResult.OK == MessageBox.Show(
						getActivity(), "控制系统将不能自动进行滴灌！", "请确定需要进入手动模式？",
						MessageBoxButtons.OKCancel)) {
					
					SimpleConfigManager.getInstance().getWaterModel()
							.setMode(false);
					SimpleConfigManager.getInstance().saveWaterCfg();
					modeChange();
				}
			} else {
				SimpleConfigManager.getInstance().getWaterModel().setMode(true);
				SimpleConfigManager.getInstance().saveWaterCfg();
				modeChange();
			}
		}

		else if (v.getId() == R.id.imageButton_setup) {

			UIHelper.showSimpleBack(getActivity(), BackPage.WATER);
		} else if (v.getId() == R.id.button_startGT) {

			HuanxinHelper.get().relink(getActivity());
		} else if (v.getId() == R.id.water_rump_open) {
			if (RunningData.getInstance().judgeWaterState())
				SerialHelper.openBeng(SerialControlRunable.SOURCE_PAD_BUTTON,false);
			else
				ToastTool.showToast("请先打开电磁阀再打开水泵！");
		} else if (v.getId() == R.id.water_rump_close) {
			SerialHelper.closeBeng(SerialControlRunable.SOURCE_PAD_BUTTON,
					false);
		} else if (v.getId() == R.id.toggle_one_key) {
			if (!isOneKeyMode) {
				if (MessageBox.DialogResult.OK == MessageBox.Show(
						getActivity(), "控制系统将开启一键灌溉", "确认后会自动灌水",
						MessageBoxButtons.OKCancel)) {					
					SerialHelper.openKeyWater(SerialControlRunable.SOURCE_PAD_BUTTON);
				}
			} else {
				if (MessageBox.DialogResult.OK == MessageBox.Show(
						getActivity(), "控制系统将取消一键灌水", "确认后会取消一键灌水",
						MessageBoxButtons.OKCancel)) {
					SerialHelper.closeKeyWater(SerialControlRunable.SOURCE_PAD_BUTTON);
				}
			}
		}
	}

	private void setRumpState(boolean open) {

		rumpState.setText(open ? "打开" : "关闭");
		rumpState.setTextColor(open ?Color.GREEN:Color.RED);
	}

	private void setYaoState(boolean open) {

		yaoState.setText(open ? "打开" : "关闭");
		yaoState.setTextColor(open ?Color.GREEN:Color.RED);
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
	public void onEvent(LocalEvent event) {
		if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_CHANGE) {
			netState.setText(AppContext.context().netIdOk() ? R.string.net_connect
					: R.string.net_disconnect);
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_WATER_COUNT_CHANGE) {
			refreshWaterCount();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_WATER_MODE_CHANGE) {
			refreshData();
			ProtocolFactory.getNoticeWaterModeProtocol().send();
			Log.d("zqq", "waterfragment");
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_RECEIVE_WATER) {
			tValue.setText(String.valueOf(event.getEventValue())+"%RH");
			tTemp.setText(event.getEventMsg()+"°C");
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_WATER_CHANGE) {
			refreshData();
		}
		else if (event.getEventType()==LocalEvent.EVENT_TYPE_WATER_ONEKEY_CHANGE){
			refreshOneKey();
		}
	}
}