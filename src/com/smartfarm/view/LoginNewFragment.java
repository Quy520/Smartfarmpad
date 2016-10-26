package com.smartfarm.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.smartfarm.db.access.UserDao;
import com.smartfarm.db.bean.ApiUser;
import com.smartfarm.db.bean.User;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.control.MyCountTimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginNewFragment extends BaseFragment {
	private Button msgBtn;
	private Button voiceBtn;
	private EditText verEdit;
	private MyCountTimer timeCountMsg;
	private MyCountTimer timeCountVoice;
	private MyCountTimer _timeCountMsg;
	private MyCountTimer _timeCountVoice;
	private String phone = "";
	private BroadcastReceiver mMsgReceiver;
	private long msgTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_login_new, container, false);
		initView(view);
		initData();
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMsgReceiver = new SmsReceiver();
		getActivity().registerReceiver(mMsgReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}
	
	@Override
	public void onDestroy() {
		if (mMsgReceiver != null)
			getActivity().unregisterReceiver(mMsgReceiver);
		super.onDestroy();
	}

	@Override
	protected void initView(View view) {
		msgBtn = (Button) view.findViewById(R.id.register_send_note);
		voiceBtn = (Button) view.findViewById(R.id.voice_code);
		verEdit = (EditText) view.findViewById(R.id.register_note_check);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		msgBtn.setOnClickListener(this);
		voiceBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		phone = AppContext.context().getPhone();
		msgTime = System.currentTimeMillis(); 
		timeCountMsg = new MyCountTimer(60 * 1000,1000,msgBtn,"获取短信验证码");
		timeCountVoice = new MyCountTimer(60 * 1000,1000,msgBtn,"获取语音验证码");
		long differ = msgTime - AppContext.context().getMsgTime();
//		long differ_voice = msgTime - AppContext.context().getVoiceTime();
		_timeCountMsg = new MyCountTimer(differ,1000,msgBtn,"获取短信验证码");
		_timeCountMsg.start();
//		if(differ_voice < 60 * 1000){
//			_timeCountVoice = new MyCountTimer(differ_voice,1000,voiceBtn,"获取语音验证码");
//			_timeCountVoice.start();
//		}
		
	}
	
	class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			SmsMessage msg = null;
			if (bundle != null) {
				Object[] smsObj = (Object[]) bundle.get("pdus");
				for (Object object : smsObj) {
					msg = SmsMessage.createFromPdu((byte[]) object);
					String yzm = msg.getDisplayMessageBody();
					if(yzm.matches("【上海洲涛】.*")){
						verEdit.setText(getNumbers(msg.getDisplayMessageBody()));
						login();
					}
				}
			}
		}
	}
	
   //截取数字  
	 public String getNumbers(String content) {
	    Pattern pattern = Pattern.compile("\\d+");  
	    Matcher matcher = pattern.matcher(content);  
	    while (matcher.find()) {  
	        return matcher.group(0);  
	    }  
	    return "";  
    }
	
	private void login(){
		String ver = verEdit.getText().toString();
		if(ver.isEmpty()){
			ToastTool.showToast("请输入验证码");
			return;
		}
		SmartfarmNetHelper.appRegisterLogin(phone,ver,new BaseAsyncHttpResHandler() {
			
			@Override
			public void onSuccess(ApiResponse res) {
				ToastTool.showToast("登录成功");
				User user = JSON.toJavaObject((JSON)res.getResponseData(), User.class);
				ApiUser localUser = user.getApiUser(user);
				user = UserDao.add(user);
				AppContext.context().getAccountManager().save(user,localUser,user.getPhone(),
						localUser.getHxPwd(), localUser.getToken(),localUser.getRefreshToken());
				HuanxinHelper.get().login(null);
				ProtocolFactory.getTestProtocolToSelf().send();
				CommonTool.HideKb(getActivity(), getView());
				
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
	}
	
	private void sendMsg(){
		showLoadingDialog();
		SmartfarmNetHelper.getSmsVerCode(phone,new BaseAsyncHttpResHandler() { 
			
			@Override
			public void onSuccess(ApiResponse res) {
				ToastTool.showToast("短信已发送，请注意查收");
				timeCountMsg.start();
				AppContext.context().setMsgTime(System.currentTimeMillis());
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
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_send_note:
			sendMsg();
			break;
			
		case R.id.voice_code:
			showLoadingDialog();
			SmartfarmNetHelper.getVoiceVerCode(phone, new BaseAsyncHttpResHandler() {
				
				@Override
				public void onSuccess(ApiResponse res) {
					timeCountVoice.start();
					AppContext.context().setVoiceTime(System.currentTimeMillis());
					ToastTool.showToast("语音已发送，请注意查收");
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
			
		case R.id.login_btn:
			login();
			break;
		default:
			break;
		}
	}
}
