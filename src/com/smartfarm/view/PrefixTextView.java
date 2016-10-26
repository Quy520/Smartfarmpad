package com.smartfarm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.TextView;

public class PrefixTextView extends TextView{
	String prefix = "";
	
	public PrefixTextView(Context context) {
		this(context, null, 0);
	}
	
	public PrefixTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PrefixTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	@CapturedViewProperty
	public CharSequence getText() {
		String text = super.getText().toString();
		
		if(text.contains(prefix))
			return text.split(prefix)[1];
		else
			return text;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(prefix + text, type);
	}
	
	
}
