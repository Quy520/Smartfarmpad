package com.smartfarm.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartfarm.db.access.UserDao;
import com.smartfarm.db.bean.ApiUser;
import com.smartfarm.db.bean.User;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.UIHelper;

public class LoginFragment extends BaseFragment {

	private TextView userName;
	private TextView userPwd;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);

		initView(view);
		initData();
		return view;
	}

	@Override
	protected void initView(View view) {

		userPwd = (TextView) view.findViewById(R.id.login_user_pwd);
		userName = (TextView) view.findViewById(R.id.login_user_name);
		
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		view.findViewById(R.id.login_to_resign).setOnClickListener(this);
		view.findViewById(R.id.login_to_forget).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.login_btn:
        	
        	if(!prepareForLogin())
        		return;
			
        	showLoadingDialog();
        	
        	SmartfarmNetHelper.appLoginPrepare(userName.getText().toString(), new BaseAsyncHttpResHandler() {

				@Override
				public void onFailure(int errorCode, String data) {
					
					ToastTool.showToast("网络连接失败，错误代码：" + errorCode);
				}

				@Override
				public void onSuccess(ApiResponse res) {
					
					final String mPwd = Md5Utils.encode(Md5Utils.encode(
							userPwd.getText().toString()) + userName.getText().toString());
					
					SmartfarmNetHelper.appLogin(Md5Utils.encode(mPwd + res.getResponseData().toString())
							, new BaseAsyncHttpResHandler() {
								
						@Override
						public void onFailure(int errorCode, String data) {
							
							ToastTool.showToast("网络连接失败，错误代码：" + errorCode);
							userPwd.setText("");
							userPwd.requestFocus();
						}

						@Override
						public void onSuccess(ApiResponse res) {
							ToastTool.showToast("登录成功");
							User user = JSON.toJavaObject((JSON)res.getResponseData(), User.class);
							ApiUser localUser = user.getApiUser(user);
							user = UserDao.add(user);
							AppContext.context().getAccountManager()
								.save(user, localUser, user.getPhone(), Md5Utils.encode(mPwd), localUser.getToken(), localUser.getRefreshToken());
							HuanxinHelper.get().login(null);
							ProtocolFactory.getTestProtocolToSelf().send();
							CommonTool.HideKb(getActivity(), getView());
							getActivity().finish();
							UIHelper.showMainView(getActivity());
						}
						
						@Override
						public void onFinish() {
							hideLoadingDialog();
						}
					});
				}
				@Override
				public void onFinish() {
					hideLoadingDialog();
				}
        	});
			break;
		case R.id.login_to_resign:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.REGISETR);
			break;
		case R.id.login_to_forget:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.MSG_MODIFY);
			break;
		default:
			break;
		}
	}
    
    private boolean prepareForLogin() {
    	
    	if(!CommonTool.isNetworkConnected()) {
    		ToastTool.showToast("网络异常，请检查网络！");
            return false;
    	}
    	
    	String uName = userName.getText().toString();
        if (ShowUtil.isEmpty(uName)) {
            ToastTool.showToast("请输入手机号码！");
            userName.requestFocus();
            return false;
        }

        String pwd = userPwd.getText().toString();
        if (ShowUtil.isEmpty(pwd)) {
        	ToastTool.showToast("请输入密码！");
            userPwd.requestFocus();
            return false;
        }
        
        return true;
    }
}
