/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @date: 2023/12/26 11:57
 * @description: 自定义控件，以键值对形式出现的文本框
 */
// valueTextTv.setOnTouchListener 会提示警告，
// 因为在 onTouch 中返回 ture ,同时又添加了 onClick 监听,这时 onClick 就不会执行了,事件被 onTouch 消化掉了。
// 所以要加个注解将这个警告移除
@SuppressLint("ClickableViewAccessibility")
class CustomKeyValueText(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private val keyTextTv: TextView
    private val valueTextTv: TextView

    var keyContent: String? = ""
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                keyTextTv.text = ""
            } else {
                keyTextTv.text = value
            }
        }
    var valueContent: String? = ""
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                valueTextTv.text = ""
            } else {
                valueTextTv.text = value
            }
        }
    var endValueTextIcon: Int = 0
        set(value) {
            field = value
            setEndValueTextIconVisible(value)
        }

    // 点击监听回调
    var listener: OnCustomKeyValueTextListener? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_key_value_text, this, true)
        keyTextTv = rootView.findViewById(R.id.keyTextTv)
        valueTextTv = rootView.findViewById(R.id.valueTextTv)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomKeyValueText)
        keyContent = a.getString(R.styleable.CustomKeyValueText_keyContent).toString().takeIf {
            it != "null"
        }
        valueContent = a.getString(R.styleable.CustomKeyValueText_valueContent).toString().takeIf {
            it != "null"
        }
        endValueTextIcon = a.getResourceId(R.styleable.CustomKeyValueText_endValueTextIcon, 0)

        valueTextTv.setOnTouchListener { v, event ->
            val tv = v as TextView
            if (event.action == MotionEvent.ACTION_UP
                && valueTextTv.compoundDrawables[2] != null
                && event.x > (tv.width - tv.totalPaddingRight).toFloat()
                && event.x < (tv.width - tv.paddingRight).toFloat()) {
                listener?.onEndValueTextIconClick(tv.text.toString())
                true
            } else {
                false
            }
        }

        a.recycle()
    }

    /**
     * 尾部图标是否显示
     */
    private fun setEndValueTextIconVisible(resId: Int) {
        LogUtil.i("setEndValueTextIconVisible, resId = $resId")
        if (resId == 0) {
            valueTextTv.setCompoundDrawables(valueTextTv.compoundDrawables[0], valueTextTv.compoundDrawables[1],
                null, valueTextTv.compoundDrawables[3])
        } else {
            val drawable: Drawable? = ResourcesCompat.getDrawable(resources, resId, null)
            drawable?.let {
                it.setBounds(0, 0,
                    it.intrinsicWidth,
                    it.intrinsicHeight)

                valueTextTv.setCompoundDrawables(valueTextTv.compoundDrawables[0], valueTextTv.compoundDrawables[1],
                    it, valueTextTv.compoundDrawables[3])
            }
        }
    }

    /**
     * 监听事件
     */
    interface OnCustomKeyValueTextListener {
        // 点击尾部图标
        fun onEndValueTextIconClick(textContent: String)
    }
}