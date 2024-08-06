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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cn.seastart.meeting.api.Callback
import cn.seastart.meeting.bean.Data
import cn.seastart.meeting.bean.LoginBean
import cn.seastart.meeting.impl.MeetingResultListener
import cn.seastart.rtc.enumerate.DeviceType
import com.freewind.seastarvideo.EnvArgument
import com.freewind.seastarvideo.MeetingEngineHelper
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.activity.HomeActivity
import com.freewind.seastarvideo.activity.RegisterActivity
import com.freewind.seastarvideo.authorize.AuthorizeEventBean
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentLoginBinding
import com.freewind.seastarvideo.utils.DeviceUtil
import com.freewind.seastarvideo.utils.KvUtil
import com.freewind.seastarvideo.utils.LogUtil
import com.freewind.seastarvideo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus
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

    // 标识：当前的登录方式。密码登录：ByPwd； 验证码登录：ByCode
    private var curLoginType: LoginType = LoginType.LoginByPwd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
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
                    val phoneNumber = binding.codeLoginInclude.codePhoneSet.text.toString().trim()
                    if (phoneNumber.isEmpty()) {
                        ToastUtil.showShortToast("手机号不可为空")
                        return@setOnClickNoRepeat
                    }
                    viewModel.getSmsCode(phoneNumber)
                }
                binding.loginStv -> {
                    // 请求登录接口
                    when(curLoginType) {
                        LoginType.LoginByPwd -> {
                            loginWithPwd()
                        }
                        LoginType.LoginByCode -> {
                            loginWithCode()
                        }
                    }
                }
                binding.registerStv -> {
                    RegisterActivity.startRegisterInfoPage(requireContext())
                }
            }
        }
        return rootView
    }

    /**
     * 初始化 viewModel
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        viewModel.loginWithPwdResult.observe(this) { uiResponse ->
            uiResponse?.let { response ->
                if (response.isSuccess) {
                    val pwd = binding.pwdLoginInclude.pwdSet.text.toString().trim()
                    KvUtil.encode(KvUtil.USER_INFO_PWD, pwd)
                    Log.i(TAG, "loginWithPwd-initViewModel: 登录成功")
                    viewModel.meetingGrant()
                } else {
                    val msg = response.mError?.mErrorMessage
                    ToastUtil.showLongToast(msg ?: "登录失败")
                }
            }

        }
        viewModel.loginWithCodeResult.observe(this) { uiResponse ->
            uiResponse?.let { response ->
                if (response.isSuccess) {
                    Log.i(TAG, "loginWithCode-initViewModel: 登录成功")
                    viewModel.meetingGrant()
                } else {
                    val msg = response.mError?.mErrorMessage
                    ToastUtil.showLongToast(msg ?: "登录失败")
                }
            }
        }
        viewModel.getSmsCodeResult.observe(this) { uiResponse ->
            uiResponse?.let { result ->
                if (result.isSuccess) {
                    ToastUtil.showShortToast("验证码发送成功")
                    binding.codeLoginInclude.getCodeStv.startCountDown()
                } else {
                    ToastUtil.showShortToast("验证码发送失败")
                }
            }
        }
        viewModel.meetingGrantResult.observe(this) { uiResponse ->
            uiResponse?.let { result ->
                if (result.isSuccess) {
                    meetingSdkLogin(KvUtil.decodeString(KvUtil.USER_INFO_UID), result.mResult!!)
                } else {
                    Log.i(TAG, "initViewModel: meeting grant fail")
                    ToastUtil.showShortToast("登录失败")
                }
            }
        }
    }

    /**
     * 选择使用密码登录
     */
    private fun selectLoginWithPwd() {
        curLoginType = LoginType.LoginByPwd

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
        curLoginType = LoginType.LoginByCode

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
     * 通过密码登录
     */
    private fun loginWithPwd() {
        val phoneNumber = binding.pwdLoginInclude.pwdPhoneSet.text.toString().trim()
        if (phoneNumber.isEmpty()) {
            ToastUtil.showShortToast("手机号不可为空")
            return
        }
        val pwd = binding.pwdLoginInclude.pwdSet.text.toString().trim()
        if (pwd.isEmpty()) {
            ToastUtil.showShortToast("密码不可为空")
            return
        }
        val isAgree = binding.selectCb.isChecked
        if (!isAgree) {
            ToastUtil.showShortToast("请先阅读并勾选下方协议")
            return
        }

        viewModel.loginWithPwd(phoneNumber, pwd)
    }

    /**
     * 通过验证码登录
     */
    private fun loginWithCode() {
        val phoneNumber = binding.codeLoginInclude.codePhoneSet.text.toString().trim()
        if (phoneNumber.isEmpty()) {
            ToastUtil.showShortToast("手机号不可为空")
            return
        }
        val code = binding.codeLoginInclude.codeSet.text.toString().trim()
        if (code.isEmpty()) {
            ToastUtil.showShortToast("验证码不可为空")
            return
        }
        val isAgree = binding.selectCb.isChecked
        if (!isAgree) {
            ToastUtil.showShortToast("请先阅读并勾选下方协议")
            return
        }

        viewModel.loginWithCode(phoneNumber, code)
    }

    /**
     * 登录 sdk
     */
    private fun meetingSdkLogin(userId: String, token: String) {
        MeetingEngineHelper.instance.init(requireActivity().application, token, null,
            object : MeetingResultListener {
                override fun onFail(code: Int, message: String) {
                    ToastUtil.showShortToast("sdk 初始化失败")
                }

                override fun onSuccess() {
                    MeetingEngineHelper.instance.engine?.login(
                        userId, 1, DeviceType.Android.value, DeviceUtil.getAndroidID(requireContext()),
                        object : Callback<Data<LoginBean>?>() {
                            override fun onSuccess(data: Data<LoginBean>?) {
                                super.onSuccess(data)
                                ToastUtil.showShortToast("登陆成功")
                                cleanEtContent()
                                EnvArgument.instance.token = token
                                EventBus.getDefault().post(AuthorizeEventBean.LoginStatusEvent(true))
                                HomeActivity.startActivity(this@LoginFragment.requireActivity())
                                this@LoginFragment.requireActivity().finish()
                            }

                            override fun onFailed(code: Int, msg: String?) {
                                super.onFailed(code, msg)
                                ToastUtil.showShortToast(msg ?: "登录失败")
                            }
                        })
                }
            }
        )
    }

    private fun cleanEtContent() {
        if (binding.pwdLoginInclude.root.visibility == View.VISIBLE) {
            binding.pwdLoginInclude.pwdPhoneSet.editableText.clear()
            binding.pwdLoginInclude.pwdSet.editableText.clear()
        } else if (binding.codeLoginInclude.root.visibility == View.VISIBLE) {
            binding.codeLoginInclude.codePhoneSet.editableText.clear()
            binding.codeLoginInclude.codeSet.editableText.clear()
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

    /**
     * 登录方式
     */
    enum class LoginType(val value: String) {
        // 通过用户名密码登录
        LoginByPwd("login_by_pwd"),
        // 通过手机号验证码登录
        LoginByCode("login_by_code")
    }
}