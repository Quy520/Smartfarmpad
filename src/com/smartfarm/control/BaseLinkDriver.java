package com.smartfarm.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;

public abstract class BaseLinkDriver implements MsgRespondDelay,Runnable {
	//正则表达式
	public static final String FEEDBACK_PATTERN = "[A-Z]\\d:\\d{3,3}.{1}\\s*";
	public static final String MOTOR_TEMP_FEEDBACK_PATTERN = "\\d+\\.\\d?C\\s*";
	public static final String MOTOR_LIGHT_FEEDBACK_PATTERN = "[0-9A-F]+Lux\\s*";
	public static final String ID_PATTERN = "\\d{3,3}";
	
	public static final int SERIAL_TYPE_FEEDBACK = 10;
	public static final int SERIAL_TYPE_TEMP = 11;
	public static final int SERIAL_TYPE_LIGHT = 12;
	public static final int SERIAL_TYPE_WATER = 13;
	public static final int SERIAL_TYPE_OTHER = 14;
	
	protected OutputStream mOutputStream;
	protected InputStream mInputStream;
	protected boolean _isOpen = false;
	protected Vector<EventHandler> handlers = new Vector<EventHandler>();
	protected Thread mThread;
	
	@Override
	public void pushMsgEvent(LocalEvent event) {
		Iterator<EventHandler> receivers = handlers.iterator();
		while(receivers.hasNext())
			receivers.next().onEvent(event);
	}

	@Override
	public abstract boolean init();
	public boolean isConnected() {
		return _isOpen;
	}
	
	public void addSerialMsgWatcher(EventHandler watcher) {
		handlers.add(watcher);
	}
	
	public void removeSerialMsgWatcher(EventHandler watcher) {
		handlers.remove(watcher);
	}
}
