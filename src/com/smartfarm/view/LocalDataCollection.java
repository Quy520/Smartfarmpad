package com.smartfarm.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;

public class LocalDataCollection {
	private static LocalDataCollection collector;
	private File root;
	private File other;
	private File sdcardPath;
	private File startLog;
	
	public static LocalDataCollection getInstance() {
		
		if(collector == null)
			collector = new LocalDataCollection();
		
		return collector;
	}

	private LocalDataCollection() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		
		sdcardPath = null;
		if (sdCardExist) {
			sdcardPath = Environment.getExternalStorageDirectory();// 获取跟目录
			
			String rootPath = sdcardPath.getAbsolutePath() + Constants.LOCAL_DATA_ROOT_PATH;
			root = new File(rootPath);
			
			other = new File(rootPath + CommonTool.getCurrentDate() + ".txt");

			startLog = new File(rootPath + "startlog.txt");
			
			if(startLog.exists())
				startLog.delete();
		} else {
			Log.e(Constants.TAG, "不存在sd卡");
		}
	}
	
	public boolean addErrorLog(String errorlog) {
		check();
		
		File errorLog = new File(root.getAbsoluteFile() + File.separator + "error-" + CommonTool.getDateString2(new Date()) + ".txt");
		
		try {
			errorLog.createNewFile();
		} catch(Exception e) {
			return false;
		}
		
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(errorLog, true));
			
			out.append(errorlog);
			out.flush();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void checkImeiFile(String imei) {
		
		if(sdcardPath == null || sdcardPath.equals(""))
			return;
		
		File imeiFile = new File(sdcardPath.getAbsoluteFile() + File.separator + imei + ".txt");
		
		if(imeiFile.exists())
			return;
		
		BufferedWriter out = null;
		try {
			imeiFile.createNewFile();
			out = new BufferedWriter(new FileWriter(imeiFile, true));
			
			out.append(imei);
			out.flush();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean check() {
		if(root == null)
			return false;
		
		if (!root.exists()) 
			root.mkdirs();
		
		try {
			
			if(!other.exists())
				other.createNewFile();
			
			return true;
			
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized boolean addControlRecord(String control) {
		check();
		
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(other, true));
			
			out.append(control);
			out.append("\r\n");
			out.flush();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
