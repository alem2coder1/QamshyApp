package kz.qamshy.app.common

import android.content.Context
import android.content.Intent
import kz.qamshy.app.models.site.ArticleModel

//fun shareArticle(context: Context, article: ArticleModel) {
//    val intent = Intent(Intent.ACTION_SEND).apply {
//        type = "text/plain"
//        putExtra(Intent.EXTRA_SUBJECT, article.title)
//        putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${}")
//    }
//    context.startActivity(Intent.createChooser(intent, "Share via"))
//}