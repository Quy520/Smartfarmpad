package com.smartfarm.tools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.AsyncTask;

public class AssistThread extends AsyncTask<Void, AssistThreadWork, Void> {
	private volatile boolean threadIsWork = false;
	private boolean running = true;
	private static AssistThread thread;
	private BlockingQueue<AssistThreadWork> works; 
	
	public static void init() {
		if(thread != null)
			thread.cancel(true);
		
		thread = new AssistThread();
		thread.execute();
	}
	
	public static AssistThread getInstance() {
		return thread;
	}
	
	
	private AssistThread() {
		works = new LinkedBlockingQueue<AssistThreadWork>();
	}
	
	public synchronized void setThreadWork(AssistThreadWork work) {
		works.add(work);
		
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

	@Override
	protected Void doInBackground(Void... params) {
		while(running) {
			while(!works.isEmpty()) {
				try {
					AssistThreadWork work = works.take();
					work.working();
					publishProgress(work);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			setThreadStop();
			waitForUiThread();
		}
		
		return null;
	}

	public void publishPro(AssistThreadWork work) {
		publishProgress(work);
	}
	
	@Override
	protected void onProgressUpdate(AssistThreadWork... values) {
		AssistThreadWork work = values[0];
		work.ok();
	}
	
	public void setStopWork() {
		running = false;
		thread = null;
	}
}
