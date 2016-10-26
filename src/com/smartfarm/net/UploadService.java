package com.smartfarm.net;

import com.smartfarm.tools.Constants;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UploadService extends Service {
	
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    	Log.i(Constants.TAG, "start login device !");
    	SmartfarmNetHelper.appDeviceOnline(new BaseAsyncHttpResHandler() {
			
			@Override
			public void onSuccess(ApiResponse res) {
				
				if(res.getErrorCode() == 0)
					toLogin();
				else if(res.getErrorCode()>0){
					if(res.getErrorCode() == ApiResponse.ERROR_MISS_DEVICE_ID){
						ToastTool.showToast("设备id缺失");
					}else if(res.getErrorCode() == ApiResponse.ERROR_MISS_VERSION){
						ToastTool.showToast("版本号缺失");
					}else if(res.getErrorCode() == ApiResponse.ERROR_CONTROL_DEVICE_TYPE){
						ToastTool.showToast("设备类型错误");
					}else{
						ToastTool.showToast("上传设备信息失败");
					}
				}
				else
					onFailure(-1, "");
			}
			
			@Override
			public void onFailure(int errorCode, String data) {

		    	Log.i(Constants.TAG, "login device error!");
				UploadService.this.stopSelf();
			}
		});

        return super.onStartCommand(intent, flags, startId);
    }
     
    private void toLogin() {

    	AppContext.context().setLastLoginTime(System.currentTimeMillis());
    	Log.i(Constants.TAG, "start update device info !");
    	SmartfarmNetHelper.appDeviceLogin(new BaseAsyncHttpResHandler() {
			
			@Override
			public void onSuccess(ApiResponse res) {
				
				if(res.getErrorCode() == 0)
					synSetting();
				else if(res.getErrorCode()>0){
					if(res.getErrorCode()==ApiResponse.ERROR_MISS_DEVICE_ID){
						ToastTool.showToast("设备id缺失");
					}else if(res.getErrorCode()==ApiResponse.ERROR_CONTROL_DEVICE_NOT_FIND){
						ToastTool.showToast("设备没有找到");
					}
				}
				else
					onFailure(-1, "");
			}
			
			@Override
			public void onFailure(int errorCode, String data) {
				
		    	Log.i(Constants.TAG, "update device info error!");
				UploadService.this.stopSelf();
			}
		});
    }
    
    private void synSetting() {

    	Log.i(Constants.TAG, "start update device setting !");
    	SmartfarmNetHelper.synSetting(new BaseAsyncHttpResHandler() {
			
			@Override
			public void onSuccess(ApiResponse res) {
				if(res.getErrorCode() == ApiResponse.ERROR_MISS_DEVICE_ID){
					ToastTool.showToast("设备id缺失");
				}else if(res.getErrorCode() == ApiResponse.ERROR_MISS_SETTING){
					ToastTool.showToast("配置信息缺失");
				}else if(res.getErrorCode() == ApiResponse.ERROR_CONTROL_DEVICE_NOT_FIND){
					ToastTool.showToast("设备没有找到");
				}else if(res.getErrorCode() == ApiResponse.ERROR_PARAM_JSON){
					ToastTool.showToast("JSON格式不合法");
				}
				UploadService.this.stopSelf();
			}
			
			@Override
			public void onFailure(int errorCode, String data) {

		    	Log.i(Constants.TAG, "update device setting error!");
				UploadService.this.stopSelf();
			}
		});
    }
}
