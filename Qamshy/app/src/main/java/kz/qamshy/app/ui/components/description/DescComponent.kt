package kz.qamshy.app.ui.components.description

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kz.qamshy.app.R
import kz.qamshy.app.common.toAnnotatedString
import kz.qamshy.app.models.site.ArticleModel
import kz.qamshy.app.ui.theme.PrimaryFontFamily

@Composable
 fun DescComponent(
    articleModel: ArticleModel, ){

     Column(modifier = Modifier.fillMaxWidth()) {

         Text(
             text = articleModel.title,
             style = TextStyle(
                 fontSize = 20.sp,
                 lineHeight = 24.sp,
                 fontFamily = PrimaryFontFamily,
                 fontWeight = FontWeight(600),
                 color = Color(0xFF000000),
             )
         )
         Spacer(modifier = Modifier.height(20.dp))

         Row(modifier = Modifier.fillMaxWidth()){
             Image(
                 painter = painterResource(id = R.drawable.calendar),
                 contentDescription = "calender",
                 modifier = Modifier.size(14.dp)
             )
             Spacer(modifier = Modifier.width(4.dp))
             Text(
                 text = articleModel.addTime,
                 style = TextStyle(
                     fontSize = 11.sp,
                     lineHeight = 14.3.sp,
                     fontFamily = PrimaryFontFamily,
                     fontWeight = FontWeight(400),
                     color = Color(0xFF747474),
                 )
             )
         }

         Spacer(modifier = Modifier.height(15.dp))

         Row(modifier = Modifier.fillMaxWidth().height(198.dp)){

             val painter = rememberAsyncImagePainter(
                 model = ImageRequest.Builder(LocalContext.current)
                     .data(articleModel.thumbnailUrl)
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
             val spannedText = HtmlCompat.fromHtml(
                 articleModel.fullDescription,
                 HtmlCompat.FROM_HTML_MODE_LEGACY
             )
             val text = spannedText.toAnnotatedString()
             Text(
                 text = text,
              style = TextStyle(
                     fontSize = 15.sp,
                     lineHeight = 24.15.sp,
                     fontFamily = PrimaryFontFamily,
                     fontWeight = FontWeight(400),
                     color = Color(0xFF000000),

                     )
             )
         }


     }

 }