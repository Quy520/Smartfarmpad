package com.smartfarm.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.SimpleTextWatcher;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.control.MyCountTimer;

public class RegisterFragment extends BaseFragment {

	private EditText userName;
	private EditText userPwd;
	private EditText userPwdAgain;
	private EditText checkNote;
	private View userNameClear;
	private View userPwdClearAgain;
	private View userPwdClear;
	private Button btnSendNote;
	private MyCountTimer timeCount;
	private Button btnVoiceCode;
	private String phone="";

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
			userPwdClear.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
		}
	};

	private final TextWatcher mAgainPassswordWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			userPwdClearAgain.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container,
				false);
		initView(view);
		initData();
		return view;
	}

	@Override
	protected void initView(View view) {

		userName = (EditText) view.findViewById(R.id.login_user_name);
		userPwd = (EditText) view.findViewById(R.id.login_user_pwd);
		userPwdAgain = (EditText) view.findViewById(R.id.login_user_pwd_again);
		userNameClear = view.findViewById(R.id.login_name_clear);
		userPwdClear = view.findViewById(R.id.login_pwd_clear);
		userPwdClearAgain = view.findViewById(R.id.login_pwd_clear_again);
		btnSendNote = (Button) view.findViewById(R.id.register_send_note);
		checkNote = (EditText) view.findViewById(R.id.register_note_check);
		btnVoiceCode =(Button) view.findViewById(R.id.voice_code);

		userName.addTextChangedListener(mUserNameWatcher);
		userPwd.addTextChangedListener(mPassswordWatcher);
		userPwdAgain.addTextChangedListener(mAgainPassswordWatcher);
		userNameClear.setOnClickListener(this);
		userPwdClear.setOnClickListener(this);
		userPwdClearAgain.setOnClickListener(this);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		btnSendNote.setOnClickListener(this);
		btnVoiceCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login_btn:
			if (!prepareForLogin())
				return;

			showLoadingDialog();
			String sUserName = userName.getText().toString();
			AppContext.context().setPhone(sUserName);
			String pwd = userPwd.getText().toString();
			String verCode = checkNote.getText().toString();
			String nickName = "";
			if (nickName.isEmpty()) {
				nickName = (int)(Math.random() * 100000) + "pad";
			}
			SmartfarmNetHelper.appResign(nickName, sUserName, Md5Utils.encode(Md5Utils.encode(pwd) + sUserName), verCode,
					new BaseAsyncHttpResHandler() {

						@Override
						public void onSuccess(ApiResponse res) {

							ToastTool.showToast("注册成功!请登录...");
							getActivity().finish();
						}

						@Override
						public void onFailure(int errorCode, String data) {

							ToastTool.showToast(data);;
						}
						@Override
						public void onFinish() {
							hideLoadingDialog();
						}
					});

			break;
		case R.id.login_name_clear:

			userName.setText("");
			userName.requestFocus();
			break;
		case R.id.login_pwd_clear:

			userPwd.setText("");
			userPwdAgain.setText("");
			userPwd.requestFocus();
			break;
		case R.id.login_pwd_clear_again:

			userPwdAgain.setText("");
			userPwdAgain.requestFocus();
			break;

		case R.id.register_send_note:
			
			String uName = userName.getText().toString();
			if (ShowUtil.isEmpty(uName)) {
				ToastTool.showToast("请输入手机号码！");
				userName.requestFocus();
				return;
			}
			showLoadingDialog();
			btnVoiceCode.setVisibility(View.VISIBLE);
			phone = userName.getText().toString();
			AppContext.context().setPhone(phone);
			SmartfarmNetHelper.getSmsVerCode(phone,new BaseAsyncHttpResHandler() {
				@Override
				public void onSuccess(ApiResponse res) {
					timeCount = new MyCountTimer(60000, 1000, btnSendNote,"获取短信验证码");
					timeCount.start();
					ToastTool.showToast("短信已发送，请注意查收");
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
			uName = userName.getText().toString();
			if (ShowUtil.isEmpty(uName)) {
				ToastTool.showToast("请输入手机号码！");
				userName.requestFocus();
				return;
			}
			showLoadingDialog();
			
			Builder dialog=new AlertDialog.Builder(getActivity());
			dialog.setMessage("是否需要发送语音验证码？");
			dialog.setTitle("提示").setPositiveButton("确认",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppContext.context().setPhone(phone);
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

		String pwd = userPwd.getText().toString();
		if (ShowUtil.isEmpty(pwd)) {
			ToastTool.showToast("请输入密码！");
			userPwd.requestFocus();
			return false;
		}

		String pwdAgain = userPwdAgain.getText().toString();
		if (ShowUtil.isEmpty(pwd)) {
			ToastTool.showToast("请确认输入密码！");
			userPwd.requestFocus();
			return false;
		} else if (!pwdAgain.equals(pwd)) {
			ToastTool.showToast("两次密码输入不一致！");
			userPwdAgain.requestFocus();
			return false;
		}
		String noteCheck = checkNote.getText().toString();
		if (ShowUtil.isEmpty(noteCheck)) {
			ToastTool.showToast("请输入验证码");
			checkNote.requestFocus();
			return false;
		}

		return true;
	}
}
