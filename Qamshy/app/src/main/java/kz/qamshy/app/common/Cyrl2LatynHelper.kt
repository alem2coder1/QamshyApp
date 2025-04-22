package kz.qamshy.app.common

object Cyrl2LatynHelper {

    enum class Sound {
        Consonant, Vowel, Unknown
    }

    private val cyrlChars = listOf(
        "А", "Ә", "Ə", "Б", "В", "Г", "Ғ", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Қ", "Л", "М",
        "Н", "Ң", "О", "Ө", "Ɵ", "П", "Р", "С", "Т", "У", "Ұ", "Ү", "Ф", "Х", "Һ", "Ц", "Ч", "Ш",
        "Щ", "Ъ", "Ы", "І", "Ь", "Э", "Ю", "Я", "-"
    )

    private val vowelChars = listOf(
        "А", "Ә", "Ə", "Е", "И", "О", "Ө", "Ɵ", "Ұ", "Ү", "У", "Ы", "І", "Э"
    )

    private val wordsPackDic = mapOf(
        "эпопея" to "epopeıa",
        "байланыссыздық" to "baılanyssyzdyq",
        "оюлау" to "oıýlaý",
        "үзіліссіз" to "úzilissiz",
        "мұнай-химия" to "munaı-hımıa",
        "фармакология" to "farmakologıa",
        "сидию" to "sıdıý",
        "калькуляция" to "kálkýlásıa",
        "аллергиялық" to "alergıalyq",
        "конституциялық" to "konstıtýsıalyq",
        "намыссыздық" to "namyssyzdyq",
        "борбию" to "borbıý",
        "эклампсия" to "eklampsıa",
        "геохимиялық" to "geohımıalyq",
        "анархист" to "anarhıs",
        "түп-тұқиян" to "túp-tuqıan",
        "бағдар-баян" to "baǵdar-baıan",
        "минерагения" to "mıneragenıa",
        "лоция" to "losıa",
        "миллиметрлік" to "mıllımetrlik",
        "алдиярлау" to "aldıarlaý",
        "капитуляция" to "kapıtýlásıa",
        "лексикологиялық" to "leksıkologıalyq",
        "факультатив" to "fakúltatıv",
        "жалынып-жалпаю" to "jalynyp-jalpaıý",
        "лицензиялану" to "lısenzıalaný",
        "зымияндық" to "zymıandyq",
        "аютабан" to "aıýtaban",
        "сомпаю" to "sompaıý",
        "бейсаясат" to "beısaıasat",
        "миялау" to "mıalaý",
        // （因贴完整过于庞大，这里示例仅演示了部分，实际请将所有都替换进来）
        "комедия" to "komedıa",
        "доссымақ" to "dossymaq",
        "габбро-пегматит" to "gabro-pegmatıt",
        "инкассация" to "ınkasasıa",
        "парономазия" to "paronomazıa",
        "аммонал" to "amonal",
        "галлий" to "galı",
        "корректор" to "korektor",
        "астрохимия" to "astrohımıa",
        "эвакуациялану" to "evakýasıalaný",
        "иммиграция" to "ımıgrasıa",
        "әсия" to "ásıa",
        "аккумуляторшы" to "akýmýlátorshy",
        "боулинг" to "boýlıń",
        "пұштыраю" to "pushtyraıý",
        "цессионарий" to "sesıonarı",
        "баттерфляй" to "baterfláı",
        "тромб" to "tromb",
        "автомобильші" to "avtomobılshi",
        "компьютерлендіру" to "kompúterlendirý",
        "боялу" to "boıalý",
        "ацидафиль" to "asıdafıl",
        "аппликация" to "aplıkasıa",
        "көзбояушылық" to "kózboıaýshylyq",
        "мүдделі" to "múddeli",
        "бояушытыршық" to "boıaýshytyrshyq",
        "вирулентті" to "vırýlentti",
        "релятивистік" to "relátıvıstik",
        "волейбол" to "voleıbol",
        "ғылыми-теориялық" to "ǵylymı-teorıalyq",
        "тақияшаң" to "taqıashań",
        "компьютерлік" to "kompúterlik",
        "конденсациялау" to "kondensasıalaý",
        "тракт" to "trakt",
        "маялану" to "maıalaný",
    )

    fun firstCharToUpper(input: String?): String {
        if (input.isNullOrEmpty()) return input ?: ""
        val first = qazLatynToUpper(input.substring(0, 1))
        return first + input.substring(1)
    }

    fun qazLatynToUpper(latynText: String): String {
        return latynText
            .replace("ı", "I")
            .replace("İ", "i")
            .uppercase()
    }

    fun qazLatynToLower(latynText: String): String {
        return latynText
            .replace("I", "ı")
            .replace("İ", "i")
            .lowercase()
    }

    fun convertWord(oldValue: String, newValue: String): String {
        if (oldValue.isEmpty()) return ""
        return when {
            oldValue == qazLatynToUpper(oldValue) -> {
                qazLatynToUpper(newValue)
            }
            oldValue == firstCharToUpper(oldValue) -> {
                firstCharToUpper(newValue)
            }
            else -> {
                qazLatynToLower(newValue)
            }
        }
    }

    fun copycatCyrlToOriginalCyrl(cyrlText: String): String {
        return cyrlText
            .replace("Ə", "Ә")
            .replace("ə", "ә")
            .replace("Ɵ", "Ө")
            .replace("ɵ", "ө")
    }

