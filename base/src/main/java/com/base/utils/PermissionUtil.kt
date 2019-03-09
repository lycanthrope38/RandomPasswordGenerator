package com.base.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale



class PermissionUtil {

    companion object {

        private fun getDeniedPermissions(context: Context, permissions: Array<String>): ArrayList<String>? {
            var deniedPermissions: ArrayList<String>? = null
            if (Build.VERSION.SDK_INT >= 23) {
                permissions.forEach {
                    if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                        if (deniedPermissions == null)
                            deniedPermissions = ArrayList()
                        deniedPermissions!!.add(it)
                    }
                }
            }
            return deniedPermissions
        }

        fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean {
            val deniedPermissions = getDeniedPermissions(activity, permissions)
            if (deniedPermissions != null) {
                ActivityCompat.requestPermissions(activity, deniedPermissions.toTypedArray(), requestCode)
                return false
            }
            return true
        }

        fun handleRequestPermissionsResult(activity: Activity, permissions: Array<String>, grantResults: IntArray,
                                           onGranted: (() -> Unit)? = null, onDenied: ((deniedForever: Boolean) -> Unit)? = null) {
            var somePermissionsDenied = false
            var somePermissionsDeniedForever = false
            for (i in 0..(grantResults.size - 1)) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    somePermissionsDenied = true
                    val permission = permissions[i]
                    val showRationale = shouldShowRequestPermissionRationale(activity, permission)
                    if (!showRationale) {
                        somePermissionsDeniedForever = true
                        break
                    }
                }
            }
            if (somePermissionsDenied) {
                onDenied?.let { it(somePermissionsDeniedForever) }
                return
            }
            onGranted?.let { it() }
        }

    }

}