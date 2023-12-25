/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.ui

import android.content.Context
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.utils.WeakHandler

/**
 * @author: wiatt
 * @date: 2023/12/21 15:01
 * @description: 带倒计时功能的验证码获取按钮
 */

class AuthCodeStateTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    StateTextView(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    // 原始文本
    var originalText: String = ""
    // 倒数时长，单位：s
    var countDownTime: Int = 60
    // 倒数时提示文本，一定要包含一个 “%s” 用以承接时长数字
    var countDownTip: String = ""

    private var countdownHandler: CountdownHandler

    init {
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.AuthCodeStateTextView)
        countDownTime = a.getInt(R.styleable.AuthCodeStateTextView_countDownTime, 60)
        countDownTip = a.getString(R.styleable.AuthCodeStateTextView_countDownTip).toString()
        originalText = text.toString()
        countdownHandler = CountdownHandler(Looper.getMainLooper(), this)
        a.recycle()
    }

    /**
     * 开始倒数
     */
    fun startCountDown() {
        countdownHandler.sendHandlerMessage(
            COUNT_DOWN_MSG, null, countDownTime, -1, 0)
    }

    /**
     * 停止倒数
     */
    fun stopCountDown() {
        countdownHandler.removeCallbacksAndMessages(null)
        isEnabled = true
        text = originalText.ifEmpty {
            resources.getString(R.string.get_code)
        }
    }

    /**
     * 设置防止重复点击事件
     * @param views 需要设置点击事件的view
     * @param interval 时间间隔 默认0.3秒
     * @param onClick 点击触发的方法
     */
    private fun setOnClickNoRepeat(vararg views: View?, interval: Long = 300, onClick: (View) -> Unit) {
        views.forEach {
            it?.clickNoRepeat(interval = interval) { view ->
                onClick.invoke(view)
            }
        }
    }

    private var lastClickTime = 0L
    /**
     * 防止重复点击事件 默认0.3秒内不可重复点击
     * @param interval 时间间隔 默认0.3秒
     * @param action 执行方法
     */
    private fun View.clickNoRepeat(interval: Long = 300, action: (view: View) -> Unit) {
        setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
                return@setOnClickListener
            }
            lastClickTime = currentTime
            action.invoke(it)
        }
    }

    class CountdownHandler(looper: Looper, owner: AuthCodeStateTextView):
        WeakHandler<AuthCodeStateTextView>(looper, owner) {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                COUNT_DOWN_MSG -> {
                    val time = msg.arg1
                    if (time > 0) {
                        getOwner()?.let {
                            it.isEnabled = false
                            it.text = if (it.countDownTip.isNotEmpty() && it.countDownTip.contains("%s")) {
                                String.format(it.countDownTip, time.toString())
                            } else {
                                String.format(it.resources.getString(
                                    R.string.count_down_tip), time.toString())
                            }
                            it.countdownHandler.sendHandlerMessage(
                                COUNT_DOWN_MSG, null, time-1, -1, 1000)
                        }
                    } else {
                        getOwner()?.let {
                            it.isEnabled = true
                            it.text = it.originalText.ifEmpty {
                                it.resources.getString(R.string.get_code)
                            }
                        }
                    }
                }
            }

        }
    }

    companion object {
        private const val COUNT_DOWN_MSG = 1001
    }
}