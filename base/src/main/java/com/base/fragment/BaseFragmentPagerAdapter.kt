package com.base.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by vophamtuananh on 3/10/18.
 */
abstract class BaseFragmentPagerAdapter(private val mFragmentManager: FragmentManager) : PagerAdapter() {

    private var mCurTransaction: FragmentTransaction? = null
    private var mCurrentPrimaryItem: Fragment? = null

    abstract fun getItem(position: Int, context: Context): Fragment

    override fun startUpdate(container: ViewGroup) {
        if (container.id == View.NO_ID) {
            throw IllegalStateException(
                "ViewPager with adapter " + this
                        + " requires a view id"
            )
        }
    }

    @SuppressLint("CommitTransaction")
    @SuppressWarnings("ReferenceEquality")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }

        val itemId = getItemId(position)

        val name = makeFragmentName(container.id, itemId)
        var fragment: Fragment? = mFragmentManager.findFragmentByTag(name)
        if (fragment != null) {
            mCurTransaction!!.attach(fragment)
        } else {
            fragment = getItem(position, container.context)
            mCurTransaction!!.add(
                container.id, fragment,
                makeFragmentName(container.id, itemId)
            )
        }
        if (fragment !== mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false)
            fragment.userVisibleHint = false
        }

        return fragment
    }

    @SuppressLint("CommitTransaction")
    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }
        mCurTransaction!!.detach(view as Fragment)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, view: Any) {
        val fragment = view as Fragment?
        if (fragment !== mCurrentPrimaryItem) {
            mCurrentPrimaryItem?.let {
                it.setMenuVisibility(false)
                it.userVisibleHint = false
            }
            fragment?.let {
                it.setMenuVisibility(true)
                it.userVisibleHint = true
            }
            mCurrentPrimaryItem = fragment
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        mCurTransaction?.let {
            it.commitNowAllowingStateLoss()
            mCurTransaction = null
        }
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return (o as Fragment).view === view
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    open fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun makeFragmentName(viewId: Int, id: Long): String {
        return "android:switcher:$viewId:$id"
    }
}