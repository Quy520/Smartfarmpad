package com.smartfarm.db.access;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.CmdRecord;
import com.smartfarm.tools.ExceptionUtils;

public class CmdDao {
	
	public static void add(int windowId, int source, int driverRes, int controlType, String ids) {
		
		try {
			
			Dao<CmdRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(CmdRecord.class);
			CmdRecord record = new CmdRecord();
			
			record.setSource(source);
			record.setSyn(false);
			record.setDriverRes(driverRes);
			record.setTime(System.currentTimeMillis());
			record.setWindowId(windowId);
			record.setControlType(controlType);
			record.setRemark(ids);
			
			dao.create(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void removeOld() {
		
		try {
			
			Dao<CmdRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(CmdRecord.class);
			long oldTime = System.currentTimeMillis() - 30 *24 *3600 * 1000;
			
			dao.updateRaw("delete cmd_record where time < " + oldTime);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
}
