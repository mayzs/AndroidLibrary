package com.cctbn.baselibrary.mvp.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.cctbn.baselibrary.common.activity.BaseActivity;
import com.cctbn.baselibrary.mvp.base.BasePresenter;
import com.cctbn.baselibrary.mvp.base.IBaseView;

import androidx.annotation.Nullable;

public abstract class BaseMvpActivity <P extends BasePresenter> extends BaseActivity implements IBaseView {

    protected P presenter;
    protected Dialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //创建present
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
    }

    @Override
    public void showLoading() {
        loadingDialog=loadingDialog();
        loadingDialog.show();
    }

    @Override
    public void showLoading(String hintText) {
        loadingDialog=loadingDialog(hintText);
        loadingDialog.show();
    }

    @Override
    public void showLoading(int hintText) {
        loadingDialog=loadingDialog(hintText);
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }



    @Override
    public void onEmpty(Object tag) {



    }



    @Override
    public void onError(Object tag, String errorMsg) {

    }



    @Override
    public Context getContext() {

        return this;

    }

    //***************************************IBaseView方法实现*************************************



    /**
     * 创建Presenter
     */

    protected abstract P createPresenter();
}
