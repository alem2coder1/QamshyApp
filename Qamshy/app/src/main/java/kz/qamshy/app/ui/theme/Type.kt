package kz.qamshy.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kz.qamshy.app.R
import kz.qamshy.app.ui.QamshyApp


val PrimaryFontFamily: FontFamily
    @Composable get() {
        val language by QamshyApp.currentLanguage.collectAsState()
        return when (language.languageCulture) {
            "tote" -> FontFamily(Font(R.font.kzsoftnet, FontWeight.Normal))
            else -> FontFamily(
                Font(R.font.inter_font, FontWeight.Normal),
                Font(R.font.suisse_intl_semi_bold, FontWeight.Bold),
                Font(R.font.suisse_intl_medium, FontWeight.Medium)
            )
        }
    }


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)