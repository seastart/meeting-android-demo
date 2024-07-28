/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.http.bean

/**
 * @author: wiatt
 * @date: 2024/4/30 15:15
 * @description: 带分页信息的实体类
 */
class Data2<T>(
    // 数据
    var data: T?,
    // 分页信息
    var meta: MetaBean?
) : BaseBean()
