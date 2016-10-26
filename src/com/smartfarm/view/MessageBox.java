package com.smartfarm.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spanned;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smartfarm.view.MessageBox.DialogResult;

public class MessageBox {
	public enum DialogResult {
		/**
		 * Nothing is returned from the dialog box. This means that the modal
		 * dialog continues running. 没有什么是从对话框返回。这意味着该模态对话框 继续运行。
		 */
		None,
		/**
		 * The dialog box return value is OK (usually sent from a button labeled
		 * OK). 该对话框返回值是OK（通常是从一个按钮标记确定发送）。
		 */
		OK,
		/**
		 * The dialog box return value is Cancel (usually sent from a button
		 * labeled Cancel). 该对话框返回值是取消（通常标有一个按钮发送取消）。
		 */
		Cancel
	}

	/**
	 * Specifies constants defining which buttons to display on a MessageBox.
	 * 指定常量定义显示在一个MessageBox的按钮。
	 */
	public enum MessageBoxButtons {
		/**
		 * The message box contains an OK button. 该消息框包含一个确定按钮。
		 */
		OK,
		/**
		 * The message box contains OK and Cancel buttons. 该消息框包含OK和Cancel按钮。
		 */
		OKCancel
	}

	/**
	 * @param owner
	 *            上下文
	 * @param text
	 *            该文本在消息框中显示
	 * @param caption
	 *            该文本在消息框的标题栏中显示
	 * @param buttons
	 *            其中一个MessageBoxButtons值，指定在消息框中显示的按钮。
	 */
	public static synchronized DialogResult Show(Context owner, int text,
			int caption, MessageBoxButtons buttons) {
		return Show(owner, owner.getString(text), owner.getString(caption), buttons);
	}
	
	/**
	 * @param owner
	 *            上下文
	 * @param text
	 *            该文本在消息框中显示
	 * @param caption
	 *            该文本在消息框的标题栏中显示
	 * @param buttons
	 *            其中一个MessageBoxButtons值，指定在消息框中显示的按钮。
	 */
	public static synchronized DialogResult Show(Context owner, String text,
			String caption, MessageBoxButtons buttons) {
		
		return Show(owner, text, caption, buttons, null);
	}

	public static synchronized DialogResult Show(Context owner, String text,
			String caption, MessageBoxButtons buttons, View child) {
		final List<DialogResult> list = new ArrayList<DialogResult>(1);
		final AlertDialog builder = new AlertDialog.Builder(owner).create();

		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				list.clear();
				list.add((DialogResult) msg.obj);
				builder.dismiss();
				throw new RuntimeException();

			}
		};

		if(child != null) 
			builder.setView(child);
		else if (text != null)
			builder.setMessage(text);

		if (caption != null)
			builder.setTitle(caption);

		if (buttons != null) {
			switch (buttons) {
			case OK:
				builder.setButton(AlertDialog.BUTTON_POSITIVE,
						owner.getString(R.string.confirm), new onButtonClickListener(mHandler,
								DialogResult.OK));
				break;
			case OKCancel:
				builder.setButton(AlertDialog.BUTTON_POSITIVE,
						owner.getString(R.string.confirm), new onButtonClickListener(mHandler,
								DialogResult.OK));

				builder.setButton(
						AlertDialog.BUTTON_NEGATIVE,
						owner.getString(R.string.cancle),
						new onButtonClickListener(mHandler, DialogResult.Cancel));
				break;
			}
		} else {
			builder.setButton(AlertDialog.BUTTON_NEGATIVE, owner.getString(R.string.cancle),
					new onButtonClickListener(mHandler, DialogResult.Cancel));
		}

		builder.show();

		try {
			Looper.getMainLooper();
			Looper.loop();
		} catch (RuntimeException e2) {
		}

		if (list.size() > 0) {
			return list.get(0);
		}

		return DialogResult.None;
	}
	
	public static synchronized DialogResult LongTextShow(Context owner, Spanned spanned,
			String caption, MessageBoxButtons buttons) {
		
		ScrollView scrollView = new ScrollView(owner);
		scrollView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		TextView tvMessage = new TextView(owner, null,
				R.style.dialog_pinterest_text);
		tvMessage.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		tvMessage.setPadding(5, 5, 5, 5);
		tvMessage.setLineSpacing(0.0F, 1.3F);
		tvMessage.setText(spanned != null ? spanned : "");
		tvMessage.setTextColor(owner.getResources().getColor(
				R.color.black));

		ScrollView.LayoutParams lp = new ScrollView.LayoutParams(
				ScrollView.LayoutParams.MATCH_PARENT,
				ScrollView.LayoutParams.WRAP_CONTENT);
		scrollView.addView(tvMessage, lp);
		
		return Show(owner, "", caption, buttons, scrollView);
	}
	
}



class onButtonClickListener implements OnClickListener {
	private Handler _handler;
	private DialogResult _drs;

	public onButtonClickListener(Handler handler, DialogResult drs) {
		_handler = handler;
		_drs = drs;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Message msg = _handler.obtainMessage();
		msg.obj = _drs;
		_handler.sendMessage(msg);
	}
}
