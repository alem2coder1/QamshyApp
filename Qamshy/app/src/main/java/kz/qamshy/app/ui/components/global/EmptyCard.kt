package kz.qamshy.app.ui.components.global

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.qamshy.app.R
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.theme.PrimaryFontFamily

@Composable
fun EmptyCard(currentLanguageModel: LanguageModel){
    Column (modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.empty_icon),
                contentDescription = "empty list",
                modifier = Modifier.width(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text= T("ls_Contentnotfound",currentLanguageModel),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontFamily = PrimaryFontFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF353535),
                )
            )
        }
    }
}