/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment


/**
 * @author wiatt
 * @description: 模块实体类
 */
data class ModuleInfo(val name: String, val navigationIc: Drawable, val fragment: Fragment)
