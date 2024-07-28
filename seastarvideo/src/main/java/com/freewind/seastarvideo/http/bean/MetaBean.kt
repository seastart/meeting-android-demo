package com.freewind.seastarvideo.http.bean

/**
 * @author: wiatt
 * @date: 2024/4/30 17:36
 * @description: 分页信息实体类
 */
data class MetaBean(
    // 总条目数
    var totalCount: Int?,
    // 总页数
    var pageCount: Int?,
    // 当前页号，从1开始
    var currentPage: Int?,
    // 每页最大条目数
    var perPage: Int?
)
