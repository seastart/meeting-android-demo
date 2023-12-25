/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.authorize.register

/**
 * @author: wiatt
 * @date: 2023/12/21 20:57
 * @description:
 */
class RegisterEventBean {

    /**
     * 跳转到指定 fragment 页面
     * @param pageType: 指定将要跳转的fragment
     * RegisterActivity.REGISTER_INFO
     * RegisterActivity.REGISTER_NICKNAME
     * @param canBack: 按返回键是否能够回到当前页
     */
    class UpdatePageEvent(var pageType: String, var canBack: Boolean)

}