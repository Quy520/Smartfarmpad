package com.smartfarm.tools;

import java.util.List;

import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.view.SimpleConfigManager;

/**
 * 冬季温控控制策略
 * 
 * @author jeff
 *
 */
public class AdjustTempByInsulation implements AdjustTempTemplate {

	@Override
	public void onReceiveTemp(int tempRange, int trend, int windowId, long percentage, int currTemp) {
		
		List<Integer> allState = SimpleConfigManager.getInstance().getConfig().getAllStalls();
		
		switch(tempRange) {
		
		case 0:
			
			if(CommonTool.compareBound(percentage, allState.get(1)) < 0) {
				
				SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, windowId);
				SerialHelper.exeOpen(SerialControlRunable.SOURCE_AUTO, windowId);
			} else if(trend >= 0) {
				
				SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, windowId);
				SerialHelper.exeOpen(SerialControlRunable.SOURCE_AUTO, windowId);
			} 
			break;
		case 1:
			
			if(CommonTool.compareBound(percentage, allState.get(1)) < 0) {
				
				SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, windowId);
				SerialHelper.exeOpen(SerialControlRunable.SOURCE_AUTO, windowId);
			}
			break;
		case 2:
			
			if(CommonTool.compareBound(percentage, allState.get(1)) < 0) {
				
				SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, windowId);
				SerialHelper.exeOpen(SerialControlRunable.SOURCE_AUTO, windowId);
			} else if(CommonTool.compareBound(percentage, allState.get(1)) > 0){
				
				SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, windowId);
				SerialHelper.exeClose(SerialControlRunable.SOURCE_AUTO, windowId);
			}
			break;
		case 3:
			
			if(CommonTool.compareBound(percentage, allState.get(1)) > 0){
				
				SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, windowId);
				SerialHelper.exeClose(SerialControlRunable.SOURCE_AUTO, windowId);
			}
			break;
		default:

			if(RunningData.getInstance().getCloseCount(windowId) <= 3) {
				SerialHelper.exeStop(SerialControlRunable.SOURCE_AUTO, windowId);                                                              
				SerialHelper.exeClose(SerialControlRunable.SOURCE_AUTO, windowId);
			}
			
				
			break;
		}
	}

}
