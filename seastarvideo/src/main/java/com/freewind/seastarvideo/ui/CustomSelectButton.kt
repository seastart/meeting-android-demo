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
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @date: 2023/12/20 18:08
 * @description: 自定义带选中状态的控件组
 */
class CustomSelectButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private var selectBtnCl: ConstraintLayout
    private var textTv: TextView
    private var flagV: View

    private var selected: Boolean = false
    // 文本内容
    var textContent: String = ""
        set(value) {
            field = value
            textTv.text = value
        }
    // 正常状态下文本颜色
    var textColorNormal: Int = 0
        set(value) {
            field = value
            if (!isSelected) {
                textTv.setTextColor(value)
            }
        }
    // 选中状态下文本颜色
    var textColorSelected: Int = 0
        set(value) {
            field = value
            if (isSelected) {
                textTv.setTextColor(value)
            }
        }
    // 背景图资源，为 selector 文件，包含选中状态和未选中状态
    var backgroundRes: Int = 0
        set(value) {
            field = value
            if (value != 0) {
                selectBtnCl.background = AppCompatResources.getDrawable(context, value)
            }
        }

    private var blackColor: Int
    private var whiteColor: Int
    private var lightBlueColor: Int

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_select_button, this, true)
        selectBtnCl = rootView.findViewById(R.id.selectBtnCl)
        textTv = rootView.findViewById(R.id.textTv)
        flagV = rootView.findViewById(R.id.flagV)

        blackColor = if (Build.VERSION.SDK_INT >= 23) {
            context.getColor(R.color.black)
        } else {
            resources.getColor(R.color.black)
        }
        whiteColor = if (Build.VERSION.SDK_INT >= 23) {
            context.getColor(R.color.white)
        } else {
            resources.getColor(R.color.white)
        }
        lightBlueColor = if (Build.VERSION.SDK_INT >= 23) {
            context.getColor(R.color.light_blue_99afe1)
        } else {
            resources.getColor(R.color.light_blue_99afe1)
        }

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSelectButton)
        selected = a.getBoolean(R.styleable.CustomSelectButton_isSelected, false)
        textContent = a.getString(R.styleable.CustomSelectButton_textContent).toString()
        textColorNormal = a.getColor(R.styleable.CustomSelectButton_textColorNormal, blackColor)
        textColorSelected = a.getColor(R.styleable.CustomSelectButton_textColorSelected, blackColor)
        backgroundRes = a.getResourceId(R.styleable.CustomSelectButton_backgroundRes, 0)

        LogUtil.i("customSelectButton_init, isSelected = $isSelected, selected = $selected")

        isSelected = selected
        if (backgroundRes != 0) {
            selectBtnCl.background = AppCompatResources.getDrawable(context, backgroundRes)
        }
        textTv.text = textContent
        updateSelectState(isSelected)
        a.recycle()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        updateSelectState(selected)
    }

    /**
     * 更新选中状态
     */
    private fun updateSelectState(selectState: Boolean) {
        selectBtnCl.isSelected = selectState
        if (selectState) {
            textTv.setTextColor(textColorSelected)
            flagV.isVisible = true
        } else {
            textTv.setTextColor(textColorNormal)
            flagV.isVisible = false
        }
        if (backgroundRes == 0) {
            if (selectState) {
                selectBtnCl.setBackgroundColor(whiteColor)
            } else {
                selectBtnCl.setBackgroundColor(lightBlueColor)
            }
        }
    }


}