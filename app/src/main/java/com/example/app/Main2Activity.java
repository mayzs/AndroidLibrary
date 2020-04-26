package com.example.app;

import android.os.Bundle;
import android.util.Log;

import com.cctbn.baselibrary.common.utils.JsonUtils;
import com.cctbn.baselibrary.mvvm.Resource;
import com.cctbn.baselibrary.mvvm.activity.BaseMvvmActivity;
import com.example.app.bean.BannerBean;
import com.example.app.databinding.ActivityMain2Binding;
import com.example.app.mvvm.Main2ViewModel;

public class Main2Activity extends BaseMvvmActivity<Main2ViewModel, ActivityMain2Binding> {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mViewModel.getBanners().observe(this, bannerBeanResource -> bannerBeanResource.handler(new Resource.OnHandleCallback<BannerBean>() {
            @Override
            public void onSuccess(BannerBean data) {
                Log.i("tag", "===123=="+JsonUtils.toJson(data));
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onProgress(int precent) {

            }
        }));
    }
}
