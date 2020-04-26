package com.cctbn.baselibrary.mvvm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctbn.baselibrary.mvvm.base.modelview.BaseViewModel;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseMvvmFragment <VM extends BaseViewModel, VDB extends ViewDataBinding> extends RxFragment {

    //获取当前activity布局文件
    protected abstract int getContentViewId();

    //处理逻辑业务
    protected abstract void processLogic(Bundle savedInstanceState);

    //所有监听放这里
    protected abstract void setListener();


    protected VM mViewModel;
    protected View mContentView;
    protected VDB binding;
    public BaseMvvmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mContentView == null) {
            binding = DataBindingUtil.inflate(inflater, getContentViewId(), null, false);
            mContentView = binding.getRoot();
            binding.setLifecycleOwner(this);
            createViewModel();
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }
    public void createViewModel() {
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) ViewModelProviders.of(getActivity()).get(modelClass);
            //mViewModel.setObjectLifecycleTransformer(bindToLifecycle());
        }
    }
}
