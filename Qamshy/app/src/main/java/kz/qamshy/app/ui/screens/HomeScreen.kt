package kz.qamshy.app.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.components.home.HomeNav
import kz.qamshy.app.ui.components.home.NavSideModal
import kz.qamshy.app.viewmodels.HomeViewModel
import kz.qamshy.app.common.CircularBarsLoading
import kz.qamshy.app.common.ThemeHelper
import kz.qamshy.app.common.ToastHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.components.global.EmptyCard
import kz.qamshy.app.ui.components.home.Block2Card
import kz.qamshy.app.ui.components.home.Block3Card
import kz.qamshy.app.ui.components.home.FocusCard
import kz.qamshy.app.ui.components.home.PinnedCard
import kz.qamshy.app.ui.components.home.WorldCard
import kz.qamshy.app.ui.components.home.WorldCardRow
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.ui.theme.darkBac
import kz.qamshy.app.ui.theme.darkColor
import kz.qamshy.app.viewmodels.CurrencyViewModel

@Composable
fun HomeScreen(context: Context, isDarkMode:Boolean, viewModel: HomeViewModel,currencyViewModel:CurrencyViewModel,
               bacColor:Color
               ) {
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()
    LaunchedEffect(currentLanguage) {
        viewModel.loadIndex(forceRefresh = true)
        currencyViewModel.lectureData()
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val listState = rememberLazyListState()
    val articleUiState by viewModel.articleUiState.collectAsState()
    when (articleUiState) {
        is OrderUiState.Loading -> {
            CircularBarsLoading(
                modifier = Modifier.fillMaxSize(),
                barCount = 12,
                barWidth = 4.dp,
                barHeight = 24.dp
            )
        }
        is OrderUiState.Success -> {
            val articleList = (articleUiState as OrderUiState.Success).data
            val isRtl by QamshyApp.isRtl.collectAsState()
            CompositionLocalProvider(
                LocalLayoutDirection provides if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
            ){
                Column (
                    modifier = Modifier.fillMaxSize()
                        .background(color = bacColor)
                ){
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 116.dp)
                                        .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(0.dp)),
                                    drawerContainerColor = Color(0xFFFFFFFF)
                                ) {
                                    NavSideModal(context,currentLanguage,isRtl,currencyViewModel)
                                }
                            },
                            gesturesEnabled = true
                        ) {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                Column(modifier = Modifier.fillMaxSize().background(bacColor)) {
                                    Spacer(modifier = Modifier.height(30.dp))
                                    Column(modifier = Modifier.weight(0.1f),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        HomeNav(isDarkMode,currentLanguage,context, homeViewModel = viewModel, modifier = Modifier.fillMaxSize()
                                            ,drawerState,scope
                                        )
                                    }
                                    Column(modifier = Modifier.weight(0.9f)){
                                        SwipeRefresh(
                                            state = rememberSwipeRefreshState(isRefreshing),
                                            onRefresh = {
                                                if (!viewModel.isRefreshing.value) {
                                                    viewModel.refreshData()
                                                }
                                            }
                                        ) {
                                            if (articleList.pinnedArticleList.isNotEmpty() && articleList.focusArticleList.isNotEmpty()
                                                && articleList.blockList.isNotEmpty()
                                            ) {
                                                LazyColumn(
                                                    state = listState,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                       ,
                                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {

                                                    items(articleList.pinnedArticleList){ article ->
                                                        Box(modifier = Modifier.padding(horizontal = 20.dp)){
                                                            PinnedCard(currentLanguage,context,article,viewModel)
                                                        }

                                                    }
                                                    item{
                                                        Box(modifier = Modifier.padding(horizontal = 20.dp)){
                                                            FocusCard(currentLanguage,context,articleList.focusArticleList, viewModel)
                                                        }

                                                    }
                                                    itemsIndexed(articleList.blockList) { index, articleBlock ->
                                                        Spacer(modifier = Modifier.height(25.dp))
                                                        when(index){
                                                            0 ->
                                                                Column(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                ) {
                                                                    Text(
                                                                        text = articleBlock.categoryTitle,
                                                                        style = TextStyle(
                                                                            fontSize = 16.sp,
                                                                            lineHeight = 20.sp,
                                                                            fontFamily = PrimaryFontFamily,
                                                                            fontWeight = FontWeight(600),
                                                                            color = Color(0xFF000000),
                                                                        ),
                                                                        modifier = Modifier.padding(bottom = 13.dp).padding(horizontal = 20.dp)
                                                                    )

                                                                    WorldCardRow(
                                                                        currentLanguage = currentLanguage,
                                                                        context = context,
                                                                        articleBlock = articleBlock,
                                                                        viewModel
                                                                    )
                                                                }
                                                            1 ->
                                                                Column(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth().padding(horizontal = 20.dp)
                                                                ){
                                                                    Spacer(modifier = Modifier.height(10.dp))
                                                                    Text(
                                                                        text = articleBlock.categoryTitle,
                                                                        style = TextStyle(
                                                                            fontSize = 16.sp,
                                                                            lineHeight = 20.sp,
                                                                            fontFamily = PrimaryFontFamily,
                                                                            fontWeight = FontWeight(600),
                                                                            color = Color(0xFF000000),
                                                                        )
                                                                    )
                                                                    Spacer(modifier = Modifier.height(28.dp))
                                                                    articleBlock.articleList.forEach{ article ->
                                                                        Block2Card(context,article,viewModel)
                                                                    }

                                                                }
                                                            2 ->
                                                                Column(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth().padding(horizontal = 20.dp)
                                                                ) {
                                                                    Text(
                                                                        text = articleBlock.categoryTitle,
                                                                        style = TextStyle(
                                                                            fontSize = 16.sp,
                                                                            lineHeight = 20.sp,
                                                                            fontFamily = PrimaryFontFamily,
                                                                            fontWeight = FontWeight(
                                                                                600
                                                                            ),
                                                                            color = Color(0xFF000000),
                                                                        )
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.height(
                                                                            10.dp
                                                                        )
                                                                    )
                                                                    Block3Card(context,articleBlock.articleList, viewModel)
                                                                }


                                                        }


                                                    }




                                                }
                                            } else {
                                                EmptyCard(currentLanguage)
                                            }
                                        }
                                    }

                                }

                                LaunchedEffect(listState) {
                                    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                                        .map { visibleItems -> visibleItems.lastOrNull()?.index ?: 0 }
                                        .distinctUntilChanged()
                                        .collect { lastVisibleIndex ->
                                            if (
                                                articleList.blockList.isNotEmpty() &&
                                                lastVisibleIndex >= articleList.blockList.lastIndex - 2 &&
                                                !viewModel.isLoadingMore &&
                                                viewModel.canLoadMore()
                                            ) {
                                                viewModel.loadIndex()
                                            }
                                        }
                                }
                            }
                        }


                    }
                }
            }

        }
        is OrderUiState.Error -> {
            ToastHelper.showMessage(context, "error", T((articleUiState as OrderUiState.Error).message,currentLanguage))
        }

        is OrderUiState.Error -> TODO()
        OrderUiState.Loading -> TODO()
        is OrderUiState.Success -> TODO()
    }

}