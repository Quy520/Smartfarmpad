package com.smartfarm.net;

import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.smartfarm.bean.DeviceInfoBean;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.DeviceIdHelper;
import com.smartfarm.view.AppContext;

public class SmartfarmNetHelper {

	/**
	 * 设备登录
	 * 
	 * @param DeviceId
	 * @param Version
	 * @param Location
	 * @param device_type
	 */
	public static void appDeviceOnline(BaseAsyncHttpResHandler handler) {
		RequestParams params = new RequestParams();
		params.put("DeviceId", DeviceIdHelper.get());
		params.put("Location", AppContext.context().getLocation());
		params.put("Version",CommonTool.getVersion(AppContext.context()));
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/device/online";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 设备基本信息同步
	 * 
	 * @param DeviceId
	 * @param Imei
	 * @param Mac
	 * @param Name
	 * @param PhoneNumber
	 */
	public static void appDeviceLogin(BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		params.put("Imei", AppContext.context().getImei());
		params.put("PhoneNumber", AppContext.context().getPadNum());
		params.put("Mac", AppContext.context().getMac());
		params.put("Name", "pad");
		params.put("DeviceId", DeviceIdHelper.get());
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/device/base-info";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 设备参数同步
	 * 
	 * @param DeviceId
	 * @param Setting
	 * @param Replace
	 */
	public static void synSetting(BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		DeviceInfoBean bean = new DeviceInfoBean();
		params.put("Setting", JSON.toJSONString(bean));
//		Log.d("cxy", "json->"+JSON.toJSONString(bean));
		params.put("DeviceId",DeviceIdHelper.get());
		String url = "v1.0/device/setting";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, url, params, handler);
	}

	/**
	 * 温控机温度上传
	 * 
	 * @param DeviceId
	 * @param Temp
	 */
	public static void synTemp(String tempInfo,BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		params.put("DeviceId", DeviceIdHelper.get());
		params.put("Temp", tempInfo);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/device/temp";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}

	/**
	 * 获取密码验证的salt
	 * 
	 * @param phone
	 * @param handler
	 */
	public static void appLoginPrepare(String phone,
			BaseAsyncHttpResHandler handler) {
		RequestParams params = new RequestParams();
		params.put("PhoneNumber", phone);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/salt";
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 用户登录
	 * 
	 * @param pwd
	 * @param handler
	 */
	public static void appLogin(String pwd, BaseAsyncHttpResHandler handler) {
		RequestParams params = new RequestParams();
		params.put("Password", pwd);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/login";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}

	/**
	 * 用户注册
	 * 
	 * @param phone
	 * @param name
	 * @param pwd
	 * @param handler
	 */
	public static void appResign(String nickName, String userName, 
			String pwd,String code, BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		params.put("PhoneNumber", userName);
		params.put("Password", pwd);
		params.put("Name", nickName);
		params.put("VerCode", code);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/register";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}
	
	/**
	 * 2.用户注册登录
	 * 
	 * @param phone
	 * @param name
	 * @param pwd
	 * @param handler
	 */
	public static void appRegisterLogin(String phoneNumber, String verCode,
			BaseAsyncHttpResHandler handler) {

		RequestParams params = new RequestParams();
		params.put("PhoneNumber", phoneNumber);
		params.put("VerCode", verCode);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/register-login";
		ApiHttpClient.post(ApiHttpClient.PYTHON_SERVER, loginurl, params, handler);
	}

	/**
	 * 获取短信验证码，注意获取间隔不要太短，一天一个手机号码上限获取八次验证码
	 * 
	 * @param phone
	 * @param handler
	 */
	public static void getSmsVerCode(String phone,
			BaseAsyncHttpResHandler handler) {
		RequestParams params = new RequestParams();
		params.put("PhoneNumber", phone);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/sms";
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 获取语音验证码，次数与短信验证码共享
	 * 
	 * @param phone
	 * @param handler
	 */
	public static void getVoiceVerCode(String phone,
			BaseAsyncHttpResHandler handler) {
		Log.d("cxy","请求语音验证码");
		RequestParams params = new RequestParams();
		params.put("PhoneNumber", phone);
		params.setContentEncoding(HTTP.UTF_8);
		String loginurl = "v1.0/auth/voice";
		ApiHttpClient.get(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 用户修改密码
	 * 
	 * @param pwd
	 * @param newPwd
	 * @param handler
	 */
	public static void appModify(String pwd, String newPwd,BaseAsyncHttpResHandler handler) {
		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		params.put("NewPassword", newPwd);
		params.put("Password", pwd);
		String loginurl = "v1.0/auth/change-password";
		ApiHttpClient.put(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}

	/**
	 * 用户动态修改密码
	 * 
	 * @param pwd
	 * @param newPwd
	 * @param VerCode
	 * @param handler
	 */
	public static void appModify(String phone, String pwd, String verCode,
			BaseAsyncHttpResHandler handler) {
		RequestParams params = new RequestParams();
		params.setContentEncoding(HTTP.UTF_8);
		params.put("PhoneNumber", phone);
		params.put("Password", pwd);
		params.put("VerCode", verCode);
		String loginurl = "v1.0/auth/change-password/sms";
		ApiHttpClient.put(ApiHttpClient.PYTHON_SERVER, loginurl, params,
				handler);
	}
	
}
