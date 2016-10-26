package com.smartfarm.bean;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.view.R;

public class LightControlBean implements OnClickListener {

	private int lightId;
	private ImageView icon;
	private View lightView;
	private View dividing;
	private boolean open = false;
	
	public LightControlBean(View lightView, View dividing, int id) {
		
		lightId = id;
		this.lightView = lightView;
		this.dividing = dividing;
		icon = (ImageView) lightView.findViewById(R.id.light_state);
		
		lightView.findViewById(R.id.light_open).setOnClickListener(this);
		lightView.findViewById(R.id.light_close).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(lightId>=1){
			return;
		}
		if(v.getId() == R.id.light_open) {
			
			SerialHelper.openLight(lightId, SerialControlRunable.SOURCE_PAD_BUTTON);
		} else {
			
			SerialHelper.closeLight(lightId, SerialControlRunable.SOURCE_PAD_BUTTON);
		}
	}
	
	public int getId() {
		
		return lightId;
	}
	
	public void setState(boolean isOpen) {
		
		open = isOpen;
		icon.setSelected(open);
	}
	
	public void setVisibility(boolean show) {
		dividing.setVisibility(show ? View.VISIBLE : View.GONE);
		lightView.setVisibility(show ? View.VISIBLE : View.GONE);
	}
}
