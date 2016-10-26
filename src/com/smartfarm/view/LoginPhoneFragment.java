package com.smartfarm.view;

import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.UIHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LoginPhoneFragment extends BaseFragment {
	private TextView phoneNumber;
	private Button nextBtn;
	private String phone;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_phone, container, false);
		initView(view);
		initData();
		return view;
	}
	
	@Override
	protected void initView(View view) {
		phoneNumber = (TextView) view.findViewById(R.id.phone_number);
		nextBtn = (Button) view.findViewById(R.id.next_btn);
		
		view.findViewById(R.id.switch_login).setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			phone = phoneNumber.getText().toString().trim();
			AppContext.context().setPhone(phone);
			showLoadingDialog();
			SmartfarmNetHelper.getSmsVerCode(phone,new BaseAsyncHttpResHandler() { 
				
				@Override
				public void onSuccess(ApiResponse res) {
					ToastTool.showToast("短信已发送，请注意查收");
					AppContext.context().setMsgTime(System.currentTimeMillis()-60*1000);
					UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_NEW);
					getActivity().finish();
				}
				
				@Override
				public void onFailure(int errorCode, String data) {
					ToastTool.showToast(data);
				}
				
				@Override
				public void onFinish() {
					hideLoadingDialog();
				}
			});
			break;
		case R.id.switch_login:
			UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN);
			break;
		default:
			break;
		}
	}
	
}
