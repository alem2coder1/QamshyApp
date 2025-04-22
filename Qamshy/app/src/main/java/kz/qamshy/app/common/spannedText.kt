package kz.qamshy.app.common

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.text.style.URLSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.getSpans

fun Spanned.toAnnotatedString(): androidx.compose.ui.text.AnnotatedString {
    val builder = androidx.compose.ui.text.AnnotatedString.Builder(this.toString())

    val spans = getSpans<Any>(0, length)
    for (span in spans) {
        val start = getSpanStart(span)
        val end = getSpanEnd(span)

        when (span) {
            is StyleSpan -> {
                when (span.style) {
                    Typeface.BOLD -> builder.addStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold),
                        start = start,
                        end = end
                    )
                    Typeface.ITALIC -> builder.addStyle(
                        style = SpanStyle(fontStyle = FontStyle.Italic),
                        start = start,
                        end = end
                    )
                }
            }
            is UnderlineSpan -> {
                builder.addStyle(
                    style = SpanStyle(textDecoration = TextDecoration.Underline),
                    start = start,
                    end = end
                )
            }
            is StrikethroughSpan -> {
                builder.addStyle(
                    style = SpanStyle(textDecoration = TextDecoration.LineThrough),
                    start = start,
                    end = end
                )
            }
            is ForegroundColorSpan -> {
                builder.addStyle(
                    style = SpanStyle(color = Color(span.foregroundColor)),
                    start = start,
                    end = end
                )
            }
            is BackgroundColorSpan -> {
                builder.addStyle(
                    style = SpanStyle(background = Color(span.backgroundColor)),
                    start = start,
                    end = end
                )
            }
            is URLSpan -> {
                builder.addStringAnnotation(
                    tag = "URL",
                    annotation = span.url,
                    start = start,
                    end = end
                )
                builder.addStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = start,
                    end = end
                )
            }
        }
    }

    return builder.toAnnotatedString()
}