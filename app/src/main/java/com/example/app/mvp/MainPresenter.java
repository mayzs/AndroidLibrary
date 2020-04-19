package com.example.app.mvp;

import com.cctbn.baselibrary.common.network.delagate.DataCallback;
import com.cctbn.baselibrary.mvp.base.BasePresenter;
import com.example.app.bean.TimeVo;

/**
 * @createDate: 2020/4/19
 * @author: mayz
 * @version: 1.0
 */
public class MainPresenter extends BasePresenter<MainContract.Model,MainContract.View> implements MainContract.Presenter {
    @Override
    protected MainContract.Model createModule() {
        return new MainModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void time() {
     getModule().getTimeBack(new DataCallback<TimeVo>() {
         @Override
         public void onSuccess(TimeVo result) {
             getView().resultTime(result);
         }

         @Override
         public void onError(String errmsg) {

         }
     });
    }
}
