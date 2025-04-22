package kz.qamshy.app.ui.components.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.ui.theme.PrimaryFontFamily

@Composable
fun FocusCard(
    currentLanguage: LanguageModel,
    context: Context,
    focusList: List<ArticleModel>
){
    Row (modifier = Modifier.fillMaxWidth()){
        focusList.forEach { article ->
            FocusCardItem(article = article, modifier = Modifier.weight(0.5f))

        }
    }


}

@Composable
fun FocusCardItem(article:ArticleModel,modifier : Modifier){
    Column(modifier = modifier){
        Row(
            modifier = modifier.fillMaxWidth().height(96.dp)
        ){
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
        Text(
            text = "Қазақстандықтар жеке куәліктерін қашан онлайн ауыстыра алады",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PrimaryFontFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFF000000),
            )
        )
    }
}