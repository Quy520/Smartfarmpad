package com.smartfarm.bean;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 
 * 获取服务器数据；
 * @author Lei
 *
 */
public class ResponseModel {
	private RequestParams params;
	private AsyncHttpResponseHandler handle;
	private int type = -1; // -1错误，0get,1post,2put
	private String url;
	public static final int MSG_GET = 0;
	public static final int MSG_POST = MSG_GET + 1;
	public static final int MSG_PUT = MSG_POST + 1;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RequestParams getParams() {
		return params;
	}

	public void setParams(RequestParams params) {
		this.params = params;
	}

	public AsyncHttpResponseHandler getHandle() {
		return handle;
	}

	public void setHandle(AsyncHttpResponseHandler handle) {
		this.handle = handle;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
