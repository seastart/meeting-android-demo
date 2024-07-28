/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.authorize.register

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.activity.RegisterActivity
import com.freewind.seastarvideo.authorize.login.LoginFragment
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentRegisterInfoBinding
import com.freewind.seastarvideo.ui.StateEditText
import com.freewind.seastarvideo.utils.KvUtil
import com.freewind.seastarvideo.utils.LogUtil
import com.freewind.seastarvideo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus
import java.util.regex.Pattern

/**
 * @author: wiatt
 * @date: 2023/12/21 16:30
 * @description: 注册信息页
 */
class RegisterInfoFragment : BaseFragment() {

    private var _binding: FragmentRegisterInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterInfoBinding.inflate(inflater, container, false)
        val rootView = binding.root
        customProtocolTipTvContent()
        initListener()

        return rootView
    }

    private fun initListener() {
        setOnClickNoRepeat(binding.getCodeStv, binding.pwdIv,
            binding.pwdVisibleIv, binding.registerStv) {
            when(it) {
                binding.getCodeStv -> {
                    // 此处要先做获取验证码的请求或者获取图形验证码的请求，然后再开始倒计时
                    val phoneNumber = binding.phoneNumSet.text.toString().trim()
                    if (phoneNumber.isEmpty()) {
                        ToastUtil.showShortToast("手机号不可为空")
                        return@setOnClickNoRepeat
                    }
                    viewModel.getSmsCode(phoneNumber)
                }
                binding.pwdIv -> {
                    togglePwdVisible(binding.pwdIv, binding.pwdSet)
                }
                binding.pwdVisibleIv -> {
                    togglePwdVisible(binding.pwdVisibleIv, binding.pwdVerifySet)
                }
                binding.registerStv -> {
                    // 先进行一次注册操作，返回成功后再跳转到昵称设置页面
                    hideSoftInput()
                    val phoneNumber = binding.phoneNumSet.text.toString().trim()
                    if (phoneNumber.isEmpty()) {
                        ToastUtil.showShortToast("手机号不可为空")
                        return@setOnClickNoRepeat
                    }
                    val code = binding.phoneNumSet.text.toString().trim()
                    if (code.isEmpty()) {
                        ToastUtil.showShortToast("手机验证码不可为空")
                        return@setOnClickNoRepeat
                    }
                    val pwd = binding.pwdSet.text.toString().trim()
                    if (pwd.isEmpty()) {
                        ToastUtil.showShortToast("密码不可为空")
                        return@setOnClickNoRepeat
                    }
                    val pwdVerify = binding.pwdVerifySet.text.toString().trim()
                    if (pwdVerify.isEmpty()) {
                        ToastUtil.showShortToast("请确认密码")
                        return@setOnClickNoRepeat
                    }
                    if (pwd != pwdVerify) {
                        ToastUtil.showShortToast("密码输入不一致")
                        return@setOnClickNoRepeat
                    }
                    if (!binding.selectCb.isChecked) {
                        ToastUtil.showShortToast("请阅读并同意用户协议和隐私协议")
                        return@setOnClickNoRepeat
                    }
                    viewModel.register(phoneNumber, code, pwd)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
        viewModel.getSmsCodeResult.observe(this) { uiResponse ->
            uiResponse?.let { result ->
                if (result.isSuccess) {
                    ToastUtil.showShortToast("验证码发送成功")
                    binding.getCodeStv.startCountDown()
                } else {
                    ToastUtil.showShortToast("验证码发送失败")
                }
            }
        }
        viewModel.registerResult.observe(this) { uiResponse ->
            uiResponse?.let { result ->
                if (result.isSuccess) {
                    val pwd = binding.pwdSet.text.toString().trim()
                    KvUtil.encode(KvUtil.USER_INFO_PWD, pwd)
                    EventBus.getDefault().post(RegisterEventBean.UpdatePageEvent(RegisterActivity.REGISTER_NICKNAME, true))
                } else {
                    ToastUtil.showShortToast("注册失败")
                }
            }
        }
    }

    /**
     * 自定义 “协议提示”文字的颜色和点击事件
     */
    private fun customProtocolTipTvContent() {
        val strComplete = getString(R.string.confirm_clause)
        val strUserAgreement = getString(R.string.user_agreement)
        val strPrivacy = getString(R.string.privacy_policy)
        val ss = SpannableString(strComplete)
        val sPattern: Pattern = Pattern.compile(strUserAgreement)
        val sMatcher = sPattern.matcher(strComplete)
        if (sMatcher.find()) {
            val what: ClickableSpan = object : ClickableSpan() {

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = if (Build.VERSION.SDK_INT >= 23) {
                        resources.getColor(R.color.dominant_tone_blue, null)
                    } else {
                        resources.getColor(R.color.dominant_tone_blue)
                    }
                }

                override fun onClick(widget: View) {
                    LogUtil.i("点击《用户协议》", LoginFragment.TAG)

                }
            }
            ss.setSpan(what, sMatcher.start(), sMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val pPatterm: Pattern = Pattern.compile(strPrivacy)
        val pMatcher = pPatterm.matcher(strComplete)
        if (pMatcher.find()) {
            val what: ClickableSpan = object : ClickableSpan() {

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = if (Build.VERSION.SDK_INT >= 23) {
                        resources.getColor(R.color.dominant_tone_blue, null)
                    } else {
                        resources.getColor(R.color.dominant_tone_blue)
                    }
                }

                override fun onClick(widget: View) {
                    LogUtil.i("点击《隐私协议》", LoginFragment.TAG)

                }
            }
            ss.setSpan(what, pMatcher.start(), pMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        // 选中时的背景色设置为透明
        binding.protocolTipTv.highlightColor = Color.TRANSPARENT
        binding.protocolTipTv.text = ss
        // 必须加上这句，否则点击失效
        binding.protocolTipTv.movementMethod = LinkMovementMethod.getInstance()
    }

    /**
     * 切换密码可见性
     */
    private fun togglePwdVisible(pwdVisibleIv: ImageView, editText: StateEditText) {
        if (pwdVisibleIv.isSelected) {
            pwdVisibleIv.isSelected = false
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            pwdVisibleIv.isSelected = true
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        // 光标移动到最后
        val textLen: Int = editText.text.toString().length
        editText.setSelection(textLen, textLen)
    }

    companion object {
        val TAG: String = RegisterInfoFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = RegisterInfoFragment()
    }
}