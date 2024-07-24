package com.freewind.seastarvideo

import android.app.Application
import android.content.Context
import android.content.Intent
import com.freewind.seastarvideo.activity.HomeActivity
import com.freewind.seastarvideo.utils.LogUtil
import com.tencent.mmkv.MMKV

/**
 * @author: wiatt
 * @date: 2023/12/20 9:59
 * @description:
 */
class SeaStarClientImpl: SeaStarClient {

    var app: Application? = null

    override fun SSC_Init(application: Application) {
        app = application
        EnvArgument.instance.app = application
        MMKV.initialize(application)
    }

    override fun SSC_StartHomeActivity(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}