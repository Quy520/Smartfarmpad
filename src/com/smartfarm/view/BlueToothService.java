package com.smartfarm.view;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.tools.BlueToothHelper;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ToastTool;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BlueToothService extends Service implements Runnable, EventHandler  {

	private Handler mHandler;
	private BlueToothHelper blueHelper;
	@Override
	public IBinder onBind(Intent intent) {
		return new MsgBinder();
	}

	public class MsgBinder extends Binder {
		public BlueToothService getService() {
			return BlueToothService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("ZQQ", "服务启动");
		if(!Constants.BLUETOOTH) return;
		blueHelper=BlueToothHelper.getBlueToothHelper();
		blueHelper.checkBlue();
		registerBoradcastReceiver();
		mHandler = new Handler();
		mHandler.postDelayed(this, 1000);
	}

	@Override
	public void run() {
		ConfigModel config=SimpleConfigManager.getInstance().getConfig();
		 if (config.getBluetoothmac() == null || config.getBluetoothmac().isEmpty()) {
			ToastTool.showToast("温控机和蓝牙从未配对过，请先配对");
		} 
		 else if(blueHelper.get_socket()==null){
			blueHelper.initSocket();
		} 
//		blueHelper.setBlueTooth();
		mHandler.postDelayed(this, 1 * 1000 * 60);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	private void registerBoradcastReceiver() {
		IntentFilter stateChangeFilter = new IntentFilter(
				BluetoothAdapter.ACTION_STATE_CHANGED);
		IntentFilter connectedFilter = new IntentFilter(
				BluetoothDevice.ACTION_ACL_CONNECTED);
		IntentFilter disConnectedFilter = new IntentFilter(
				BluetoothDevice.ACTION_ACL_DISCONNECTED);
		registerReceiver(stateChangeReceiver, stateChangeFilter);
		registerReceiver(stateChangeReceiver, connectedFilter);
	}

	private BroadcastReceiver stateChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				Log.d("zqq", "1");
			}
			if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				Log.d("zqq", "2");
			}
			if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				blueHelper.setBlueTooth();
			}
		}
	};
	@Override
	public void onEvent(LocalEvent event) {
		// TODO Auto-generated method stub
		if (event.getEventType()==LocalEvent.EVENT_TYPE_RESTART_SERVICE){
			stopSelf();
		}
	}
	
}