package com.smartfarm.control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author QSD
 *线程的等待，通知主线程更新
 */
public class HelperThread {

	private volatile boolean threadIsWork = false;
	private boolean running = true;
	private static HelperThread mThread;
	private BlockingQueue<Runnable> otherworks; 

	public static HelperThread getInstance() {
		
		if(mThread == null)
			mThread = new HelperThread();
		
		return mThread;
	}
	
	private HelperThread() {
		otherworks = new LinkedBlockingQueue<Runnable>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(running) {
					
					while(!otherworks.isEmpty()) {
						try {
							Runnable work = otherworks.take();
							work.run();
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					
					setThreadStop();
					waitForUiThread();
				}
			}
		}).start();
	}
	
	public synchronized void setThreadWork(Runnable work) {
		otherworks.add(work);
		
		if (!threadIsWork) {
			threadIsWork = true;
			this.notify();
		}
	}
	
	private synchronized void setThreadStop() {
		threadIsWork = false;
	}
	
	private synchronized void waitForUiThread() {
		while(!threadIsWork) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public void setStopWork() {
		running = false;
	}
}
