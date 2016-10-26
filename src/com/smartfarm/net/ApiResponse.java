package com.smartfarm.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class ApiResponse {

	private Object responseData;
	private String message;
	private int errorCode;
	private long serverTime;
	private boolean valid;

	public ApiResponse() {}
	
	public static ApiResponse getApiResponse(String jsonString) {
		
		try {
			return JSON.parseObject(jsonString, ApiResponse.class);
		} catch(JSONException e) {
			return null;
		}
	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	@Override
	public String toString() {
		return "data:" + responseData + " message:" + message + " errocode:"
				+ errorCode;
	}
/*	
	//参数异常
	public static final int ERROR_PARAMETER = -100;

	//没有此帐号
	public static final int ERROR_ACCOUNT_NOT = ERROR_PARAMETER  - 1;

	//动作超时
	public static final int ERROR_RUNTIME = ERROR_ACCOUNT_NOT  - 1;

	//密码错误
	public static final int ERROR_PASSWORD = ERROR_RUNTIME  - 1;

	//帐号已存在
	public static final int ERROR_ACCOUNT_EXIST = ERROR_PASSWORD  - 1;

	//同步环信修改密码失败
	public static final int ERROR_HUANXIN_MODIFY = ERROR_ACCOUNT_EXIST  - 1;

	//同步环信注册帐号失败
	public static final int ERROR_HUANXIN_RESIGN = ERROR_HUANXIN_MODIFY  - 1;

	//服务器操作异常
	public static final int ERROR_SERVER_EXCEPTION = ERROR_HUANXIN_MODIFY  - 1;

	//令牌不匹配
	public static final int ERROR_TOKEN_ERROR = ERROR_SERVER_EXCEPTION  - 1;

	//逻辑错误
	public static final int ERROR_LOGICAL_ERROR = ERROR_TOKEN_ERROR - 1;*/
	// 参数不合法
	public static final int ERROR_PARAMETER = 61000;

	// 手机号码不合法
	public static final int ERROR_PARAM_PHONE = ERROR_PARAMETER + 1;

	// 验证码不合法
	public static final int ERROR_PARAM_VER = ERROR_PARAM_PHONE + 1;

	// 个人信息参数不合法
	public static final int ERROR_PARAM_USER_INFO = ERROR_PARAM_VER + 1;

	// 图片格式不合法
	public static final int ERROR_PARAM_IMG = ERROR_PARAM_USER_INFO + 1;

	// 页码参数不合法
	public static final int ERROR_PARAM_PAGE = ERROR_PARAM_IMG + 1;

	// 页面大小参数不合法
	public static final int ERROR_PARAM_SIZE = ERROR_PARAM_PAGE + 1;

	// 发布消息内容格式不合法
	public static final int ERROR_PARAM_PUB_DATA = ERROR_PARAM_SIZE + 1;

	// 评论类型不合法
	public static final int ERROR_PARAM_COMMENT_TYPE = ERROR_PARAM_PUB_DATA + 1;

	// 评论中提到的用户id不合法
	public static final int ERROR_PARAM_MENTION = ERROR_PARAM_COMMENT_TYPE + 1;

	// 操作的对象类型不合法
	public static final int ERROR_PARAM_FEEL_TYPE = ERROR_PARAM_MENTION + 1;

	// 消息类型不合法
	public static final int ERROR_PARAM_MSG_TYPE = ERROR_PARAM_FEEL_TYPE + 1;

	// JSON格式不合法
	public static final int ERROR_PARAM_JSON = ERROR_PARAM_MSG_TYPE + 1;

	// temp格式不合法
	public static final int ERROR_PARAM_TEMP = ERROR_PARAM_JSON + 1;

	// date格式不合法
	public static final int ERROR_PARAM_DATE = ERROR_PARAM_TEMP + 1;

	// temp_type不合法
	public static final int ERROR_PARAM_TEMP_TYPE = ERROR_PARAM_DATE + 1;

	// 参数缺失
	public static final int ERROR_PARAMETER_MISS = 62000;

	// 手机号码缺失
	public static final int ERROR_MISS_PHONE = ERROR_PARAMETER_MISS + 1;

	// 密码缺失
	public static final int ERROR_MISS_PWD = ERROR_MISS_PHONE + 1;

	// 校验码缺失
	public static final int ERROR_MISS_VER = ERROR_MISS_PWD + 1;

	// 登录认证令牌缺失
	public static final int ERROR_MISS_TOKEN = ERROR_MISS_VER + 1;

	// 用户名缺失
	public static final int ERROR_MISS_NAME = ERROR_MISS_TOKEN + 1;

	// 未调用获取salt值API
	public static final int ERROR_MISS_SALT = ERROR_MISS_NAME + 1;

	// 新密码缺失
	public static final int ERROR_MISS_PWD_NEW = ERROR_MISS_SALT + 1;

	// 个人信息缺失
	public static final int ERROR_MISS_USER_INFO = ERROR_MISS_PWD_NEW + 1;

	// 头像缺失
	public static final int ERROR_MISS_PORTRAIT = ERROR_MISS_USER_INFO + 1;

	// 发布消息内容缺失
	public static final int ERROR_MISS_PUB_DATA = ERROR_MISS_PORTRAIT + 1;

	// 发布消息内容中消息内容缺失
	public static final int ERROR_MISS_PUB_DATA_CONTENT = ERROR_MISS_PUB_DATA + 1;

	// 评论内容缺失
	public static final int ERROR_MISS_COMMENT_CONTENT = ERROR_MISS_PUB_DATA_CONTENT + 1;

	// 设备id缺失
	public static final int ERROR_MISS_DEVICE_ID = ERROR_MISS_COMMENT_CONTENT + 1;

	// 版本号缺失
	public static final int ERROR_MISS_VERSION = ERROR_MISS_DEVICE_ID + 1;

	// 配置信息缺失
	public static final int ERROR_MISS_SETTING = ERROR_MISS_VERSION + 1;

	// 备注内容缺失
	public static final int ERROR_MISS_REMARK_CONTENT = ERROR_MISS_SETTING + 1;

	// 建议内容缺失
	public static final int ERROR_MISS_SUGGEST_CONTENT = ERROR_MISS_REMARK_CONTENT + 1;
	
	// 查询新用户日期参数缺失
	public static final int ERROR_MISS_NEW_USER_DATE = ERROR_MISS_SUGGEST_CONTENT + 1;
	
	// 温度参数缺失
	public static final int ERROR_MISS_TEMP  = ERROR_MISS_NEW_USER_DATE + 1;
	
	// 日期参数缺失
	public static final int ERROR_MISS_QUERY_DATE  = ERROR_MISS_TEMP + 1;

	// 操作超时
	public static final int ERROR_RUNTIME = 63000;

	// TOKEN过期
	public static final int ERROR_RUNTIME_TOKEN = ERROR_RUNTIME + 1;

	// 登录过期
	public static final int ERROR_RUNTIME_LOGIN = ERROR_RUNTIME + 1;

	// 操作超时
	public static final int ERROR_LIMIT = 64000;

	// 超过验证码发送上限
	public static final int ERROR_LIMIT_VER = ERROR_LIMIT + 1;

	// 没有权限
	public static final int ERROR_LIMIT_PERMISSION = ERROR_LIMIT_VER + 1;

	// 操作失败
	public static final int ERROR_CONTROL = 65000;

	// 同步环信修改密码失败
	public static final int ERROR_CONTROL_HUANXIN_MODIFY = ERROR_CONTROL + 1;

	// 同步环信注册帐号失败
	public static final int ERROR_CONTROL_HUANXIN_RESIGN = ERROR_CONTROL_HUANXIN_MODIFY + 1;

	// 同步发送验证短信操作失败
	public static final int ERROR_CONTROL_TAOBAO_SMS = ERROR_CONTROL_HUANXIN_RESIGN + 1;

	// 验证码错误
	public static final int ERROR_CONTROL_VER = ERROR_CONTROL_TAOBAO_SMS + 1;

	// 没有此帐号
	public static final int ERROR_CONTROL_ACCOUNT_NOT = ERROR_CONTROL_VER + 1;

	// 密码错误
	public static final int ERROR_CONTROL_PASSWORD = ERROR_CONTROL_ACCOUNT_NOT + 1;

	// 帐号已存在
	public static final int ERROR_CONTROL_ACCOUNT_EXIST = ERROR_CONTROL_PASSWORD + 1;

	// 令牌不匹配
	public static final int ERROR_CONTROL_TOKEN = ERROR_CONTROL_ACCOUNT_EXIST + 1;

	// 查找的id没有找到
	public static final int ERROR_CONTROL_ID_NOT_FIND = ERROR_CONTROL_TOKEN + 1;

	// 帐号被移除
	public static final int ERROR_CONTROL_USER_REMOVE = ERROR_CONTROL_ID_NOT_FIND + 1;

	// 查找的消息没有找到
	public static final int ERROR_CONTROL_MSG_NOT_FIND = ERROR_CONTROL_USER_REMOVE + 1;

	// 删除消息失败
	public static final int ERROR_CONTROL_MSG_REMOVE = ERROR_CONTROL_MSG_NOT_FIND + 1;

	// 查找的新闻没有找到
	public static final int ERROR_CONTROL_NEWS_NOT_FIND = ERROR_CONTROL_MSG_REMOVE + 1;

	// 评论的目标没有找到
	public static final int ERROR_CONTROL_DST_NOT_FIND = ERROR_CONTROL_NEWS_NOT_FIND + 1;

	// 评论删除失败
	public static final int ERROR_CONTROL_COMMENT_REMOVE = ERROR_CONTROL_DST_NOT_FIND + 1;

	// 点赞目标没有找到
	public static final int ERROR_CONTROL_THUMB_NOT_FIND = ERROR_CONTROL_COMMENT_REMOVE + 1;

	// 拍砖目标没有找到
	public static final int ERROR_CONTROL_TILE_NOT_FIND = ERROR_CONTROL_THUMB_NOT_FIND + 1;

	// 设备类型错误
	public static final int ERROR_CONTROL_DEVICE_TYPE = ERROR_CONTROL_TILE_NOT_FIND + 1;

	// 设备没有找到
	public static final int ERROR_CONTROL_DEVICE_NOT_FIND = ERROR_CONTROL_DEVICE_TYPE + 1;


}
