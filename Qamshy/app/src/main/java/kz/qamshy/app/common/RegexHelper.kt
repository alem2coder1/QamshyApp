package kz.qamshy.app.common

class RegexHelper {
    fun isPhoneNumber(phone: String): String? {
        return when {
            phone.startsWith("1") && phone.length == 11 -> "+86$phone"
            phone.startsWith("7") && phone.length == 10 -> "+7$phone"
            phone.startsWith("77") && phone.length == 11 -> "+$phone"
            phone.startsWith("87") && phone.length == 11 -> phone
            phone.startsWith("+861") && phone.length == 14 -> phone
            phone.startsWith("+77") && phone.length == 12 -> phone
            else -> null
        }
    }
}