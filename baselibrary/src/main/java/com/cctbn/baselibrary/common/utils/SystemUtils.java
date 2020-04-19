package com.cctbn.baselibrary.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Method;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

/**
 * @createDate: 2018/6/22
 * @author: mayz
 * @version: 1.0
 */
public class SystemUtils {
    private SystemUtils() {
    }

    private static volatile SystemUtils instance = null;

    public static SystemUtils getIstance() {
        if (instance == null) {
            synchronized (SystemUtils.class) {
                if (instance == null) {
                    instance = new SystemUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context
     * @return
     */
    public String getVersionName(Context context) {
        String verName = "1.0";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取手机IMEI号
     */


    @SuppressLint("MissingPermission")
    public String getDeviceId(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager != null) {
            String imei = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei = mTelephonyManager.getImei();
            } else {
                imei = mTelephonyManager.getDeviceId();
            }

            if (!TextUtils.isEmpty(imei) && imei.length() == 15) {
                return imei;
            } else {
                String imei_1 = "";
                try {
                    Method method = mTelephonyManager.getClass().getMethod("getImei", int.class);
                    imei_1 = (String) method.invoke(mTelephonyManager, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(imei_1) && imei_1.length() == 15) {
                    return imei_1;
                } else {
                    return !TextUtils.isEmpty(imei) ? imei : "000";
                }

            }
        }
        return "000";
    }

    /**
     * 安装apk
     *
     * @param file
     * @param context
     */
    public void installApk(File file, Context context) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
        context.startActivity(intent);
    }

    /**
     * 检查未知来源权限
     *
     * @param context
     * @return
     */
    public boolean checkInstallPermission(Context context) {
        boolean haveInstallPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先判断是否有安装未知来源应用的权限
            haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
        }
        return haveInstallPermission;
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void toInstallPermission(Context context, int requestCode) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
    }

    public int getScreenWidth_PX(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return width;
    }

    public int getScreenWidth_DP(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return screenWidth;
    }

    public int getScreenHeight_DP(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度

        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return screenHeight;
    }

    public int getScreenHeight_PX(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;       // 屏幕高度（像素）
        return height;
    }

    /**
     * 显示键盘
     *
     * @param view
     */
    public void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
//        isPush(view.getBottom(),true);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        isPush(0,false);

    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard(Context mContext) {
        InputMethodManager inputMgr = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }
}
