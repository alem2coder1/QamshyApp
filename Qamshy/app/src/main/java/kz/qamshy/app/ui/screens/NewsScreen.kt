package kz.qamshy.app.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.qamshy.app.common.CircularBarsLoading
import kz.qamshy.app.common.ToastHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.models.OrderUiState
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.components.category.NewsComponent
import kz.qamshy.app.viewmodels.HomeViewModel
import kz.qamshy.app.viewmodels.NewsViewModel

@Composable
fun NewsScreen(context: Context, isDarkMode:Boolean, viewModel: NewsViewModel) {
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()
    LaunchedEffect(currentLanguage) {
        viewModel.lectureData()
    }
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

            NewsComponent(viewModel,articleList,context, currentLanguage)

        }

        is OrderUiState.Error -> {
            ToastHelper.showMessage(
                context,
                "error",
                T((articleUiState as OrderUiState.Error).message, currentLanguage)
            )
        }
    }

}

