/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: wiatt
 * @date: 2024/6/7 14:08
 * @description: 避免 MutableLiveData 不必要的多次回调
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        // Observe the internal MutableLiveData
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    override fun postValue(t: T?) {
        mPending.set(true)
        super.postValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    fun call() {
        setValue(null)
    }
}