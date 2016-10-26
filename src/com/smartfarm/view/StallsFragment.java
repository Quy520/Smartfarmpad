package com.smartfarm.view;

import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * 档位管理界面
 * 
 * @author jeff
 *
 */
public class StallsFragment extends BaseFragment {
	
	private TextView openLen;
	private TextView openFirst;
	private TextView openSecond;
	private TextView openThird;
	private TextView openFourth;
	private TextView openFifth;

	@Override
	protected int getLayoutId() {
		
		return R.layout.tempinfo_view;
	}

	@Override
	protected void initView(View view) {
		
		openLen = (TextView) view.findViewById(R.id.open_len_edit);
		openFirst = (TextView) view.findViewById(R.id.open_first_edit);
		openSecond = (TextView) view.findViewById(R.id.open_second_edit);
		openThird = (TextView) view.findViewById(R.id.open_third_edit);
		openFourth = (TextView) view.findViewById(R.id.open_fourth_edit);
		openFifth = (TextView) view.findViewById(R.id.open_fifth_edit);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.submit_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		submit();
		
		return true;
	}

	@Override
	protected void initData() {

		ConfigModel config = SimpleConfigManager.getInstance().getConfig();
		
		openLen.setText("");
		openFirst.setText("");
		openSecond.setText("");
		openThird.setText("");
		openFourth.setText("");
		openFifth.setText("");
		
		openLen.setHint(String.valueOf(config.getOpenLen()));
		openFirst.setHint(String.valueOf(config.getOpenLenFirst()));
		openSecond.setHint(String.valueOf(config.getOpenLenSecond()));
		openThird.setHint(String.valueOf(config.getOpenLenThird()));
		openFourth.setHint(String.valueOf(config.getOpenLenFourth()));
		openFifth.setHint(String.valueOf(config.getOpenLenFifth()));
	}
	
	/**
	 * 提交数据
	 */
	private void submit() {
		
		SimpleConfigManager scManager = SimpleConfigManager.getInstance();
		
		if(!ShowUtil.isEmpty(openLen.getText().toString())) {
			
			int openLenValue = Integer.valueOf(openLen.getText().toString());
			
			if(openLenValue < 100) {
				
				ToastTool.showToast("链条总长度输入有误，请重新设置!");
				return;
			}
			
			scManager.putInt(SimpleConfigManager.KEY_OPEN_LEN, openLenValue);
		}
		
		if(!ShowUtil.isEmpty(openFirst.getText().toString())) {
			
			int openLenValue = Integer.valueOf(openFirst.getText().toString());
			
			if(openLenValue > 100 || openLenValue <= 0) {
				
				ToastTool.showToast("第一档长度输入有误，请重新设置!");
				return;
			}
			
			scManager.putInt(SimpleConfigManager.KEY_OPEN_FIRST, openLenValue);
		}
		
		if(!ShowUtil.isEmpty(openSecond.getText().toString())) {
			
			int openLenValue = Integer.valueOf(openSecond.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("第二档长度输入有误，请重新设置!");
				return;
			}
			
			scManager.putInt(SimpleConfigManager.KEY_OPEN_SECOND, openLenValue);
		}
		
		if(!ShowUtil.isEmpty(openThird.getText().toString())) {
			
			int openLenValue = Integer.valueOf(openThird.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("第三档长度输入有误，请重新设置!");
				return;
			}
			
			scManager.putInt(SimpleConfigManager.KEY_OPEN_THIRD, openLenValue);
		}
		
		if(!ShowUtil.isEmpty(openFourth.getText().toString())) {
			
			int openLenValue = Integer.valueOf(openFourth.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("第四档长度输入有误，请重新设置!");
				return;
			}
			
			scManager.putInt(SimpleConfigManager.KEY_OPEN_FOURTH, openLenValue);
		}
		
		if(!ShowUtil.isEmpty(openFifth.getText().toString())) {
			
			int openLenValue = Integer.valueOf(openFifth.getText().toString());
			
			if(openLenValue > 50 || openLenValue <= 0) {
				
				ToastTool.showToast("剩余档位长度输入有误，请重新设置!");
				return;
			}
			
			scManager.putInt(SimpleConfigManager.KEY_OPEN_FIFTH, openLenValue);
		}
		
		scManager.updateApplicationCfg();
		ToastTool.showToast("修改成功!");
		
		CommonTool.HideKb(getActivity(), getView());
		initData();
	}
}
