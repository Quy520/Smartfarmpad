package com.smartfarm.db.access;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.SerialMsgRecord;
import com.smartfarm.tools.ExceptionUtils;

public class SerialMsgDao {

	public static void add(String msg) {
		
		try {
			
			Dao<SerialMsgRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(SerialMsgRecord.class);
			SerialMsgRecord record = new SerialMsgRecord();
			
			record.setMsg(msg);
			
			dao.create(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void removeOld() {
		
		try {
			
			Dao<SerialMsgRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(SerialMsgRecord.class);
			long oldTime = System.currentTimeMillis() - 30 *24 *3600 * 1000;
			
			dao.updateRaw("delete serial_msg where time < " + oldTime);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
}
