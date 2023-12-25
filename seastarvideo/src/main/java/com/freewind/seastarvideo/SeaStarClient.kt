package com.freewind.seastarvideo

import android.app.Application
import android.content.Context

/**
 * @author: wiatt
 * @date: 2023/12/20 9:56
 * @description:
 */
interface SeaStarClient {

    companion object {
        val instance: SeaStarClientImpl by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SeaStarClientImpl()
        }
    }

    /**
     * 初始化
     */
    fun SSC_Init(application: Application)

    fun SSC_StartHomeActivity(context: Context)
}