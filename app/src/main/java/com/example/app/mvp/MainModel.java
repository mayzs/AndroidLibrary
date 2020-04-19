package com.example.app.mvp;

import android.content.Context;

import com.cctbn.baselibrary.common.network.RetrofitUtils;
import com.cctbn.baselibrary.common.network.delagate.DataCallback;
import com.example.app.bean.TimeVo;

/**
 * @createDate: 2020/4/19
 * @author: mayz
 * @version: 1.0
 */
public class MainModel implements MainContract.Model{
    @Override
    public void getTimeBack(DataCallback<TimeVo> dataCallback) {
        RetrofitUtils.getInstance().get().url("https://carapi.anmirror.cn/time").execute(dataCallback);
    }
}
