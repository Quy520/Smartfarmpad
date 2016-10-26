package com.smartfarm.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.protocol.Protocol;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.UIHelper;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

/**
 * 温度控制界面
 * 
 * @author jeff
 *
 */
public class TempFragment extends BaseFragment implements EventHandler {

	private boolean isAuto = false;
	private boolean isHighAuto = false;

	private TextView maxTempValue;
	private TextView minTempValue;
	private TextView[] windowTemp;
	private TextView[] windowState;
	private TextView btn_service;
	private TextView venModeShow;
	private TextView tShowLogView;
	private TextView tIstest;
	private TextView tAirMoisture;
	private ImageView toggle_AT;
	private ImageView toggleOpenEnable;
	private View[] windowView;
	private View[] dividing;
	private TextView btnMenu;

	int color_step[] = new int[49];
	int alfa = 255;

	@Override
	protected int getLayoutId() {
		return R.layout.main_view;
	}

	@Override
	protected void initView(View view) {

		maxTempValue = (TextView) view.findViewById(R.id.maxTempValue);//主页面最大报警温度
		minTempValue = (TextView) view.findViewById(R.id.minTempValue);
		toggle_AT = (ImageView) view.findViewById(R.id.toggle_AT);//自动模式
		toggleOpenEnable = (ImageView) view.findViewById(R.id.toggle_open_enable);//手动自动切换
		tShowLogView = (TextView) view.findViewById(R.id.tvlog);//底部窗口
		btn_service = (TextView) view.findViewById(R.id.button_startGT);//网络状态
		venModeShow = (TextView) view.findViewById(R.id.ven_mode);//放风状态的textView
		tIstest = (TextView) view.findViewById(R.id.istest);//底部TextView
		tIstest.setVisibility(Constants.IS_DEBUG ? View.VISIBLE : View.GONE);//判断是否为测试或者生产模式
		tAirMoisture = (TextView) view.findViewById(R.id.temp_air_moisture);//空气湿度
		btnMenu = (TextView)view.findViewById(R.id.menu_btn);//呼出菜单

		toggle_AT.setOnClickListener(this);
		toggleOpenEnable.setOnClickListener(this);
		venModeShow.setOnClickListener(this);
		btn_service.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		view.findViewById(R.id.imageButton_setup).setOnClickListener(this);

		windowView = new View[9];
		dividing = new View[9];
		windowTemp = new TextView[9];
		windowState = new TextView[9];

		windowView[Constants.WINDOW_ID_1] = view
				.findViewById(R.id.main_window1);
		windowView[Constants.WINDOW_ID_2] = view
				.findViewById(R.id.main_window2);
		windowView[Constants.WINDOW_ID_3] = view
				.findViewById(R.id.main_window3);
		windowView[Constants.WINDOW_ID_4] = view
				.findViewById(R.id.main_window4);
		windowView[Constants.WINDOW_ID_5] = view
				.findViewById(R.id.main_window5);
		windowView[Constants.WINDOW_ID_6] = view
				.findViewById(R.id.main_window6);
		windowView[Constants.WINDOW_ID_7] = view
				.findViewById(R.id.main_window7);
		windowView[Constants.WINDOW_ID_8] = view
				.findViewById(R.id.main_window8);
		windowView[Constants.WINDOW_ID_9] = view
				.findViewById(R.id.main_window9);

		dividing[Constants.WINDOW_ID_1] = view
				.findViewById(R.id.main_window1_dividing);
		dividing[Constants.WINDOW_ID_2] = view
				.findViewById(R.id.main_window2_dividing);
		dividing[Constants.WINDOW_ID_3] = view
				.findViewById(R.id.main_window3_dividing);
		dividing[Constants.WINDOW_ID_4] = view
				.findViewById(R.id.main_window4_dividing);
		dividing[Constants.WINDOW_ID_5] = view
				.findViewById(R.id.main_window5_dividing);
		dividing[Constants.WINDOW_ID_6] = view
				.findViewById(R.id.main_window6_dividing);
		dividing[Constants.WINDOW_ID_7] = view
				.findViewById(R.id.main_window7_dividing);
		dividing[Constants.WINDOW_ID_8] = view
				.findViewById(R.id.main_window8_dividing);
		dividing[Constants.WINDOW_ID_9] = view
				.findViewById(R.id.main_window9_dividing);

		windowTemp[Constants.WINDOW_ID_1] = (TextView) windowView[Constants.WINDOW_ID_1]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_2] = (TextView) windowView[Constants.WINDOW_ID_2]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_3] = (TextView) windowView[Constants.WINDOW_ID_3]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_4] = (TextView) windowView[Constants.WINDOW_ID_4]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_5] = (TextView) windowView[Constants.WINDOW_ID_5]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_6] = (TextView) windowView[Constants.WINDOW_ID_6]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_7] = (TextView) windowView[Constants.WINDOW_ID_7]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_8] = (TextView) windowView[Constants.WINDOW_ID_8]
				.findViewById(R.id.textViewTemper);
		windowTemp[Constants.WINDOW_ID_9] = (TextView) windowView[Constants.WINDOW_ID_9]
				.findViewById(R.id.textViewTemper);

		windowState[Constants.WINDOW_ID_1] = (TextView) windowView[Constants.WINDOW_ID_1]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_2] = (TextView) windowView[Constants.WINDOW_ID_2]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_3] = (TextView) windowView[Constants.WINDOW_ID_3]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_4] = (TextView) windowView[Constants.WINDOW_ID_4]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_5] = (TextView) windowView[Constants.WINDOW_ID_5]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_6] = (TextView) windowView[Constants.WINDOW_ID_6]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_7] = (TextView) windowView[Constants.WINDOW_ID_7]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_8] = (TextView) windowView[Constants.WINDOW_ID_8]
				.findViewById(R.id.window_state);
		windowState[Constants.WINDOW_ID_9] = (TextView) windowView[Constants.WINDOW_ID_9]
				.findViewById(R.id.window_state);

		refreshWindowInfo();
	}

	/**
	 * 刷新窗口相关信息
	 */
	private void refreshWindowInfo() {

		int len = SimpleConfigManager.getInstance().getConfig().getWindowCount();

		for (int i = 0; i < windowView.length; i++) {
			if (i < len) {
				dividing[i].setVisibility(View.VISIBLE);
				windowView[i].setVisibility(View.VISIBLE);
			} else {
				dividing[i].setVisibility(View.GONE);
				windowView[i].setVisibility(View.GONE);
			}
		}

		int size = 1;
		int showTextSize = 1;

		if (len == 2) {
			size = 56;
			showTextSize = 16;
		} else if (len == 3) {
			size = 48;
			showTextSize = 14;
		} else if (len == 4) {
			size = 36;
			showTextSize = 14;
		} else if (len == 5) {
			size = 24;
			showTextSize = 12;
		} else if (len < 8) {
			size = 20;
			showTextSize = 12;
		} else {
			size = 20;
			showTextSize = 10;
		}
		//设置textView的大小
		maxTempValue.setTextSize(showTextSize);
		minTempValue.setTextSize(showTextSize);
		btn_service.setTextSize(showTextSize);
		venModeShow.setTextSize(showTextSize);

		for (int id = Constants.WINDOW_ID_1; id < len; id++) {

			ControlBtnOnClickListener listener = new ControlBtnOnClickListener(
					id);

			windowTemp[id].setTextSize(size);
			windowState[id].setTextSize(showTextSize);
			windowState[id].setText(getWindowStateDescr(RunningData.getInstance().getWindowState(id)));
			windowView[id].findViewById(R.id.imageButton_open).setOnClickListener(listener);
			windowView[id].findViewById(R.id.imageButton_close).setOnClickListener(listener);
			windowView[id].findViewById(R.id.imageButton_stop).setOnClickListener(listener);
		}
	}

	@Override
	protected void initData() {

		for (int i = 0; i <= 12; i++) {
			color_step[i] = Color.argb(alfa, 0, i * 20, 240);
		}

		for (int i = 13; i <= 24; i++) {
			color_step[i] = Color.argb(alfa, 0, 240, 480 - i * 20);
		}

		for (int i = 25; i <= 36; i++) {
			color_step[i] = Color.argb(alfa, (i - 24) * 20, 240, 0);
		}

		for (int i = 37; i <= 48; i++) {
			color_step[i] = Color.argb(alfa, 240, 240 - (i - 36) * 20, 0);
		}
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

		refreshData();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		//判断是否在自动
		if (v.getId() == R.id.toggle_AT) {
			/*判断是否在通风模式下*/
			if (AppContext.context().inVenMode()) {
				ToastTool.showToast("正处于通风模式下，模式无法更改！");
				return;
			}

			if (isAuto) {
				if (MessageBox.DialogResult.OK == MessageBox.Show(
						getActivity(), "控制系统将不能自动调整温度！", "请确定需要重进入手动模式？",
						MessageBoxButtons.OKCancel)) {

					SimpleConfigManager.getInstance().modeChange(
							SerialControlRunable.SOURCE_PAD_BUTTON, false);
				}
			} else {
				
				SimpleConfigManager.getInstance().modeChange(
						SerialControlRunable.SOURCE_PAD_BUTTON, true);
			}
		} else if (v.getId() == R.id.toggle_open_enable) {
			if(isHighAuto){
				if (MessageBox.DialogResult.OK == MessageBox.Show(getActivity(),
						"控制系统将不能根据温度自动转化为”自动模式“！", "请确定是否需要退出高温，转”自动模式“？",
						MessageBoxButtons.OKCancel)) {

					SimpleConfigManager.getInstance().modeOpenChange(
							SerialControlRunable.SOURCE_PAD_BUTTON, false);
				}
			}else {
				if (MessageBox.DialogResult.OK == MessageBox
						.Show(getActivity(),
								"当开启此功能时，温度超过高温报警温度后，将在原有报警提示的基础上，自动将模式切换到自动模式进行温度调节，保护作物正常生长。不开启则不会自动切换到自动模式。",
								"请确定需要进入高温转自动模式", MessageBoxButtons.OKCancel)) {

					SimpleConfigManager.getInstance().modeOpenChange(
							SerialControlRunable.SOURCE_PAD_BUTTON, true);

				}
			}
		} else if (v.getId() == R.id.button_startGT) {

			if (AppContext.context().getAccountManager().isLogined())
				HuanxinHelper.get().relink(getActivity());
			else
				UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
		} else if (v.getId() == R.id.ven_mode) {

			if (MessageBox.DialogResult.OK == MessageBox.Show(getActivity(),
					"确定要退出通风模式吗？", "确认", MessageBoxButtons.OKCancel)) {

				AppContext.context().getVenBean().endVen();
			}
		} else if (v.getId() == R.id.imageButton_setup) {

			UIHelper.showSimpleBack(getActivity(), BackPage.SETTING);
		} else if (v.getId() == R.id.menu_btn){
			MainActivity activity = (MainActivity)getActivity();
			activity.getmNaviFragment().openDrawerMenu();
		}
	}

	private void modeChange(boolean mode) {

		isAuto = mode;
		toggle_AT.setImageResource(mode ? R.drawable.button_auto : R.drawable.button_auto_manual);
		ProtocolFactory.getNoticeModeProtocol().send();
	}

	private void modeHighAuto(boolean autoMode) {
		
		isHighAuto = autoMode;
		toggleOpenEnable.setImageResource(autoMode ? R.drawable.button_auto
				: R.drawable.button_auto_manual);
//		ProtocolFactory.getNoticeHighModeProtocol().send();
	}
	

	/**
	 * 刷新右边显示配置信息及模式状态
	 */
	public void refreshData() {
		ConfigModel config = SimpleConfigManager.getInstance().getConfig();
		//设置最高，最低温度
		maxTempValue.setText(String.format(getString(R.string.max_temp),config.getAlarmMax()));
		minTempValue.setText(String.format(getString(R.string.min_temp),config.getAlarmMin()));

		refreshLenInfo();
		modeChange(config.isAutoOrManual());
		modeHighAuto(config.isAutoOpenEnable());
		
		btn_service.setText(AppContext.context().netIdOk() ? R.string.net_connect : R.string.net_disconnect);
		if (ShowUtil.isEmpty(windowTemp[0].getText().toString())) {

			Log.d("mmsg", "temp empty to set data !");
			int len = SimpleConfigManager.getInstance().getConfig()
					.getWindowCount();
			for (int id = Constants.WINDOW_ID_1; id < len; id++)
				setTempToView(id, Math.round(RunningData.getInstance()
						.getTempRecordRecently(id)));
		}
	}

	/**
	 * 刷新放风长度信息
	 */
	private void refreshLenInfo() {

		int len = SimpleConfigManager.getInstance().getConfig()
				.getWindowCount();
		for (int id = Constants.WINDOW_ID_1; id < len; id++)
			windowState[id].setText(getWindowStateDescr(RunningData
					.getInstance().getWindowState(id)));
	}

	private String getWindowStateDescr(long currState) {

		int percentage = Math.round((float) (currState)
				/ (float) (Constants.MOTOR_SPEED * 1000));

		return "已打开:" + percentage + "厘米";
	}

	private void setShowLogText(String text) {
		if (tShowLogView != null)
			tShowLogView.setText(String.format(getString(R.string.receive_msg),
					text));
	}

	class ControlBtnOnClickListener implements OnClickListener {
		int windowId;

		ControlBtnOnClickListener(int windowId) {
			this.windowId = windowId;
		}
		//放关停的操作
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageButton_open:

				SerialHelper.exeOpen(SerialControlRunable.SOURCE_PAD_BUTTON,
						windowId);
				break;
			case R.id.imageButton_close:

				SerialHelper.exeClose(SerialControlRunable.SOURCE_PAD_BUTTON,
						windowId);
				break;
			case R.id.imageButton_stop:
				SerialHelper.exeStop(SerialControlRunable.SOURCE_PAD_BUTTON,
						windowId);
				break;
			}
		}
	}

	private String getTempRecord(int temp, int windowId) {
		StringBuilder result = new StringBuilder();

		result.append("窗口");
		result.append(windowId + 1);
		result.append("收到温度值为: ");
		result.append(temp);

		return result.toString();
	}

	private void setTempToView(int id, int temp) {

		if (temp < 0)
			temp = 0;

		windowTemp[id].setTextColor(color_step[temp > 48 ? 48 : temp]);
		windowTemp[id].setText(String.valueOf(temp));
	}

	@Override
	public void onEvent(LocalEvent event) {

		if (event.getEventType() == LocalEvent.EVENT_TYPE_TEMP) {

			int temp = 0;
			if (event.getEventData() != null)
				temp = Math.round((Float) event.getEventData());

			setShowLogText(getTempRecord(temp, event.getEventValue()));
			setTempToView(event.getEventValue(), temp);
			if(temp >= 45){
				if(!SimpleConfigManager.getInstance().getConfig().isAutoOrManual()){
					SerialHelper.stopAll(SerialControlRunable.SOURCE_PAD_BUTTON);
					SimpleConfigManager.getInstance().modeChangeAll(SerialControlRunable.SOURCE_PAD_BUTTON, true);
				}
			}
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_VEN) {

			venModeShow.setVisibility(AppContext.context().inVenMode() ? View.VISIBLE : View.GONE);
			ProtocolFactory.getNoticeModeProtocol().send();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_RECEIVE_MSG) {

			Protocol data = (Protocol) event.getEventData();
			Log.d(Constants.TAG, data.getDescr());
			setShowLogText(data.getDescr());
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_MODE_CHANGE) {

			modeChange(SimpleConfigManager.getInstance().getConfig().isAutoOrManual());
			modeHighAuto(SimpleConfigManager.getInstance().getConfig().isAutoOpenEnable());
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_LEN_CHANGE) {

			refreshLenInfo();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_WINDOW_CHANGE) {

			refreshWindowInfo();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_CFG_CHANGE) {

			refreshData();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_AUTO_OPEN_ENABLE) {

			modeHighAuto(SimpleConfigManager.getInstance().getConfig().isAutoOpenEnable());
			
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_CHANGE) {

			int resCode = event.getEventValue();
			if (resCode != 1) {

				if (resCode == HuanxinHelper.CONN_RES_CONFLTCT)
					setShowLogText("您的手机号码在异地登录!");
				else if (resCode == HuanxinHelper.CONN_RES_NO_NET)
					setShowLogText("温控机失去网络连接！");
				else
					setShowLogText("温控机连接服务器异常！");

				btn_service.setText(R.string.net_disconnect);
			} else {

				btn_service.setText(R.string.net_connect);
				setShowLogText("已经连接上温控机！");
			}
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_CHECK_OTHER2) {
			tAirMoisture.setText(String.format(getString(R.string.air_moisture), RunningData.getInstance().getHumidity() + "%RH"));
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_IS_DEBUG) {
			tIstest.setText("测试模式");
			tIstest.setVisibility(View.VISIBLE);
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_AUTO_CHANGE){
			ConfigModel config = SimpleConfigManager.getInstance().getConfig();
			toggle_AT.setImageResource(config.isAutoOrManual() ? R.drawable.button_auto
					: R.drawable.button_auto_manual);
			toggleOpenEnable.setImageResource(config.isAutoOpenEnable() ? R.drawable.button_auto
					: R.drawable.button_auto_manual);
		}
		else if (event.getEventType() == LocalEvent.EVENT_TYPE_RAIN_MODE){
			
			ProtocolFactory.getRainModeProtocol(event.getEventMsg()).send();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_WIND_MODE ){
			
			ProtocolFactory.getOpenWindowsModeProtocol(event.getEventMsg()).send();
		} else if(event.getEventType() == LocalEvent.EVENT_TYPE_WATER_HX_OUT){
			
			HuanxinHelper.get().relink(getActivity());
		}
	}
}
