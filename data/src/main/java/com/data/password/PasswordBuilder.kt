package com.data.password

class PasswordBuilder {
    var useLower: Boolean = false
    var useUpper: Boolean = false
    var useNumber: Boolean = false
    var usePunctuation: Boolean = false
    var length: Int = 5

    companion object {
        const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val LOWER = "abcdefghijklmnopqrstuvwxyz"
        const val NUMBERS = "0123456789"
        const val PUNCTUATION = "!@#\$%&*()_+-=[]|,./?><"
    }
}