package kz.qamshy.app.ui.components.category

import android.content.Context
import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.site.ArticleListModel
import kz.qamshy.app.ui.components.global.EmptyCard
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.CategoryViewModel


@Composable
fun ArticleComponent(
    context: Context,
    viewModel: CategoryViewModel,
    currentLanguage: LanguageModel,
    articleLists : ArticleListModel
){
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var currentPlayingLecture by remember { mutableStateOf<ArticleListModel?>(null) }
    val listState = rememberLazyListState()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            if (!viewModel.isRefreshing.value) {
                viewModel.refreshData()
            }
        }
    ) {

        if (articleLists.articleList.isNotEmpty()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                ,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(articleLists.articleList) { article ->
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
        } else {
            EmptyCard(currentLanguage)
        }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { visibleItems -> visibleItems.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (
                    articleLists.articleList.isNotEmpty() &&
                    lastVisibleIndex >= articleLists.articleList.lastIndex - 2 &&
                    !viewModel.isLoadingMore &&
                    viewModel.canLoadMore()
                ) {
                    viewModel.loadMoreData()
                }
            }
    }



}