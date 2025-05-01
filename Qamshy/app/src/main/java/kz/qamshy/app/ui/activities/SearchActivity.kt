package kz.qamshy.app.ui.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kz.qamshy.app.R
import kz.qamshy.app.common.CircularBarsLoading
import kz.qamshy.app.common.ToastHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.models.site.ArticleListModel
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.components.category.ArticleComponent
import kz.qamshy.app.ui.components.global.EmptyCard
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.ui.theme.QamshyTheme
import kz.qamshy.app.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity: ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val tegId by lazy {
                intent.getIntExtra("tegId",0)
            }
            val tagTitle by lazy {
                intent.getStringExtra("tagTitle").orEmpty()
            }
            val searchText by lazy {
                intent.getStringExtra("searchText").orEmpty()
            }
            LaunchedEffect(tegId){
                searchViewModel.performSearch(tagId = tegId)
            }
            val context = LocalContext.current
            val currentLanguage by QamshyApp.currentLanguage.collectAsState()
            QamshyTheme{
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { paddingValues ->
                        SearchList(viewModel = searchViewModel,context ,tagTitle = tagTitle,searchText = searchText,modifier = Modifier.padding(paddingValues))
                    }
                )
            }
        }
    }
}

@Composable
fun SearchList(viewModel: SearchViewModel, context: Context,tagTitle:String = "", searchText:String = "",modifier: Modifier = Modifier){
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var currentPlayingLecture by remember { mutableStateOf<ArticleListModel?>(null) }
    val listState = rememberLazyListState()
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()
    val articleUiState by viewModel.articleUiState.collectAsState()
    if (articleUiState is OrderUiState.Loading) {
        CircularBarsLoading(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            barCount = 12,
            barWidth = 4.dp,
            barHeight = 24.dp
        )
    } else if (articleUiState is OrderUiState.Success) {
        val articleList = (articleUiState as OrderUiState.Success).data
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Spacer(modifier = Modifier.height(54.dp))
            Row(modifier=Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "back",
                    modifier = Modifier.size(16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            viewModel.toggleSearch()
                            viewModel.onBackButtonPressed(context)
                        }
                )
                Spacer(modifier = Modifier.width(10.dp))
                if(tagTitle.isNotEmpty()){
                    Text(
                        text = T("ls_Tags",currentLanguage) + ": " + tagTitle,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = PrimaryFontFamily,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF4E4A4A),
                        )
                    )
                }else{

                    Text(
                        text = T("ls_Keyword",currentLanguage) +": " + searchText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = PrimaryFontFamily,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF4E4A4A),
                        )
                    )
                }


            }
            Spacer(modifier = Modifier.height(30.dp))
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    if (!viewModel.isRefreshing.value) {
                        viewModel.refreshData()
                    }
                }
            ) {

                if (articleList.articleList.isNotEmpty()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        items(articleList.articleList) { article ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
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
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                    .map { visibleItems -> visibleItems.lastOrNull()?.index ?: 0 }
                    .distinctUntilChanged()
                    .collect { lastVisibleIndex ->
                        if (
                            articleList.articleList.isNotEmpty() &&
                            lastVisibleIndex >= articleList.articleList.lastIndex - 2 &&
                            !viewModel.isLoadingMore &&
                            viewModel.canLoadMore()
                        ) {
                            viewModel.loadMoreData()
                        }
                    }
            }
        }


    } else if (articleUiState is OrderUiState.Error) {
        ToastHelper.showMessage(
            context,
            "error",
            T((articleUiState as OrderUiState.Error).message, currentLanguage)
        )
    }

}