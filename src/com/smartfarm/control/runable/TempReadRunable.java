package com.smartfarm.control.runable;

import android.os.Handler;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialCmd;
import com.smartfarm.control.SerialDriver;
import com.smartfarm.db.access.TempDao;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.tencent.bugly.crashreport.CrashReport;
/**
 * 
 * 
 * @author QSD
 *读取温度
 */
public class TempReadRunable extends SerialControlRunable {
	
	private int destWindowId;
	private int needSave;
	private int source = -1;
	private float lastTemp = -1;
	private float currTemp = 0;
	
	public TempReadRunable(int windowId, int needSave, int source) {
		
		this.source = source;
		destWindowId = windowId;
		this.needSave = needSave;
	}

	@Override
	public int onExecute(Handler handler, int currId) {

		currTemp = 0;
		int count = 0;
		String res = null;
		lastTemp = RunningData.getInstance().getTempRecordRecently(currId);
		long start = System.currentTimeMillis();
		
		while(true) {
			
			if(count >= 10) 
				break;

			sendSerialMsg(SerialCmd.CMD_READ_ALL[currId]);
			CommonTool.delay(300);
			
			boolean find = false;
			while(!msgs.isEmpty()) {
				
				try {
					LocalEvent msg = msgs.take();
					Log.d("zqq", " take msg -> " + msg.getEventMsg());

					if(msg.getEventValue() == SerialDriver.SERIAL_TYPE_TEMP) {

						res = msg.getEventMsg();
						int index = res.indexOf("C");
						String temp = res.substring(0, index);
						currTemp = Float.parseFloat(temp);

						if(currTemp == 0) {
							
							count++;
							
							if(count >= 4 && count < 8) {
								Log.d(Constants.TAG, "read temp zero, wait 1s! ");
								CommonTool.delay(1000);
							}
							continue;
						} else if((currTemp > (lastTemp + 1) || currTemp < (lastTemp - 1)) && count < 6) {
							
							if(count >= 1)
								lastTemp = currTemp;
							
							count++;
							CommonTool.delay(300);
							continue;
						}
						
						find = true;
						break;
					}
				} catch (Exception e) {
					CrashReport.postCatchedException(e);
					e.printStackTrace();
				}
			}
			
			if(find)
				break;

			count++;

			if(count >= 5 && count < 10) {
				Log.d(Constants.TAG, "read temp error, wait 1s! ");
				CommonTool.delay(1000);
			}
			continue;
		}

		if(res == null) {
			
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_TEMP, currId, String.valueOf(needSave)));
			TempDao.add(currId, source, -1, count, (int)(System.currentTimeMillis() - start));
			return LINK_ERROR;
		}
		
		TempDao.add(currId, source, currTemp, count, (int)(System.currentTimeMillis() - start));

		if(currTemp<10 && RunningData.getInstance().getTempRecordRecently(currId) - currTemp>5)
			currTemp=RunningData.getInstance().getTempRecordRecently(currId);
		
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_TEMP, currId, String.valueOf(needSave), currTemp));

		return SUCCESS;
	}

	@Override
	public int getId() {
		return destWindowId;
	}

	@Override
	public int getType() {
		return Constants.MOTOR_CONTROL_TYPE_READ;
	}

	@Override
	public int getSource() {
		return source;
	}

}
