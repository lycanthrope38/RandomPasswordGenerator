package com.base.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.base.R
import com.base.dialog.ConnectionErrorDialog
import com.base.dialog.LoadingDialog
import com.base.dialog.UnknownErrorDialog
import com.base.exception.ErrorMessageException
import com.base.exception.ManuallyException
import com.base.exception.NoConnectionException
import com.base.exception.TokenExpiredException
import com.base.utils.DeviceUtil
import com.base.utils.DeviceUtil.Companion.CAMERA_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.GALLERY_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_CALL_PHONE_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_CAMERA_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_READ_EXTERNAL_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_WRITE_STORAGE_REQUEST_CODE
import com.base.viewmodel.ActivityViewModel
import com.base.viewmodel.CommonView
import com.tbruyelle.rxpermissions2.RxPermissions
import retrofit2.HttpException


/**
 * Created by vophamtuananh on 1/7/18.
 */
abstract class BaseActivity<B : ViewDataBinding, VM : ActivityViewModel> : AppCompatActivity(), CommonView {

    protected lateinit var mViewDataBinding: B

    protected var mViewModel: VM? = null

    private var mCapturedPath: String? = null

    private var mCurrentPhoneNumber: String? = null

    private var mLoadingDialog: LoadingDialog? = null

    private var mConnectionErrorDialog: ConnectionErrorDialog? = null

    private var mInformDialog: MaterialDialog? = null

    private var mConfirmDialog: MaterialDialog? = null

