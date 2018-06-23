package com.example.a13612.weather.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 服务器交互：
 *   调用sendOkHttpRequest（parm1,parm2）方法发起一条HTTP请求
 *   parm1:传入请求地址
 *   parm2:注册一个回调来处理服务器响应
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
