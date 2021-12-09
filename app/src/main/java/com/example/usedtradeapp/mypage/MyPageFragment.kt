package com.example.usedtradeapp.mypage

import androidx.fragment.app.viewModels
import com.example.usedtradeapp.BaseFragment
import com.example.usedtradeapp.R
import com.example.usedtradeapp.databinding.FragmentMypageBinding

class MyPageFragment  : BaseFragment<FragmentMypageBinding, MyPageViewModel>() {
    override fun getLayoutResourceId() = R.layout.fragment_mypage
    override val mViewModel: MyPageViewModel by viewModels()
}