package com.smartfarm.protocol;

import android.util.Log;

import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;

public class MsgSendHelper {
	
	
	public static void send(Protocol protocol) {
		
		sendByHuanxin(protocol);
		Log.d(Constants.TAG, "send by huanxin");
		String dataLog = protocol.getCmdType()+","
				+CommonTool.getTotalRxBytes()+","+CommonTool.getTotalTxBytes();
		CommonTool.writerDataLog(dataLog);
	}
	
	private static void sendByHuanxin(Protocol protocol) {
		
		HuanxinHelper.get().sendMessage(protocol);
	}
	
}
