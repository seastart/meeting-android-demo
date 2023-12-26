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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.freewind.seastarvideo.R

/**
 * @author: wiatt
 * @date: 2023/12/25 17:44
 * @description:
 */
class CustomEnterItem(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private val iconIv: ImageView
    private val desTv: TextView
    private val enterIconIv: ImageView

    var itemIcon: Int = 0
        set(value) {
            field = value
            if (value != 0) {
                iconIv.setImageResource(value)
            }
        }
    var itemDes: String? = ""
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                desTv.text = ""
            } else {
                desTv.text = value
            }
        }
    var itemEnterIcon: Int = 0
        set(value) {
            field = value
            if (value != 0) {
                enterIconIv.setImageResource(value)
                enterIconIv.visibility = View.VISIBLE
            } else {
                enterIconIv.visibility = View.GONE
            }
        }

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_enter_item, this, true)
        iconIv = rootView.findViewById(R.id.iconIv)
        desTv = rootView.findViewById(R.id.desTv)
        enterIconIv = rootView.findViewById(R.id.enterIconIv)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEnterItem)
        itemIcon = a.getResourceId(R.styleable.CustomEnterItem_itemIcon, 0)
        itemDes = a.getString(R.styleable.CustomEnterItem_itemDes).toString().takeIf {
            it != "null"
        }
        itemEnterIcon = a.getResourceId(R.styleable.CustomEnterItem_itemEnterIcon, 0)
        a.recycle()
    }
}
