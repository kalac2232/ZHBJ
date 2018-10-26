package com.example.zhbj.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by 97210 on 2018/2/3.
 */

public class SharePreferenceUtil {

    private static SharedPreferences sharedPreferences;

    /**
     * 向sp中存入boolean类型的值
     * @param context 上下文
     * @param key 关键字
     * @param value 存储的值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        if (sharedPreferences == null) {

            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key,value);
        edit.commit();//提交
    }

    /**
     * 从sp中取Boolean类型的值
     * @param context 上下文
     * @param key 关键字
     * @param defaultValue 没有取得值返回的默认值
     * @return 返回取得的值
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (sharedPreferences == null) {

            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key,defaultValue);
    }
    /**
     * 向sp中存入Int类型的值
     * @param context 上下文
     * @param key 关键字
     * @param value 存储的值
     */
    public static void putInt(Context context, String key, int value) {
        if (sharedPreferences == null) {

            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key,value);
        edit.commit();//提交
    }
    /**
     * 向sp中存入String类型的值
     * @param context 上下文
     * @param key 关键字
     * @param value 存储的值
     */
    public static void putString(Context context, String key, String value) {
        if (sharedPreferences == null) {

            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value);
        edit.commit();//提交
    }

    /**
     * 从sp中取String类型的值
     * @param context 上下文
     * @param key 关键字
     * @param defaultValue 没有取得值返回的默认值
     * @return 返回取得的值
     */
    public static String getString(Context context, String key, String defaultValue) {
        if (sharedPreferences == null) {

            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key,defaultValue);
    }
    /**
     * 从sp中取Int类型的值
     * @param context 上下文
     * @param key 关键字
     * @param defaultValue 没有取得值返回的默认值
     * @return 返回取得的值
     */
    public static int getInt(Context context, String key, int defaultValue) {
        if (sharedPreferences == null) {

            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key,defaultValue);
    }

    /**
     * 移除节点
     * @param context 上下文
     * @param key 关键字
     */
    public static void remove(Context context, String key) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().remove(key);
    }
}
