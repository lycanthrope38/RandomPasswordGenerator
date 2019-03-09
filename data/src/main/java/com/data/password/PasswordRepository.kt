package com.data.password

import io.reactivex.Single

interface PasswordRepository {
    fun generatePassword(passwordBuilder: PasswordBuilder): Single<String>
}