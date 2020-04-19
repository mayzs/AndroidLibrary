package com.cctbn.baselibrary.common.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cctbn.baselibrary.R;

import org.greenrobot.eventbus.EventBus;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        if (regEvent()) {
            EventBus.getDefault().register(this);
        }
        initView(rootView,savedInstanceState);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (regEvent()) {
            EventBus.getDefault().unregister(this);
        }

    }
    /**
     * 需要接收事件 重写该方法 并返回true
     */
    protected boolean regEvent() {
        return false;
    }

    /**
     * 可以重新此方法来显示自己的loading
     * @return
     */
    protected Dialog loadingDialog(String hintText){
        Dialog dialog=new Dialog(mContext, R.style.defuse_loading_dialog);
        View view= LayoutInflater.from(mContext).inflate(R.layout.defuse_loading_dialog,null);
        TextView text = view.findViewById(R.id.loading_text);
        if (!TextUtils.isEmpty(hintText)){
            text.setText(hintText);
        }
        dialog.setContentView(view);
        return dialog;
    }

    protected Dialog loadingDialog(){
        return loadingDialog("");
    }
    protected Dialog loadingDialog(int hintText){
        String text=getText(hintText).toString();
        return loadingDialog(text);
    }
    protected abstract int getLayoutId();



    protected abstract void initView(View view);
    protected void initView(View view,Bundle savedInstanceState){

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_open, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.activity_open, 0);
    }
}
