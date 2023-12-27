package com.freewind.seastarvideo.authorize.login

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
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.activity.RegisterActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentLoginBinding
import com.freewind.seastarvideo.utils.LogUtil
import java.util.regex.Pattern

/**
 * @author: wiatt
 * @date: 2023/12/20 11:19
 * @description: 登录页
 */
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    // 标识：当前的登录方式。密码登录：0； 验证码登录：1
    private var curLoginType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val rootView = binding.root

        customProtocolTipTvContent()
        setOnClickNoRepeat(binding.pwdLoginCsb, binding.codeLoginCsb,
            binding.pwdLoginInclude.pwdVisibleIv, binding.codeLoginInclude.getCodeStv,
            binding.loginStv, binding.registerStv) {
            when(it) {
                binding.pwdLoginCsb -> {
                    selectLoginWithPwd()
                }
                binding.codeLoginCsb -> {
                    selectLoginWithCode()
                }
                binding.pwdLoginInclude.pwdVisibleIv -> {
                    togglePwdVisible()
                }
                binding.codeLoginInclude.getCodeStv -> {
                    // 此处要先做获取验证码的请求或者获取图形验证码的请求，然后再开始倒计时
                }
                binding.loginStv -> {
                    // 请求登录接口
                }
                binding.registerStv -> {
                    RegisterActivity.startRegisterInfoPage(requireContext())
                }
            }
        }
        return rootView
    }

    /**
     * 选择使用密码登录
     */
    private fun selectLoginWithPwd() {
        curLoginType = 0

        binding.pwdLoginCsb.isSelected = true
        binding.codeLoginCsb.isSelected = false

        binding.codeLoginInclude.codePhoneSet.editableText.clear()
        binding.codeLoginInclude.codeSet.editableText.clear()
        binding.codeLoginInclude.root.visibility = View.GONE
        binding.pwdLoginInclude.root.visibility = View.VISIBLE

        hideSoftInput()
    }

    /**
     * 选择使用验证码登录
     */
    private fun selectLoginWithCode() {
        curLoginType = 1

        binding.pwdLoginCsb.isSelected = false
        binding.codeLoginCsb.isSelected = true

        binding.pwdLoginInclude.pwdPhoneSet.editableText.clear()
        binding.pwdLoginInclude.pwdSet.editableText.clear()
        binding.pwdLoginInclude.pwdVisibleIv.isSelected = false
        binding.pwdLoginInclude.root.visibility = View.GONE
        binding.codeLoginInclude.root.visibility = View.VISIBLE

        hideSoftInput()
    }

    /**
     * 切换密码可见性
     */
    private fun togglePwdVisible() {
        val pwdVisibleIv = binding.pwdLoginInclude.pwdVisibleIv
        if (pwdVisibleIv.isSelected) {
            pwdVisibleIv.isSelected = false
            binding.pwdLoginInclude.pwdSet.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            pwdVisibleIv.isSelected = true
            binding.pwdLoginInclude.pwdSet.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        // 光标移动到最后
        val textLen: Int = binding.pwdLoginInclude.pwdSet.text.toString().length
        binding.pwdLoginInclude.pwdSet.setSelection(textLen, textLen)
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
                    LogUtil.i("点击《用户协议》", TAG)

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
                    LogUtil.i("点击《隐私协议》", TAG)

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

    companion object {
        var TAG: String = LoginFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}