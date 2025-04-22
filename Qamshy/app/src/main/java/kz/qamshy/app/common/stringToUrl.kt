package kz.qamshy.app.common

import java.net.MalformedURLException
import java.net.URL

fun stringToUrl(urlString: String?): URL? {
    return try {
        if (!urlString.isNullOrEmpty()) URL(urlString) else null
    } catch (e: MalformedURLException) {
        e.printStackTrace()
        null
    }
}