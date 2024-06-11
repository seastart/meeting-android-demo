package com.freewind.seastarvideo.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber

/**
 * @author wiatt
 * @description: log工具类
 */
object LogUtil{
    private const val TAG = "SeaStarVideo"

    @JvmStatic
    fun initDebug() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
            .methodCount(0) // (Optional) How many method line to show. Default 2
            .methodOffset(5) // (Optional) Hides internal method calls up to offset. Default 5
            .tag(TAG) // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return BuildConfig.DEBUG
                return true
            }
        })

        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                Logger.log(priority, tag, message, t)
            }
        })
    }

    @JvmStatic
    fun initDisk() {
        val formatStrategy: FormatStrategy = CsvFormatStrategy.newBuilder()
            .tag("MeMine")
            .build()

        Logger.addLogAdapter(object : DiskLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return BuildConfig.DEBUG
                return true
            }
        })
        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                Logger.log(priority, tag, message, t)
            }
        })
    }

    @JvmStatic
    fun d(msg: String, tag: String = TAG) {
        Timber.tag(tag).d(msg)
    }
    @JvmStatic
    fun i(msg: String, tag: String = TAG) {
        Timber.tag(tag).i( msg)
    }
    @JvmStatic
    fun e(msg: String, tag: String = TAG) {
        Timber.tag(tag).e(msg)
    }
    @JvmStatic
    fun v(msg: String, tag: String = TAG) {
        Timber.tag(tag).v( msg)
    }
    @JvmStatic
    fun w(msg: String, tag: String = TAG) {
        Timber.tag(tag).w(msg)
    }
}

open class CrashReportingTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    }
}