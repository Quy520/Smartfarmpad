package com.smartfarm.view;

import android.view.View;
import android.widget.TextView;

public class IndexFragment extends BaseFragment {

	@Override
	protected int getLayoutId() {
		return R.layout.mian;
	}

	@Override
	protected void initView(View view) {

		TextView text = (TextView) view.findViewById(R.id.test);
		text.setText(this.getClass().getName());
	}
}
