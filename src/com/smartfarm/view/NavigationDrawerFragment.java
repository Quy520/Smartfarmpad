package com.smartfarm.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.MainViewTab;
import com.smartfarm.protocol.NetCheckDialog;
import com.smartfarm.tools.BackPage;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.UIHelper;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

public class NavigationDrawerFragment extends BaseFragment implements
		OnClickListener,EventHandler {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	private DrawerLayout mDrawerLayout;
	private View mDrawerListView;
	private View mFragmentContainerView;

	private OnMenuItemClickListener mListener;
	private int mCurrentSelectedPosition = 0;
	
	private View waterEnableDividing;
	private View lightEnableDividing;
	private TextView waterItem;
	private TextView lightItem;
	private TextView userInfo;
	
	private TextView settingItem;
	private View settingEnableDividing;
	private TextView settingItemMulti;
	private View settingEnableMultiDividing;
	
	private TextView settingTest;
	
	private boolean showWaterItem = false;
	private boolean showLightItem = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().add(this);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
		}

		selectItem(mCurrentSelectedPosition);
	}
	
	private void checkSuperMode(){
		if(Constants.IS_DEBUG){
			settingItem.setVisibility(View.VISIBLE);
			settingEnableDividing.setVisibility(View.VISIBLE);
//			settingItemMulti.setVisibility(View.VISIBLE);
			settingEnableMultiDividing.setVisibility(View.VISIBLE);
		}
		if(Constants.IS_FACTORY){
			settingTest.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public void onStart() {
		super.onStart();
		checkSuperMode();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(mDrawerListView == null) {
			mDrawerListView = inflater
					.inflate(R.layout.fragment_navigation_drawer, container, false);
			mDrawerListView.setOnClickListener(this);

			lightEnableDividing = mDrawerListView.findViewById(R.id.menu_light_dividing);
			waterEnableDividing = mDrawerListView.findViewById(R.id.menu_water_dividing);
			settingEnableDividing=mDrawerListView.findViewById(R.id.menu_setting_dividing);
			lightItem =  (TextView) mDrawerListView.findViewById(R.id.menu_item_light);
			waterItem =  (TextView) mDrawerListView.findViewById(R.id.menu_item_water);
			userInfo =  (TextView) mDrawerListView.findViewById(R.id.menu_item_login);
			settingItem=(TextView) mDrawerListView.findViewById(R.id.menu_item_setting_num);
			settingItemMulti=(TextView) mDrawerListView.findViewById(R.id.menu_item_setting_multi_num);
			settingTest = (TextView) mDrawerListView.findViewById(R.id.menu_item_test);
			settingEnableMultiDividing=mDrawerListView.findViewById(R.id.menu_setting_multi_dividing);
			
			
			mDrawerListView.findViewById(R.id.menu_item_index).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_light).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_temp).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_water).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_net).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_protocol).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_version).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_exit).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_login).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_setting_num).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_setting_multi_num).setOnClickListener(this);
			mDrawerListView.findViewById(R.id.menu_item_test).setOnClickListener(this);
			
			initView(mDrawerListView);
			initData();
			checkSuperMode();
		}
		
		return mDrawerListView;
	}

	@Override
	public void onClick(View v) {

		if(mListener == null)
			return;
		
		switch (v.getId()) {
		case R.id.menu_item_index:

			mListener.onClick(MainViewTab.VIEW_FLAG_INDEX);
			break;
		case R.id.menu_item_exit:

			EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_EXIT));
			AppContext.context().finishProgram();
			break;
		case R.id.menu_item_temp:

			mListener.onClick(MainViewTab.VIEW_FLAG_TEMP);
			break;
		case R.id.menu_item_light:

			mListener.onClick(MainViewTab.VIEW_FLAG_LIGHT);
			break;
		case R.id.menu_item_water:

			mListener.onClick(MainViewTab.VIEW_FLAG_WATER);
			break;
		case R.id.menu_item_version:
		
			UIHelper.showSimpleBack(getActivity(), BackPage.ABOUT);
			break;
		case R.id.menu_item_net:
		
			NetCheckDialog.Show(getActivity());
			break;
		case R.id.menu_item_protocol:
			
			MessageBox.LongTextShow(getActivity(), CommonTool.getUserProtocol(getActivity()), 
					getActivity().getString(R.string.user_protocol), MessageBoxButtons.OK);
			break;
		case R.id.menu_item_login:
			
			if(AppContext.context().getUser() == null)
				UIHelper.showSimpleBack(getActivity(), BackPage.LOGIN_PHONE);
			else
				UIHelper.showSimpleBack(getActivity(), BackPage.USER_INFO);
			break;
			
		case R.id.menu_item_setting_num:
			UIHelper.showSimpleBack(getActivity(), BackPage.SETTING_NUM);
			break;
		case R.id.menu_item_setting_multi_num:
			UIHelper.showSimpleBack(getActivity(), BackPage.SETTING_NUM_MULTI);
			break;
			
		case R.id.menu_item_test:
			UIHelper.showSimpleBack(getActivity(), BackPage.SETTING_TEST);
			break;
			
		default:
			break;
		}
		mDrawerLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				mDrawerLayout.closeDrawers();
			}
		}, 400);
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	@SuppressLint("NewApi")
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {}
			
			@Override
			public void onDrawerOpened(View arg0) {
				
				ConfigModel cm = SimpleConfigManager.getInstance().getConfig();
				
				if(showLightItem ^ cm.isLightEnable()) {
					showLightItem = cm.isLightEnable();
					lightItem.setVisibility(showLightItem ? View.VISIBLE : View.GONE);
					lightEnableDividing.setVisibility(showLightItem ? View.VISIBLE : View.GONE);
				}
				
				if(showWaterItem ^ cm.isWaterEnable()) {
					showWaterItem = cm.isWaterEnable();
					waterItem.setVisibility(showWaterItem ? View.VISIBLE : View.GONE);
					waterEnableDividing.setVisibility(showWaterItem ? View.VISIBLE : View.GONE);
				}
				
				if(AppContext.context().getUser() == null)
					userInfo.setText("用户登录");
				else
					userInfo.setText("个人信息");
			}
			
			@Override
			public void onDrawerClosed(View arg0) {}
		});
	}

	public void openDrawerMenu() {
		mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().remove(this);
	}

	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		mListener = listener;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	public static interface NavigationDrawerCallbacks {
		void onNavigationDrawerItemSelected(int position);
	}
	
	public static interface OnMenuItemClickListener {
		void onClick(int index);
	}

	@Override
	public void onEvent(LocalEvent event) {
		if(event.getEventType()==LocalEvent.EVENT_TYPE_IS_DEBUG){
			checkSuperMode();
		}
	}
}
