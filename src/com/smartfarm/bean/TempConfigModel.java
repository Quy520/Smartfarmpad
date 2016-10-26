package com.smartfarm.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.smartfarm.db.access.TempConfigDao;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.SimpleConfigManager;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * @author QSD
 *温度的控制与设置的类
 *
 */
public class TempConfigModel implements Serializable {

	private static final long serialVersionUID = 8834648880876050144L;

	@XStreamAlias("mode")
	private boolean moreMode;

	@XStreamAlias("temps")
	private Map<Integer, TempConfig> tempConfigs;

	@XStreamAlias("temp")
	private TempConfig tempConfig;
	
	public TempConfigModel(boolean mode) {
		
		moreMode = mode;
		reset();
	}

	@SuppressLint("UseSparseArrays")
	private void reset() {
		
		if(moreMode) {
			
			tempConfigs = new HashMap<Integer, TempConfig>();
			
			List<TempConfig> data = TempConfigDao.findAll();
			
			for(TempConfig bean : data)
				tempConfigs.put(bean.getWindowId(), bean);
		} else {
			
			tempConfig = new TempConfig();
			tempConfig.setMax(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_MAX, 34));
			tempConfig.setNorMax(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_NOR_MAX, 30));
			tempConfig.setNorMin(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_NOR_MIN, 26));
			tempConfig.setMin(SimpleConfigManager.getInt(AppContext.context(), SimpleConfigManager.KEY_TEMP_MIN, 22));
		}
	}
	
	public void updateConfig(TempConfig config) {
		
		if(moreMode) {
			
			TempConfigDao.update(config);
		} else {
			
			SimpleConfigManager sManager = SimpleConfigManager.getInstance();
			sManager.putInt(SimpleConfigManager.KEY_TEMP_MAX, config.getMax());
			sManager.putInt(SimpleConfigManager.KEY_TEMP_MIN, config.getMin());
			sManager.putInt(SimpleConfigManager.KEY_TEMP_NOR_MAX, config.getNorMax());
			sManager.putInt(SimpleConfigManager.KEY_TEMP_NOR_MIN, config.getNorMin());
		}
	}
	
	public TempConfig getConfig(int windowId) {
		
		if(moreMode) 
			return tempConfigs.get(windowId);
		else
			return tempConfig;
	}
	
	public boolean isTempMoreMode() {
		
		return moreMode;
	}
	
	public void synTemp(TempConfigModel model) {
		
		SimpleConfigManager.getInstance().putBoolean(SimpleConfigManager.KEY_TEMP_MORE_MODE, model.isTempMoreMode());
		moreMode = model.isTempMoreMode();
		
		if(moreMode) {
			
			TempConfigDao.update(model.tempConfigs);
		} else {

			TempConfig config = model.getConfig(0);
			SimpleConfigManager sManager = SimpleConfigManager.getInstance();
			sManager.putInt(SimpleConfigManager.KEY_TEMP_MAX, config.getMax());
			sManager.putInt(SimpleConfigManager.KEY_TEMP_MIN, config.getMin());
			sManager.putInt(SimpleConfigManager.KEY_TEMP_NOR_MAX, config.getNorMax());
			sManager.putInt(SimpleConfigManager.KEY_TEMP_NOR_MIN, config.getNorMin());
		}
		reset();
		
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_TEMP_CONFIG_CHANGE));
	}
}
