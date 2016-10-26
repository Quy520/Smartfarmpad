package com.smartfarm.bean;

import android.os.Handler;

import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.SimpleConfigManager;

public class VenBean {
	
	private boolean lastMode;
	private long startTime;
	private int venTime = -1;
	private Handler mHandler;
	
	public VenBean() {
		
		mHandler = new Handler();
	}
	
	public VenBean(int venTime) {
		
		this();
		this.venTime = venTime;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void start() {
		long opentime = SimpleConfigManager.getInstance().getConfig()
				.getOpenLenFirst() * Constants.MOTOR_SPEED;
		
		lastMode = SimpleConfigManager.getInstance().getConfig().isAutoOrManual();
		
		startTime = System.currentTimeMillis() + opentime + 3 * 1000;
		SimpleConfigManager.getInstance().modeChangeAll(SerialControlRunable.SOURCE_PHONE, false);
		AppContext.context().setVenBean(this);
		
		int delay = SimpleConfigManager.getInstance().getConfig().getVenTime() * 60000 + 
				SimpleConfigManager.getInstance().getConfig().getOpenLenFirst() * Constants.MOTOR_SPEED * 1000;
		
		EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_VEN, 1));

		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				end(SerialControlRunable.SOURCE_AUTO);
			}
		}, venTime == -1 ? delay : venTime);
	}
	
	public void endVen() {

		SerialHelper.stopAll(SerialControlRunable.SOURCE_AUTO);
		ProtocolFactory.getOpenWindowsModeProtocol("stop").send();
		mHandler.removeCallbacksAndMessages(null);
		end(SerialControlRunable.SOURCE_AUTO);
	}
	
	private void end(int source) {
		SerialHelper.closeAll(source);
		SimpleConfigManager.getInstance().modeChangeLast(SerialControlRunable.SOURCE_PHONE,lastMode,true);
		AppContext.context().setVenBean(null);
		EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_VEN, 0));
	}
}
