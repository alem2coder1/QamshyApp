package kz.qamshy.app.common

import java.net.URLDecoder

object Cyrl2ToteHelper {

    private enum class Sound {
        Vowel, Consonant, Unknown
    }

    private val cyrlChars = setOf(
        "А", "Ә", "Ə", "Б", "В", "Г", "Ғ", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Қ", "Л", "М", "Н", "Ң", "О", "Ө",
        "Ɵ", "П", "Р", "С", "Т", "У", "Ұ", "Ү", "Ф", "Х", "Һ", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "І", "Ь", "Э", "Ю", "Я",
        "-"
    )

    private val dialectWordsDic = mapOf(
        "قر" to "ق ر", "جحر" to "ج ح ر", "جشس" to "ج ش س", "شۇار" to "ش ۇ ا ر", "باق" to "ب ا ق",
        "ءباسپاسوز" to "باسپا ءسوز", "قىتاي" to "جۇڭگو"
    )

    fun cyrl2Tote(cyrlText: String): String {
        var cyrl = copycatCyrlToOriginalCyrl(cyrlText) + "."
        cyrl = URLDecoder.decode(cyrl, "UTF-8")

        val chars = cyrl.toCharArray().map { it.toString() }
        val toteStrs = Array(chars.size) { "" }
        var prevSound = Sound.Unknown
        var cyrlWord = ""

        for (i in chars.indices) {
            if (!cyrlChars.contains(chars[i].uppercase())) {
                if (cyrlWord.isNotEmpty()) {
                    val toteChars = mutableListOf<String>()
                    var j = i - cyrlWord.length
                    while (j < i) {
                        val key = if (j + 1 < chars.size) chars[j] + chars[j + 1] else ""
                        when (key.lowercase()) {
                            "ия" -> { toteChars += "يا"; j++ }
                            "йя" -> { toteChars += "ييا"; j++ }
                            "ию", "йю" -> { toteChars += "يۋ"; j++ }
                            "сц" -> { toteChars += "س"; j++ }
                            "тч" -> { toteChars += "چ"; j++ }
                            "ий" -> { toteChars += "ي"; j++ }
                            "хх" -> { toteChars += "ХХ"; j++ }
                            else -> toteChars += when (chars[j].lowercase()) {
                                "я" -> if (prevSound == Sound.Consonant) "ءا" else "يا"
                                "ю" -> if (prevSound == Sound.Consonant) "ءۇ" else "يۋ"
                                "щ" -> "شش"
                                "э" -> "ە"
                                "а" -> "ا"
                                "б" -> "ب"
                                "ц" -> "س"
                                "д" -> "د"
                                "е" -> "ە"
                                "ф" -> "ف"
                                "г" -> "گ"
                                "х" -> "ح"
                                "һ" -> "ھ"
                                "і" -> "ءى"
                                "и", "й" -> "ي"
                                "к" -> "ك"
                                "л" -> "ل"
                                "м" -> "م"
                                "н" -> "ن"
                                "о" -> "و"
                                "п" -> "پ"
                                "қ" -> "ق"
                                "р" -> "ر"
                                "с" -> "س"
                                "т" -> "ت"
                                "ұ" -> "ۇ"
                                "в" -> "ۆ"
                                "у" -> "ۋ"
                                "ы" -> "ى"
                                "з" -> "ز"
                                "ә" -> "ءا"
                                "ё", "ө" -> "ءو"
                                "ү" -> "ءۇ"
                                "ч" -> "چ"
                                "ғ" -> "ع"
                                "ш" -> "ش"
                                "ж" -> "ج"
                                "ң" -> "ڭ"
                                "ь", "ъ", "¬" -> ""
                                else -> chars[j]
                            }
                        }
                        j++
                    }

                    var toteWord = toteChars.joinToString("")
                    if ("ء" in toteWord) {
                        toteWord = toteWord.replace("ء", "")
                        if (!toteWord.contains(Regex("[كگە]"))) toteWord = "ء$toteWord"
                    }
                    toteStrs[i - cyrlWord.length] = replaceDialectWords(toteWord)
                    cyrlWord = ""
                }

                toteStrs[i] = when (chars[i]) {
                    "," -> "،"
                    "?" -> "؟"
                    ";" -> "؛"
                    else -> chars[i]
                }

                prevSound = Sound.Unknown
                continue
            }

            cyrlWord += chars[i]
            prevSound = Sound.Unknown
        }

        toteStrs[toteStrs.size - 1] = ""
        return toteStrs.joinToString("")
    }

    private fun replaceDialectWords(word: String): String {
        var result = dialectWordsDic[word] ?: word
        result = result.replace("(?<=\\w)ۇلى(\\s|\$)".toRegex(), " ۇلى")
        result = result.replace("(?<=\\w)ۇلىنىڭ(\\s|\$)".toRegex(), " ۇلىنىڭ")
        result = result.replace("(?<=\\w)قىزى(\\s|\$)".toRegex(), " قىزى")
        result = result.replace("(?<=\\w)قىزىنىڭ(\\s|\$)".toRegex(), " قىزىنىڭ")
        result = result.replace("(?<=\\w)ەۆ".toRegex(), "يەۆ")
        return result
    }

    private fun copycatCyrlToOriginalCyrl(text: String) = text
        .replace("Ə", "Ә")
        .replace("ə", "ә")
        .replace("Ɵ", "Ө")
        .replace("ɵ", "ө")
}

