package com.smartfarm.tools;

import android.bluetooth.BluetoothAdapter;

public class DeviceIdHelper {

	private static String DEVICE_ID = "";
	
	public static String get() {
		
		if(ShowUtil.isEmpty(DEVICE_ID)) {
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			DEVICE_ID = "pad" + Md5Utils.encode(Md5Utils.encode(bluetoothAdapter.getAddress()));
		}
		
		return DEVICE_ID;
	}
}
