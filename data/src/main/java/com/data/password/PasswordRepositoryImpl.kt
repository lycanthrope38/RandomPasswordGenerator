package com.data.password

import io.reactivex.Single
import java.util.*


class PasswordRepositoryImpl : PasswordRepository {
    override fun generatePassword(passwordBuilder: PasswordBuilder): Single<String> {
        return Single.fromCallable {
            var countOptionUse = -1
            val maxText = StringBuffer("")
            val random = Random()
            val password = CharArray(passwordBuilder.length)

            if (passwordBuilder.useUpper) {
                maxText.append(PasswordBuilder.UPPER)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.UPPER[random.nextInt(PasswordBuilder.UPPER.length)]
            }
            if (passwordBuilder.useLower) {
                maxText.append(PasswordBuilder.LOWER)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.LOWER[random.nextInt(PasswordBuilder.LOWER.length)]
            }
            if (passwordBuilder.useNumber) {
                maxText.append(PasswordBuilder.NUMBERS)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.NUMBERS[random.nextInt(PasswordBuilder.NUMBERS.length)]
            }
            if (passwordBuilder.usePunctuation) {
                maxText.append(PasswordBuilder.PUNCTUATION)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.PUNCTUATION[random.nextInt(PasswordBuilder.PUNCTUATION.length)]
            }
            for (i in (countOptionUse + 1) until passwordBuilder.length) {
                password[i] = maxText[random.nextInt(maxText.length)]
            }
            for (i in 0 until passwordBuilder.length) {
                val randomPosition = random.nextInt(password.size)
                val temp = password[i]
                password[i] = password[randomPosition]
                password[randomPosition] = temp
            }

            return@fromCallable String(password)
        }
    }

}