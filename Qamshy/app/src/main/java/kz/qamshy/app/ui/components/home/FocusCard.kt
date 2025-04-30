package kz.qamshy.app.ui.components.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.HomeViewModel

@Composable
fun FocusCard(
    currentLanguage: LanguageModel,
    context: Context,
    focusList: List<ArticleModel>,viewModel: HomeViewModel
){
    Spacer(modifier = Modifier.height(11.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        focusList.take(2).forEach { article ->
            FocusCardItem(
                article = article,
                modifier = Modifier.weight(1f),
                viewModel,
                context
            )
        }
    }
}

@Composable
fun FocusCardItem(article: ArticleModel, modifier: Modifier = Modifier, viewModel: HomeViewModel, context: Context) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    viewModel.navigateToDescActivity(context,article.id)
                }
        ) {
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
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(20.dp)
                        .background(Color(0xFF00D084), shape = RoundedCornerShape(5.dp))
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = article.categoryTitle,
                        fontSize = 8.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = article.title,
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = PrimaryFontFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFF000000),
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = article.addTime,
            style = TextStyle(
                fontSize = 8.sp,
                fontFamily = PrimaryFontFamily,
                fontWeight = FontWeight(400),
                color = Color(0xFF747474),
            )
        )
    }
}