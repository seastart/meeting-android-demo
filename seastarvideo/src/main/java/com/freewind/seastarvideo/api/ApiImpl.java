package com.freewind.seastarvideo.api;

import com.freewind.seastarvideo.bean.BaseBean;

public interface ApiImpl<T extends BaseBean> {
    void onSuccess(T data);

    void onFailed(int code, String msg);

    void onStart();

    void onCancel();
}
