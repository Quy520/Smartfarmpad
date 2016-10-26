package com.smartfarm.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.db.access.CmdDao;
import com.smartfarm.db.access.ControlDao;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.LocalDataCollection;

public class SerialControl implements Runnable {

	private boolean running = true;
	private static Thread mThread;
	private BlockingQueue<SerialControlRunable> otherworks;
	private volatile boolean threadIsWork = false;

	private ArrayList<DelayRunable> delayList = new ArrayList<DelayRunable>();

	private BaseLinkDriver driver;

	public SerialControl() {
		if(!Constants.BLUETOOTH)
			driver = new SerialDriver();
		else driver=new BlueToothDriver();
		otherworks = new LinkedBlockingDeque<SerialControlRunable>();

		mThread = new Thread(this);
		mThread.start();
	}

	private int dealWithRunable(SerialControlRunable runable, int id) {
		if (runable.getType() == Constants.MOTOR_CONTROL_TYPE_STOP
				|| runable.getType() == Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE
				|| runable.getType() == Constants.MOTOR_CONTROL_TYPE_BENG_CLOSE) {

			DelayRunable delay = null;

			synchronized (delayList) {

				Iterator<DelayRunable> it = delayList.iterator();

				while (it.hasNext()) {

					DelayRunable dr = it.next();

					if (dr.cancleable(runable.getType(), id)) {
						delay = dr;
						it.remove();
						break;
					}
				}
			}
			if (delay != null)
				delay.onCancle();
		}
		int res = runable.onExecute(hardwareHandler, id);

		long nowState = RunningData.getInstance().getWindowState(id);
		ControlDao.add(id, runable.getSource(), res % 1000, runable.getType(),
				(int) (nowState / 6000));
		return res;
	}

	@Override
	public void run() { 

		driver.init();
		while (running) {
			while (!otherworks.isEmpty()) {
				try {

					if (!driver.isConnected()) {

						CommonTool.delay(5000);
						continue;
					}

					SerialControlRunable runable = otherworks.take();

					CmdDao.add(runable.getId(), runable.getSource(), 1, runable
							.getType(), runable.getIds() == null ? "" : runable
							.getIds().toString());

					runable.setSerialControl(SerialControl.this);
					runable.beforeExecute();

					List<Integer> ids = runable.getIds();
					driver.addSerialMsgWatcher(runable);

					StringBuilder result = new StringBuilder();

					if (ids == null) {

						result.append(dealWithRunable(runable, runable.getId()));
						result.append(";");
					} else {

						for (int id : ids) {
							result.append(dealWithRunable(runable, id));
							result.append(";");
						}
					}

					driver.removeSerialMsgWatcher(runable);

					runable.afterExecute(result.toString());

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			setThreadStop();
			waitForUiThread();
		}
	}
	
	public void cleanRunnable(){
		synchronized(otherworks){
			otherworks.clear();
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_DEVICE_CHANGE_ERROR));
		}
	}

	@SuppressLint("HandlerLeak")
	Handler hardwareHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {

			try {
				switch (msg.what) {
				case Constants.MSG_ADD_DELAY_STOP:

					DelayRunable addTuple = (DelayRunable) msg.obj;
					
					// 添加延迟任务
					synchronized (delayList) {
						delayList.add(addTuple);
					}

					Message delayMag = obtainMessage(Constants.MSG_RECEIVE_DELAY_STOP, addTuple);
					sendMessageDelayed(delayMag, msg.arg1);

					break;
				case Constants.MSG_RECEIVE_DELAY_STOP:
					// 延迟任务被触发
					DelayRunable delay = (DelayRunable) msg.obj;

					boolean removeRes = false;
					// 找延迟任务
					synchronized (delayList) {
						Iterator<DelayRunable> it = delayList.iterator();

						while (it.hasNext()) {

							DelayRunable dr = it.next();

							if (dr.executeable(delay.time)) {
								removeRes = true;
								it.remove();
								break;
							}
						}
					}
					// 任务延迟执行
					if (removeRes)
						delay.onExecute();
					break;
				}
			} catch (Exception e) {

				e.printStackTrace();
				String errorLog = CommonTool.getErrorMsg(e);
				LocalDataCollection.getInstance().addErrorLog(errorLog);
			}

			super.dispatchMessage(msg);
		}

	};

	public void send(byte[] msg) {

		driver.write(msg, msg.length);
	}

	public synchronized void setSerialRunable(SerialControlRunable runable) {

		otherworks.add(runable);

		if (!threadIsWork) {
			threadIsWork = true;
			this.notify();
		}
	}

	private synchronized void setThreadStop() {
		threadIsWork = false;
	}

	private synchronized void waitForUiThread() {
		while (!threadIsWork) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}