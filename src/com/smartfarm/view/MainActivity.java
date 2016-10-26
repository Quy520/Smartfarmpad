package com.smartfarm.view;

import java.io.IOException;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.MainViewTab;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.receiver.BatteryBroadcastReceiver;
import com.smartfarm.receiver.TempSMSreceiver;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.FileHelper;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.UIHelper;
import com.smartfarm.view.MessageBox.MessageBoxButtons;
import com.smartfarm.view.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.smartfarm.view.NavigationDrawerFragment.OnMenuItemClickListener;

public class MainActivity extends FragmentActivity implements
		NavigationDrawerCallbacks, OnMenuItemClickListener, EventHandler {

	public static boolean running = false;

	private NavigationDrawerFragment mNaviFragment;
	private ViewPager viewPager;
	private PowerManager.WakeLock wakeLock = null;
	private TempSMSreceiver tempSMSreceiver;
	private BatteryBroadcastReceiver batteryReceiver;
	private PendingIntent clearIntent;
	private MyViewpagerAdapter adapter;
	private boolean fragmentChanged = false;

	Fragment[] childs = new Fragment[4];

	private boolean showLightView = false;
	private boolean showWaterView = false;

	private FileHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		running = true;
		Log.d(Constants.TAG, getClass().getName()
				+ " onCreate , start to init soft ...");

		helper = new FileHelper(getApplicationContext());
		try {
			helper.createSDFile("data.txt").getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.index_view);

		mNaviFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mNaviFragment.setOnMenuItemClickListener(this);
		mNaviFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		ConfigModel cm = SimpleConfigManager.getInstance().getConfig();

		showLightView = cm.isLightEnable();
		showWaterView = cm.isWaterEnable();

		adapter = new MyViewpagerAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.index_viewpager);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(adapter);

		tempSMSreceiver = new TempSMSreceiver();
		batteryReceiver = new BatteryBroadcastReceiver();

		registerReceiver(tempSMSreceiver, new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED"));
		registerReceiver(batteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		// if(AppContext.context().getAccountManager().isLogined())
		// loginHuanxin();

		ProtocolFactory.getTestProtocolToSelf().send();

		Intent blueIntent = new Intent(this, BlueToothService.class);
		startService(blueIntent);
		Intent it = new Intent();
		it.setAction(MsgService.ACTION_START);
		it.setClass(this, MsgService.class);
		startService(it);

		if (!Constants.IS_FACTORY) {
			SerialHelper.readAll(SerialControlRunable.SOURCE_AUTO);
			SerialHelper.stopAll(SerialControlRunable.SOURCE_AUTO);
		}

		if (savedInstanceState != null) {

			for (Fragment f : getSupportFragmentManager().getFragments()) {

				if (TempFragment.class.isInstance(f))
					childs[0] = f;
				else if (LightFragment.class.isInstance(f))
					childs[1] = f;
				else if (WaterFragment.class.isInstance(f))
					if (showLightView)
						childs[1] = f;
					else
						childs[2] = f;
			}
		}

		EventBus.getDefault().add(this);

	}

	public NavigationDrawerFragment getmNaviFragment() {
		return mNaviFragment;
	}

	@Override
	protected void onResume() {

		if (fragmentChanged)
			adapter.notifyDataSetChanged();

		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected void onDestroy() {

		running = false;

		if (tempSMSreceiver != null)
			unregisterReceiver(tempSMSreceiver);

		if (batteryReceiver != null)
			unregisterReceiver(batteryReceiver);

		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.cancel(clearIntent);

		EventBus.getDefault().remove(this);

		releaseWakeLock();
		CommonTool.delay(3000);
		Intent i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		super.onDestroy();
	}

	private class MyViewpagerAdapter extends FragmentStatePagerAdapter
			implements OnPageChangeListener {

		public MyViewpagerAdapter(FragmentManager fm) {

			super(fm);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			int realIndex = getRealIndex(position);
			childs[realIndex] = null;

			super.destroyItem(container, position, object);
		}

		@Override
		public Fragment getItem(int index) {

			int realIndex = getRealIndex(index);

			if (childs[realIndex] == null)
				childs[realIndex] = Fragment.instantiate(MainActivity.this,
						getViewTab(realIndex).getName());

			return childs[realIndex];
		}

		@Override
		public int getCount() {

			int count = 1;

			if (showLightView)
				count++;

			if (showWaterView)
				count++;

			return count;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		private int getRealIndex(int index) {

			if (index == 0) {
				return 0;
			} else if (index == 1) {
				if (showLightView)
					return 1;
				else
					return 2;
			} else if (index == 2) {
				return 2;
			}

			return 3;
		}

		public MainViewTab getViewTab(int index) {

			if (index == 0) {
				return MainViewTab.TEMP_VIEW;
			} else if (index == 1) {
				return MainViewTab.LIGHT_VIEW;
			} else if (index == 2) {
				return MainViewTab.WATER_VIEW;
			}

			return MainViewTab.INDEX_VIEW;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}

	}

	@Override
	public void onClick(int index) {

		viewPager.setCurrentItem(index);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			UIHelper.showHome(this);

			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public void acquireWakeLock(Context context) {

		if (wakeLock == null) {
			PowerManager powerManager = (PowerManager) (context
					.getSystemService(Context.POWER_SERVICE));
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					Constants.TAG);
			wakeLock.acquire();
		}
	}

	public void releaseWakeLock() {

		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	private int errorCount = 0;

	@Override
	public void onEvent(LocalEvent event) {

		if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_CHANGE) {

			int resCode = event.getEventValue();
			if (resCode != 1) {

				if (resCode == HuanxinHelper.CONN_RES_CONFLTCT) {

					try {

						String title = "帐号提示";
						String content = "您的手机号码在异地登录,如需重连请点击连接状态下的重新连接按钮，"
								+ "请尽量不要让其他人使用自己的手机号码控制，避免发生其他的安全性问题。";

						ShowUtil.showNotice(AppContext.context(), title,
								content, null, content);
						AppContext.context().getAccountManager().exit();
						MessageBox.Show(this, content, title,
								MessageBoxButtons.OK);

					} catch (Exception e) {
					}
				} else if (resCode == HuanxinHelper.CONN_RES_NO_NET) {

					Log.d(Constants.TAG, "温控机失去网络连接！");
				} else if (resCode == HuanxinHelper.CONN_RES_REMOVEED
						|| resCode == HuanxinHelper.CONN_RES_SERVER_ERROR) {

					if (errorCount <= 0) {
						errorCount++;
						return;
					}
					String content = "温控机连接服务器异常！";
					ShowUtil.showNotice(AppContext.context(), "帐号提示", content,
							null, content);
				}
			} else {

				errorCount = 0;
			}
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_PAGER_CHANGE) {

			ConfigModel cm = SimpleConfigManager.getInstance().getConfig();

			showLightView = cm.isLightEnable();
			showWaterView = cm.isWaterEnable();

			if (!showLightView)
				childs[1] = null;

			if (!showWaterView)
				childs[2] = null;

			fragmentChanged = true;
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_NO_ROOT) {

			new AlertDialog.Builder(this).setTitle("警告")
					.setMessage("未取到root权限，请查看是否禁用本软件的root权限!")
					.setNegativeButton("确定", null).show();
		}
	}
}
