package com.base.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.base.utils.ListUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by vophamtuananh on 1/12/18.
 */
abstract class RecyclerAdapter<VH : RecyclerAdapter.BaseHolder<*, T>, T>(
    private var mItemListener: ItemListener<T>? = null,
    private var mComparator: ItemComparator<T>? = null
) : RecyclerView.Adapter<VH>() {

    val itemList = ArrayList<T>()

    private var mCalculateDiffDisposable: Disposable? = null

    private var mAdapterDataObserver: RecyclerView.AdapterDataObserver? = null

    protected abstract fun getViewHolder(parent: ViewGroup, viewType: Int): VH?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewHolder = getViewHolder(parent, viewType)
        if (viewHolder != null && mItemListener != null) {
            viewHolder.mViewDataBinding.root.setOnClickListener { view ->
                val pos = viewHolder.adapterPosition
                if (pos != NO_POSITION) {
                    mItemListener!!.onItemClick(view, pos, itemList[pos])
                }
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun registerDataObserver(adapterDataObserver: RecyclerView.AdapterDataObserver) {
        if (mAdapterDataObserver != null)
            unregisterAdapterDataObserver(mAdapterDataObserver!!)
        mAdapterDataObserver = adapterDataObserver
        registerAdapterDataObserver(mAdapterDataObserver!!)
    }

    private fun unRegisterDataObserver() {
        if (mAdapterDataObserver != null)
            unregisterAdapterDataObserver(mAdapterDataObserver!!)
        mAdapterDataObserver = null
    }

    fun update(items: List<T>) {
        if (ListUtil.isNotEmpty(itemList) && mComparator != null) {
            updateDiffItemsOnly(items)
        } else {
            updateAllItems(items)
        }
    }

    fun appenItems(items: List<T>?) {
        if (itemList.isEmpty()) {
            updateAllItems(items!!)
        } else {
            if (items != null && !items.isEmpty()) {
                val positionStart = itemList.size
                itemList.addAll(items)
                notifyItemRangeInserted(positionStart, items.size)
            }
        }
    }

    fun addItem(item: T) {
        itemList.add(item)
        notifyItemInserted(itemList.size)
    }

    fun addItem(position: Int, item: T) {
        itemList.add(position, item)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItem(position: Int, item: T) {
        itemList[position] = item
        notifyItemChanged(position)
    }

    open fun updateAllItems(items: List<T>) {
        updateItemsInModel(items)
        notifyDataSetChanged()
    }

    open fun updateDiffItemsOnly(items: List<T>) {
        if (mCalculateDiffDisposable != null && !mCalculateDiffDisposable!!.isDisposed)
            mCalculateDiffDisposable!!.dispose()
        mCalculateDiffDisposable = Single.fromCallable { calculateDiff(items) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { updateItemsInModel(items) }
            .subscribe { result -> updateAdapterWithDiffResult(result) }
    }

    private fun calculateDiff(newItems: List<T>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(DiffUtilCallback(itemList, newItems, mComparator!!))
    }

    private fun updateItemsInModel(items: List<T>) {
        itemList.clear()
        itemList.addAll(items)
    }

    private fun updateAdapterWithDiffResult(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(this)
    }

    protected fun getData(): ArrayList<T> {
        return itemList
    }

    open fun release() {
        mItemListener = null
        unRegisterDataObserver()
    }

    open class BaseHolder<out V : ViewDataBinding, in T>(val mViewDataBinding: V) :
        RecyclerView.ViewHolder(mViewDataBinding.root) {

        open fun bindData(data: T) {}
    }

    interface ItemListener<in T> {
        fun onItemClick(v: View, position: Int, data: T)
    }
}