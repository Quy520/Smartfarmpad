package com.smartfarm.db.access;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.HuanxinMsgRecord;
import com.smartfarm.tools.ExceptionUtils;

public class HuanxinMsgDao {

	public static void add(String msg) {
		
		try {
			
			Dao<HuanxinMsgRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(HuanxinMsgRecord.class);
			HuanxinMsgRecord record = new HuanxinMsgRecord();
			
			record.setMsg(msg);
			
			dao.create(record);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
	
	public static void removeOld() {
		
		try {
			
			Dao<HuanxinMsgRecord, Integer> dao = DatabaseHelper.getDbHelper().getDao(HuanxinMsgRecord.class);
			long oldTime = System.currentTimeMillis() - 30 *24 *3600 * 1000;
			
			dao.updateRaw("delete huanxin_msg where time < " + oldTime);
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
		}
	}
}
