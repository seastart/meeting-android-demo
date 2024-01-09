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
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.freewind.seastarvideo.R

/**
 * @author: wiatt
 * @date: 2024/1/4 15:40
 * @description:
 */
class SelectTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    AppCompatTextView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    // 未选中状态下文本内容
    var unselectedContent: String? = ""
    // 选中状态下文本内容
    var selectedContent: String? = ""

    init {
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.SelectTextView)
        unselectedContent = a.getString(R.styleable.SelectTextView_unselectedContent).toString().takeIf {
            it != "null"
        }
        selectedContent = a.getString(R.styleable.SelectTextView_selectedContent).toString().takeIf {
            it != "null"
        }

        this.gravity = Gravity.CENTER
        text = if (isSelected) {
            selectedContent
        } else {
            unselectedContent
        }
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        text = if (isSelected) {
            selectedContent
        } else {
            unselectedContent
        }
    }
}