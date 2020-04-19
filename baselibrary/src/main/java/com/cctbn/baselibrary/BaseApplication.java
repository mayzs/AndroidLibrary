package com.cctbn.baselibrary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.cctbn.baselibrary.common.webview.widget.X5WebService;

import androidx.multidex.MultiDex;

/**
 * @createDate: 2018/12/27
 * @author: mayz
 * @version: 1.0
 */
public class BaseApplication extends Application{
    //全局唯一的context
    private static BaseApplication application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preInitX5Core();
    }
    /**
     * 获取全局唯一上下文
     *
     * @return BaseApplication
     */
    public static BaseApplication getContext() {
        return application;
    }
    private void preInitX5Core() {
        //预加载x5内核
        Intent intent = new Intent(this, X5WebService.class);
        startService(intent);
    }
}
