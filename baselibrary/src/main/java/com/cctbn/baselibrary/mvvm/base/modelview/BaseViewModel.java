package com.cctbn.baselibrary.mvvm.base.modelview;

import android.app.Application;

import com.cctbn.baselibrary.mvvm.base.model.BaseModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @createDate: 2020/4/21
 * @author: mayz
 * @version: 1.0
 */
public  class BaseViewModel<T extends BaseModel> extends AndroidViewModel {
    public CompositeDisposable compositeDisposable;
    private T repository;
    public BaseViewModel(@NonNull Application application) {
        super(application);
        createRepository();
        compositeDisposable = new CompositeDisposable();
    }
    protected  T createRepository(){
        if (repository==null){
            repository= (T) new BaseModel();
        }
        return repository;
    };
    public T getRepository() {
        return repository;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        //销毁后，取消当前页所有在执行的网络请求。
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
