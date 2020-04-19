package com.cctbn.baselibrary.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author ty
 * @createdate 2012-6-4 下午2:41:24
 * @Description: 网络工具类
 */
public class NetWorkUtil {
    // true 得采用代理上网，false不需要
    public static boolean ONLYWAP;
    // true 没有网络，false有网络
    public static boolean NO_NETWORK;

    /**
     * 获取网络连接NetworkInfo对象
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo;
    }

    /**
     * 网络类型
     *
     * @param context
     */
    public static void getNetWorkInfoType(Context context) {
        NetworkInfo networkinfo = getNetworkInfo(context);
        if (networkinfo != null) {
            NO_NETWORK = false;
            if (networkinfo.getTypeName().equals("WIFI")) {
                ONLYWAP = true;
            }
            if (networkinfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                if ("cmwap".equalsIgnoreCase(networkinfo.getExtraInfo()) || "3gwap".equalsIgnoreCase(networkinfo.getExtraInfo()) || "ctwap".equalsIgnoreCase(networkinfo.getExtraInfo())
                        || "3gnet".equalsIgnoreCase(networkinfo.getExtraInfo()) || "cmnet".equalsIgnoreCase(networkinfo.getExtraInfo())) {
                    ONLYWAP = false;
                } else {
                    ONLYWAP = true;
                }
            }

        } else {
            NO_NETWORK = true;
        }
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /**
     * @param context
     * @return
     */
    public static boolean noNetConnected(Context context) {
        getNetWorkInfoType(context);
        return NO_NETWORK;
    }

    // 判断网络状态
    public static boolean isConnectionType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = cm.getActiveNetworkInfo();
        boolean flag = active != null && active.getTypeName().equals("WIFI");
        return flag;
    }

    public static boolean isWifi(Context context) {
        return !isConnectionType(context);
    }

    /**
     * 判断手机又没有连数据MOBILE
     *
     * @param context
     * @return
     */
    public static boolean isMOBILEConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否连接了wifi
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnectted(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            } else {
                NetworkInfo[] allNetworkInfo = connectivity.getAllNetworkInfo();
                if (allNetworkInfo != null) {
                    int length = allNetworkInfo.length;
                    for (int i = 0; i < length; i++) {
                        // 判断获得的网络状态是否是处于连接状态
                        if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
                return false;

            }
        }
        return false;
    }
}
