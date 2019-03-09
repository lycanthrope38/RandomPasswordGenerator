package com.data.password

class PasswordBuilder {
    var useLower: Boolean = false
    var useUpper: Boolean = false
    var useNumber: Boolean = false
    var usePunctuation: Boolean = false
    var length: Int = 5

    companion object {
        const val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val lower = "abcdefghijklmnopqrstuvwxyz"
        const val numbers = "0123456789"
        const val punctuation = "!@#\$%&*()_+-=[]|,./?><"
    }
}