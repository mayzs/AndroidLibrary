package com.cctbn.baselibrary.common.network.builder;

import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.cctbn.baselibrary.common.network.Params;
import com.cctbn.baselibrary.common.network.RetrofitManager;
import com.cctbn.baselibrary.common.network.apiservice.APIService;
import com.cctbn.baselibrary.common.network.delagate.Callback;
import com.cctbn.baselibrary.common.network.delagate.DataCallback;
import com.cctbn.baselibrary.common.network.delagate.FileCallback;
import com.cctbn.baselibrary.common.network.delagate.StringCallback;
import com.cctbn.baselibrary.common.network.schedulers.SchedulerProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * @createDate: 2019/1/8
 * @author: mayz
 * @version: 1.0
 */
public class BaseBuilder {
    private String baseUrl = "";
    private String url = "";
    private Map<String, String> headMap = new TreeMap<>();
    private Map<String, String> paramsMap = new TreeMap<>();
    private Params params;

    public BaseBuilder(Params params) {
        this.params = params;
    }

    public BaseBuilder url(String url) {
        int index = url.indexOf("/");
        index = url.indexOf("/", index + 2);
        this.baseUrl = url.substring(0, index + 1);
        this.url = url;
        return this;
    }

    public BaseBuilder header(String key, String value) {
        this.headMap.put(key, value);
        return this;
    }

    public BaseBuilder headers(Map<String, String> headMaps) {
        this.headMap.putAll(headMaps);
        return this;
    }

    public BaseBuilder param(String key, String value) {
        this.paramsMap.put(key, value);
        return this;
    }

    public BaseBuilder params(Map<String, String> params) {
        this.paramsMap.putAll(params);
        return this;
    }


    public void execute(Callback callback) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(baseUrl)) {
            Observable<String> observable = null;
            APIService apiService = RetrofitManager.getInstance().setUrl(baseUrl).getAPIService();
            if (this.params == Params.GET) {
                observable = apiService.get(headMap, url, paramsMap);
            } else if (this.params == Params.POST) {
                observable = apiService.post(headMap, url, paramsMap);
            } else if (this.params == Params.POST_JSON || this.params == Params.POST_FILE) {
                observable = getObservable(apiService, headMap, url, paramsMap);
            } else if (this.params == Params.DELETE) {
                observable = apiService.delete(headMap, url, paramsMap);
            } else if (this.params == Params.PUT) {
                observable = apiService.put(headMap, url, paramsMap);
            }
            if (callback == null)
                return;
            if (callback instanceof StringCallback) {
                execute(observable, (StringCallback) callback);
            }
            if (callback instanceof DataCallback) {
                execute(observable, (DataCallback) callback);
            }

        } else {
            if (callback != null) {
                callback.onError("请检查url是否正确!");
            }
        }
    }

    private void execute(Observable<String> observable, StringCallback callback) {
        if (observable == null) {
            return;
        }
        observable.subscribeOn(SchedulerProvider.getInstance().io())//请求数据的事件发生在io线程
                .observeOn(SchedulerProvider.getInstance().ui())//请求完成后在主线程更显UI
        .subscribe(new Observer<String>() {//订阅
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(); //请求过程中发生错误
                        if (callback != null) {
                            callback.onError(e.getMessage());
                        }
                    }


                    @Override
                    public void onNext(String s) {//这里的book就是我们请求接口返回的实体类
                        if (callback != null) {
                            callback.onSuccess(s);
                        }
                    }
                });
    }

    private void execute(Observable<String> observable, DataCallback callback) {
        if (observable == null) {
            return;
        }
        observable.subscribeOn(SchedulerProvider.getInstance().io())//请求数据的事件发生在io线程
                .observeOn(SchedulerProvider.getInstance().ui())//请求完成后在主线程更显UI
               .subscribe(new Observer<String>() {//订阅
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(); //请求过程中发生错误
                        if (callback != null) {
                            callback.onError(e.getMessage());
                        }
                    }


                    @Override
                    public void onNext(String s) {
                        if (callback == null) {
                            return;
                        }
                        Type[] a = callback.getClass().getGenericInterfaces();
                        if (a != null && a.length > 0) {
                            if ("java.lang.Class".equals(a[0].getClass().getName())) {
                                return;
                            }
                            Type[] temp = ((ParameterizedType) a[0]).getActualTypeArguments();
                            if (temp != null && temp.length > 0) {
                                Object object = JSON.parseObject(s, (Class) temp[0]);
                                callback.onSuccess(object);
                            }
                        }
                    }
                });
    }

    protected Observable<String> getObservable(APIService apiService, Map<String, String> headMap, String url, Map<String, String> paramsMap) {
        return null;
    }
    public void execute(FileCallback fileCallback) {
        if (TextUtils.isEmpty(url)){
            return;
        }
        long range = 0;
        APIService apiService = RetrofitManager.getInstance().setUrl(baseUrl).getAPIService();
        Observable<ResponseBody> observable=apiService.download(url);
        observable.subscribeOn(SchedulerProvider.getInstance().io())//请求数据的事件发生在io线程
         .observeOn(SchedulerProvider.getInstance().ui())//请求完成后在主线程更显UI
        .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                Looper.prepare();
                                if (fileCallback!=null){
                                    fileCallback.saveFile(range,responseBody);
                                }
                                Looper.loop();
                            }
                        }.start();

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (fileCallback!=null){
                            fileCallback.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                });

    }
}
