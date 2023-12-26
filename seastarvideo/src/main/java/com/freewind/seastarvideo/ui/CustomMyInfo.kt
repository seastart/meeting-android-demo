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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @date: 2023/12/25 16:55
 * @description: 自定义我的信息
 */
class CustomMyInfo(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null, 0)

    private val avatarIv: ImageView
    private val nickNameTv: TextView
    private val idTv: TextView

    var avatarIcon: Int = 0
        set(value) {
            field = value
            if (value != 0) {
                avatarIv.setImageResource(avatarIcon)
            }
        }
    var nickNameContent: String? = ""
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                nickNameTv.text = ""
            } else {
                nickNameTv.text = value
            }
        }
    var idContent: String? = ""
        set(value) {
            field = value
            val tmpValue = value ?: ""
            val id = String.format(resources.getString(R.string.id_format), tmpValue)
            LogUtil.i("idContent = $tmpValue, id = $id")
            idTv.text = id
        }

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.view_my_info, this, true)
        avatarIv = rootView.findViewById(R.id.avatarIv)
        nickNameTv = rootView.findViewById(R.id.nickNameTv)
        idTv = rootView.findViewById(R.id.idTv)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomMyInfo)
        avatarIcon = a.getResourceId(R.styleable.CustomMyInfo_avatarIcon, 0)
        nickNameContent = a.getString(R.styleable.CustomMyInfo_nickNameContent).toString().takeIf {
            it != "null"
        }
        idContent = a.getString(R.styleable.CustomMyInfo_idContent).toString().takeIf {
            it != "null"
        }
        a.recycle()
    }
}