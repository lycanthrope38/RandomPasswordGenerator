package com.base.recyclerview

import androidx.recyclerview.widget.RecyclerView

class DataObserver(private val mAdapterWrapper: LoadMoreAdapter) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        mAdapterWrapper.notifyDataSetChanged()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        if (mAdapterWrapper.itemCount == 0) {
            mAdapterWrapper.notifyDataSetChanged()
            return
        }
        mAdapterWrapper.notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        if (mAdapterWrapper.itemCount == 0) {
            mAdapterWrapper.notifyDataSetChanged()
            return
        }
        mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        if (mAdapterWrapper.itemCount == 0) {
            mAdapterWrapper.notifyDataSetChanged()
            return
        }
        mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount, payload)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        if (mAdapterWrapper.itemCount == 0) {
            mAdapterWrapper.notifyDataSetChanged()
            return
        }
        mAdapterWrapper.notifyItemRangeRemoved(positionStart, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        if (mAdapterWrapper.itemCount == 0) {
            mAdapterWrapper.notifyDataSetChanged()
            return
        }
        mAdapterWrapper.notifyItemMoved(fromPosition, toPosition)
    }
}