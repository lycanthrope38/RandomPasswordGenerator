package com.base.dialog

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.base.R
import com.base.databinding.DialogInformBinding

/**
 * Created by vophamtuananh on 1/7/18.
 */
class InformDialog(context: Context) : BaseDialog<DialogInformBinding>(context) {

    private var mConfirmAction: (() -> Unit)? = null

    private var mTitle: String? = null

    private var mDescription: String? = null

    private var mButtonText: String? = null

    private var mInformType = InformType.ERROR

    override fun getLayoutId(): Int {
        return R.layout.dialog_inform
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.event = this
    }

    override fun onStart() {
        super.onStart()
        setupTexts()
    }

    private fun setupTexts() {
        mViewDataBinding.tvDescription.text = mDescription
        mViewDataBinding.btnOk.text = mButtonText
        if (!TextUtils.isEmpty(mTitle)) {
            mViewDataBinding.tvTitle.text = mTitle
            return
        }
        val title = when (mInformType) {
            InformDialog.InformType.SUCCESS -> context.getString(R.string.success)
            InformDialog.InformType.WARNING -> context.getString(R.string.warning)
            else -> context.getString(R.string.error)
        }
        mViewDataBinding.tvTitle.text = title
    }

    override fun dismiss() {
        mConfirmAction = null
        super.dismiss()
    }

    fun onConfirmClicked() {
        mConfirmAction?.let { it() }
        dismiss()
    }

    fun show(title: String? = null,
             description: String = "",
             informType: InformType = InformType.WARNING,
             buttonText: String = context.getString(R.string.yes),
             cancelable: Boolean = true,
             confirmAction: (() -> Unit)? = null) {
        mConfirmAction = confirmAction
        mTitle = title
        mDescription = description
        mInformType = informType
        mButtonText = buttonText
        setCancelable(cancelable)
        super.show()
    }

    enum class InformType {
        SUCCESS, ERROR, WARNING
    }
}