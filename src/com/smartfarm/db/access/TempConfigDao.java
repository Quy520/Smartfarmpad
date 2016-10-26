package com.smartfarm.db.access;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.tools.ExceptionUtils;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.SimpleConfigManager;

public class TempConfigDao {

	public static List<TempConfig> findAll() {

		List<TempConfig> res;
		try {
			
			Dao<TempConfig, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempConfig.class);
			
			res = dao.queryForAll();
			
			if(res == null || res.isEmpty()) {
				
				res = getDefaultConfig();
				
				for (TempConfig liveItemBean : res) {  
                	dao.create(liveItemBean);
                }  
			}
			return res;
			
		} catch (SQLException e) {
			
			ExceptionUtils.report(e);
			return null;
		}
	}
	
	public static void update(Map<Integer, TempConfig> configs) {
		
		try {
			
			Dao<TempConfig, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempConfig.class);
			
			for(TempConfig config : configs.values()) {
				
				String updateSql = "UPDATE temp_config SET max = " + config.getMax() 
						+ ", nor_max = " + config.getNorMax() + ", nor_min = " + config.getNorMin()
						+ ", min = " + config.getMin() + " WHERE window_id = " + config.getWindowId();
				int res = dao.updateRaw(updateSql);
				Log.d("mmsg",  "temp config update sql -> " + updateSql + " ,res -> " + res);
			}
			
		} catch (SQLException e) {
			ExceptionUtils.report(e);
		}
	}
	
	public static boolean update(TempConfig config) {
		
		try {

			Dao<TempConfig, Integer> dao = DatabaseHelper.getDbHelper().getDao(TempConfig.class);
			
			return dao.update(config) > 0;
		} catch(SQLException e) {
			ExceptionUtils.report(e);
			return false;
		}
	}
	
	public static List<TempConfig> getDefaultConfig() {
		
		List<TempConfig> data = new ArrayList<TempConfig>();
		
		for(int i = 0; i < 9; i++) {
			
			TempConfig bean = new TempConfig();
			
			bean.setWindowId(i);
			bean.setMax(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_MAX, 34));
			bean.setNorMax(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_NOR_MAX, 30));
			bean.setNorMin(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_NOR_MIN, 26));
			bean.setMin(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_MIN, 22));
			data.add(bean);
		}
		
		return data;
	}
}
