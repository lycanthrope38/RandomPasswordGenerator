package com.base.fragment

import androidx.fragment.app.FragmentManager

/**
 * Created by vophamtuananh on 1/7/18.
 */
interface FragmentProvider<T : BaseFragment<*, *>> {

    fun getFragments(): Array<T>

    fun getContentLayoutId(): Int

    fun fragmentManager(): FragmentManager

}