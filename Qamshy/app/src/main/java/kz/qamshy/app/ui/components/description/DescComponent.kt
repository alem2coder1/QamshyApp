package kz.qamshy.app.ui.components.description

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kz.qamshy.app.R
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.common.WebViewWithHeaders
import kz.qamshy.app.common.shareArticle
import kz.qamshy.app.models.site.DescriptionModel
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.DescriptionViewModel

@Composable
 fun DescComponent(
    articleModel: DescriptionModel,
    context:Context,
    viewModel:DescriptionViewModel,
    ){
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()
     Column(modifier = Modifier.fillMaxWidth()) {

         Text(
             text = articleModel.article.title,
             style = TextStyle(
                 fontSize = 20.sp,
                 lineHeight = 24.sp,
                 fontFamily = PrimaryFontFamily,
                 fontWeight = FontWeight(600),
                 color = Color(0xFF000000),
             )
         )
         Spacer(modifier = Modifier.height(20.dp))

         Row(modifier = Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.SpaceBetween,
             verticalAlignment = Alignment.CenterVertically
             ){
             Row {
                 Image(
                     painter = painterResource(id = R.drawable.calendar),
                     contentDescription = "calender",
                     modifier = Modifier.size(14.dp)
                 )
                 Spacer(modifier = Modifier.width(4.dp))
                 Text(
                     text = articleModel.article.addTime,
                     style = TextStyle(
                         fontSize = 11.sp,
                         lineHeight = 14.3.sp,
                         fontFamily = PrimaryFontFamily,
                         fontWeight = FontWeight(400),
                         color = Color(0xFF747474),
                     )
                 )
             }

             Row {
                 Image(
                     painter = painterResource(id = R.drawable.eye_icon),
                     contentDescription = "calender",
                     modifier = Modifier.size(14.dp)
                 )
                 Spacer(modifier = Modifier.width(4.dp))
                 Text(
                     text = "${articleModel.article.viewCount}",
                     style = TextStyle(
                         fontSize = 11.sp,
                         lineHeight = 14.3.sp,
                         fontFamily = PrimaryFontFamily,
                         fontWeight = FontWeight(400),
                         color = Color(0xFF747474),
                     )
                 )


             }
         }

         Spacer(modifier = Modifier.height(15.dp))

         Row(modifier = Modifier.fillMaxWidth().height(198.dp)){

             val painter = rememberAsyncImagePainter(
                 model = ImageRequest.Builder(LocalContext.current)
                     .data(articleModel.article.thumbnailUrl)
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

         Spacer(modifier = Modifier.height(22.dp))
         Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()){
             WebViewWithHeaders(
                 articleModel.article.latynUrl,
                 extraHeaders = mapOf(
                     "app" to "android"
                 )
             )
         }
         Spacer(modifier = Modifier.height(20.dp))
         if (articleModel.tagList.isNotEmpty()) {
             @OptIn(ExperimentalLayoutApi::class)
             (FlowRow
                 (
                 horizontalArrangement = Arrangement.spacedBy(8.dp),
                 verticalArrangement = Arrangement.spacedBy(8.dp),
                 maxItemsInEachRow = Int.MAX_VALUE,
                 modifier = Modifier.fillMaxWidth()
             ) {
                 articleModel.tagList.forEach { tag ->
                     Column(
                         modifier = Modifier
                             .wrapContentWidth()
                             .height(31.dp)
                             .background(color = Color(0x0D58A0C8), shape = RoundedCornerShape(size = 10.dp))
                             .padding(horizontal = 10.dp)
                             .clickable(
                                 indication = null,
                                 interactionSource = remember { MutableInteractionSource() }
                             ) {
                                 viewModel.navigateToSearchActivity(context = context,tagId = tag.id,tag.title)
                             }
                         ,
                         horizontalAlignment = Alignment.CenterHorizontally,
                         verticalArrangement = Arrangement.Center
                     ) {
                         Text(
                             text = tag.title,
                             style = TextStyle(
                                 fontSize = 11.sp,
                                 lineHeight = 17.71.sp,
                                 fontFamily = PrimaryFontFamily,
                                 fontWeight = FontWeight(400),
                                 color = Color(0xFF363636),
                             )
                         )
                     }
                 }
             })
         }
         Spacer(modifier = Modifier.height(20.dp))
         Row(modifier = Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.SpaceBetween,
             verticalAlignment = Alignment.CenterVertically
             ){
             Row(
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Image(
                     painter = painterResource(id = R.drawable.avatar_icon),
                     contentDescription = "login",
                     modifier = Modifier.size(24.dp)
                 )
                 Spacer(modifier = Modifier.width(10.dp))
                 Text(
                     text = articleModel.article.author,
                     style = TextStyle(
                         fontSize = 12.sp,
                         lineHeight = 15.6.sp,
                         fontFamily = PrimaryFontFamily,
                         fontWeight = FontWeight(400),
                         color = Color(0xFF747474),
                     )
                 )
             }
             Image(
                 painter = painterResource(id = R.drawable.share_icon),
                 contentDescription = "share",
                 modifier = Modifier.size(24.dp)
                     .clickable(
                         indication = null,
                         interactionSource = remember { MutableInteractionSource() }
                     ) {
                         shareArticle(context,articleModel.article)
                     }
             )
         }
         Spacer(modifier = Modifier.height(20.dp))
         Row(modifier = Modifier.fillMaxWidth()){
             Text(
                 text = T("ls_Relatedarticles",currentLanguage),
                 style = TextStyle(
                     fontSize = 18.sp,
                     fontFamily = PrimaryFontFamily,
                     fontWeight = FontWeight(600),
                     color = Color(0xFF000000),
                 )
             )
         }
         Spacer(modifier = Modifier.height(20.dp))
         if (articleModel.reletedArticleList.isNotEmpty()) {
            articleModel.reletedArticleList.forEach(){ article ->
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
                        .padding(vertical = 14.dp),
                    thickness = 1.dp,
                    color = Color(0xFFC1C1C1).copy(alpha = 0.5f)
                )
            }
         }

     }

 }