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
 * @description: 自定义控件，以键值对形式出现的输入框
 */
@SuppressLint("ClickableViewAccessibility")
class CustomKeyValueEdit(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private val keyTextTv: TextView
    private val valueEditSet: StateEditText

    var keyContent: String? = ""
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                keyTextTv.text = ""
            } else {
                keyTextTv.text = value
            }
        }
    var editHintContent: String? = ""
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                valueEditSet.hint = ""
            } else {
                valueEditSet.hint = value
            }
        }
    var editContent: String? = ""
        set(value) {
            field = value
            LogUtil.i("CustomKeyValueEdit, value = $value")
            if (!value.isNullOrEmpty()) {
                valueEditSet.setText(value)
            }
        }
        get() {
            field = valueEditSet.text.toString()
            return field
        }
    var endEditIcon: Int = 0
        set(value) {
            field = value
            setEndEditIconVisible(value)
        }

    // 点击监听回调
    var listener: OnCustomKeyValueEditListener? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_key_value_edit, this, true)
        keyTextTv = rootView.findViewById(R.id.keyTextTv)
        valueEditSet = rootView.findViewById(R.id.valueEditSet)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomKeyValueEdit)
        keyContent = a.getString(R.styleable.CustomKeyValueEdit_keyContent).toString().takeIf {
            it != "null"
        }
        editHintContent = a.getString(R.styleable.CustomKeyValueEdit_editHintContent).toString().takeIf {
            it != "null"
        }
        editContent = a.getString(R.styleable.CustomKeyValueEdit_editContent).toString().takeIf {
            it != "null"
        }
        endEditIcon = a.getResourceId(R.styleable.CustomKeyValueEdit_endEditIcon, 0)

        valueEditSet.setOnTouchListener { v, event ->
            val tv = v as StateEditText
            if (event.action == MotionEvent.ACTION_UP
                && tv.compoundDrawables[2] != null
                && event.x > (tv.width - tv.totalPaddingRight).toFloat()
                && event.x < (tv.width - tv.paddingRight).toFloat()) {

                listener?.onEndEditIconClick(valueEditSet)
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
    private fun setEndEditIconVisible(resId: Int) {
        if (resId == 0) {
            valueEditSet.setCompoundDrawables(valueEditSet.compoundDrawables[0], valueEditSet.compoundDrawables[1],
                null, valueEditSet.compoundDrawables[3])
        } else {
            val drawable: Drawable? = ResourcesCompat.getDrawable(resources, resId, null)
            drawable?.let {
                it.setBounds(0, 0,
                    it.intrinsicWidth,
                    it.intrinsicHeight)

                valueEditSet.setCompoundDrawables(valueEditSet.compoundDrawables[0], valueEditSet.compoundDrawables[1],
                    it, valueEditSet.compoundDrawables[3])
            }
        }
    }

    /**
     * 监听事件
     */
    interface OnCustomKeyValueEditListener {
        // 点击尾部图标，直接返回控件，方便外部执行特殊操作
        fun onEndEditIconClick(editText: StateEditText)
    }
}