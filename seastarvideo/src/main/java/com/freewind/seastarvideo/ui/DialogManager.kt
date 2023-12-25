/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.ui

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.freewind.seastarvideo.R


/**
 * @author wiatt
 * @description: 自定义 dailog 管理类，其中包含一个自定义 dialog 的内部类
 */
class DialogManager {

   companion object {
      val TAG = DialogManager::class.java.simpleName
      val instance: DialogManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
         DialogManager()
      }
   }

   /**
    * 初始化 dialog,
    * 传入 上下文 和 布局
    * dialog 的位置默认居中
    */
   fun initView(context: Context, layout: Int): DialogView {
      return DialogView(context, layout, R.style.Theme_Custom_Dialog, Gravity.CENTER)
   }

   /**
    * 初始化 dialog,
    * 传入 上下文、布局、位置
    */
   fun initView(context: Context, layout: Int, gravity: Int): DialogView {
      return DialogView(context, layout, R.style.Theme_Custom_Dialog, gravity)
   }

   /**
    * 初始化 dialog,
    * 传入 上下文、布局、位置、宽、高
    * 占满父控件： WindowManager.LayoutParams.MATCH_PARENT，即 -1
    * 自适应控件： WindowManager.LayoutParams.WRAP_CONTENT，即 -2
    */
   fun initView(context: Context, layout: Int, gravity: Int, width: Int, height: Int): DialogView {
      return DialogView(context, layout, width, height, R.style.Theme_Custom_Dialog, gravity)
   }

   /**
    * 显示 dialog
    */
   fun show(dialog: DialogView) {
      if (!dialog.isShowing) {
         dialog.show()
      }
   }

   /**
    * 隐藏 dialog
    */
   fun dismiss(dialog: DialogView) {
      if (dialog.isShowing) {
         dialog.dismiss()
      }
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
    * 自定义 dialog
    */
   class DialogView(context: Context, layout: Int, width: Int, height: Int, style: Int, gravity: Int):
      Dialog(context, style) {

      constructor(context: Context, layout: Int, style: Int, gravity: Int):
              this(context, layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, style, gravity)

      init {
         setContentView(layout)
         val layoutParams: WindowManager.LayoutParams? = window?.attributes
         if (width == 0) {
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
         } else {
            layoutParams?.width = width
         }
         if (height == 0) {
            layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
         } else {
            layoutParams?.height = height
         }
         layoutParams?.gravity = gravity
         window?.attributes = layoutParams
      }
   }
}