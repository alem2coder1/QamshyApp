package kz.qamshy.app.ui.components.home

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.qamshy.app.R
import kz.qamshy.app.common.Translator
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.activities.MainActivity
import kz.qamshy.app.ui.components.global.LanguageModal
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.ui.theme.selectedColor
import kz.qamshy.app.viewmodels.HomeViewModel
import kz.sira.app.viewmodels.LanguageModalViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeNav(
    isDarkMode: Boolean,
    currentLanguage: LanguageModel,
    context: Context,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    drawerState: DrawerState, scope: CoroutineScope
    ){
    var showLanguageModal by remember { mutableStateOf(false) }
  val notifiPainter = if(!isDarkMode) painterResource(id = R.drawable.notification_icon) else painterResource(id = R.drawable.notification_dark)
   val sidePaitner = if(isDarkMode) painterResource(id = R.drawable.side_dark_icon) else painterResource(id = R.drawable.side_icon)
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            modifier = Modifier.weight(0.5f)
        ){
            Image(
                painter = painterResource(id = R.drawable.logo_dark),
                contentDescription = "dark logo",
                modifier = Modifier.clickable (
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
        Row (
            modifier = Modifier.weight(0.5f),
           horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = currentLanguage.shortName,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    fontFamily = PrimaryFontFamily,
                    fontWeight = FontWeight(500),
                    color = selectedColor,
                ),
                modifier = Modifier
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ){ showLanguageModal = true }
            )
            if (showLanguageModal) {
                val viewModel: LanguageModalViewModel = koinViewModel()
                LanguageModal(
                    onDismissRequest = { showLanguageModal = false },
                    onLanguageSelected = { language ->
                        Translator.loadLanguagePack(language) { success ->
                            if (success) {
                                QamshyApp.updateLanguage(language)
                                showLanguageModal = false
                            }
                        }
                    },
                    viewModel = viewModel
                )
            }

            Spacer(
                modifier = Modifier.width(15.dp)
            )
            Box (
                modifier = Modifier
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ){

                    }

            ){
                Image(
                    painter = notifiPainter,
                    contentDescription = "notification",
                    modifier = Modifier
                        .size(24.dp)

                )
                val CustomAlignment = Alignment { size, space, _ ->
                    val x = (space.width - size.width) * 1.0f
                    val y = (space.height - size.height) * -0.2f
                    IntOffset(x.toInt(), y.toInt())
                }
//                if(messageCount > 0)

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .align(CustomAlignment)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.has_notifi_icon),
                            contentDescription = "notification",
                            modifier = Modifier.fillMaxSize()
                        )

                    }


            }
            Spacer(
                modifier = Modifier.width(15.dp)
            )
            Image(
                painter =sidePaitner ,
                contentDescription = "notification",
                modifier = Modifier.size(24.dp)
                    .clickable {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }

            )
        }
        

    }
}