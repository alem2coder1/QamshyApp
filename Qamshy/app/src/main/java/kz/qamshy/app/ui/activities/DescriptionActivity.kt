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
import kz.qamshy.app.ui.components.description.DescComponent
import kz.qamshy.app.ui.components.global.EmptyCard
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.ui.theme.QamshyTheme
import kz.qamshy.app.viewmodels.DescriptionViewModel
import kz.qamshy.app.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DescriptionActivity: ComponentActivity() {
    private val descViewModel: DescriptionViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val currentLanguage by QamshyApp.currentLanguage.collectAsState()
            val id by lazy {
                intent.getIntExtra("id",0)
            }
            if(id != 0){
                LaunchedEffect(id) {
                    descViewModel.descData(id = id)
                }
            }
            QamshyTheme{
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { paddingValues ->
                        DescScreen(viewModel = descViewModel,context ,modifier = Modifier.padding(paddingValues))
                    }
                )
            }
        }
    }
}

@Composable
fun DescScreen(viewModel: DescriptionViewModel, context: Context, modifier: Modifier = Modifier){
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var currentPlayingLecture by remember { mutableStateOf<ArticleListModel?>(null) }
    val listState = rememberLazyListState()
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()
    val descUiState by viewModel.descUiState.collectAsState()
    if (descUiState is OrderUiState.Loading) {
        CircularBarsLoading(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            barCount = 12,
            barWidth = 4.dp,
            barHeight = 24.dp
        )
    } else if (descUiState is OrderUiState.Success) {
        val articleList = (descUiState as OrderUiState.Success).data

        if (articleList.reletedArticleList.isNotEmpty()) {
            Column (
                modifier = Modifier.fillMaxSize()
            ){
                Spacer(modifier = Modifier.height(54.dp))
                Row(modifier=Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.back_icon),
                        contentDescription = "back",
                        modifier = Modifier.size(12.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                viewModel.onBackButtonPressed(context)
                            }
                    )

                }
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 20.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    item{
                        DescComponent(articleList.article)
                    }
                }

            }

        } else {
            EmptyCard(currentLanguage)
        }

    } else if (descUiState is OrderUiState.Error) {
        ToastHelper.showMessage(
            context,
            "error",
            T((descUiState as OrderUiState.Error).message, currentLanguage)
        )
    }

}