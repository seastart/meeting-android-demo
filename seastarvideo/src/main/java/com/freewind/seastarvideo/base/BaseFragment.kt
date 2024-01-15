/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.base

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.freewind.seastarvideo.EnvArgument
import com.freewind.seastarvideo.ui.StatusBarManager

abstract class BaseFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        StatusBarManager.instance.setStatusBarTextColor(requireActivity(), Color.TRANSPARENT)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 设置防止重复点击事件
     * @param views 需要设置点击事件的view
     * @param interval 时间间隔 默认0.3秒
     * @param onClick 点击触发的方法
     */
    fun setOnClickNoRepeat(vararg views: View?, interval: Long = 300, onClick: (View) -> Unit) {
        views.forEach {
            it?.clickNoRepeat(interval = interval) { view ->
                onClick.invoke(view)
            }
        }
    }

    var lastClickTime = 0L

    /**
     * 防止重复点击事件 默认0.3秒内不可重复点击
     * @param interval 时间间隔 默认0.3秒
     * @param action 执行方法
     */
    private fun View.clickNoRepeat(interval: Long = 300, action: (view: View) -> Unit) {
        setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
                return@setOnClickListener
            }
            lastClickTime = currentTime
            action.invoke(it)
        }
    }

    /**
     * 显示软键盘
     */
    fun showSoftInput() {
        EnvArgument.instance.app?.let {
            val inputManager = it.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    // 隐藏软键盘
    fun hideSoftInput() {
        EnvArgument.instance.app?.let {
            val inputManager = it.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    // 隐藏软键盘
    fun hideSoftInput(view: View) {
        EnvArgument.instance.app?.let {
            val inputManager = it.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * fragment的扩展函数，用于操作fragment
     */
    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    /**
     * 添加fragment
     */
    fun addFragment(fragment: Fragment, frameId: Int){
        childFragmentManager.inTransaction { add(frameId, fragment) }
    }

    /**
     * 移除fragment
     */
    fun removeFragment(fragment: Fragment){
        childFragmentManager.inTransaction { remove(fragment) }
    }

    /**
     * 展示fragment
     */
    fun showFragment(fragment: Fragment) {
        childFragmentManager.inTransaction{show(fragment)}
    }

    /**
     * 隐藏fragment
     */
    fun hideFragment(fragment: Fragment){
        childFragmentManager.inTransaction { hide(fragment) }
    }

    /**
     * 连接fragment
     */
    fun attachFragment(fragment: Fragment) {
        childFragmentManager.inTransaction{attach(fragment)}
    }

    /**
     * 分离fragment
     */
    fun detachFragment(fragment: Fragment) {
        childFragmentManager.inTransaction{detach(fragment)}
    }
}