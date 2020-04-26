package com.cctbn.baselibrary.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @createDate: 2020/4/19
 * @author: mayz
 * @version: 1.0
 */
public class SPUtils {
    public static final String SHARED_PREFERENCE_NAME = "library_sf";
    public static void saveBoolean(Context context, String key, boolean value){
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        edit.putBoolean(key,value);
        edit.apply();
    }
    public static boolean getBoolean(Context context,String key){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            boolean aBoolean = sharedPreferences.getBoolean(key, false);
            return aBoolean;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
