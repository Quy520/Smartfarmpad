package com.smartfarm.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.SimpleTextWatcher;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.UIHelper;

public class ModifyPasswordFragemt extends BaseFragment {
	
	private EditText userName;
	private EditText userPwd;
	private EditText userPwdAgain;
	private EditText userPwdOld;
	private View userNameClear;
	private View userPwdClearAgain;
	private View userPwdClearOld;
	private View userPwdClear;

	private boolean modifying = false;

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

	private final TextWatcher mOldPassswordWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			userPwdClearOld.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE
					: View.VISIBLE);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_modify, container, false);

		initView(view);
		initData();

		return view;
	}

	@Override
	public void initView(View view) {

		userName = (EditText) view.findViewById(R.id.login_user_name);
		userPwd = (EditText) view.findViewById(R.id.login_user_pwd);
		userPwdAgain = (EditText) view.findViewById(R.id.login_user_pwd_again);
		userPwdOld = (EditText) view.findViewById(R.id.login_user_old_pwd);
		userNameClear = view.findViewById(R.id.login_name_clear);
		userPwdClear = view.findViewById(R.id.login_new_pwd_clear);
		userPwdClearAgain = view.findViewById(R.id.login_pwd_clear_again);
		userPwdClearOld = view.findViewById(R.id.login_old_pwd_clear);

		userName.addTextChangedListener(mUserNameWatcher);
		userPwd.addTextChangedListener(mPassswordWatcher);
		userPwdOld.addTextChangedListener(mOldPassswordWatcher);
		userPwdAgain.addTextChangedListener(mAgainPassswordWatcher);
		userPwdClearOld.setOnClickListener(this);
		userNameClear.setOnClickListener(this);
		userPwdClear.setOnClickListener(this);
		userPwdClearAgain.setOnClickListener(this);
		
		view.findViewById(R.id.login_btn).setOnClickListener(this);
	}

	@Override
	public void initData() {

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

		switch (v.getId()) {
		case R.id.login_btn:

			if (!prepareForLogin())
				return;

			modifying = true;
			showLoadingDialog();

			SmartfarmNetHelper.appLoginPrepare(userName.getText().toString(),
					new BaseAsyncHttpResHandler() {

						@Override
						public void onFailure(int errorCode, String data) {

							modifying = false;
							hideLoadingDialog();
							ToastTool.showToast(data);
						}

						@Override
						public void onSuccess(ApiResponse res) {

							final String mPwd = Md5Utils.encode(Md5Utils.encode(
									userPwdOld.getText().toString()) + userName.getText().toString());
							final String mNewPwd = Md5Utils.encode(Md5Utils.encode(
									userPwd.getText().toString()) + userName.getText().toString());
							SmartfarmNetHelper.appModify(Md5Utils.encode(mPwd + res.getResponseData()),
									mNewPwd, new BaseAsyncHttpResHandler() {
	
									@Override
									public void onFailure(int errorCode, String data) {
										modifying = false;
										hideLoadingDialog();
										ToastTool.showToast(data);
									}
	
									@Override
									public void onSuccess(ApiResponse res) {
										modifying = false;
										hideLoadingDialog();
										ToastTool.showToast("修改成功,请重新登录!");
										AppContext.context().getAccountManager().exit();
										UIHelper.showSimpleBack(getActivity(),BackPage.LOGIN_PHONE);
										CommonTool.HideKb(getActivity(),getView());
										getActivity().finish();
									}
								});
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
		case R.id.login_new_pwd_clear:

			userPwd.setText("");
			userPwdAgain.setText("");
			userPwd.requestFocus();
			break;
		case R.id.login_pwd_clear_again:

			userPwdAgain.setText("");
			userPwdAgain.requestFocus();
			break;
		case R.id.login_old_pwd_clear:

			userPwdOld.setText("");
			userPwdOld.requestFocus();
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
			if(uName.length()!=11){
				ToastTool.showToast("输入的手机号码错误，请重新输入");
			}
			userName.requestFocus();
			return false;
		}

		String pwdOld = userPwdOld.getText().toString();
		if (ShowUtil.isEmpty(pwdOld)) {
			ToastTool.showToast("请输入旧密码！");
			userPwd.requestFocus();
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
}
