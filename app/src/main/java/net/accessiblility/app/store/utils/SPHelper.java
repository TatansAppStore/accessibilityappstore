package net.accessiblility.app.store.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 2016/11/29.
 */

public class SPHelper {

    private static final String SP_NAME = "appstore-config";

    private static SharedPreferences sp;

    /**
     *  通过sp保存应用中的boolean状态值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     *
     * @param context 上下文
     * @param key 存储的键名
     * @param defValue 默认值
     * @return 默认值或者结果
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     *
     * @param context 上下文
     * @param key 存储键的名称
     * @param value 存储值
     */
    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     *
     * @param context 上下文
     * @param key 存储的键名
     * @param defValue 默认值
     * @return 默认值或者结果
     */
    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /**
     *
     * @param context 上下文
     * @param key 存储键的名称
     * @param value 存储值
     */
    public static void putInt(Context context, String key, int value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    /**
     *
     * @param context 上下文
     * @param key 存储的键名
     * @param defValue 默认值
     * @return 默认值或者结果
     */
    public static int getInt(Context context, String key, int defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }


}
