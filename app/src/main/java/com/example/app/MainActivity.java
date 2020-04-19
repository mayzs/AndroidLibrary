package com.example.app;

import android.os.Bundle;
import android.util.Log;

import com.cctbn.baselibrary.common.utils.JsonUtils;
import com.cctbn.baselibrary.mvp.activity.BaseMvpActivity;
import com.example.app.bean.TimeVo;
import com.example.app.mvp.MainContract;
import com.example.app.mvp.MainPresenter;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter.time();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void resultTime(TimeVo timeVo) {
     Log.i("tag","===123="+ JsonUtils.toJson(timeVo));
    }
}
