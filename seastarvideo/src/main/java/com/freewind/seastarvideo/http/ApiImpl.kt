package com.freewind.seastarvideo.http

import com.freewind.seastarvideo.http.bean.BaseBean

interface ApiImpl<T : BaseBean> {
    fun onSuccess(data: T)

    fun onFailed(code: Int, msg: String?)

    fun onStart()

    fun onCancel()
}
