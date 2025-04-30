package kz.qamshy.app.ui.activities

import android.content.Context
import android.os.Bundle
import android.os.Message
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kz.qamshy.app.R
import kz.qamshy.app.common.CircularBarsLoading
import kz.qamshy.app.common.ThemeHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.site.PushItem
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.ui.theme.QamshyTheme
import kz.qamshy.app.viewmodels.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationActivity: AppCompatActivity() {
    private val viewModel: NotificationViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val currentLanguage by QamshyApp.currentLanguage.collectAsState()
            val requiredRole = intent.getStringExtra("required_role")
            QamshyTheme{
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { paddingValues ->

                        Message(
                            currentLanguage = currentLanguage,
                            viewModel = viewModel,
                            modifier = Modifier.padding(paddingValues),
                            context
                        )
                    }
                )
            }
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun Message(
    currentLanguage: LanguageModel,
    viewModel: NotificationViewModel,
    modifier: Modifier = Modifier,
    context:Context
) {
    val notificationList by viewModel.notificationList.collectAsState()
    val isLoadingIndicator by viewModel.isLoadingIndicator.collectAsState()  // 全屏加载用
    val isRefreshing by viewModel.isRefreshing.collectAsState()             // 下拉刷新用

    val themeHelper = ThemeHelper(context)
    val isDarkMode = themeHelper.isDarkModeEnabled()

    val notiFullBac = Color(0xFFF2F5F9)
    val notifiBac = Color(0xFFF8FBFF)

    val messageTitleColor = if (isDarkMode) {
       Color.White
    } else {
       Color.Black
    }

    val messageTitleStyle = TextStyle(
        color = messageTitleColor,
        fontSize = 20.sp,
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.W600
    )
    val messageTextStyle = TextStyle(
        color = messageTitleColor,
        fontSize = 16.sp,
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.W600
    )

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = notiFullBac)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_chevron),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(19.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            viewModel.navigateToVerification(context)
                        }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = T("ls_Message", currentLanguage),
                        style = messageTitleStyle
                    )
                }
            }
            Spacer(modifier = Modifier.height(23.dp))

            if (isLoadingIndicator && !viewModel.isLoadingMore) {
                CircularBarsLoading(
                    modifier = Modifier.fillMaxSize(),
                    barCount = 12,
                    barWidth = 4.dp,
                    barHeight = 24.dp
                )
            } else {
                if (notificationList.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = notifiBac, shape = RoundedCornerShape(10.dp))
                    ) {
                        val isEnabled = true
                        SwipeRefresh(
                            state = rememberSwipeRefreshState(isRefreshing),
                            onRefresh = { viewModel.refreshData(isEnabled = isEnabled) }
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // 原有的分组日期 + 消息
                                notificationList.forEach { (date, messages) ->
                                    item {
                                        Text(
                                            text = T(date, currentLanguage),
                                            style = messageTitleStyle,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                    items(messages) { message ->
                                        MessageItem(
                                            message = message,
                                            messageTextStyle = messageTextStyle,
                                            context
                                        )
                                    }
                                }

                                if (viewModel.isLoadingMore) {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "加载中...",
                                                style = TextStyle(
                                                    color = messageTitleColor,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        LaunchedEffect(listState) {
                            snapshotFlow { listState.layoutInfo }
                                .debounce(300)
                                .collect { info ->
                                    val visibleItems = info.visibleItemsInfo
                                    val totalItemsCount = info.totalItemsCount
                                    val lastVisibleItemIndex = visibleItems.lastOrNull()?.index

                                    if (
                                        lastVisibleItemIndex != null &&
                                        lastVisibleItemIndex >= totalItemsCount - 1 &&
                                        !viewModel.isLoadingMore && !viewModel.isRefreshing.value
                                    ) {
                                        viewModel.loadMoreData()
                                    }
                                }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = notifiBac, shape = RoundedCornerShape(10.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        color = Color.Red,
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.messag),
                                    contentDescription = "notification",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = T("ls_Yhnm", currentLanguage),
                                style = messageTextStyle,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    message: PushItem,
    messageTextStyle: TextStyle,
    context: Context
) {
    val themeHelper = ThemeHelper(context)
    val isDarkMode = themeHelper.isDarkModeEnabled()
    val messageTimeColor = if (isDarkMode) Color(0xFFBABABA) else Color(0xFFBABABA)
    val messageIdColor = if (isDarkMode) Color(0xFF9C9C9C) else Color(0xFF9C9C9C)

    val messageIdStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 12.sp,
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.W300,
        color = messageIdColor
    )
    val messageTimeStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 14.4.sp,
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.W500,
        color = messageTimeColor
    )


}