package com.smartfarm.view.control;

import com.smartfarm.view.AppContext;

import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountTimer extends CountDownTimer {
	public static final int TIME_COUNT = 61000;// 时间防止从59开始显示（以倒计时120s为例子）
	private TextView btn;
	private int normalColor, timingColor;// 未计时的文字颜色，计时期间的文字颜色
	private String content;

	public MyCountTimer(long millisInFuture, long countDownInterval,
			TextView btn,String content) {
		super(millisInFuture, countDownInterval);
		this.btn = btn;
		this.content=content;
	}

	/**
	 * 
	 * 参数上面有注释
	 */
	public MyCountTimer(TextView btn, int endStrRid) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
	}

	public MyCountTimer(TextView btn) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
	}

	public MyCountTimer(TextView tv_varify, int normalColor, int timingColor) {
		this(tv_varify);
		this.normalColor = normalColor;
		this.timingColor = timingColor;
	}

	@Override
	public void onFinish() {
		if (normalColor > 0) {
			btn.setTextColor(normalColor);
		}
		btn.setText(content);
		btn.setEnabled(true);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (timingColor > 0) {
			btn.setTextColor(timingColor);
		}
		btn.setEnabled(false);
		String phone = AppContext.context().getPhone();
		String _phone = phone.substring(0, 3) + "****" + phone.substring(7, phone .length());
		btn.setText("验证码已发送给" + _phone + "，请在"+millisUntilFinished / 1000 + "秒后重试");
	}
}
