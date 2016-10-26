package com.smartfarm.view;

import java.io.DataOutputStream;
import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.RunningData;
import com.smartfarm.control.SerialHelper;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.net.ApiResponse;
import com.smartfarm.net.BaseAsyncHttpResHandler;
import com.smartfarm.net.SmartfarmNetHelper;
import com.smartfarm.net.UploadService;
import com.smartfarm.protocol.Protocol;
import com.smartfarm.protocol.ProtocolFactory;
import com.smartfarm.tools.Alarm;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ToastTool;

/**
 * 
 * 后台任务执行service
 * 
 * @author jeff
 *
 */
public class MsgService extends Service implements EventHandler {

	private static final long DAY_IN_MILLIS = 3600 * 24 * 1000;

	public static final String ACTION_CLEAR_RUNNING_DATA = "clear_running_data_action";
	public static final String ACTION_START = "start_action";

	private final MyBinder mBinder = new MyBinder();
	private Handler mHandler;

	private ControlLightRunable controlLightRunable;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d("mmsg", "MsgService onCreate !");

		mHandler = new Handler();
		EventBus.getDefault().add(this);

		if(!Constants.IS_FACTORY){
			postRunable();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {

				upgradeRootPermission(getPackageCodePath());
			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d("mmsg", "MsgService onStartCommand, startId = " + startId);

		if (intent != null && intent.getAction() != null) {

			if (intent.getAction().equals(MsgService.ACTION_CLEAR_RUNNING_DATA)) {

				Log.d(Constants.TAG, " clear running data !");
				SimpleConfigManager.getInstance().getLightModel().clear();
			}
		}
//		postRunable();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		Log.d("mmsg", "MsgService onDestroy!");
		EventBus.getDefault().remove(this);
		mHandler.removeCallbacksAndMessages(null);

		super.onDestroy();
	}

	private void postRunable() {

		mHandler.removeCallbacksAndMessages(null);

		mHandler.postDelayed(new ReadTempRunable(), Constants.TEMP_PERIOD / SimpleConfigManager.getInstance().getConfig().getWindowCount());
		mHandler.postDelayed(new PushTempRunable(), 15000);
//		ConfigModel cm = SimpleConfigManager.getInstance().getConfig();
		/*if (cm.isLightEnable()) {

			mHandler.post(new ReadLightRunable());

			if (SimpleConfigManager.getInstance().getBoolean(
					SimpleConfigManager.KEY_LIGHT_DIY_MODE))
				setLightDiyMode();
		}*/

		/*if (cm.isWaterEnable()) {

			mHandler.post(new ReadWaterRunable());
		}*/
	}

	private void setLightDiyMode() {

		String startTime = SimpleConfigManager.getInstance().getLightModel()
				.getStartTime();
		String hour = startTime.split(":")[0];
		String min = startTime.split(":")[1];
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
		c.set(Calendar.MINUTE, Integer.valueOf(min));
		c.set(Calendar.SECOND, 0);

		if (c.getTimeInMillis() < System.currentTimeMillis())
			c.add(Calendar.DATE, 1);

		if (controlLightRunable != null)
			mHandler.removeCallbacks(controlLightRunable);

		controlLightRunable = new ControlLightRunable();

		long delay = c.getTimeInMillis() - System.currentTimeMillis();
		mHandler.postDelayed(controlLightRunable, delay);

		SerialHelper.closeLightAll(SerialControlRunable.SOURCE_AUTO);
	}

	public class MyBinder extends Binder {
		public MsgService getService() {
			return MsgService.this;
		}
	}

	class ReadLightRunable implements Runnable {
		@Override
		public void run() {
			SerialHelper.readLight(SerialControlRunable.SOURCE_AUTO);

			mHandler.postDelayed(this, 60000);
		}
	};

	class ReadWaterRunable implements Runnable {

		@Override
		public void run() {

			SerialHelper.readWater(SerialControlRunable.SOURCE_AUTO);

			mHandler.postDelayed(this, 80000);
		}
	};

	class ControlLightRunable implements Runnable {

		boolean open = false;
		boolean stop = false;

		@Override
		public void run() {

			if (!open) {

				open = true;
				SerialHelper.openLightAll(SerialControlRunable.SOURCE_AUTO);

				mHandler.postDelayed(this, SimpleConfigManager.getInstance()
						.getLightModel().getDiyTime() * 60 * 1000);
			} else {

				SerialHelper.closeLightAll(SerialControlRunable.SOURCE_AUTO);

				setLightDiyMode();
			}
		}
	}

	class PushTempRunable implements Runnable {

		@Override
		public void run() {

			if (Alarm.isShouldAlarm()) {
				Protocol protocol = ProtocolFactory.getReadTempProtocol();
				protocol.setData(RunningData.getInstance().getTempInfo());
				protocol.send();
			}
			//如果夜间模式，不上传温度
			if(!Alarm.isShouldAlarm()){
				return;
			}

			SmartfarmNetHelper.synTemp(RunningData.getInstance().getTempInfo(),new BaseAsyncHttpResHandler() {
				
				@Override
				public void onSuccess(ApiResponse res) {
					if (res.getErrorCode() == 0)
						Log.d(Constants.TAG, " syn temp success !");
					else if(res.getErrorCode() > 0){
						if(res.getErrorCode() == ApiResponse.ERROR_MISS_TEMP){
							ToastTool.showToast("温度参数缺失");
						}else if(res.getErrorCode() == ApiResponse.ERROR_MISS_DEVICE_ID){
							ToastTool.showToast("设备id缺失");
						}else if(res.getErrorCode() == ApiResponse.ERROR_PARAM_TEMP){
							ToastTool.showToast("temp格式不合法");
						}else if(res.getErrorCode() == ApiResponse.ERROR_CONTROL_DEVICE_NOT_FIND){
							ToastTool.showToast("设备没有找到");
						}
					}
				}
				
				@Override
				public void onFailure(int errorCode, String data) {
					
				}
			});

			if (System.currentTimeMillis()
					- AppContext.context().getLastLoginTime() > DAY_IN_MILLIS) {

				Intent upload = new Intent(AppContext.context(),
						UploadService.class);
				startService(upload);
			}

			mHandler.postDelayed(this, SimpleConfigManager.getInstance()
					.getConfig().getPushTime()
					* (Constants.IS_DEBUG ? 10 * 1000 : 60 * 1000));
		}
	};

	class ReadTempRunable implements Runnable {
		int index = 0;

		@Override
		public void run() {

			int count = SimpleConfigManager.getInstance().getConfig()
					.getWindowCount();

			if (index >= count) {
				index = 0;
				Log.d(Constants.TAG, "to check temp !");
				Alarm.getInstance().Check();
			}

//			HuanxinHelper.get().checkLink();
			SerialHelper.ReadTemp(SerialControlRunable.SOURCE_AUTO, index, 1,
					RunningData.getInstance().getTempRecordRecently(index));
			index++;
			mHandler.postDelayed(this, Constants.TEMP_PERIOD / count);
		}
	}

	/**
	 * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	 * 
	 * @return 应用程序是/否获取Root权限
	 */
	public static boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // 切换到root帐号
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	@Override
	public void onEvent(LocalEvent event) {
		if(Constants.IS_FACTORY) 
			return;
		if (event.getEventType() == LocalEvent.EVENT_TYPE_LIGHT_DIY_MODE) {

			if (SimpleConfigManager.getInstance().getBoolean(
					SimpleConfigManager.KEY_LIGHT_DIY_MODE))
				setLightDiyMode();
			else {
				if (controlLightRunable != null)
					mHandler.removeCallbacks(controlLightRunable);
			}
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_PAGER_CHANGE) {

			postRunable();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_EXIT) {
			mHandler.removeCallbacksAndMessages(null);
			SerialHelper.clearRunnable();
			stopSelf();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_RESTART_SERVICE) {
			stopSelf();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_ON_SUPERMODE) {
			mHandler.removeCallbacksAndMessages(null);
			SerialHelper.clearRunnable();
			SerialHelper.readDevice();
		} else if (event.getEventType() == LocalEvent.EVENT_TYPE_OFF_SUPERMODE) {
			postRunable();
		}
	}
}
