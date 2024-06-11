package com.freewind.seastarvideo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.authorize.AuthorizeEventBean
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.databinding.ActivityHomeBinding
import com.freewind.seastarvideo.home.enterpriseService.EnterpriseServiceFragment
import com.freewind.seastarvideo.home.ModuleFragmentPageAdapter
import com.freewind.seastarvideo.home.ModuleInfo
import com.freewind.seastarvideo.home.mine.MineFragment
import com.freewind.seastarvideo.utils.LogUtil
import com.freewind.seastarvideo.utils.OtherUiManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val modules: MutableList<ModuleInfo> = mutableListOf()
    private lateinit var myFragmentPageAdapter: ModuleFragmentPageAdapter
    private val enterpriseServiceFragment: EnterpriseServiceFragment = EnterpriseServiceFragment.newInstance()
    private val mineFragment: MineFragment = MineFragment.newInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        EventBus.getDefault().register(this)
        OtherUiManager.instance.adaptBottomHeight(binding.homeRl)
        updateViewPage2Sensitivity(binding.contentVp)
        initData()
        initPage()
        TabLayoutMediator(binding.navigationTl, binding.contentVp) { tab, position ->
            initTab(tab, modules[position])
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initData() {
        modules.add(0,
            ModuleInfo(getString(R.string.enterprise_service),
                ResourcesCompat.getDrawable(resources,
                    R.drawable.selector_tab_enterprise_service, null)!!,
                enterpriseServiceFragment)
        )
        modules.add(1,
            ModuleInfo(getString(R.string.mine),
                ResourcesCompat.getDrawable(resources,
                    R.drawable.selector_tab_mine, null)!!,
                mineFragment)
        )
    }

    private fun initPage() {
        val fragments = mutableListOf<Fragment>()
        for (module in modules) {
            fragments.add(module.fragment)
        }
        myFragmentPageAdapter = ModuleFragmentPageAdapter(
            supportFragmentManager, lifecycle, fragments)
        binding.contentVp.adapter = myFragmentPageAdapter
        binding.contentVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                myFragmentPageAdapter.fragments[position].onHiddenChanged(false)
            }
        })
    }

    private fun initTab(tab: TabLayout.Tab, module: ModuleInfo) {
        val itemView = View.inflate(this, R.layout.item_tab_home_navigation, null)
        itemView.findViewById<ImageView>(R.id.tabIconIv).setImageDrawable(module.navigationIc)
        itemView.findViewById<TextView>(R.id.tabTextTv).text = module.name
        tab.customView = itemView
    }

    /**
     * 更新viewPage的滑动灵敏度
     */
    private fun updateViewPage2Sensitivity(mViewPage: ViewPager2) {
        try {
            val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true

            val recyclerView = recyclerViewField[mViewPage] as RecyclerView

            val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.isAccessible = true

            val touchSlop = touchSlopField[recyclerView] as Int
            touchSlopField[recyclerView] = touchSlop * 4 //6 is empirical value
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventLoginStatus(event: AuthorizeEventBean.LoginStatusEvent) {
        LogUtil.i("onEventLoginStatus, event = ${event.isLogin}")
        enterpriseServiceFragment.loginStatusChange(event.isLogin)
        mineFragment.loginStatusChange(event.isLogin)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, HomeActivity::class.java)
            activity.startActivity(intent)
        }
    }
}