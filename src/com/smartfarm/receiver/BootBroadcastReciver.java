package com.smartfarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smartfarm.view.MainActivity;

public class BootBroadcastReciver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Intent it = new Intent();
		it.setClass(context, MainActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
	}
}
