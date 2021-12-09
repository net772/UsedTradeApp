package com.example.usedtradeapp.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object ActivityUtil {

    // Activity FrameLayout에 Fragment add.
    fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
        fragmentManager.beginTransaction().add(frameId, fragment, fragment::class.java.name).apply {
            if (addBackStack) addToBackStack(null)
        }.commitAllowingStateLoss()


    // 현재 fragment replace
    fun replaceFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int, addBackStack: Boolean) =
        fragmentManager.beginTransaction().replace(frameId, fragment, fragment::class.java.name).apply {
            if (addBackStack) addToBackStack(null)
        }.commitAllowingStateLoss()
}
