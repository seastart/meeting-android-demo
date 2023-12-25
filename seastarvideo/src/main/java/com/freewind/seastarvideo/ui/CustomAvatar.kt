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
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.freewind.seastarvideo.R

/**
 * @author: wiatt
 * @date: 2023/12/22 14:53
 * @description: 带选中标识的头像控件
 */
class CustomAvatar(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private var avatarIv: ImageView
    private var selectedIv: ImageView

    private var selected: Boolean = false
    var avatarIcon: Int = 0
        set(value) {
            field = value
            if (avatarIcon != 0) {
                avatarIv.setImageResource(avatarIcon)
            }
        }
    var selectedIcon: Int = 0
        set(value) {
            field = value
            if (selectedIcon != 0) {
                avatarIv.setImageResource(selectedIcon)
            }
        }
    var selectedIconSize: Int = 0
        set(value) {
            field = value
            val params = selectedIv.layoutParams
            params.width = selectedIconSize
            params.height = selectedIconSize
            selectedIv.layoutParams = params
        }

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_avatar, this, true)
        avatarIv = rootView.findViewById(R.id.avatarIv)
        selectedIv = rootView.findViewById(R.id.selectedIv)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomAvatar)
        avatarIcon = a.getResourceId(R.styleable.CustomAvatar_avatarIcon, 0)
        selectedIcon = a.getResourceId(R.styleable.CustomAvatar_selectedIcon, 0)
        selectedIconSize = a.getDimension(R.styleable.CustomAvatar_selectedIconSize,
            resources.getDimension(R.dimen.dp_16)).toInt()
        selected = a.getBoolean(R.styleable.CustomAvatar_isSelected, false)

        if (avatarIcon != 0) {
            avatarIv.setImageResource(avatarIcon)
        }
        if (selectedIcon != 0) {
            selectedIv.setImageResource(selectedIcon)
        }
        val params = selectedIv.layoutParams
        params.width = selectedIconSize
        params.height = selectedIconSize
        selectedIv.layoutParams = params
        isSelected = selected
        updateSelectState(isSelected)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        updateSelectState(selected)
    }

    private fun updateSelectState(selectState: Boolean) {
        if (selectState) {
            selectedIv.visibility = VISIBLE
        } else {
            selectedIv.visibility = GONE
        }
    }
}