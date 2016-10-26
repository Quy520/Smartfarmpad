package com.smartfarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.smartfarm.protocol.SmsMsgResolve;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.view.ConfigModel;
import com.smartfarm.view.SimpleConfigManager;

public class TempSMSreceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage msg = null;

		if (bundle != null) {
			Object[] smsObj = (Object[]) bundle.get("pdus");
			ConfigModel cnofig = SimpleConfigManager.getInstance().getConfig();
			String phone1 = CommonTool.checkMobile(cnofig.getPhonenumber()) ? cnofig.getPhonenumber() : "noPhone1";
			String phone2 = CommonTool.checkMobile(cnofig.getPhonenumber2()) ? cnofig.getPhonenumber2() : "noPhone2";
			String phone3 = CommonTool.checkMobile(cnofig.getPhonenumber3()) ? cnofig.getPhonenumber3() : "noPhone3";
			
			for (Object object : smsObj) {
				msg = SmsMessage.createFromPdu((byte[]) object);
				String telephony = msg.getOriginatingAddress();
				
				int phone = -1;
				if(telephony.contains(phone1))
					phone = 1;
				else if(telephony.contains(phone2))
					phone = 2;
				else if(telephony.contains(phone3))
					phone = 3;
				
				if (phone > 0) {
					
					SmsMsgResolve.resolve(msg.getDisplayMessageBody(), telephony);
				}
			}
		}
	}
}