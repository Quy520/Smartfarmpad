package com.smartfarm.tools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;

public class RebootDialog {
	
	private static final String CONTENT_PATTERN = "温控机电量低，将在%ds后自动关闭！点击确定立即关闭，点击取消则取消自动关闭。";
	private AlertDialog dialog;
	private OnBtnClickListener mListener;
	private int lastCount = 10;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			
			if(lastCount-- > 0) {
				
				if(dialog != null)
					dialog.setMessage(String.format(CONTENT_PATTERN, lastCount));
				
				sendEmptyMessageDelayed(0, 1000);
				return;
			}
			
			if(dialog != null)
				dialog.dismiss();
			mListener.onClickPositiveButton();
			
			super.dispatchMessage(msg);
		}
	};
	
	public RebootDialog(Context context, OnBtnClickListener listener) {
		
		mListener = listener;
		
		dialog = new AlertDialog.Builder(context)
			.setTitle("提示")
			.setMessage(String.format(CONTENT_PATTERN, lastCount))
			.setCancelable(false)
			.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					mListener.onClickNegativeButton();
					mHandler.removeCallbacksAndMessages(null);
				}
			})
			.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					mListener.onClickPositiveButton();
				}
			})
			.show();
		
		mHandler.sendEmptyMessageDelayed(0, 1000);
	}
	
	public interface OnBtnClickListener {
		
		public void onClickNegativeButton();
		public void onClickPositiveButton();
	}
}
