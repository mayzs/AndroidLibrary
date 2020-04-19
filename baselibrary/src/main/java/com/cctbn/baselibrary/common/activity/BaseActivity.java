package com.cctbn.baselibrary.common.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cctbn.baselibrary.R;
import com.cctbn.baselibrary.common.manager.ActivityManage;
import com.cctbn.baselibrary.common.permissions.PermissionsManager;
import com.cctbn.baselibrary.common.utils.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private ImmersionBar immersionBar;
    private ViewGroup mainLayout;
    private int barColor = R.color.white;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mainLayout = findViewById(R.id.layout);
        if (isImmersionBar()) {
            immersionBar = ImmersionBar.with(this);
            if (barColor != 0) {
                immersionBar.barColor(barColor);
//                if (isAddTitleBar()) {
//                    immersionBar.titleBar(mainLayout);
//                }
            }
            immersionBar.autoStatusBarDarkModeEnable(true, 0.2f).fitsSystemWindows(true).keyboardEnable(keyboardEnable()).init();
        }
        if (regEvent()) {
            EventBus.getDefault().register(this);
        }
        if (isAddManager()) {
            ActivityManage.getIstance().addActivity(this);
        }
    }

    public void showToastMessage(String message) {
        ToastUtils.getInstance().showToast(BaseActivity.this, message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (immersionBar != null) {
//            immersionBar.destroy();
//        }
        if (regEvent()) {
            EventBus.getDefault().unregister(this);
        }
        if (isAddManager()) {
            ActivityManage.getIstance().finishActivity(this);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        if (mainLayout != null) {
            mainLayout.removeAllViews();
            mainLayout.addView(this.getLayoutInflater().inflate(layoutResID, null));
        } else {
            super.setContentView(layoutResID);
        }

    }

    @Override
    public void setContentView(View view, android.view.ViewGroup.LayoutParams params) {
        // TODO Auto-generated method stub
        mainLayout.removeAllViews();
        mainLayout.addView(view, params);
    }

    @Override
    public void setContentView(View view) {
        // TODO Auto-generated method stub
        mainLayout.removeAllViews();
        mainLayout.addView(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    /**
     * 是否使用沉浸式
     *
     * @return
     */
    protected boolean isImmersionBar() {
        return true;
    }

    /**
     * 设置 状态栏颜色
     *
     * @param barColor
     */
    public void setBarColor(int barColor) {
        this.barColor = barColor;
        if (barColor != 0 && immersionBar != null) {
            immersionBar.barColor(barColor);
//            if (isAddTitleBar()) {
//                immersionBar.titleBar(mainLayout);
//            }
            immersionBar.init();
        }
    }

    public void setfitsSystem(boolean boo){
        immersionBar.autoStatusBarDarkModeEnable(true, 0.2f).fitsSystemWindows(boo).keyboardEnable(keyboardEnable()).init();
    }

    public boolean isAddTitleBar() {
        return true;
    }


    /**
     * 需要接收事件 重写该方法 并返回true
     */
    protected boolean regEvent() {
        return false;
    }

    protected boolean isAddManager() {
        return true;
    }

    protected boolean keyboardEnable() {
        return true;
    }

    protected boolean isOpenAnim() {
        return true;
    }

    /**
     * 可以重新此方法来显示自己的loading
     *
     * @return
     */
    protected Dialog loadingDialog(String hintText) {
        Dialog dialog = new Dialog(this, R.style.defuse_loading_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.defuse_loading_dialog, null);
        TextView text = view.findViewById(R.id.loading_text);
        if (!TextUtils.isEmpty(hintText)){
            text.setText(hintText);
        }
        dialog.setContentView(view);
        return dialog;
    }
    protected Dialog loadingDialog(){
        return loadingDialog("");
    }
    protected Dialog loadingDialog(int hintText){
        String text=getText(hintText).toString();
        return loadingDialog(text);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (isOpenAnim()) {
            this.overridePendingTransition(R.anim.activity_open, 0);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (isOpenAnim()) {
            this.overridePendingTransition(R.anim.activity_open, 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (isOpenAnim()) {
            this.overridePendingTransition(0, R.anim.activity_close);
        }
    }

}
