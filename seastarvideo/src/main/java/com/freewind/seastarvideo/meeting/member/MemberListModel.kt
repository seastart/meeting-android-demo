/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.member

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class MemberListModel(viewModelImpl: MemberListViewModel.MemberListViewModelImpl):
    BaseModel<MemberListContract.IMemberListViewModel, MemberListContract.IMemberListModel>
        (viewModelImpl) {

    override fun getContract(): MemberListContract.IMemberListModel {
        return MemberListModelImpl()
    }

    inner class MemberListModelImpl: MemberListContract.IMemberListModel {

    }
}