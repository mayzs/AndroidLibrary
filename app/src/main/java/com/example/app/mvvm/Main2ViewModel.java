package com.example.app.mvvm;

import android.app.Application;

import com.cctbn.baselibrary.mvvm.ParamsBuilder;
import com.cctbn.baselibrary.mvvm.Resource;
import com.cctbn.baselibrary.mvvm.base.modelview.BaseViewModel;
import com.example.app.bean.BannerBean;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * @createDate: 2020/4/21
 * @author: mayz
 * @version: 1.0
 */
public class Main2ViewModel extends BaseViewModel {


    public Main2ViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Resource<BannerBean>> getBanners(){
        MutableLiveData<Resource<BannerBean>> liveData = new MutableLiveData<>();
        return getRepository().observe(new BannerBean(),liveData, ParamsBuilder.build().url("https://www.wanandroid.com/banner/json"));
    }
}
