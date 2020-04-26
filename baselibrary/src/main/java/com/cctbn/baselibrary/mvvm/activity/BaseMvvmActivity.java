package com.cctbn.baselibrary.mvvm.activity;

import android.os.Bundle;

import com.cctbn.baselibrary.common.activity.BaseActivity;
import com.cctbn.baselibrary.mvvm.base.modelview.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseMvvmActivity<VM extends BaseViewModel,VDB extends ViewDataBinding> extends BaseActivity {

    protected VM mViewModel;
    protected VDB binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        binding = DataBindingUtil.setContentView(this,getContentViewId());
        binding.setLifecycleOwner(this);
        createViewModel();
        initData(savedInstanceState);
    }

    protected abstract int getContentViewId();
    protected abstract void initData(Bundle savedInstanceState);
    public void createViewModel(){
        if (mViewModel==null){
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType){
                modelClass = (Class) ((ParameterizedType)type).getActualTypeArguments()[0];
            }else {
                modelClass = BaseViewModel.class;
            }
            mViewModel =  (VM) ViewModelProviders.of(this).get(modelClass);
        }
    }
}
