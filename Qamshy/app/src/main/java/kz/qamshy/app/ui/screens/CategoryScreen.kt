package kz.qamshy.app.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.qamshy.app.common.CircularBarsLoading
import kz.qamshy.app.common.ConnectivityObserver
import kz.qamshy.app.common.ConnectivityStatus
import kz.qamshy.app.common.ToastHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.ui.components.category.ArticleComponent
import kz.qamshy.app.ui.components.category.CategoryListScreen
import kz.qamshy.app.ui.components.global.NoInternetPage
import kz.qamshy.app.viewmodels.CategoryViewModel
import kz.qamshy.app.viewmodels.HomeViewModel

@Composable
fun CategoryScreen(context: Context, isDarkMode:Boolean, viewModel: CategoryViewModel
,currentLanguage: LanguageModel, bacColor:Color
){
    val connectivityObserver = remember { ConnectivityObserver(context) }
    val status by connectivityObserver.status.collectAsState(initial = ConnectivityStatus.Available)
    when(status){
        ConnectivityStatus.Available -> {
            val categoryUiState by viewModel.categoryUiState.collectAsState()
            val articleUiState by viewModel.articleUiState.collectAsState()

            LaunchedEffect(Unit) {
                if (categoryUiState !is OrderUiState.Success) {
                    viewModel.loadDataTea()
                }
                viewModel.lectureData()
            }

            if (categoryUiState is OrderUiState.Loading && articleUiState is OrderUiState.Loading) {
                CircularBarsLoading(
                    modifier = Modifier.fillMaxSize(),
                    barCount = 12,
                    barWidth = 4.dp,
                    barHeight = 24.dp
                )
            } else if (categoryUiState is OrderUiState.Success) {
                val categoryList = (categoryUiState as OrderUiState.Success).data
                Column(
                    modifier = Modifier
                        .fillMaxWidth().background(bacColor)
                ) {
                        Column(
                            modifier = Modifier
                                .weight(0.15f)
                                .fillMaxSize()
                                .padding(horizontal = 24.dp)
                        ) {
                            Spacer(modifier = Modifier.height(54.dp))
                            CategoryListScreen(context, viewModel, currentLanguage, categoryList)
                        }
                        Column(
                            modifier = Modifier
                                .weight(0.85f)
                                .padding(horizontal = 24.dp)
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))
                            if (articleUiState is OrderUiState.Loading) {
                                CircularBarsLoading(
                                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                                    barCount = 12,
                                    barWidth = 4.dp,
                                    barHeight = 24.dp
                                )
                            } else if (articleUiState is OrderUiState.Success) {
                                val articleList = (articleUiState as OrderUiState.Success).data
                                ArticleComponent(context, viewModel, currentLanguage, articleList)
                            } else if (articleUiState is OrderUiState.Error) {
                                ToastHelper.showMessage(
                                    context,
                                    "error",
                                    T((articleUiState as OrderUiState.Error).message, currentLanguage)
                                )
                            }
                        }

                }
            } else if (categoryUiState is OrderUiState.Error) {
                ToastHelper.showMessage(
                    context,
                    "error",
                    T((categoryUiState as OrderUiState.Error).message, currentLanguage)
                )
            }
        }
        ConnectivityStatus.Unavailable -> {
            NoInternetPage()
        }
    }

}