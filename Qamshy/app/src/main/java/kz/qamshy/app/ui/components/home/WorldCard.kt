package kz.qamshy.app.ui.components.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.site.ArticleBlockModel
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.ui.theme.PrimaryFontFamily

@Composable
fun WorldCardRow(
    currentLanguage: LanguageModel,
    context: Context,
    articleBlock: ArticleBlockModel
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp) // 卡片之间的间距
    ) {
        item{
            Spacer(modifier = Modifier.width(12.dp))
        }
        items(articleBlock.articleList) { article ->
            WorldCard(article)
        }
        item{
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
fun WorldCard(article: ArticleModel) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(173.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { /* 处理点击事件 */ }
    ) {
        // 图片背景
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.thumbnailUrl)
                .decoderFactory(SvgDecoder.Factory())
                .build()
        )

        Image(
            painter = painter,
            contentDescription = "article image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 100f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = article.title,
                fontSize = 14.sp,
                fontFamily = PrimaryFontFamily,
                fontWeight = FontWeight(600),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}