package kz.qamshy.app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay
import kz.qamshy.app.R
import kz.qamshy.app.ui.theme.PrimaryFontFamily

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.Transparent.toArgb()

        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF84C3E6)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Image(
                painter = painterResource(id = R.drawable.start_logo),
                contentDescription = "App Logo",
            )
            Spacer(modifier = Modifier.height(19.dp))
            Text(
                text = "Қамшы - Ұл болсаң қолыңда, құл болсаң төбеңде ойнайды!Qamshy News",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 21.sp,
                    fontFamily = PrimaryFontFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }
    }
}