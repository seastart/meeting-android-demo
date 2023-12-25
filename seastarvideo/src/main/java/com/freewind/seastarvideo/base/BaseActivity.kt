/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.freewind.seastarvideo.ui.StatusBarManager


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarManager.instance.immersiveStatusBar(
            this, Color.TRANSPARENT
        )
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
     * @param interval: 时间间隔 默认0.3秒
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

    // 隐藏软键盘
    /**
     * 隐藏软键盘
     * @param view: 获取软键盘输入内容的控件，一般为一个 EditText
     */
    fun hideSoftInput(view: View) {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }



    /**
     * fragment的扩展函数，用于操作fragment
     */
    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    /**
     * 添加fragment
     * FragmentActivity.addFragment 表示将addFragment是作为FragmentActivity的扩展函数存在
     * 这样addFragment内部就能直接访问到FragmentActivity中的supportFragmentManager
     */
    fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    /**
     * 移除fragment
     */
    fun FragmentActivity.removeFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { remove(fragment) }
    }

    /**
     * 展示fragment
     */
    fun FragmentActivity.showFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{show(fragment)}
    }

    /**
     * 隐藏fragment
     */
    fun FragmentActivity.hideFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { hide(fragment) }
    }

    /**
     * 连接fragment
     */
    fun FragmentActivity.attachFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{attach(fragment)}
    }

    /**
     * 分离fragment
     */
    fun FragmentActivity.detachFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{detach(fragment)}
    }
}