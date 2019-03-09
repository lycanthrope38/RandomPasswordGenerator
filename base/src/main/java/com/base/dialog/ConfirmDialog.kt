package com.base.dialog

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.base.R
import com.base.databinding.DialogConfirmBinding

/**
 * Created by vophamtuananh on 1/7/18.
 */
class ConfirmDialog(context: Context) : BaseDialog<DialogConfirmBinding>(context) {

    private var mYesAction: (() -> Unit)? = null
    private var mNoAction: (() -> Unit)? = null
    private var mTitle: String? = null
    private var mDescription: String? = null
    private var mYesButtonText: String? = null
    private var mNoButtonText: String? = null
    private var mConfirmType = ConfirmType.WARNING

    override fun getLayoutId(): Int {
        return R.layout.dialog_confirm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.event = this
    }

    override fun onStart() {
        super.onStart()
        setupTexts()
    }

    override fun dismiss() {
        mYesAction = null
        mNoAction = null
        super.dismiss()
    }

    private fun setupTexts() {
        mViewDataBinding.tvDescription.text = if (!TextUtils.isEmpty(mDescription)) mDescription else ""
        mViewDataBinding.btnYes.text = if (!TextUtils.isEmpty(mYesButtonText)) mYesButtonText else context.getString(R.string.yes)
        mViewDataBinding.btnNo.text = if (!TextUtils.isEmpty(mNoButtonText)) mNoButtonText else context.getString(R.string.cancel)
        if (!TextUtils.isEmpty(mTitle)) {
            mViewDataBinding.tvTitle.text = mTitle
            return
        }
        val title = when (mConfirmType) {
            ConfirmDialog.ConfirmType.ERROR -> context.getString(R.string.error)
            ConfirmDialog.ConfirmType.SUCCESS -> context.getString(R.string.success)
            else -> context.getString(R.string.warning)
        }
        mViewDataBinding.tvTitle.text = title
    }

    fun show(title: String? = null,
             description: String = "",
             confirmType: ConfirmType = ConfirmType.WARNING,
             yesText: String = context.getString(R.string.yes),
             noText: String = context.getString(R.string.no),
             cancelable: Boolean = true,
             yesAction: (() -> Unit)? = null,
             noAction: (() -> Unit)? = null) {
        mYesAction = yesAction
        mNoAction = noAction
        mTitle = title
        mDescription = description
        mConfirmType = confirmType
        mYesButtonText = yesText
        mNoButtonText = noText
        setCancelable(cancelable)
        super.show()
    }

    fun onYesClicked() {
        mYesAction?.let { it() }
        dismiss()
    }

    fun onNoClicked() {
        mNoAction?.let { it() }
        dismiss()
    }

    enum class ConfirmType {
        SUCCESS, ERROR, WARNING
    }
}