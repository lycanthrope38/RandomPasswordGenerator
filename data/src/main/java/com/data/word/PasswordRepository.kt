package com.data.word

import io.reactivex.Single

interface PasswordRepository {
    fun generatePassword(passwordBuilder: PasswordBuilder): Single<String>
}