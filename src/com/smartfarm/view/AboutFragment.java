package com.smartfarm.view;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.Md5Utils;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

public class AboutFragment extends BaseFragment {
	private TextView mVersionName;
	private int count = 0;
	private TextView mBlueToothId;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, container, false);

		initView(view);
		initData();
		return view;
	}

	@Override
	public void initView(View view) {
		mVersionName = (TextView) view.findViewById(R.id.about_version_name);
		mBlueToothId = (TextView) view.findViewById(R.id.blue_tooth_id);
		view.findViewById(R.id.debug_btn).setOnClickListener(this);
		view.findViewById(R.id.about_check_update).setOnClickListener(this);
		view.findViewById(R.id.about_knowmore).setOnClickListener(this);
		view.findViewById(R.id.about_oscsite).setOnClickListener(this);
		view.findViewById(R.id.about_new_version).setOnClickListener(this);
		view.findViewById(R.id.about_user_protocol).setOnClickListener(this);
	}

	public String getVersion() {
		try {
			PackageManager manager = getActivity().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getActivity()
					.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public void initData() {
		mVersionName.setText(String.format(getString(R.string.curr_version),
				getVersion()));
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		mBlueToothId.setText(Md5Utils.encode(bluetoothAdapter.getAddress()
				.toString().replaceAll(":", "")));
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
		case R.id.about_user_protocol:

			MessageBox.LongTextShow(getActivity(), CommonTool
					.getUserProtocol(getActivity()),
					getActivity().getString(R.string.user_protocol),
					MessageBoxButtons.OK);
			break;
		case R.id.about_check_update:

			break;
		case R.id.about_knowmore:

			ShowUtil.openUrl(getActivity(), Constants.MORE_SITE);
			break;
		case R.id.about_oscsite:

			ShowUtil.openUrl(getActivity(), Constants.ZT_SITE);
			break;
		case R.id.about_new_version:

			MessageBox.Show(getActivity(), R.string.update_log,
					R.string.update_log_title, MessageBoxButtons.OK);
			break;
		case R.id.debug_btn:

			if (Constants.IS_DEBUG)
				return;

			switch (count) {
			case 11:
				ToastTool.showToast("成功进入开发者模式");
				Constants.IS_DEBUG = true;
				Log.d("zqq", "is_debug TEMP_PERIOD->" + Constants.TEMP_PERIOD);
				Constants.TEMP_PERIOD = Constants.IS_DEBUG ? 30 * 1000
						: 5 * 60 * 1000;
				EventBus.getDefault().postInOtherThread(
						LocalEvent.getEvent(LocalEvent.EVENT_TYPE_IS_DEBUG));
				break;
			default:
				count++;
				break;
			}
			break;
		default:
			break;
		}
	}
}