    private var mUnknownErrorDialog: UnknownErrorDialog? = null


    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected open fun getViewModelClass(): Class<VM>? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (showFullScreen()) {
            makeFullscreen()
        }
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        if (getViewModelClass() != null)
            mViewModel = ViewModelProviders.of(this).get(getViewModelClass()!!)
        mViewModel?.onAttached(this)

    }

    override fun onPause() {
        super.onPause()
        mLoadingDialog?.dismiss()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mViewModel?.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mViewModel?.onRestoreInstanceState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        mViewDataBinding.unbind()
        super.onDestroy()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v != null && v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
            if (showFullScreen())
                makeFullscreen()
        }
        return try {
            super.dispatchTouchEvent(event)
        } catch (e: Exception) {
            true
        }

    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        PermissionUtil.handleRequestPermissionsResult(this, permissions, grantResults,
//            onGranted = { handelPermissionGrant(requestCode) }, onDenied = { handlePermissionDenied(requestCode, it) })
//    }

    private fun handelPermissionGrant(requestCode: Int) {
        when (requestCode) {
//            PERMISSION_CAMERA_REQUEST_CODE -> openCamera()
            PERMISSION_READ_EXTERNAL_REQUEST_CODE -> openGallery()
            PERMISSION_CALL_PHONE_REQUEST_CODE -> mCurrentPhoneNumber?.let { callToPhoneNumber(it) }
            PERMISSION_WRITE_STORAGE_REQUEST_CODE -> onAgreedWriteExternal()
        }
    }

    protected open fun handlePermissionDenied(requestCode: Int, deniedForever: Boolean) {
        when (requestCode) {
            PERMISSION_CAMERA_REQUEST_CODE -> onRejectedCameraPermission()
            PERMISSION_READ_EXTERNAL_REQUEST_CODE -> onRejectedReadExternalPermission()
            PERMISSION_CALL_PHONE_REQUEST_CODE -> onRejectedPhoneCallPermission()
            PERMISSION_WRITE_STORAGE_REQUEST_CODE -> onRejectedWriteExternalPermission()
        }
//        if (deniedForever)
//            getConfirmDialog().show(title = getString(R.string.notice),
//                description = getString(R.string.you_have_denied_permission),
//                yesText = getString(R.string.settings),
//                noText = getString(R.string.cancel),
//                yesAction = { DeviceUtil.openSettings(this) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (mCapturedPath != null)
                    onCapturedImage(mCapturedPath!!)
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    val selectedImage = data.data
                    onChoseImage(selectedImage)
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            onChoseNoImage()
        }
    }

    override fun showLoading() {
        showLoadingDialog()
    }

    override fun hideLoading() {
        hideLoadingDialog()
    }

    override fun showError(throwable: Throwable, tryAgainAction: (() -> Unit)?) {
        when (throwable) {
            is NoConnectionException -> {
                val dialog = getConnectionErrorDialog()
                dialog.show(tryAgainAction)
            }
            is TokenExpiredException -> tokenExpired()
            else -> {
                val msg = getThrowableMessage(throwable)
                if (msg == getString(R.string.unknown_error)) {
                    val dialog = getUnknownErrorDialog()
                    dialog.show(tryAgainAction)
                    return
                }
                val dialog = getInformDialog()
                dialog?.show {
                    title(R.string.warning)
                    message(text = getThrowableMessage(throwable))
                    positiveButton(R.string.ok) { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun showError(throwable: Throwable) {
        if (throwable is HttpException) {
            if (throwable.code() == 401) {
                tokenExpired()
            }
            return
        }
        val dialog = getInformDialog()
        if (!dialog?.isShowing!!) {
            dialog.show {
                title(R.string.warning)
                message(text = getThrowableMessage(throwable))
                positiveButton(R.string.ok) { dialog ->
                    dialog.dismiss()
                }
            }
        }
    }

    override fun lifecycleOwner(): LifecycleOwner {
        return this
    }

    fun getThrowableMessage(e: Throwable): String {
        var msg: String? = null
        if (e is NoConnectionException || e is ManuallyException || e is ErrorMessageException)
            msg = e.message
        else if (e is HttpException) {
            msg = getHttpExceptionMessage(e)
        }
        return if (TextUtils.isEmpty(msg)) getString(R.string.unknown_error) else msg!!
    }

    protected open fun getHttpExceptionMessage(httpException: HttpException): String {
        return getString(R.string.server_error)
    }

    private fun makeFullscreen() {
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
        decorView.systemUiVisibility = uiOptions
    }

    @SuppressLint("CheckResult")
    fun openCamera(fileName: String? = null) {
        RxPermissions(this)
            .request(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .take(1)
            .subscribe({ granted ->
                if (granted) {
                    mCapturedPath = DeviceUtil.openCamera(this, fileName)
                } else {
                }
            }, {})
    }

    open fun tokenExpired() {

    }

    fun openGallery() {
        DeviceUtil.openGallery(this)
    }

    fun callToPhoneNumber(phoneNumber: String) {
        val telMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.simState
        if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            return
        }
        mCurrentPhoneNumber = phoneNumber
        DeviceUtil.callToPhoneNumber(this, mCurrentPhoneNumber!!)
    }

    protected open fun showFullScreen(): Boolean {
        return false
    }

    protected open fun onCapturedImage(path: String) {}

    protected open fun onChoseImage(uri: Uri) {}

    protected open fun onChoseNoImage() {}

    protected open fun onRejectedCameraPermission() {}

    protected open fun onRejectedReadExternalPermission() {}

    protected open fun onRejectedPhoneCallPermission() {}

    protected open fun onAgreedWriteExternal() {}

    protected open fun onRejectedWriteExternalPermission() {}

    fun showLoadingDialog(onLoadingDialogListener: LoadingDialog.OnLoadingDialogListener? = null) {
        if (mLoadingDialog == null)
            mLoadingDialog = LoadingDialog(this)

        if (mLoadingDialog!!.isShowing)
            return

        mLoadingDialog?.showWithListener(onLoadingDialogListener)
    }

    fun hideLoadingDialog() {
        mLoadingDialog?.dismiss()
    }

    fun getConnectionErrorDialog(): ConnectionErrorDialog {
        if (mConnectionErrorDialog == null)
            mConnectionErrorDialog = ConnectionErrorDialog(this)
        return mConnectionErrorDialog!!
    }

    fun getInformDialog(): MaterialDialog? {
        if (mInformDialog == null)
            mInformDialog = MaterialDialog(this)
        return mInformDialog
    }

    fun getConfirmDialog(): MaterialDialog? {
        if (mConfirmDialog == null)
            mConfirmDialog = MaterialDialog(this)
        return mConfirmDialog
    }

    fun getUnknownErrorDialog(): UnknownErrorDialog {
        if (mUnknownErrorDialog == null) {
            mUnknownErrorDialog = UnknownErrorDialog(this)
        }
        return mUnknownErrorDialog!!
    }
}