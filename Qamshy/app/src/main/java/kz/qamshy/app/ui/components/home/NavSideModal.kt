package kz.qamshy.app.ui.components.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.qamshy.app.R
import kz.qamshy.app.common.Cyrl2LatynHelper
import kz.qamshy.app.common.Cyrl2ToteHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.common.openFacebook
import kz.qamshy.app.common.openInstagram
import kz.qamshy.app.common.openTelegram
import kz.qamshy.app.common.openTwitter
import kz.qamshy.app.common.openVK
import kz.qamshy.app.common.openWhatsApp
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.activities.MainActivity
import kz.qamshy.app.ui.components.global.ComposHr
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.CurrencyViewModel


@Composable
fun NavSideModal(context: Context,
                 currentLanguage:LanguageModel,
                 isRtl:Boolean,
                 currencyViewModel: CurrencyViewModel
                 ){
    val currencyList by currencyViewModel.currencyList.collectAsState()
    Spacer(modifier = Modifier.height(54.dp))

    Column (
        modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp)
    ){
        Row (
            modifier = Modifier.weight(0.1f)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.Center
        ){

            Image(
                painter = painterResource(id = R.drawable.logo_dark),
                contentDescription = "dark logo",
                modifier = Modifier
                    .padding(0.dp)
                    .width(143.09062.dp)
                    .height(57.dp)
                    .clickable (
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ){
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }
            )
        }
        Row(
            modifier = Modifier.weight(0.1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            ComposHr()
        }
        Column(
            modifier = Modifier.weight(0.3f)
                .fillMaxWidth(),
        ){
            Row(modifier = Modifier.weight(0.25f)
            .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ){
   val settingText  = when(currentLanguage.languageCulture){
       "tote" -> Cyrl2ToteHelper.cyrl2Tote("Баптаулар")
       "latyn" -> Cyrl2LatynHelper.cyrl2Latyn("Баптаулар")
       else -> T("ls_Settings2",currentLanguage)
   }
                Image(
                    painter = painterResource(id = R.drawable.back_right_icon),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = settingText,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF363636),
                    )
                )

            }
            Row(modifier = Modifier.weight(0.25f)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ){
                Image(
                    painter = painterResource(id = R.drawable.back_right_icon),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = T("ls_Abouttheproject",currentLanguage),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF363636),
                    )
                )
            }
            Row(modifier = Modifier.weight(0.25f)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                ){
                Image(
                    painter = painterResource(id = R.drawable.back_right_icon),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = T("ls_Submitnews",currentLanguage),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF363636),
                    )
                )
            }
            Row(modifier = Modifier.weight(0.25f)
                .fillMaxWidth()
                ,){
                ComposHr()
            }
        }
        Column(
            modifier = Modifier.weight(0.2f)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.3f),
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    text = T("ls_Exchangerate",currentLanguage),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF363636),
                    ),

                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.4f)
            ){
                currencyList.forEach(){ currency ->
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currency.flagUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .build()
                    )
                    Column(
                        modifier = Modifier.weight(0.33f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = painter,
                            contentDescription = "usd",
                            modifier = Modifier
                                .padding(0.dp)
                                .width(25.dp)
                                .height(25.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = currency.title + "${currency.rate}",
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = PrimaryFontFamily,
                                fontWeight = FontWeight(500),
                                color = Color(0xFF363636),
                                textAlign = TextAlign.Center
                            )
                        )
                    }

                }

            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.3f),
                verticalAlignment = Alignment.CenterVertically
            ){
                ComposHr()
            }

        }
        Column (
            modifier = Modifier.weight(0.3f)
                .fillMaxWidth(),
        ){
            Row(
                modifier = Modifier.weight(0.15f).fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    text = T("ls_Oursocialnetworks",currentLanguage),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF363636),
                    )
                )
            }
            Column (
                modifier = Modifier.weight(0.5f)
            ){
                Row(
                    modifier = Modifier.weight(0.4f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Image(
                        painter = painterResource(id = R.drawable.telegream_icon),
                        contentDescription = "telegram",
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                openTelegram(context, "qamshy","https://t.me/qamshy")
                            }
                    )
                    Spacer(modifier = Modifier.width(23.dp))
                    Image(
                        painter = painterResource(id = R.drawable.whatsapp_icon),
                        contentDescription = "whatsapp",
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                openWhatsApp(context,"+77071200871","https://api.whatsapp.com/send?phone=77071200871&amp;text=")
                            }
                    )
                    Spacer(modifier = Modifier.width(23.dp))
                    Image(
                        painter = painterResource(id = R.drawable.facebook_icon),
                        contentDescription = "facebook",
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                openFacebook(context,"https://www.facebook.com/qamshy")
                            }
                    )

                }
                Row(
                    modifier = Modifier.weight(0.1f).fillMaxWidth()
                ){}
                Row(
                    modifier = Modifier.weight(0.5f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Image(
                        painter = painterResource(id = R.drawable.tiwiter_icon),
                        contentDescription = "tiwiter_icon",
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                openTwitter(context,"qamshy","https://www.facebook.com/qamshy")
                            }
                    )
                    Spacer(modifier = Modifier.width(23.dp))
                    Image(
                        painter = painterResource(id = R.drawable.vk_icon),
                        contentDescription = "vk_icon",
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                openVK(context,"https://vk.com/qamshyagency","https://vk.com/qamshyagency")
                            }
                    )
                    Spacer(modifier = Modifier.width(23.dp))
                    Image(
                        painter = painterResource(id = R.drawable.instagram_icon),
                        contentDescription = "instagram",
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ){
                                openInstagram(context,"qamshy_media","https://www.instagram.com/")
                            }
                    )
                }
            }
            Column (
                modifier = Modifier.weight(0.15f).fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ){
                ComposHr()
            }
            Column (
                modifier = Modifier.weight(0.2f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = T("ls_Allrightsreserved",currentLanguage),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF747474),
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Qamshy.kz 2025",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF747474),
                    )
                )
            }
        }
    }
}

