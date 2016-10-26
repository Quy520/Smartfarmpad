package com.smartfarm.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.smartfarm.view.MainActivity;
import com.smartfarm.view.SimpleBackActivity;

public class UIHelper {

    public static void showMainView(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    
    public static void showSimpleBack(Activity context, BackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent); 
    }
    
    public static void showHome(Context context) {
    	
    	Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
    }
}
