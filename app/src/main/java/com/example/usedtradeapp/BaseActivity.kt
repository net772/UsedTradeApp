package com.example.usedtradeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.usedtradeapp.util.ActivityUtil

abstract class BaseActivity<DB: ViewDataBinding> : AppCompatActivity() {
    lateinit var mBinding: DB

    abstract fun getLayoutResourceId(): Int
    abstract fun initDataBinding()
    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutResourceId())
        initDataBinding()
        initView()
    }

    protected fun <T : Fragment> findOrCreateFragment(type: Class<T>, args: Bundle? = null, frameResId: Int = R.id.fragmentContainer, addBackStack: Boolean = true): T {
        val currentFragment = supportFragmentManager.findFragmentById(frameResId)

        return when {
            isFinishing -> currentFragment as T
            currentFragment == type -> currentFragment as T
            currentFragment == null -> {
                val instance: T = type.newInstance()
                args?.let { instance.arguments = it }

                ActivityUtil.addFragmentToActivity(supportFragmentManager, instance, frameResId, addBackStack)
                instance
            }
            else -> {
                val instance: T = type.newInstance()
                args?.let { instance.arguments = it }

                ActivityUtil.replaceFragmentToActivity(supportFragmentManager, instance, frameResId, addBackStack)
                instance
            }
        }
    }

}