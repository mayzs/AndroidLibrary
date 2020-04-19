package com.cctbn.baselibrary.mvp.base;

import android.content.Context;

/**
 *所有View基类
 */

public interface IBaseView {

    /**
     * 显示加载框
     */
    void showLoading();

    /**
     * 显示加载框
     */
    void showLoading(String hintText);

    /**
     * 显示加载框
     */
    void showLoading(int hintText);

    /**
     * 隐藏加载框
     */
    void dismissLoading();

    /**
     * 空数据
     *
     * @param tag TAG
     */
    void onEmpty(Object tag);

    /**
     * 错误数据
     *
     * @param tag      TAG
     * @param errorMsg 错误信息
     */
    void onError(Object tag, String errorMsg);

    /**
     * 上下文
     *
     * @return context
     */
    Context getContext();
}
