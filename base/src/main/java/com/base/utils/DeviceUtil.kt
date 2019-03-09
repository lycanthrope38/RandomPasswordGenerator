package com.base.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import com.base.activity.BaseActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by vophamtuananh on 1/7/18.
 */
class DeviceUtil {

    companion object {
        const val PERMISSION_CAMERA_REQUEST_CODE = 1001
        const val PERMISSION_READ_EXTERNAL_REQUEST_CODE = 1002
        const val PERMISSION_CALL_PHONE_REQUEST_CODE = 1003
        const val PERMISSION_WRITE_STORAGE_REQUEST_CODE = 1004

        const val CAMERA_REQUEST_CODE = 1011
        const val GALLERY_REQUEST_CODE = 1012

        fun openCamera(activity: Activity, fileName: String?): String? {
            val tempFile = FileUtil.getOutputMediaFile(activity, fileName)
            tempFile?.let {
                camera(activity, it)
                return tempFile.absolutePath
            }
            return ""
        }

        private fun camera(activity: Activity, tempFile: File) {
            val capturedFileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    activity,
                    activity.applicationContext.packageName + ".provider", tempFile
                )
            } else {
                Uri.fromFile(tempFile)
            }
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePhotoIntent.resolveActivity(activity.packageManager) != null) {
                takePhotoIntent.putExtra("return-data", true)
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedFileUri)
                val chooserIntent = Intent.createChooser(takePhotoIntent, "Selection Photo")
                if (chooserIntent != null)
                    activity.startActivityForResult(chooserIntent, CAMERA_REQUEST_CODE)
            }
        }

        @SuppressLint("CheckResult")
        fun openGallery(activity: BaseActivity<*, *>) {
            RxPermissions(activity)
                .request(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).subscribeOn(Schedulers.io())
                .subscribe({
                    if (it) {
                        gallery(activity)
                    }
                }, {})
        }

        private fun gallery(activity: Activity) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            if (photoPickerIntent.resolveActivity(activity.packageManager) != null) {
                photoPickerIntent.type = "image/*"
                val chooserIntent = Intent.createChooser(photoPickerIntent, "Selection Photo")
                if (chooserIntent != null)
                    activity.startActivityForResult(chooserIntent, GALLERY_REQUEST_CODE)
            }
        }

        @SuppressLint("MissingPermission")
        fun callToPhoneNumber(activity: Activity, phoneNumber: String) {
            if (PermissionUtil.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    PERMISSION_CALL_PHONE_REQUEST_CODE
                )
            ) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                activity.startActivity(intent)
            }
        }

        fun openSettings(activity: Activity) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
        }
    }

}