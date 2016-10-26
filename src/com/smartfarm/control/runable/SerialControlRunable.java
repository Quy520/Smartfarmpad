package com.smartfarm.control.runable;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import android.os.Handler;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.SerialControl;
import com.smartfarm.control.SerialHelper.OnTaskFinishListener;

/**
 * 串口任务接口
 * 
 * @author jeff
 *
 */
public abstract class SerialControlRunable implements EventHandler {
	
	public static final int SOURCE_AUTO = 1;
	public static final int SOURCE_PAD_BUTTON = 2;
	public static final int SOURCE_PHONE = 3;
	
	public static final int SUCCESS = 1;
	public static final int DRIVER_ERROR = 101;
	public static final int MOTOR_IS_WORKING = 102;
	public static final int LINK_ERROR = 103;
	public static final int VEN_MODE = 104;
	
	public static final int WINDOW_OPEN_MAX = 1000;
	public static final int WINDOW_CLOASE_MIN = 2000;
	
	private SerialControl serialControl;
	protected LinkedBlockingDeque<LocalEvent> msgs = new  LinkedBlockingDeque<LocalEvent>();
	private List<Integer> ids;
	private List<Integer> devices;
	private OnTaskFinishListener listener;
	
	public void setOnTaskFinishListener(OnTaskFinishListener listener) {
		this.listener = listener;
	}
	
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	
	public List<Integer> getIds() {
		return ids;
	}
	
	public void beforeExecute() {};

	public List<Integer> getDevices() {
		return devices;
	}

	public void setDevices(List<Integer> devices) {
		this.devices = devices;
	}

	public abstract int onExecute(Handler handler, int currId);

	public void afterExecute(String res) {

		if(listener != null)
			listener.onTaskFinish(res);
	};
	
	public abstract int getId();
	
	public abstract int getType();
	
	public abstract int getSource();

	@Override
	public void onEvent(LocalEvent event) {
		
		msgs.add(event);
	};
	
	public void setSerialControl(SerialControl serialControl) {
		
		this.serialControl = serialControl;
	}
	
	public void sendSerialMsg(byte[] msg) {
		
		serialControl.send(msg);
	}

}