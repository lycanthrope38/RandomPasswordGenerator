package com.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.base.R

/**
 * Created by vophamtuananh on 12/24/17.
 */

inline fun <reified T> Activity.start(clearBackStack: Boolean = false, bundle: Bundle? = null,arrayTransition : Array<Int>? = null) {
    val intent = Intent(this, T::class.java)
    if (clearBackStack)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    bundle?.let {
        intent.putExtras(bundle)
    }
    startActivity(intent)
    arrayTransition?.run {
        overridePendingTransition(arrayTransition[0],arrayTransition[1])
    }
}

inline fun <reified T> Activity.startForResult(requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Fragment> createNewFragment(context: Context, bundle: Bundle? = null): T {
    return T::class.java.cast(Fragment.instantiate(context, T::class.java.name, bundle))
}

inline fun <reified T : Fragment> Context.newFragment(bundle: Bundle? = null): T {
    return T::class.java.cast(Fragment.instantiate(this, T::class.java.name, bundle))
}

inline fun <reified T : Activity> Fragment.getOwnActivity(): T? {
    activity ?: return null
    return T::class.java.cast(activity)
}

inline fun SharedPreferences.put(body: SharedPreferences.Editor.() -> Unit) {
    val editor = this.edit()
    editor.body()
    editor.apply()
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()