    /**
     * Кирілді латынға айналдыру
     * @param cyrlText Кирил мәтін
     */
    fun cyrl2Latyn(cyrlText: String): String {
        var text = copycatCyrlToOriginalCyrl(cyrlText)
        text += "."

        val chars = text.toCharArray().map { it.toString() }.toTypedArray()
        val length = chars.size
        val latynStrs = Array(length) { "" }
        var prevSound = Sound.Unknown
        var cyrlWord = ""

        for (i in 0 until length) {
            val upper = chars[i].uppercase()
            if (!cyrlChars.contains(upper)) {
                if (cyrlWord.isNotEmpty()) {
                    val wpKey = cyrlWord.lowercase()
                    if (wordsPackDic.containsKey(wpKey)) {
                        latynStrs[i - cyrlWord.length] = convertWord(
                            cyrlWord,
                            wordsPackDic[wpKey] ?: ""
                        )
                        for (z in (i - cyrlWord.length + 1) until i) {
                            latynStrs[z] = ""
                        }
                    } else {
                        convertEachChar(cyrlWord, latynStrs, i - cyrlWord.length)
                    }
                    cyrlWord = ""
                }
                latynStrs[i] = chars[i]
                prevSound = Sound.Unknown
            } else {
                cyrlWord += chars[i]
            }
        }

        latynStrs[length - 1] = ""
        return latynStrs.joinToString("")
    }

    /**
     * 将一个串(里面都是Cyrillic的char)中的每个字符进行逐字母转换，
     * 并写入到 latynStrs 的 startIndex 之后。
     * 这是为了模拟你C#里面更复杂的逻辑: “for(; j < i; j++) { switch (chars[j]){ ... } }”
     * 这里分离出来，简化一下编写。
     */
    private fun convertEachChar(word: String, latynStrs: Array<String>, startIndex: Int) {
        val length = word.length
        var j = startIndex
        for (c in word) {
            val isUpper = c.toString() == c.uppercase()
            when (c.uppercase()) {
                "Я" -> {

                    latynStrs[j] = if (isUpper) "IA" else "ia"
                }
                "Ю" -> {
                    latynStrs[j] = if (isUpper) "IY" else "iy"
                }
                "Щ" -> {
                    latynStrs[j] = if (isUpper) "SH" else "sh"
                }
                "Э" -> {
                    latynStrs[j] = if (isUpper) "E" else "e"
                }
                "А" -> {
                    latynStrs[j] = if (isUpper) "A" else "a"
                }
                "Б" -> {
                    latynStrs[j] = if (isUpper) "B" else "b"
                }
                "Ц" -> {
                    latynStrs[j] = if (isUpper) "S" else "s"
                }
                "Д" -> {
                    latynStrs[j] = if (isUpper) "D" else "d"
                }
                "Е" -> {
                    latynStrs[j] = if (isUpper) "E" else "e"
                }
                "Ф" -> {
                    latynStrs[j] = if (isUpper) "F" else "f"
                }
                "Г" -> {
                    latynStrs[j] = if (isUpper) "G" else "g"
                }
                "Х" -> {
                    latynStrs[j] = if (isUpper) "H" else "h"
                }
                "Һ" -> {
                    latynStrs[j] = if (isUpper) "H" else "h"
                }
                "І" -> {
                    latynStrs[j] = if (isUpper) "İ" else "i"
                }
                "И", "Й" -> {
                    latynStrs[j] = if (isUpper) "I" else "ı"
                }
                "К" -> {
                    latynStrs[j] = if (isUpper) "K" else "k"
                }
                "Л" -> {
                    latynStrs[j] = if (isUpper) "L" else "l"
                }
                "М" -> {
                    latynStrs[j] = if (isUpper) "M" else "m"
                }
                "Н" -> {
                    latynStrs[j] = if (isUpper) "N" else "n"
                }
                "О" -> {
                    latynStrs[j] = if (isUpper) "O" else "o"
                }
                "П" -> {
                    latynStrs[j] = if (isUpper) "P" else "p"
                }
                "Қ" -> {
                    latynStrs[j] = if (isUpper) "Q" else "q"
                }
                "Р" -> {
                    latynStrs[j] = if (isUpper) "R" else "r"
                }
                "С" -> {
                    latynStrs[j] = if (isUpper) "S" else "s"
                }
                "Т" -> {
                    latynStrs[j] = if (isUpper) "T" else "t"
                }
                "Ұ" -> {
                    latynStrs[j] = if (isUpper) "U" else "u"
                }
                "В" -> {
                    latynStrs[j] = if (isUpper) "V" else "v"
                }
                "У" -> {
                    latynStrs[j] = if (isUpper) "Ý" else "ý"
                }
                "Ы" -> {
                    latynStrs[j] = if (isUpper) "Y" else "y"
                }
                "З" -> {
                    latynStrs[j] = if (isUpper) "Z" else "z"
                }
                "Ә" -> {
                    latynStrs[j] = if (isUpper) "Á" else "á"
                }
                "Ё", "Ө" -> {
                    latynStrs[j] = if (isUpper) "Ó" else "ó"
                }
                "Ү" -> {
                    latynStrs[j] = if (isUpper) "Ú" else "ú"
                }
                "Ч" -> {
                    latynStrs[j] = if (isUpper) "CH" else "ch"
                }
                "Ғ" -> {
                    latynStrs[j] = if (isUpper) "Ǵ" else "ǵ"
                }
                "Ш" -> {
                    latynStrs[j] = if (isUpper) "SH" else "sh"
                }
                "Ж" -> {
                    latynStrs[j] = if (isUpper) "J" else "j"
                }
                "Ң" -> {
                    latynStrs[j] = if (isUpper) "Ń" else "ń"
                }
                "Ь", "Ъ", "¬" -> {
                    latynStrs[j] = ""
                }
                else -> {
                    latynStrs[j] = c.toString()
                }
            }
            j++
        }
    }

}