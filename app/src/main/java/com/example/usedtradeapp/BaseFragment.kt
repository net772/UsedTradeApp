package com.example.usedtradeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    lateinit var mBinding: DB
    abstract val mViewModel: VM

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)


        return mBinding.root
    }



}