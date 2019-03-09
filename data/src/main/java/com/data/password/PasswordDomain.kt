package com.data.password

import io.reactivex.Single
import javax.inject.Inject

class PasswordDomain @Inject constructor(
    private var mRepository: PasswordRepository
) {
    fun generatePassword(passwordBuilder: PasswordBuilder): Single<String> {
        return mRepository.generatePassword(passwordBuilder)
    }

}