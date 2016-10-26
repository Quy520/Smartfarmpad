package com.smartfarm.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class BaseFragment extends Fragment implements OnClickListener {

	protected View holdView;
	private Dialog loadingDialog;
	private boolean show = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(holdView()) {
        	
        	if(holdView == null) {
        		holdView = inflater.inflate(getLayoutId(), container, false);
        		
        		initView(holdView);
    			initData();
        	}
        	
        	ViewGroup parent = (ViewGroup) holdView.getParent();
    		if (parent != null) {
    			parent.removeView(holdView);
    		}

    		return holdView;	
        } else {
        	
        	holdView = inflater.inflate(getLayoutId(), container, false);
    		
        	initView(holdView);
			initData();
			
			return holdView;
        }
	} 

	protected int getLayoutId() {
		return 0;
	}

	protected boolean holdView() {
		return true;
	}

	public boolean onBackPressed() {
		return false;
	}

	protected void initView(View view) {
	}

	protected void initData() {

	}
	
	protected void openDrawerMenu(){
		
	}

	@Override
	public void onClick(View v) {

	}

	@SuppressLint("InflateParams")
	public void showLoadingDialog() {
		
		if(loadingDialog == null) {
			
			View loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_dialog, null);
			
			loadingDialog = new AlertDialog.Builder(getActivity())
				.setTitle("正在努力加载，请耐心等待...")
				.setView(loadingView)
				.setCancelable(false)
				.create();
		}
		
		if(show)
			return;
		
		show = true;
		loadingDialog.show();
	}
	
	public void hideLoadingDialog() {
		if(loadingDialog != null){
			loadingDialog.hide();
			loadingDialog = null;
			show = false;
		}
	}
}