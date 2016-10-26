package com.smartfarm.db.access;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.ControlRecord;
import com.smartfarm.tools.ExceptionUtils;

public class ControlDao {
	
	public static void add(int windowId, int source, int res, int controlType, int len) {
		
		try {
			
			Dao<ControlRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(ControlRecord.class);
			ControlRecord record = new ControlRecord();
			
			record.setSource(source);
			record.setSyn(false);
			record.setRes(res);
			record.setLen(len);
			record.setTime(System.currentTimeMillis());
			record.setWindowId(windowId);
			record.setControlType(controlType);
			
			dao.create(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void removeOld() {
		
		try {
			
			Dao<ControlRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(ControlRecord.class);
			long oldTime = System.currentTimeMillis() - 30 *24 *3600 * 1000;
			
			dao.updateRaw("delete control_record where time < " + oldTime);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
}
