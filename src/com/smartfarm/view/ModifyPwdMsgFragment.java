package com.smartfarm.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.SimpleTextWatcher;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.control.MyCountTimer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ModifyPwdMsgFragment extends BaseFragment {

	private EditText userName;
	private EditText userPwd;
	private EditText userPwdAgain;
	private EditText verCode;
	private Button sendCode;
	private Button modify;

	private View userNameClear;
	private View userPwdClearAgain;
	private View userPwdClear;

	private Button btnVoiceCode;

	private MyCountTimer timeCount;

	private boolean modifying = false;
	private BroadcastReceiver mMsgReceiver;

	private final TextWatcher mUserNameWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			userNameClear.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE
					: View.VISIBLE);
		}
	};

	private final TextWatcher mPassswordWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			userPwdClear.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE
					: View.VISIBLE);
		}
	};

	private final TextWatcher mAgainPassswordWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			userPwdClearAgain
					.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE
							: View.VISIBLE);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMsgReceiver = new SmsReceiver();
		getActivity().registerReceiver(mMsgReceiver,
				new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}

	@Override
	public void onDestroy() {
		if (mMsgReceiver != null)
			getActivity().unregisterReceiver(mMsgReceiver);
		super.onDestroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_msg_modify, container,
				false);

		initView(view);
		initData();

		return view;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);

		userName = (EditText) view.findViewById(R.id.login_user_name);
		userPwd = (EditText) view.findViewById(R.id.login_user_pwd);
		userPwdAgain = (EditText) view.findViewById(R.id.login_user_pwd_again);
		userNameClear = view.findViewById(R.id.login_name_clear);
		userPwdClear = view.findViewById(R.id.login_new_pwd_clear);
		userPwdClearAgain = view.findViewById(R.id.login_pwd_clear_again);
		verCode = (EditText) view.findViewById(R.id.register_note_check);
		sendCode = (Button) view.findViewById(R.id.register_send_note);
		modify = (Button) view.findViewById(R.id.login_btn);
		btnVoiceCode = (Button) view.findViewById(R.id.voice_code);

		userName.addTextChangedListener(mUserNameWatcher);
		userPwd.addTextChangedListener(mPassswordWatcher);
		userPwdAgain.addTextChangedListener(mAgainPassswordWatcher);
		userNameClear.setOnClickListener(this);
		userPwdClear.setOnClickListener(this);
		userPwdClearAgain.setOnClickListener(this);
		sendCode.setOnClickListener(this);
		modify.setOnClickListener(this);
		btnVoiceCode.setOnClickListener(this);
	}

	@Override
	public boolean onBackPressed() {

		if (modifying)
			return true;

		return super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		if (modifying)
			return;
		String phone = userName.getText().toString();
		switch (v.getId()) {
		case R.id.login_btn:
			if (!prepareForLogin())
				return;

			showLoadingDialog();

			String account = userName.getText().toString();
			String pwd = userPwd.getText().toString();
			String code = verCode.getText().toString();
			SmartfarmNetHelper.appModify(account,
					Md5Utils.encode(Md5Utils.encode(pwd) + account), code,
					new BaseAsyncHttpResHandler() {

						@Override
						public void onSuccess(ApiResponse res) {
							ToastTool.showToast("修改成功，请登陆");
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
		case R.id.register_send_note:
			if (ShowUtil.isEmpty(phone)) {
				ToastTool.showToast("请输入手机号码！");
				userName.requestFocus();
				return;
			}
			AppContext.context().setPhone(phone);
			showLoadingDialog();
			SmartfarmNetHelper.getSmsVerCode(phone,
					new BaseAsyncHttpResHandler() {

						@Override
						public void onSuccess(ApiResponse res) {
							sendCode.setEnabled(false);
							timeCount = new MyCountTimer(60000, 1000, sendCode,
									"获取短信验证码");
							timeCount.start();
							ToastTool.showToast("短信已发送");
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
		case R.id.voice_code:
			
			if (ShowUtil.isEmpty(phone)) {
				ToastTool.showToast("请输入手机号码！");
				userName.requestFocus();
				return;
			}
			showLoadingDialog();
			AppContext.context().setPhone(phone);
			Builder dialog=new AlertDialog.Builder(getActivity());
			dialog.setMessage("是否需要发送语音验证码？");
			dialog.setTitle("提示").setPositiveButton("确认",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SmartfarmNetHelper.getVoiceVerCode(userName.getText().toString(),
							new BaseAsyncHttpResHandler() {
								@Override
								public void onSuccess(ApiResponse res) {
									timeCount = new MyCountTimer(60000, 1000, btnVoiceCode,"短信验证码收不到嘛？可以试试语音验证码");
									timeCount.start();
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
				}
			});
			dialog.setNegativeButton("取消", null);
			dialog.show();
			break;
		case R.id.login_name_clear:

			userName.setText("");
			userName.requestFocus();
			break;
		case R.id.login_new_pwd_clear:

			userPwd.setText("");
			userPwdAgain.setText("");
			userPwd.requestFocus();
			break;
		case R.id.login_pwd_clear_again:

			userPwdAgain.setText("");
			userPwdAgain.requestFocus();
			break;
		default:
			break;
		}
	}

	private boolean prepareForLogin() {

		if (!CommonTool.isNetworkConnected()) {
			ToastTool.showToast("网络异常，请检查网络！");
			return false;
		}

		String uName = userName.getText().toString();
		if (ShowUtil.isEmpty(uName)) {
			ToastTool.showToast("请输入手机号码！");
			if (uName.length() != 11) {
				ToastTool.showToast("输入的手机号码错误，请重新输入");
			}
			userName.requestFocus();
			return false;
		}

		String pwdOld = verCode.getText().toString();
		if (ShowUtil.isEmpty(pwdOld)) {
			ToastTool.showToast("请输入短信验证码！");
			verCode.requestFocus();
			return false;
		}

		String pwd = userPwd.getText().toString();
		if (ShowUtil.isEmpty(pwd)) {
			ToastTool.showToast("请输入密码！");
			userPwd.requestFocus();
			return false;
		}

		String pwdAgain = userPwdAgain.getText().toString();
		if (ShowUtil.isEmpty(pwd)) {
			ToastTool.showToast("请输入确认密码！");
			userPwd.requestFocus();
			return false;

		} else if (!pwdAgain.equals(pwd)) {
			ToastTool.showToast("两次密码输入不一致！");
			userPwdAgain.requestFocus();
			return false;
		}

		return true;
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
						verCode.setText(getNumbers(msg.getDisplayMessageBody()));
					}
				}
			}
		}
	}
	
	// 截取数字
	public String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	} 
}
