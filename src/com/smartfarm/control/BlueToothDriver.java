package com.smartfarm.control;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Text;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.db.access.SerialMsgDao;
import com.smartfarm.tools.BlueToothHelper;
import com.smartfarm.view.SimpleConfigManager;
/**
 * 
 * @author QSD
 *连接蓝牙接收串口消息
 */

public class BlueToothDriver extends BaseLinkDriver {
	private BlueToothHelper blueHelper;
	private BluetoothSocket socket;

	@Override
	public boolean init() {
		blueHelper = BlueToothHelper.getBlueToothHelper();
		if(SimpleConfigManager.getInstance().getConfig().getBluetoothmac()==null || SimpleConfigManager.getInstance().getConfig().getBluetoothmac().isEmpty()) {
//			ToastTool.showToast("温控机和蓝牙从未配对过！！！");
			return false;
		}
		if (blueHelper.get_socket() == null) {
			blueHelper.initSocket();
		}
		socket = blueHelper.get_socket();
		try {
			mInputStream = socket.getInputStream();
			mOutputStream = socket.getOutputStream();
			_isOpen = true;

			mThread = new Thread(this);
			mThread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void run() {
		int num = 0;
		byte[] buffer = new byte[1024];
		// 接收线程
		while (true) {
			try {
				if (mInputStream.available() == 0) {
					TimeUnit.MILLISECONDS.sleep(100);
				}
				while (mInputStream.available() != 0) {
					num = mInputStream.read(buffer); // 读入数据
					if (num > 0) {
						StringBuffer sbHex = new StringBuffer();

						for (int j = 0; j < num; j++) {
							sbHex.append((char) (buffer[j] & 0x000000FF));
						}
						receiveSerialMsg(sbHex.toString(), buffer);
						Log.d("mmsg", sbHex.toString());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeDriver() {

	}

	@Override
	public boolean write(byte[] data, int len) {
		try {
			if (socket == null)
				blueHelper.initSocket();
			mOutputStream.write(data);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public void receiveSerialMsg(String receiveMsg, byte[] data) {
		SerialMsgDao.add("Bluetooth:"+receiveMsg);
		if(receiveMsg.matches(FEEDBACK_PATTERN)) {
			//接收到满足 X1:00n格式的消息
			try {
				if(receiveMsg.contains("C4") || receiveMsg.contains("B4")) {
					int id = Integer.valueOf(receiveMsg.split(":")[1].trim());
					SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, id - 1);
				}
			} catch(Exception e) {}
			pushMsgEvent(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_SERIAL, 
					SERIAL_TYPE_FEEDBACK, receiveMsg));
		} else if(receiveMsg.matches(MOTOR_TEMP_FEEDBACK_PATTERN)){
			//接收到温度消息
			pushMsgEvent(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_SERIAL, 
					SERIAL_TYPE_TEMP, receiveMsg));
		} else if(receiveMsg.matches(MOTOR_LIGHT_FEEDBACK_PATTERN)) {
			//接收到照度消息
			pushMsgEvent(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_SERIAL, 
					SERIAL_TYPE_LIGHT, receiveMsg));
		}else {
			//接收到其他消息
			pushMsgEvent(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_SERIAL, 
					SERIAL_TYPE_OTHER, receiveMsg, data));
		}
	}
}