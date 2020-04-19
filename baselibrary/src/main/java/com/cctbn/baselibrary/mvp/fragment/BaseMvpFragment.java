package com.cctbn.baselibrary.mvp.fragment;

import android.app.Dialog;
import android.os.Bundle;

import com.cctbn.baselibrary.common.fragment.BaseFragment;
import com.cctbn.baselibrary.mvp.base.BasePresenter;
import com.cctbn.baselibrary.mvp.base.IBaseView;

import androidx.annotation.Nullable;

/**
 * @createDate: 2019/1/14
 * @author: mayz
 * @version: 1.0
 */
public abstract class BaseMvpFragment <P extends BasePresenter> extends BaseFragment implements IBaseView {

    protected P presenter;
    protected Dialog loadingDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建present
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    //***************************************IBaseView方法实现*************************************

    @Override
    public void showLoading() {
            loadingDialog=loadingDialog();
            loadingDialog.show();
    }

    @Override
    public void showLoading(int hintText) {
        loadingDialog=loadingDialog(hintText);
        loadingDialog.show();
    }

    @Override
    public void showLoading(String hintText) {
        loadingDialog=loadingDialog(hintText);
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }



    @Override
    public void onEmpty(Object tag) {



    }



    @Override
    public void onError(Object tag, String errorMsg) {



    }

    //***************************************IBaseView方法实现*************************************



    /**

     * 创建Presenter

     */

    protected abstract P createPresenter();
}
