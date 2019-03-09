package com.base.utils

import androidx.databinding.ObservableField
import kotlin.reflect.KProperty

/**
 * Created by vophamtuananh on 3/8/18.
 */
class ObservableDelegate<T> {

    private val field = ObservableField<T>()

    operator fun getValue(self: Any?, prop: KProperty<*>): T? = field.get()
    operator fun setValue(self: Any?, prop: KProperty<*>, value: T) = field.set(value)
}