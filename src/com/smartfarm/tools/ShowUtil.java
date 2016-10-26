package com.smartfarm.tools;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.smartfarm.view.MainActivity;
import com.smartfarm.view.R;

public class ShowUtil {

    
	public static void openUrl(Context context, String url) {
		if(url != null && !url.equals("")) {
			
			Uri uri = Uri.parse(url);  
	        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intent);
		}
	}
	
	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 * @param scrollToView
	 *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	public static void controlKeyboardLayout(final Activity activity, final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						// 获取root在窗体的可视区域
						root.getWindowVisibleDisplayFrame(rect);
						// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
						int rootInvisibleHeight = root.getRootView()
								.getHeight() - rect.bottom;
						// 若不可视区域高度大于100，则键盘显示
						if (rootInvisibleHeight > 100) {
							
							int[] location = new int[2];
							// 获取scrollToView在窗体的坐标
							scrollToView.getLocationInWindow(location);
							// 计算root滚动高度，使scrollToView在可见区域
							int intsrollHeight = (location[1]
									+ scrollToView.getHeight() + ShowUtil
										.dip2px(activity, 10))
									- rect.bottom;
							
							if(intsrollHeight > 0) {

								root.scrollTo(0, intsrollHeight);
							}
						} else {

							// 键盘隐藏
							root.scrollTo(0, 0);
						}
					}
				});
	}

	public static int dip2px(Context context, float dipValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * m + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / m + 0.5f);
	}
	
	
	/**
	 * 判断字符串是否为空字符串
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	public static boolean isNetworkEnabled(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public static String getImei(Context context, String imei) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		return imei;
	}

	public static String getAppKey(Context context) {
		Bundle metaData = null;
		String appKey = null;
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				appKey = metaData.getString("YUNBA_APPKEY");
				if ((null == appKey) || appKey.length() != 24) {
					appKey = "Error";
				}
			}
		} catch (NameNotFoundException e) {

		}
		return appKey;
	}
	
	public static void showNotice(Context context, String title, String content, String url, String ticker) {


		Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.attn)
				.setTicker(ticker)
				.setContentTitle(title)
				.setContentText(content)
				.setAutoCancel(true);
		
		builder.setDefaults(Notification.DEFAULT_SOUND);
		
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		if(url != null && !url.equals("")) {
			Uri uri = Uri.parse(url);  
	        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
	        
	        PendingIntent restartIntent = PendingIntent.getActivity(context, -1,
	        		intent, Intent.FLAG_ACTIVITY_NEW_TASK);
	        builder.setContentIntent(restartIntent);
		} else {
			Intent resultIntent = new Intent(context, MainActivity.class);
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
	
			PendingIntent restartIntent = PendingIntent.getActivity(context, -1,
					resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
	
			builder.setContentIntent(restartIntent);
		}
		
		nm.notify(23456, builder.build());
	}
}
