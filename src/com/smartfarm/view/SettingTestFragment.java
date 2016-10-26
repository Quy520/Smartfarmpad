package com.smartfarm.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.tools.EventBus;

public class SettingTestFragment extends BaseFragment implements EventHandler {

	private Button openAll;
	private Button closeAll;
	private Button stopAll;
	private Button readAll;
	
	private TextView temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().add(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().remove(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_setting_test, container,
				false);
		initView(view);
		return view;
	}

	@Override
	protected void initView(View view) {
		openAll = (Button) view.findViewById(R.id.open_temp_all);
		closeAll = (Button) view.findViewById(R.id.close_temp_all);
		stopAll = (Button) view.findViewById(R.id.stop_temp_all);
		readAll = (Button) view.findViewById(R.id.read_temp_all);
		
		temp1 = (TextView) view.findViewById(R.id.temp1);
		temp2 = (TextView) view.findViewById(R.id.temp2);
		temp3 = (TextView) view.findViewById(R.id.temp3);
		temp4 = (TextView) view.findViewById(R.id.temp4);
		temp5 = (TextView) view.findViewById(R.id.temp5);
		temp6 = (TextView) view.findViewById(R.id.temp6);
		temp7 = (TextView) view.findViewById(R.id.temp7);
		temp8 = (TextView) view.findViewById(R.id.temp8);

		openAll.setOnClickListener(this);
		closeAll.setOnClickListener(this);
		stopAll.setOnClickListener(this);
		readAll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.read_temp_all:
			SerialHelper.readAll(SerialControlRunable.SOURCE_PAD_BUTTON);
			
			break;
		case R.id.open_temp_all:
			SerialHelper.openAll(SerialControlRunable.SOURCE_PAD_BUTTON);

			break;
		case R.id.close_temp_all:
			SerialHelper.closeAll(SerialControlRunable.SOURCE_PAD_BUTTON);

			break;
		case R.id.stop_temp_all:
			SerialHelper.stopAll(SerialControlRunable.SOURCE_PAD_BUTTON);
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onEvent(LocalEvent event) {
		if(event.getEventType()==LocalEvent.EVENT_TYPE_TEMP){
			if(event.getEventData()==null){
				return;
			}
			switch (event.getEventValue()) {
			case 0:
				temp1.setText(event.getEventData().toString());
				break;
			case 1:
				temp2.setText(event.getEventData().toString());
				break;
			case 2:
				temp3.setText(event.getEventData().toString());
				break;
			case 3:
				temp4.setText(event.getEventData().toString());
				break;
			case 4:
				temp5.setText(event.getEventData().toString());
				break;
			case 5:
				temp6.setText(event.getEventData().toString());
				break;
			case 6:
				temp7.setText(event.getEventData().toString());
				break;
			case 7:
				temp8.setText(event.getEventData().toString());
				break;

			default:
				break;
			}
		}
	}

}
