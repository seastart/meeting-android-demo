package com.freewind.seastarvideo.http

import com.freewind.seastarvideo.http.bean.BaseBean

abstract class Callback<T : BaseBean> : ApiImpl<T> {
    override fun onStart() {

    }

    override fun onCancel() {

    }

    override fun onSuccess(data: T) {

    }

    override fun onFailed(code: Int, msg: String?) {

    }
}
