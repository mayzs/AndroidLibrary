package com.example.app.mvp;

import com.cctbn.baselibrary.common.network.delagate.DataCallback;
import com.cctbn.baselibrary.mvp.base.IBaseModel;
import com.cctbn.baselibrary.mvp.base.IBaseView;
import com.example.app.bean.TimeVo;

/**
 * @createDate: 2020/4/19
 * @author: mayz
 * @version: 1.0
 */
public class MainContract {
    public interface Model extends IBaseModel{
        void getTimeBack(DataCallback<TimeVo> dataCallback);
    }
    public interface View extends IBaseView {
        void resultTime(TimeVo timeVo);

    }
    public interface Presenter{
        void time();
    }
}
