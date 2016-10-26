package com.smartfarm.db.access;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.TempRecord;
import com.smartfarm.tools.ExceptionUtils;

public class TempDao {
	
	public static void add(int windowId, int source, float temp, int count, int consume) {
		
		try {
			
			Dao<TempRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempRecord.class);
			TempRecord record = new TempRecord();
			
			record.setCount(count);
			record.setConsum(consume);
			record.setSource(source);
			record.setSyn(false);
			record.setTemp(temp);
			record.setTime(System.currentTimeMillis());
			record.setWindowId(windowId);
			
			dao.create(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void removeOld() {
		
		try {
			
			Dao<TempRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempRecord.class);
			long oldTime = System.currentTimeMillis() - 30 *24 *3600 * 1000;
			
			dao.updateRaw("delete temp_record where time < " + oldTime);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
}
