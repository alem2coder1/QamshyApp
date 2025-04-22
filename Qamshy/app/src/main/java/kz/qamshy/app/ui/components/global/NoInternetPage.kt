package kz.qamshy.app.ui.components.global

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.qamshy.app.R
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.theme.PrimaryFontFamily


@Composable
fun NoInternetPage(){
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        val currentLanguage by QamshyApp.currentLanguage.collectAsState()
        Image(
            painter = painterResource(id = R.drawable.no_wifi_svgrepo_com),
            contentDescription = "no internet",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ){
            Text(
                text = "Желілік сұрау сәтсіз аяқталды, желіңізді тексеріңіз",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = PrimaryFontFamily,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center
                ),
            )
        }

    }
}