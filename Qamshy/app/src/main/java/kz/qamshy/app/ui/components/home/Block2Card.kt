package kz.qamshy.app.ui.components.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.site.ArticleBlockModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kz.qamshy.app.ui.theme.PrimaryFontFamily

@Composable
fun Block2Card(
    currentLanguage: LanguageModel,
    context: Context,
    articleBlock: ArticleBlockModel
){
LazyColumn {
    items(articleBlock.articleList){ article ->
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(0.4f)){
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(article.thumbnailUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = "pinned image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.weight(0.6f)) {
                Text(
                    text = article.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.8.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF363636),
                    )
                )
                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = article.addTime,
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF535353),
                    )
                )
            }


        }

    }
}
}