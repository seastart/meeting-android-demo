package com.freewind.seastarvideo.api;

import com.freewind.seastarvideo.bean.BaseBean;

public abstract class Callback<T extends BaseBean> implements ApiImpl<T> {
    @Override
    public void onStart() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onSuccess(T data) {

    }

    @Override
    public void onFailed(int code, String msg) {

    }
}
