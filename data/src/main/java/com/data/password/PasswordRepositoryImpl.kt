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
                maxText.append(PasswordBuilder.upper)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.upper[random.nextInt(PasswordBuilder.upper.length)]
            }
            if (passwordBuilder.useLower) {
                maxText.append(PasswordBuilder.lower)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.lower[random.nextInt(PasswordBuilder.lower.length)]
            }
            if (passwordBuilder.useNumber) {
                maxText.append(PasswordBuilder.numbers)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.numbers[random.nextInt(PasswordBuilder.numbers.length)]
            }
            if (passwordBuilder.usePunctuation) {
                maxText.append(PasswordBuilder.punctuation)
                countOptionUse += 1
                password[countOptionUse] = PasswordBuilder.punctuation[random.nextInt(PasswordBuilder.punctuation.length)]
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