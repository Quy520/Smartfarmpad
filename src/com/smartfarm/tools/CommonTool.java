package com.smartfarm.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.smartfarm.view.AppContext;
import com.smartfarm.view.SimpleConfigManager;

public class CommonTool {
	
	/**
	 * 获取用户协议
	 * 
	 * @return
	 */
	public static Spannable getUserProtocol(Context context) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					context.getAssets().open("user_protocol.txt"), "GBK"));
			
			String line = "";
			while((line = in.readLine()) != null) {

				result.append(line);
				result.append("\r\n");
			}
			
			SpannableStringBuilder builder = new SpannableStringBuilder();
			builder.append(result);
			
			return builder;
		} catch(Exception e) {
			return null;
		} finally {
			try {
				if(in != null)
					in.close();
			} catch(Exception e) {}
		}
	}
	
	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param v
	 */
	public static void HideKb(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	/**
	 * 判断一个数是否在边界左右50的范围内
	 * @param now 用于判断的数
	 * @param bound 边界
	 * @return -1表示不在范围的左边，0表示在范围内，1表示不在范围内的右边
	 */
	public static int compareBound(long now, long bound) {
		
		if(now < bound - 50)
			return -1;
		else if(now <= bound + 50)
			return 0;
		else
			return 1;
	}
	
	/**
	 * 重启
	 * 
	 */
	public static void ToReboot() {
		if(Constants.IS_DEBUG || Constants.SUPER_MODE) return;
		SimpleConfigManager.getInstance().putBoolean("shutdown", true);
		String cmd = "su -c reboot";
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关机
	 * 
	 */
	public static void ToClose() {
		if(Constants.IS_DEBUG || Constants.SUPER_MODE) return;
		try {
			Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});
      
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ping 202.108.22.5 判断是否能上网
	 * 
	 * @return
	 */
	public static boolean isConnected() {
		try {
			Process p = Runtime.getRuntime().exec(
					"/system/bin/ping -c " + 1 + " 202.108.22.5");
			int status = p.waitFor();// 只有0时表示正常返回。
			return status == 0;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 将异常用对话框显示
	 * 
	 * @param context
	 * @param e
	 */
	public static void ShowExceptionStack(Context context, Exception e) {
		StringBuilder result = new StringBuilder();
		for (StackTraceElement elem : e.getStackTrace()) {
			result.append(elem);
			result.append("\n");
		}
		new AlertDialog.Builder(context).setTitle("error").setMessage(result)
				.setNegativeButton("ok", null).show();
	}

	/**
	 * 判断手机号码合理性
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean checkMobile(String mobile) {
		String telRegex = "[1]\\d{10}";
		if (TextUtils.isEmpty(mobile))
			return false;
		else
			return mobile.matches(telRegex);
	};

	public static boolean checkTemper(String temper) {

		String telRegex = "\\d{2}[.]\\d{1}[C]";
		if (TextUtils.isEmpty(temper))
			return false;
		else
			return temper.matches(telRegex);
	};

	public static boolean isNetworkConnected() {

		// 判断网络是否连接
		ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext.context()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}

		return false;
	}

	/**
	 * 直接调用短信接口发短信，不含发送报告和接受报告
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static void SendSMS(String phoneNumber, String message) {

		if(ShowUtil.isEmpty(phoneNumber))
			return;
		if(SimpleConfigManager.getInstance().getConfig().isAlarmEnable()){
			return;
		}
		try {
			// 获取短信管理器
			android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
			// 拆分短信内容（手机短信长度限制）
			List<String> divideContents = smsManager.divideMessage(message);
			for (String text : divideContents) {
				smsManager.sendTextMessage(phoneNumber, null, text, null, null);
			}
		} catch(IllegalArgumentException e) {
			ToastTool.showToast("设置手机号码有误或未设置，无法发送报警信息。");
		}
	}
	
	public static void delay(int ms) {
		try {
			Thread.currentThread();
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String getCurrentDate(){  

        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd", Locale.getDefault());  
        Date date = new Date(System.currentTimeMillis());  
        
        return format.format(date);
    } 
	
	public static String getDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.getDefault());

		return format.format(date);
	}
	
	public static String getDateString2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

		return format.format(date);
	}
	
	/**
	 * 获取异常堆栈信息
	 * 
	 * @param ex
	 * @return
	 */
	public static String getErrorMsg(Throwable ex) {
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		return writer.toString();
	}
	
	public static int getValidCount(byte[] msg){
		
		int count = 0;
		
		for (int i = 0; i < msg.length - 2; i++)  {
			
			if(msg[i] ==0 && msg[i + 1] == 0 && msg[i + 2] == 0) 
				break;
			
			count++;
		}
		
		return count;
	}
	
	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String printByteArray(byte[] msg){
		
		StringBuilder res = new StringBuilder();
		for (byte b : msg) {
			
			if(b==0) continue;
			
			if(b < 0)
				res.append(Integer.toHexString((int)b + 128));
			else
				res.append(Integer.toHexString((int)b));
			res.append(", ");
		}
		return res.toString();
	}
	
	public static void writerDataLog(String dataLog){
		FileHelper fileHelper = AppContext.context().getFileHelper();
		fileHelper.writeSDFile(System.currentTimeMillis()+","+dataLog+"\r\n");
	}
	
	//流量统计
	public static long getTotalRxBytes(){  //获取总的接受字节数，包含Mobile和WiFi等  
        return TrafficStats.getTotalRxBytes()==TrafficStats.UNSUPPORTED?0:(TrafficStats.getTotalRxBytes()/1024);  
    }  
    public static long getTotalTxBytes(){  //总的发送字节数，包含Mobile和WiFi等  
        return TrafficStats.getTotalTxBytes()==TrafficStats.UNSUPPORTED?0:(TrafficStats.getTotalTxBytes()/1024);  
    }  
    public long getMobileRxBytes(){  //获取通过Mobile连接收到的字节总数，不包含WiFi  
        return TrafficStats.getMobileRxBytes()==TrafficStats.UNSUPPORTED?0:(TrafficStats.getMobileRxBytes()/1024);  
    }  
}
