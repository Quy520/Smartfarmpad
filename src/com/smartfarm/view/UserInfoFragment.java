package com.smartfarm.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smartfarm.db.access.UserDao;
import com.smartfarm.db.bean.User;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.UIHelper;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

public class UserInfoFragment extends BaseFragment {

	private TextView tvSex;
	private TextView tvName;
	private TextView tvAddr;
	private TextView tvPhone;
	
	private User user;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_user_info;
	}

	@Override
	protected void initView(View view) {
		
		view.findViewById(R.id.user_info_addr).setOnClickListener(this);
		view.findViewById(R.id.user_info_face).setOnClickListener(this);
		view.findViewById(R.id.user_info_name).setOnClickListener(this);
		view.findViewById(R.id.user_info_phone).setOnClickListener(this);
		view.findViewById(R.id.user_info_exit).setOnClickListener(this);
		view.findViewById(R.id.user_info_pwd).setOnClickListener(this);
		view.findViewById(R.id.user_info_sex).setOnClickListener(this);
		view.findViewById(R.id.diaoyong).setOnClickListener(this);

		tvSex = (TextView) view.findViewById(R.id.user_info_show_sex);
		tvName = (TextView) view.findViewById(R.id.user_info_show_name);
		tvAddr = (TextView) view.findViewById(R.id.user_info_show_addr);
		tvPhone = (TextView) view.findViewById(R.id.user_info_show_phone);
	}

	@Override
	protected void initData() {

		user = AppContext.context().getUser();
		
		if(user == null)
			return;
		
		tvName.setText(user.getName());
		tvAddr.setText(user.getAddr());
		tvPhone.setText(user.getPhone());
		tvSex.setText(user.getSex() ? "男" : "女");
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		
		case R.id.user_info_addr:
			
			View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog, null);
			
			final EditText addrEdit = (EditText) dialogView.findViewById(R.id.dialog_edit);
			new AlertDialog.Builder(getActivity())
				.setView(dialogView)
				.setTitle("请输入新的地址")
				.setNegativeButton("取消", null)
				.setPositiveButton("修改", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						String newAddr = addrEdit.getText().toString();
						if(ShowUtil.isEmpty(newAddr))
							return;
						
						user.setAddr(newAddr);
						UserDao.update(user);
						initData();
						ToastTool.showToast("修改成功!");
					}
				}).show();
			break;
		case R.id.user_info_face:
			
			break;
		case R.id.user_info_name:
			
			View nameView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog, null);
			
			final EditText nameEdit = (EditText) nameView.findViewById(R.id.dialog_edit);
			new AlertDialog.Builder(getActivity())
				.setView(nameView)
				.setTitle("请输入新的昵称")
				.setNegativeButton("取消", null)
				.setPositiveButton("修改", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						String newName = nameEdit.getText().toString();
						if(ShowUtil.isEmpty(newName))
							return;
						
						user.setName(newName);
						UserDao.update(user);
						initData();
						ToastTool.showToast("修改成功!");
					}
				}).show();
			break;
		case R.id.user_info_pwd:
			
			UIHelper.showSimpleBack(getActivity(), BackPage.MODIFY);
			getActivity().finish();
			break;
		case R.id.user_info_exit:
			
			if(MessageBox.Show(getActivity(), "确认要退出帐号嘛？", "确认", 
					MessageBoxButtons.OKCancel) == MessageBox.DialogResult.OK) {
				
				AppContext.context().getAccountManager().exit();
				UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
				getActivity().finish();
			}
			break;
		case R.id.user_info_sex:
			
			new AlertDialog.Builder(getActivity())
				.setTitle("请选择性别")
				.setNegativeButton("男", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						user.setSex(true);
						UserDao.update(user);
						initData();
						ToastTool.showToast("修改成功!");
					}
				})
				.setPositiveButton("女", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						user.setSex(false);
						UserDao.update(user);
						initData();
						ToastTool.showToast("修改成功!");
					}
				}).show();
			break;
		case R.id.diaoyong:
			
			break;
		}
	}
}
