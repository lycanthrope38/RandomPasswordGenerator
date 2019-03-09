package com.base.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.databinding.ItemLoadMoreBinding

class LoadMoreAdapter(private val mAdapter: RecyclerAdapter<RecyclerAdapter.BaseHolder<*, Any>, Any>)
    : RecyclerAdapter<RecyclerAdapter.BaseHolder<*, Any>, Any>() {

    companion object {
        private const val LOAD_MORE_TYPE = 100
    }

    private var mReachedEnd = false

    private var mLoading = false

    private var mShowProgressBar = false

    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.BaseHolder<*, Any>? {
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.BaseHolder<*, Any> {
        if (viewType == LOAD_MORE_TYPE) {
            val itemLoadMoreBinding = ItemLoadMoreBinding.inflate(LayoutInflater.from(
                    parent.context), parent, false)
            return LoadMoreHolder(itemLoadMoreBinding)
        }
        return mAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.BaseHolder<*, Any>, position: Int) {
        if (holder is LoadMoreHolder) {
            holder.setLoading(mShowProgressBar)
        } else {
            mAdapter.onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int {
        if (mAdapter.itemCount > 0) {
            return mAdapter.itemCount + 1
        }
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mAdapter.itemCount) LOAD_MORE_TYPE else mAdapter.getItemViewType(position)
    }

    private fun bottomItemPosition(): Int {
        return itemCount - 1
    }

    fun isLoadMoreView(position: Int): Boolean {
        return getItemViewType(position) == LOAD_MORE_TYPE
    }

    internal fun reachedEnd(reachedEnd: Boolean) {
        if (mReachedEnd == reachedEnd)
            return
        mReachedEnd = reachedEnd
        setShowProgressBar(mReachedEnd, mLoading)
    }

    internal fun setLoading(loading: Boolean) {
        if (mLoading == loading)
            return
        mLoading = loading
        setShowProgressBar(mReachedEnd, mLoading)
    }

    private fun setShowProgressBar(reachedEnd: Boolean, loading: Boolean) {
        val showProgressBar = !reachedEnd && loading
        if (mShowProgressBar == showProgressBar)
            return
        mShowProgressBar = showProgressBar
        notifyItemChanged(bottomItemPosition())
    }

    private class LoadMoreHolder<in T>(viewDataBinding: ItemLoadMoreBinding) : RecyclerAdapter.BaseHolder<ItemLoadMoreBinding, T>(viewDataBinding) {

        fun setLoading(showProgressBar: Boolean) {
            mViewDataBinding.lvLoadMore.visibility = if (showProgressBar) View.VISIBLE else View.GONE
        }

    }

}