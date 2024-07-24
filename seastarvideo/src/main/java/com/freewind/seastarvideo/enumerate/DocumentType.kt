/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.enumerate

/**
 * @author: wiatt
 * @date: 2024/7/24 10:34
 * @description:
 */
enum class DocumentType(val value: String) {
    // 关于我们
    AboutUs("about-us"),
    // 免责声明
    Disclaimer("disclaimer"),
    // 	用户协议
    UserAgreement("user-agreement"),
    // 隐私政策
    PrivacyPolicy("privacy-policy"),
    // 个人信息收集清单
    CheckList("check-list"),
    // 第三方信息共享清单
    ShareList("share-list")
}