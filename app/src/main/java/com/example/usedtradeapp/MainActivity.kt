package com.example.usedtradeapp

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.usedtradeapp.chatlist.ChatListFragment
import com.example.usedtradeapp.databinding.ActivityMainBinding
import com.example.usedtradeapp.home.HomeFragment
import com.example.usedtradeapp.mypage.MyPageFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutResourceId() = R.layout.activity_main
    private val viewModel : MainViewModel by viewModels()


    override fun initDataBinding() {
        mBinding.lifecycleOwner = this
        mBinding.vm = viewModel
        setBottomNavigation()
    }

    override fun initView() {
        findOrCreateFragment(HomeFragment::class.java, addBackStack = false)
    }

    private fun setBottomNavigation() {
        mBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> findOrCreateFragment(HomeFragment::class.java, addBackStack = false)
                R.id.chatList -> findOrCreateFragment(ChatListFragment::class.java, addBackStack = false)
                R.id.myPage -> findOrCreateFragment(MyPageFragment::class.java, addBackStack = false)
            }
            true
        }
    }
}