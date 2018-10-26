package com.example.zhbj.Utils;

import android.content.Context;

public class CacheUtils {
    public static void setCache(String url, String json, Context context){
        SharePreferenceUtil.putString(context,url,json);
    }
    public static String getCache(String url, Context context){
        return SharePreferenceUtil.getString(context,url,null);
    }
}
