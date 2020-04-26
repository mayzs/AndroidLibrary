package com.cctbn.baselibrary.mvvm.base.model;


import com.alibaba.fastjson.JSON;
import com.cctbn.baselibrary.common.network.RetrofitUtils;
import com.cctbn.baselibrary.common.network.builder.BaseBuilder;
import com.cctbn.baselibrary.common.network.delagate.FileCallback;
import com.cctbn.baselibrary.common.network.delagate.StringCallback;
import com.cctbn.baselibrary.mvvm.ParamsBuilder;
import com.cctbn.baselibrary.mvvm.Resource;

import java.io.File;

import androidx.lifecycle.MutableLiveData;

/**
 * @createDate: 2020/4/23
 * @author: mayz
 * @version: 1.0
 */
public class BaseModel {
    public <T> MutableLiveData<T> observe(final Object object, final MutableLiveData<T> liveData, ParamsBuilder paramsBuilder) {
        if (paramsBuilder==null){
            paramsBuilder = ParamsBuilder.build();
        }
        BaseBuilder builder;
        switch (paramsBuilder.getMethod()){
            case GET:
                builder= RetrofitUtils.getInstance().get();
                break;
            case POST:
                builder=RetrofitUtils.getInstance().post();
                break;
            case POST_JSON:
                builder=RetrofitUtils.getInstance().post(paramsBuilder.getParamsMap());
                break;
            case POST_FILE:
                builder = RetrofitUtils.getInstance().post(paramsBuilder.getFileMethodName(),paramsBuilder.getFiles());
                break;
            case PUT:
                builder = RetrofitUtils.getInstance().put();
                break;
            case DELETE:
                builder = RetrofitUtils.getInstance().delete();
                break;
            default:
                builder=RetrofitUtils.getInstance().get();
                break;
        }

        builder.url(paramsBuilder.getUrl()).headers(paramsBuilder.getHeadMap()).params(paramsBuilder.getParamsMap()).execute(new StringCallback() {

            @Override
            public void onSuccess(String result) {
                liveData.postValue((T)Resource.success((T) JSON.parseObject(result,object.getClass())));
            }

            @Override
            public void onError(String errmsg) {

                liveData.postValue((T)Resource.failure(errmsg));
            }});
        return liveData;
    }

    public <T> MutableLiveData<T> observeDownload(final MutableLiveData<T> liveData,String url,String filePath, String fileName){
        RetrofitUtils.getInstance().get().url(url).execute(new FileCallback(filePath,fileName) {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {
                liveData.postValue((T) Resource.progress(progress));
            }

            @Override
            public void onCompleted(File file) {
                liveData.postValue((T)Resource.success(file));
            }

            @Override
            public void onError(String errmsg) {
                liveData.postValue((T)Resource.failure((errmsg)));
            }
        });

        return liveData;
    }
}
