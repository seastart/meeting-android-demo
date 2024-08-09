/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

/**
 * @author: wiatt
 * @date: 2023/12/27 14:22
 * @description:
 */
class MeetingRoomEventBean {
    /**
     * 跳转到指定 fragment 页面
     * @param pageType: 指定将要跳转的fragment
     * AboutUsActivity.ABOUT_US_DETAIL
     * @param canBack: 按返回键是否能够回到当前页
     */
    class UpdatePageEvent(var pageType: String, var canBack: Boolean)

    /**
     * 点击返回按钮
     */
    class goBackPageEvent()

    /**
     * 结束当前 activity
     */
    class finishActivityEvent(var isPrompt: Boolean)
}