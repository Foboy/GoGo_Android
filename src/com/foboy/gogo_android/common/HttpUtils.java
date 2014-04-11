package com.foboy.gogo_android.common;

import android.app.Application;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtils {
	private static AsyncHttpClient client =new FBAsyncHttpClient();
	private static Context context = new Application();


    public static void post(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
    	client.post(context,urlString, params,res);
    }

    public static void post(String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
    	client.post(context, urlString, params,res);
    }
    
    public static void cancelRequests()
    {
    	client.cancelRequests(context, true);
    }

}
