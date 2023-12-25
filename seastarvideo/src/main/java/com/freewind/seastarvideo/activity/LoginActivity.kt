package com.freewind.seastarvideo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.authorize.login.LoginFragment
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.databinding.ActivityLoginBinding

/**
 * @author: wiatt
 * @date: 2023/12/20 11:19
 * @description: 登录页 activity
 */
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var currentFragment: Fragment? = null
    private val loginFragment: LoginFragment by lazy {
        LoginFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        switchFragment(loginFragment)
    }

    /**
     * 用于切换Fragment
     * 当不显示fragment时，nextFragment为空
     */
    private fun switchFragment(nextFragment: Fragment?): Boolean{
        if(nextFragment == currentFragment) {
            return false
        }

        if (currentFragment != null && currentFragment!!.isAdded) {
            hideFragment(currentFragment!!)
        }

        if (nextFragment != null) {
            if (nextFragment.isAdded) {
                showFragment(nextFragment)
            } else {
                addFragment(nextFragment, R.id.contentFl)
            }
        }
        currentFragment = nextFragment
        return true
    }
}