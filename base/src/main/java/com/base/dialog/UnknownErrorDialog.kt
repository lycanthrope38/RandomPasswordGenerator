package com.base.dialog

import android.content.Context
import android.os.Bundle
import com.base.R
import com.base.databinding.DialogUnknownErrorBinding

class UnknownErrorDialog(context: Context) : BaseDialog<DialogUnknownErrorBinding>(context) {

    var mTryAgainAction: (() -> Unit)? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_unknown_error
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.event = this
    }

    override fun dismiss() {
        mTryAgainAction = null
        super.dismiss()
    }

    fun show(action: (() -> Unit)? = null) {
        mTryAgainAction = action
        super.show()
    }

    fun onTryAgain() {
        mTryAgainAction?.let { it() }
        dismiss()
    }
}