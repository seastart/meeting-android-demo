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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.freewind.seastarvideo.R

/**
 * @author: wiatt
 * @date: 2023/12/21 17:05
 * @description: 自定义顶部栏（用于复用）
 */
class CustomTopBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private var topBarCl: ConstraintLayout
    private var exitIv: ImageView
    private var titleTv: TextView

    // 标题内容
    var titleContent: String? = ""
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                titleTv.text = ""
            } else {
                titleTv.text = value
            }

        }
    // 是否显示左侧退出箭头
    var isShowExitArrow: Boolean = true
        set(value) {
            field = value
            if (value) {
                exitIv.visibility = VISIBLE
            } else {
                exitIv.visibility = GONE
            }
        }
    // 是否显示标题
    var isShowTitle: Boolean = true
        set(value) {
            field = value
            if (value) {
                titleTv.visibility = VISIBLE
            } else {
                titleTv.visibility = GONE
            }
        }
    // 点击监听回调
    var listener: OnTopBarListener? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_top_bar, this, true)
        topBarCl = rootView.findViewById(R.id.topBarCl)
        exitIv = rootView.findViewById(R.id.exitIv)
        titleTv = rootView.findViewById(R.id.desTv)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTopBar)
        titleContent = a.getString(R.styleable.CustomTopBar_titleContent).toString().takeIf {
            it != "null"
        }
        isShowExitArrow = a.getBoolean(R.styleable.CustomTopBar_isShowExitArrow, true)
        isShowTitle = a.getBoolean(R.styleable.CustomTopBar_isShowTitle, true)

        setOnClickNoRepeat(titleTv, exitIv) {
            when(it) {
                titleTv -> {
                    listener?.onClickTitle()
                }
                exitIv -> {
                    listener?.onClickExitArrow()
                }
            }
        }

        a.recycle()
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

    var lastClickTime = 0L
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

    /**
     * 监听事件
     */
    interface OnTopBarListener {
        // 点击退出箭头
        fun onClickExitArrow()
        // 点击标题
        fun onClickTitle()
    }
}