package com.tiki.randompasswordgenerator

import android.view.View
import androidx.databinding.ObservableField
import com.base.viewmodel.ActivityViewModel
import com.data.password.PasswordBuilder
import com.data.password.PasswordDomain
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : ActivityViewModel() {
    lateinit var mPasswordDomain: PasswordDomain

    private var mPasswordBuilder = PasswordBuilder()
    var passwordObser = ObservableField<String>("")

    fun onCheckedChange(view: View, isChecked: Boolean) {
        when (view.id) {
            R.id.cb_upper_case -> mPasswordBuilder.useUpper = isChecked
            R.id.cb_lower_case -> mPasswordBuilder.useLower = isChecked
            R.id.cb_number -> mPasswordBuilder.useNumber = isChecked
            R.id.cb_punctuation_case -> mPasswordBuilder.usePunctuation = isChecked
        }
        generatePassword()
    }

    fun passwordLength(length: Int) {
        mPasswordBuilder.length = length
        generatePassword()
    }

    fun generatePassword() {
        if (mPasswordBuilder.useUpper || mPasswordBuilder.useLower || mPasswordBuilder.useNumber || mPasswordBuilder.usePunctuation) {
            addDisposable(
                mPasswordDomain.generatePassword(mPasswordBuilder)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        passwordObser.set(it)
                    }, {})
            )
        }

    }
}