package kz.qamshy.app.ui.components.category

import android.content.Context
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.android.datatransport.runtime.dagger.Component
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.site.ArticleListModel
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.ui.components.global.EmptyCard
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.NewsViewModel

@Composable
fun NewsComponent(
    viewModel: NewsViewModel,
    articleLists : List<ArticleModel>,
    context: Context,
    currentLanguage : LanguageModel
){
    val isTop by viewModel.isTop.collectAsState()
    val backColor = if(isTop) Color(0xFF58A0C8) else Color.White
    val newsBac =  if(!isTop) Color(0xFF58A0C8) else Color.White
    val textStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 15.6.sp,
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight(600),
        color = if(isTop) Color(0xFFFFFFFF) else Color(0xFF000000),
    )
    val newTextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 15.6.sp,
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight(600),
        color = if(!isTop) Color(0xFFFFFFFF) else Color(0xFF000000),
    )

    val listState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()){
        Spacer(modifier = Modifier.height(54.dp))
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(30.dp)
            .border(width = 1.dp, color = Color(0xFF58A0C8), shape = RoundedCornerShape(size = 4.dp))


        ){
            Column (modifier= Modifier.weight(0.5f).fillMaxHeight()
                .background(newsBac, shape = RoundedCornerShape(3.dp))
                .clickable (
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ){
                    viewModel.updateIsTop(false)
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = T("ls_Latestnews", currentLanguage),
                    style = newTextStyle
                )
            }
            Column (modifier= Modifier.weight(0.5f).fillMaxHeight()
                .background(backColor, shape = RoundedCornerShape(3.dp))
                .clickable (
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ){
                    viewModel.updateIsTop(true)
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = T("ls_Mostviewed",currentLanguage),
                    style = textStyle
                )
            }

        }
        Spacer(modifier = Modifier.height(31.dp))

        Column {
            if (articleLists.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(articleLists) { article ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    viewModel.navigateToDescActivity(context,article.id)
                                }
                        ) {
                            Column(modifier = Modifier.weight(0.4f)){
                                val painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(
                                        LocalContext.current)
                                        .data(article.thumbnailUrl)
                                        .decoderFactory(
                                            SvgDecoder.Factory())
                                        .build()
                                )
                                Image(
                                    painter = painter,
                                    contentDescription = "pinned image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.width(124.dp)
                                        .height(68.dp)
                                        .clip(RoundedCornerShape(10.dp))
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
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp)
                                .height(1.dp),
                                    color = Color(0xFFC1C1C1).copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                EmptyCard(currentLanguage)
            }
        }
    }


}