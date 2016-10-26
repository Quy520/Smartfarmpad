package com.smartfarm.control;

import java.io.File;
import java.io.IOException;

import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.db.access.SerialMsgDao;
import com.smartfarm.tools.Constants;

public class SerialDriver extends BaseLinkDriver {
	
	private SerialPort mSerialPort;
	private String sPort = "/dev/ttyMT0";
	private int iBaudRate = 9600;
	
	@Override
	public boolean init() {
		try {
			mSerialPort = new SerialPort(new File(sPort), iBaudRate, 0);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			_isOpen = true; 

			mThread = new Thread(this);
			mThread.start();
			Log.d(Constants.TAG, "serial new thread id -> " + mThread.getId() + " , name -> " + mThread.getName());

			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean write(byte[] data, int len) {
		try {
			mOutputStream.write(data);
		} catch (IOException e) {
			Log.d(Constants.TAG, "serial send error ! ");
			return false;
		}
		return true;
	}

	@Override
	public void closeDriver() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}

		try {
			if (mInputStream != null)
				mInputStream.close();
		} catch (Exception e) {
		}

		try {
			if (mOutputStream != null)
				mOutputStream.close();
		} catch (Exception e) {
		}

		_isOpen = false;
	}

	@Override
	public void run() {
		while (!mThread.isInterrupted()) {

			try {
				if (mInputStream == null) {

					Log.e(Constants.TAG, "serial mInputStream = null !");
					closeDriver();
					break;
				}

				byte[] buffer = new byte[512];

				Log.d(Constants.TAG, "serial thread listening ...");
				int size = mInputStream.read(buffer);

				if (size > 0) {
					StringBuffer sbHex = new StringBuffer();

					for (int j = 0; j < size; j++) {
						sbHex.append((char) (buffer[j] & 0x000000FF));
					}

					Log.d(Constants.TAG, " receive serial msg -> " + sbHex.toString());
					receiveSerialMsg(sbHex.toString(), buffer);
				}
				
			} catch (Exception e) {

				Log.w(Constants.TAG, "serial read error ! ");
				e.printStackTrace();
				return;
			}
		}
		Log.e(Constants.TAG, "thread isInterrupted");
	}
	
	@Override
	public void receiveSerialMsg(String receiveMsg, byte[] data) {
		SerialMsgDao.add(receiveMsg);
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
		}
		/*else if(receiveMsg.matches("(.*)\\s*ID:(\\d{2})\\s*")){
			String res = receiveMsg;
			int index = res.indexOf(":");
			int temp = Integer.parseInt(res.substring(index+1, res.length()-2));
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_GET_DEVICE_ID,temp));
		}*/
		
		else {
			//接收到其他消息
			pushMsgEvent(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_RECEIVE_SERIAL, 
					SERIAL_TYPE_OTHER, receiveMsg, data));
		}
	}
}