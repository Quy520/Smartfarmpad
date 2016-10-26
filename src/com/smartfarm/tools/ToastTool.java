package com.smartfarm.tools;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.smartfarm.view.AppContext;

/**
 * toast工具
 * 
 * @author jeff
 *
 */
@SuppressLint("ShowToast")
public class ToastTool {

	public static void showToast(String text) {
		
		Toast.makeText(AppContext.context(), text, Toast.LENGTH_SHORT).show();
	}
	
	public static void showToast(int text) {
		
		Toast.makeText(AppContext.context(), text, Toast.LENGTH_SHORT).show();
	}
}
