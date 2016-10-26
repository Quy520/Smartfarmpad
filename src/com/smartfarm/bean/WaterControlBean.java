package com.smartfarm.bean;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.view.R;
/**
 * 
 * 开关水阀
 * @author Lei
 *
 */
public class WaterControlBean implements OnClickListener {
	
	private int waterId;
	private ImageView icon;
	private boolean open = false;
	private View waterView;
	private View dividing;
	
	public WaterControlBean(View waterView, View dividing, int id) {
		
		waterId = id;
		this.waterView = waterView;
		this.dividing = dividing;
		
		icon = (ImageView) waterView.findViewById(R.id.water_state);
		
		waterView.findViewById(R.id.water_open).setOnClickListener(this);
		waterView.findViewById(R.id.water_close).setOnClickListener(this);
	}
	
//	public void setValue(int value) {
//		
//		this.value.setText(value + "%RH");
//	}
//	
//	public void setTemp(float temp) {
//		
//		this.temp.setText(temp + "℃");
//	}
	
	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.water_open) 
			SerialHelper.openWater(waterId, SerialControlRunable.SOURCE_PAD_BUTTON, false);
		else 
			SerialHelper.closeWater(waterId, SerialControlRunable.SOURCE_PAD_BUTTON,false);
	}
	
	public void setState(boolean state) {
		
		open = state;
		icon.setSelected(open);
	}
	
	public int getWaterId() {
		
		return waterId;
	}
	
	public void setVisibility(boolean show) {
		dividing.setVisibility(show ? View.VISIBLE : View.GONE);
		waterView.setVisibility(show ? View.VISIBLE : View.GONE);
	}
}
