package com.example.usedtradeapp.home

import androidx.fragment.app.viewModels
import com.example.usedtradeapp.BaseFragment
import com.example.usedtradeapp.R
import com.example.usedtradeapp.databinding.FragmentHomeBinding

class HomeFragment  : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getLayoutResourceId() = R.layout.fragment_home
    override val mViewModel: HomeViewModel by viewModels()
}