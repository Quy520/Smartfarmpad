package com.smartfarm.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.smartfarm.control.SerialHelper;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.tools.Alarm;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.RebootDialog;
import com.smartfarm.tools.RebootDialog.OnBtnClickListener;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.SimpleConfigManager;

public class BatteryBroadcastReceiver extends BroadcastReceiver {
	
	public static volatile boolean isCharging = true;
	
	private boolean lastState = true;
	private boolean hasError = false;
	private boolean checkBattery = true;
	private boolean isShow = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			int status = intent.getIntExtra("status", 0);

			switch (status) {
			case BatteryManager.BATTERY_STATUS_UNKNOWN:
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				isCharging = false;
				break;
			case BatteryManager.BATTERY_STATUS_CHARGING:
			case BatteryManager.BATTERY_STATUS_FULL:
				isCharging = true;
				break;
			}

			if(!isCharging) {
				//获取当前电量  
                int level = intent.getIntExtra("level", 0);  
                //电量的总刻度  
                int scale = intent.getIntExtra("scale", 100);  
                //把它转成百分比  
               
                int now = (level*100)/scale;
                if(now < 50 && checkBattery && !isShow) {
                	
                	isShow = true;
                	new RebootDialog(context, new OnBtnClickListener() {
						
						@Override
						public void onClickPositiveButton() {

							isShow = false;
							CommonTool.ToClose();
						}
						
						@Override
						public void onClickNegativeButton() {

							isShow = false;
							checkBattery = false;
						}
					});
                }
			}
			
			if(lastState == isCharging) 
				return;
			
			lastState = isCharging;
			
			if(!isCharging) {
				
				hasError = true;
				dealwith(isCharging);
				
				SerialHelper.losePowerStopAll();
				
			} else {
				if(hasError) {

					hasError = false;
					dealwith(isCharging);
				}
			}
		}
	}
	
	private void dealwith(boolean isCharging) {
		
		String text = isCharging ? "温控机重新连接电源。" : "温控机失去电源，可能已经停电";
		
		ToastTool.showToast(text);
		
		String phone = SimpleConfigManager.getInstance().getConfig().getPhonenumber();
		if(phone.equals("")) {
			
			ToastTool.showToast("请设置主手机号码！");
		} else if(Alarm.isShouldAlarm()) {
			
			ProtocolFactory.getAlarmProtocol(text, "" + System.currentTimeMillis()).send();
			CommonTool.SendSMS(phone, text);
		}
		
		if(isCharging) {
			CommonTool.ToReboot();
		}
	}
}
