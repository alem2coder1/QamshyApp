package kz.qamshy.app.common

object StringHelper {
    fun calculatePasswordStrength(password: String): Int {
        var strength = 0

        if (password.length >= 8) strength += 25
        if (password.length >= 12) strength += 25

        if (password.any { it.isDigit() }) strength += 15
        if (password.any { it.isUpperCase() }) strength += 15
        if (password.any { it.isLowerCase() }) strength += 15
        if (password.any { "!@#$%^&*()_-+=<>?".contains(it) }) strength += 15

        if (password.lowercase().contains("password") || password.lowercase().contains("1234")) {
            strength -= 20
        }

        return strength.coerceIn(0, 100)
    }
}