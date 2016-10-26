package com.smartfarm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.smartfarm.tools.CommonTool;

public class TitlesView extends LinearLayout implements OnClickListener {

    private String title;
    private TextView otherIcon;
    private TextView titleTextView;
    private OnClickListener otherIconListener;
    private boolean showBackBtn = false;
    private OnClickListener backBtnListener;
    private TextView leftBtnRes;
    private RelativeLayout leftBtnRl;
	
    public TitlesView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
	}
    
    public TitlesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initTitle(context);
	}

    @SuppressLint("InflateParams")
	void initTitle(Context context) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		this.setLayoutParams(lp);
		this.addView(LayoutInflater.from(context).inflate(R.layout.titles, null));
		otherIcon = (TextView) this.findViewById(R.id.titles_other_icon);
		leftBtnRes = (TextView) this.findViewById(R.id.titles_sliding_menu_res);
		titleTextView = (TextView) this.findViewById(R.id.titles_title);
		leftBtnRl = (RelativeLayout) this.findViewById(R.id.titles_sliding_menu);
		leftBtnRl.setOnClickListener(this);
		otherIcon.setOnClickListener(this);
		
		hideBackBtn();
    }
    
    public void showBackBtn(OnClickListener listener) {
    	Drawable mBackDrawable = getResources().getDrawable(R.drawable.back_btn_selector);
    	mBackDrawable.setBounds(0, 0, mBackDrawable.getIntrinsicWidth() + 5, mBackDrawable.getIntrinsicHeight() + 5);
    	leftBtnRes.setCompoundDrawables(mBackDrawable, null, null, null);
    	leftBtnRes.setText("返回");
    	showBackBtn = true;
    	this.backBtnListener = listener;	
    }
    
    public void hideBackBtn() {
//    	Drawable mDrawable = getResources().getDrawable(R.drawable.icon_list);
//    	mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth() + 3, mDrawable.getIntrinsicHeight() + 3);
//    	leftBtnRes.setCompoundDrawables(mDrawable, null, null, null);
//    	leftBtnRes.setText("");
    	showBackBtn = false;
    	this.backBtnListener = null;
    }
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		titleTextView.setText(title);
	}
	
	public void setOtherBtnOnClickListener(int imgRes, OnClickListener onClickListener) {
    	Drawable mDrawable = getResources().getDrawable(imgRes);
    	mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth() + 5, mDrawable.getIntrinsicHeight() + 5);
		otherIcon.setCompoundDrawables(null, null, mDrawable, null);
		otherIcon.setText("");
		otherIconListener = onClickListener;
		otherIcon.setVisibility(View.VISIBLE);
	}
	
	public void setOtherBtnOnClickListener(int imgRes, String btnText, OnClickListener onClickListener) {
    	Drawable mDrawable = getResources().getDrawable(imgRes);
    	mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
		otherIcon.setCompoundDrawables(null, null, mDrawable, null);
		otherIcon.setText(btnText);
		otherIconListener = onClickListener;
		otherIcon.setVisibility(View.VISIBLE);
	}
	
	public void hideOtherBtn() {
		otherIcon.setVisibility(View.INVISIBLE);
	}
	
	public int getOtherBtnId() {
		return otherIcon.getId();
	}
	
	public int getLeftBtnId() {
		return leftBtnRes.getId();
	}
	
	@Override
	public void onClick(View v) {
		
		CommonTool.HideKb(getContext(), v);
		
		switch(v.getId()) {
		case R.id.titles_sliding_menu:
			if(showBackBtn) {
				if(backBtnListener != null){
					backBtnListener.onClick(leftBtnRes);	
				}
			}
			break;
		case R.id.titles_other_icon:
			if(otherIconListener != null)
				otherIconListener.onClick(otherIcon);
			break;
		}
	}
}
