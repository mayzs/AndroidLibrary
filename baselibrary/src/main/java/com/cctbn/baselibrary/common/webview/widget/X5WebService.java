package com.cctbn.baselibrary.common.webview.widget;

import android.app.IntentService;
import android.content.Intent;

import com.tencent.smtt.sdk.QbSdk;

public class X5WebService extends IntentService {


    public X5WebService() {
        super("X5WebService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initX5Web();
    }


    public void initX5Web() {
        if (!QbSdk.isTbsCoreInited()) {
            // 设置X5初始化完成的回调接口
            QbSdk.preInit(getApplicationContext(), null);
        }
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
        @Override
        public void onViewInitFinished(boolean arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onCoreInitFinished() {
            // TODO Auto-generated method stub
        }
    };
}
