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
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.meeting.room.ReportTagAdapter
import com.freewind.seastarvideo.meeting.room.ReportTagBean


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

      val FLAG_SHARE_TYPE_SCREEN = "shareType_screen"
      val FLAG_SHARE_TYPE_WHITE_BOARD = "shareType_whiteBoard"
   }

   /**
    * 展示麦克风权限请求弹框
    */
   fun showRequestMicPermissionDialog(context: Context, handler: () -> Unit) {
      val dialog = initView(context, R.layout.dialog_request_mic_permission)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            cancelStv -> {
               dismiss(dialog)
            }
            goStv -> {
               handler.invoke()
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示屏幕共享选择框
    */
   fun showShareScreenDialog(context: Context, handler: (shareType: String) -> Unit) {
      val dialog = initView(context, R.layout.dialog_share_screen_type, Gravity.BOTTOM)
      val shareScreenStv: StateTextView = dialog.findViewById(R.id.shareScreenStv)
      val whiteBoardStv: StateTextView = dialog.findViewById(R.id.whiteBoardStv)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      setOnClickNoRepeat(shareScreenStv, whiteBoardStv, cancelStv) {
         when(it) {
            shareScreenStv -> {
               handler.invoke(FLAG_SHARE_TYPE_SCREEN)
               dismiss(dialog)
            }
            whiteBoardStv -> {
               handler.invoke(FLAG_SHARE_TYPE_WHITE_BOARD)
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示主持人请求解除静音弹框
    */
   fun showRequestOpenMicDialog(context: Context, content: String, handler: () -> Unit) {
      val dialog = initView(context, R.layout.dialog_request_open_mic)
      val contentTv: TextView = dialog.findViewById(R.id.titleTv)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      contentTv.text = content
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke()
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示主持人请求开启视频弹框
    */
   fun showRequestOpenCameraDialog(context: Context, content: String, handler: () -> Unit) {
      val dialog = initView(context, R.layout.dialog_request_open_camera)
      val contentTv: TextView = dialog.findViewById(R.id.titleTv)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      contentTv.text = content
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke()
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示离开房间弹框
    */
   fun showLeaveRoomDialog(context: Context, handler: () -> Unit) {
      val dialog = initView(context, R.layout.dialog_leave_room)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke()
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示移除成员弹框
    */
   fun showRemoveMemberDialog(context: Context, content: String, handler: () -> Unit) {
      val dialog = initView(context, R.layout.dialog_remove_member)
      val contentTv: TextView = dialog.findViewById(R.id.titleTv)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      contentTv.text = content
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke()
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示举报弹框
    */
   fun showReportDialog(context: Context, reportTags: ArrayList<ReportTagBean>, handler: (reportTypes: ArrayList<ReportTagBean>, reportContent: String) -> Unit) {
      val dialog = initView(context, R.layout.dialog_report, Gravity.BOTTOM)
      val exitIv: ImageView = dialog.findViewById(R.id.exitIv)
      val tagRv: RecyclerView = dialog.findViewById(R.id.tagRv)
      val contentEt: EditText = dialog.findViewById(R.id.contentEt)
      val reportSureStv: StateTextView = dialog.findViewById(R.id.reportSureStv)

      val adapter = ReportTagAdapter(reportTags)
      dealWithReportTagView(context, tagRv, adapter)

      setOnClickNoRepeat(exitIv, reportSureStv) {
         when(it) {
            exitIv -> {
               dismiss(dialog)
            }
            reportSureStv -> {
               val selectedTags = arrayListOf<ReportTagBean>()
               adapter.items.forEach { reportTag ->
                  if (reportTag.isSelected) {
                     selectedTags.add(reportTag)
                  }
               }
               val content = contentEt.text.toString()
               if (selectedTags.isEmpty()) {
                  Toast.makeText(context, context.resources.getText(R.string.report_lack_tag), Toast.LENGTH_SHORT).show()
               } else {
                  handler.invoke(selectedTags, content)
                  dismiss(dialog)
               }
            }
         }
      }

      show(dialog)
   }

   /**
    * 处理举报弹框中的举报标签列表
    */
   private fun dealWithReportTagView(context: Context, tagRv: RecyclerView, adapter: ReportTagAdapter) {
      val layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
      tagRv.layoutManager = layoutManager
      tagRv.addItemDecoration(object : ItemDecoration() {
         override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
         ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            var left = 0
            var right = 0
            var top = 0
            var bottom = 0
            when(position % 3) {
               0 -> {
                  right = context.resources.getDimension(R.dimen.dp_4).toInt()
               }
               1 -> {
                  left = context.resources.getDimension(R.dimen.dp_4).toInt()
                  right = context.resources.getDimension(R.dimen.dp_4).toInt()
               }
               2 -> {
                  left = context.resources.getDimension(R.dimen.dp_4).toInt()
               }
            }
            if (position != 0 && position != 1 && position != 2) {
               top = context.resources.getDimension(R.dimen.dp_8).toInt()
            }
            bottom = 0
            outRect.set(left, top, right, bottom)
         }
      })

      tagRv.adapter = adapter
      adapter.setOnItemClickListener { itemAdapter, view, position ->
         val curSelectState = !view.isSelected
         adapter.getItem(position)?.isSelected = curSelectState
         view.isSelected = curSelectState
      }
   }

   /**
    * 展示全员静音弹框
    */
   fun showAllMemberCloseMicDialog(context: Context, enableSelfOpen: Boolean,
                                   handler: (enableSelfOpen: Boolean) -> Unit) {
      val dialog = initView(context, R.layout.dialog_all_member_close_mic)
      val openMicCb: CheckBox = dialog.findViewById(R.id.openMicCb)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      openMicCb.isChecked = enableSelfOpen
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke(openMicCb.isChecked)
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示解除全体静音弹框
    */
   fun showAllMemberOpenMicDialog(context: Context, handler: () -> Unit) {
      val dialog = initView(context, R.layout.dialog_all_member_open_mic)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke()
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示全员禁画弹框
    */
   fun showAllMemberCloseCameraDialog(context: Context, enableSelfOpen: Boolean,
                                      handler: (enableSelfOpen: Boolean) -> Unit) {
      val dialog = initView(context, R.layout.dialog_all_member_close_camera)
      val openCameraCb: CheckBox = dialog.findViewById(R.id.openCameraCb)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      openCameraCb.isChecked = enableSelfOpen
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke(openCameraCb.isChecked)
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
   }

   /**
    * 展示解除全体禁画弹框
    */
   fun showAllMemberOpenCameraDialog(context: Context, handler: () -> Unit) {
      val dialog = initView(context, R.layout.dialog_all_member_open_camera)
      val cancelStv: StateTextView = dialog.findViewById(R.id.cancelStv)
      val goStv: StateTextView = dialog.findViewById(R.id.goStv)
      setOnClickNoRepeat(cancelStv, goStv) {
         when(it) {
            goStv -> {
               handler.invoke()
               dismiss(dialog)
            }
            cancelStv -> {
               dismiss(dialog)
            }
         }
      }

      show(dialog)
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