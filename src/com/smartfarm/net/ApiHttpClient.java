package com.smartfarm.net;

import java.util.Locale;

import org.apache.http.client.params.ClientPNames;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.smartfarm.tools.Constants;


public class ApiHttpClient {

	public static final int JSP_SERVER = 1;
	public static final int PYTHON_SERVER = 2;
	public static final String PYTHON_SERVER_STATIC_URL = "http://115.29.48.182/static";
	
	private static final String JSP_SERVER_ADDR = "101.200.239.85:8080/smartfarm";
//	private static final String PYTHON_SERVER_ADDR = "115.29.48.182/client_api";
	private static final String PYTHON_SERVER_ADDR = "api.shztzn.com/api";
//	private static final String PYTHON_SERVER_ADDR = "192.168.10.115:5000/api";
//	private static final String PYTHON_SERVER_ADDR = "192.168.10.125:8000/client_api";
	
    public static String API_URL = "http://%s/%s"; 
    
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static AsyncHttpClient client;

    public ApiHttpClient() {}

    public static AsyncHttpClient getHttpClient() {
        return client;
    }

    public static void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }

    public static void clearUserCookies(Context context) {
        // (new HttpClientCookieStore(context)).a();
    }

    public static void delete(int serverType,String partUrl, AsyncHttpResponseHandler handler) {
        client.delete(getAbsoluteApiUrl(serverType,partUrl), handler);
        log(new StringBuilder("DELETE ").append(partUrl).toString());
    }

    public static void get(int serverType,String partUrl, AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(serverType,partUrl), handler);
        log(new StringBuilder("GET ").append(partUrl).toString());
    }

    public static void get(int serverType,String partUrl, RequestParams params,
            AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(serverType,partUrl), params, handler);
        log(new StringBuilder("GET ").append(partUrl).append("&").append(params).toString());
    }

    public static String getAbsoluteApiUrl(int serverType,String partUrl) {
    	String serverAddr = serverType == JSP_SERVER ? JSP_SERVER_ADDR : PYTHON_SERVER_ADDR;
        String url = String.format(API_URL, serverAddr, partUrl);
        return url;
    }

    public static String getApiUrl() {
        return API_URL;
    }

    public static void getDirect(String url, AsyncHttpResponseHandler handler) {
        client.get(url, handler);
        log(new StringBuilder("GET ").append(url).toString());
    }

    public static void log(String log) {
        Log.d(Constants.TAG, log);
    }

    public static void post(int serverType,String partUrl, AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(serverType,partUrl), handler);
        log(new StringBuilder("POST ").append(partUrl).toString());
    }

    public static void post(int serverType,String partUrl, RequestParams params,
            AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(serverType,partUrl), params, handler);
        log(new StringBuilder("POST ").append(partUrl).append("&")
                .append(params).toString());
    }

    public static void postDirect(String url, RequestParams params,
            AsyncHttpResponseHandler handler) {
        client.post(url, params, handler);
        log(new StringBuilder("POST ").append(url).append("&").append(params)
                .toString());
    }

    public static void put(int serverType,String partUrl, AsyncHttpResponseHandler handler) {
        client.put(getAbsoluteApiUrl(serverType,partUrl), handler);
        log(new StringBuilder("PUT ").append(partUrl).toString());
    }

    public static void put(int serverType,String partUrl, RequestParams params,
            AsyncHttpResponseHandler handler) {
        client.put(getAbsoluteApiUrl(serverType,partUrl), params, handler);
        log(new StringBuilder("PUT ").append(partUrl).append("&")
                .append(params).toString());
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    public static void setHttpClient(AsyncHttpClient c) {
        client = c;
        client.addHeader("Accept-Language", Locale.getDefault().toString());
//        client.addHeader("Host", HOST);
        client.addHeader("Connection", "Keep-Alive");
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

//        setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
    }

    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }
}
