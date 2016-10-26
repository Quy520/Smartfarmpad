package com.smartfarm.view;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.loopj.android.http.AsyncHttpClient;
import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.bean.VenBean;
import com.smartfarm.db.access.DatabaseHelper;
import com.smartfarm.db.access.UserDao;
import com.smartfarm.db.bean.ApiUser;
import com.smartfarm.db.bean.User;
import com.smartfarm.net.ApiHttpClient;
import com.smartfarm.net.UploadService;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.protocol.Protocol;
import com.smartfarm.protocol.ResolveMsg;
import com.smartfarm.tools.AccountManager;
import com.smartfarm.tools.AssistThread;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.FileHelper;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.tencent.bugly.crashreport.CrashReport;

public class AppContext extends Application implements EventHandler {

	private static AppContext instance;
	protected boolean isNeedCaughtExeption = false;// 是否捕获未知异常
	protected boolean needRestart = true;
	private PendingIntent restartIntent;
	private String packgeName;
	private AlertDialog dialog;
	private String imei = "";
	private String currLocation = "";
	private String mac = "";
	private VenBean venBean;
	private boolean netIdOk = false;
	private long lastLoginTime = 0;
	private String phone;
	private long msgTime;
	private long voiceTime;

	private User user;
	private ApiUser apiUser;
	private AccountManager aManager;
	private FileHelper fileHelper;

	public AccountManager getAccountManager() {
		return aManager;
	}

	public User getUser() {
		return user;
	}
	
	public ApiUser getApiUser() {
		return apiUser;
	}

	public void setApiUser(ApiUser apiUser) {
		this.apiUser = apiUser;
	}

	public String getPadNum() {

		if (user == null)
			return "";
		else
			return user.getPhone();
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public static AppContext context() {
		return instance;
	}

	public boolean inVenMode() {
		return venBean != null;
	}

	public void setVenBean(VenBean venBean) {
		this.venBean = venBean;
	}

	public boolean netIdOk() {
		return netIdOk;
	}

	public VenBean getVenBean() {
		return venBean;
	}

	public String getLocation() {
		return currLocation;
	}

	public String getMac() {
		return mac;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public long getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(long msgTime) {
		this.msgTime = msgTime;
	}

	public long getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(long voiceTime) {
		this.voiceTime = voiceTime;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		packgeName = getPackageName();

		instance = this;
		fileHelper=new FileHelper(this);
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次， ---+++加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName.equalsIgnoreCase("com.smartfarm.view")) {
			Log.e(Constants.TAG, "enter the service process!");
			// "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}

		if (isNeedCaughtExeption) {

			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread thread, Throwable ex) {

					if (needRestart) {
						Log.d(Constants.TAG, "set alarm to restart app!");
						AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						mgr.set(AlarmManager.RTC,
								System.currentTimeMillis() + 3000,
								restartIntent);
					}

					finishProgram();
				}
			});
		}

		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式

		restartIntent = PendingIntent.getActivity(this, -1, resultIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		CrashReport.initCrashReport(this, "900012724", false);

		try {

			DatabaseHelper.init();
		} catch (SQLiteCantOpenDatabaseException e) {

			ToastTool.showToast("初始化数据库异常，正在尝试重启程序...");

			AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
					restartIntent);

			finishProgram();
		}

		EventBus.getDefault().add(instance);
		RunningData.getInstance();

		aManager = new AccountManager();
		if (aManager.isLogined()) {

			Log.i(Constants.TAG, " user is logined, getUserInfo!");
			User user = UserDao.findById(aManager.getUserId());
			Log.d("cxy", "aManager.getUserId()->"+aManager.getUserId());
			if (user == null) {

				Log.i(Constants.TAG, " find user error, exit account!");
				aManager.exit();
			} else {
				Log.i(Constants.TAG, " find user success!");
				setUser(user);
				CrashReport.setUserId(user.getPhone());
			}
		}


		HuanxinHelper.get();
		ApiHttpClient.setHttpClient(new AsyncHttpClient());

		imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();
		LocalDataCollection.getInstance().checkImeiFile(imei);

		AssistThread.init();

		final LocationClient mLocationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setIsNeedAddress(true);
		option.setOpenGps(false);
		option.setIsNeedLocationDescribe(true);
		option.SetIgnoreCacheException(false);
		option.setProdName("SmartfarmPad");
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级

		mLocationClient.setLocOption(option);

		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {

				currLocation = location.getAddrStr();
				Log.d(Constants.TAG, "cruurnt location -> " + currLocation);
				mLocationClient.stop();

				Intent upload = new Intent(AppContext.context(),
						UploadService.class);
				startService(upload);
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {

				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				mac = wifi.getConnectionInfo().getMacAddress();

				if (ShowUtil.isEmpty(mac)) {

					wifi.setWifiEnabled(true);
					CommonTool.delay(5000);
					mac = wifi.getConnectionInfo().getMacAddress();
					wifi.setWifiEnabled(false);
				}

				mLocationClient.start();
			}
		}).start();
		
	}
	
	public void startBlueTooth(){
		
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getImei() {
		return imei;
	}
	
	public FileHelper getFileHelper(){
		return fileHelper;
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		Iterator<?> i = am.getRunningAppProcesses().iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					pm.getApplicationLabel(pm.getApplicationInfo(
							info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	// 结束线程,一般与finishAllActivity()一起使用
	// 例如: finishAllActivity;finishProgram();
	public void finishProgram() {

		ActivityManager amanager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		amanager.killBackgroundProcesses(packgeName);
		System.exit(0);
	}

	@SuppressLint("InflateParams")
	public void showLoadingDialog() {
		if (dialog == null) {
			View dialogView = LayoutInflater.from(this).inflate(
					R.layout.loading_dialog, null);
			dialog = new AlertDialog.Builder(this).setTitle("正在加载...")
					.setCancelable(false).setView(dialogView).create();
		}
		dialog.show();
	}

	public void hideLoadingDialog() {
		if (dialog != null || dialog.isShowing())
			dialog.dismiss();
	}
	

	@Override
	public void onEvent(LocalEvent event) {

		if (event.getEventType() == LocalEvent.EVENT_TYPE_RECEIVE_MSG)
			ResolveMsg.resolve((Protocol) event.getEventData());
		else if (event.getEventType() == LocalEvent.EVENT_TYPE_NET_CHANGE) {

			int resCode = event.getEventValue();
			Log.d(Constants.TAG, " net change , res code -> " + resCode);

			netIdOk = resCode == 1;
		}
	}
}
