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
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.core.content.res.ResourcesCompat
import com.freewind.seastarvideo.R

/**
 * @author: wiatt
 * @date: 2023/12/22 18:29
 * @description: 带清除按钮的输入框
 */
class ClearEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    StateEditText(context, attrs, defStyleAttr), OnFocusChangeListener {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private var mClearDrawable: Drawable?
    private var hasFocus: Boolean = false

    init {
        // 设置可以获得焦点
        isFocusable = true
        // 设置可以在触控模式下获得焦点
        isFocusableInTouchMode = true

        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = ResourcesCompat.getDrawable(resources, R.mipmap.icon_clean, null)
        }
        mClearDrawable!!.setBounds(0, 0,
            mClearDrawable!!.intrinsicWidth,
            mClearDrawable!!.intrinsicHeight)

        setClearIconVisible(false)
        onFocusChangeListener = this
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (this@ClearEditText.hasFocus) {
                    setClearIconVisible(this@ClearEditText.text?.isNotEmpty() == true)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP
            && this.compoundDrawables[2] != null
            && event.x > (this.width - this.totalPaddingRight).toFloat()
            && event.x < (this.width - this.paddingRight).toFloat()) {
            this.editableText.clear()
        }

        return super.onTouchEvent(event)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            setClearIconVisible(this.text?.isNotEmpty() == true)
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 清除按钮是否显示
     */
    private fun setClearIconVisible(visible: Boolean) {

        val drawable: Drawable? = if (visible) {
            mClearDrawable
        } else {
            null
        }
        setCompoundDrawables(
            compoundDrawables[0], compoundDrawables[1],
            drawable, compoundDrawables[3]
        )
    }

}