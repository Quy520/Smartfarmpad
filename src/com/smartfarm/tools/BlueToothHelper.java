package com.smartfarm.tools;

import java.io.IOException;
import java.util.UUID;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BlueToothHelper {
	private BlueToothHelper() {
	}
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter(); // 获取本地蓝牙适配器，即蓝牙设备
	private BluetoothSocket _socket; // 蓝牙通信socket
	private BluetoothDevice _device; // 蓝牙设备
	private ConfigModel config = SimpleConfigManager.getInstance().getConfig();
	private static BlueToothHelper bHelper=new BlueToothHelper();
	private int count=1;
	
	public BluetoothAdapter get_bluetooth() {
		return _bluetooth;
	}

	public BluetoothSocket get_socket() {
		return _socket;
	}
	public void set_socket(BluetoothSocket _socket) {
		this._socket=_socket;
	}

	public static BlueToothHelper getBlueToothHelper() {
		if (bHelper == null) {
			return new BlueToothHelper();
		}
		return bHelper;
	}

	//检查是否有蓝牙设备
	public void checkBlue(){
		if (_bluetooth== null) {
			ToastTool.showToast("平板无法启动蓝牙，请联系代理商！");
			return;
		}
	}
	//检查蓝牙连接状态  若没连接则连接
	public boolean setBlueTooth(){
		Log.d("mmsg", "setBlueTooth");
		//激活蓝牙
		if(!_bluetooth.isEnabled()){
			Log.e("mmsg", " setBlueTooth set null");
			try {
				_socket.close();
				_socket=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			_bluetooth.enable();
		}
		
		if(_socket==null)
			//连接设备
			initSocket();
			return true; 
	}
	//检查socket是否存在
	public boolean SocketState(){
		
		if(_socket==null){
			return false;
		}
		return true;
	}
	//初始化socket 成功返回true  失败false
	public void initSocket(){
		Log.d("mmsg", "linkBlueTooth");
		if(_device==null){
			_device = _bluetooth.getRemoteDevice(config.getBluetoothmac());
		}
		try {
			_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.MY_UUID));
		} catch (IOException e1) {
			Log.d("zqq", "连接蓝牙失败1："+e1);
		}
		try {
			_socket.connect();
			EventBus.getDefault().postInOtherThread(
					LocalEvent.getEvent(LocalEvent.EVENT_TYPE_BLUETOOTH_ON));
		} catch (IOException e2) {
			e2.printStackTrace();
			Log.d("zqq", "连接蓝牙失败2："+e2);
			try {
				Log.e("mmsg", " initSocket set null");
				_socket.close();
				_socket=null;
				count++;
				if(count==10){
					//重启平板
					/*CommonTool.ToReboot();*/
				}
				Thread.sleep(1000);
				initSocket();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return;
		}
		return;
	}
}
