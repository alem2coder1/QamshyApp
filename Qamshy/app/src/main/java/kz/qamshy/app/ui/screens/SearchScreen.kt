package kz.qamshy.app.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.qamshy.app.common.SearchHistoryManager
import kz.qamshy.app.common.ThemeHelper
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.components.global.CustomTextField
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.HomeViewModel
import kz.qamshy.app.viewmodels.SearchViewModel


@Composable
fun SearchScreen(context: Context, isDarkMode:Boolean, viewModel: SearchViewModel) {
    val searchText by viewModel.searchText.collectAsState()
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()
    val searchHistoryManager = SearchHistoryManager.getInstance()
    val searchHistory by searchHistoryManager.searchHistory.collectAsState(initial = emptyList())
    val themeHelper = ThemeHelper(context)
    val isDarkMode = themeHelper.isDarkModeEnabled()
    val bacColor = if (isDarkMode) {
        Color(0xFF202020)
    }else{
        Color.White
    }
    val tagList by viewModel.tagList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadDataTag()
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .background(bacColor)
    ){
        Spacer(modifier = Modifier.height(54.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        ){
            CustomTextField(
                text = searchText,
                onTextChange = { text ->
                    viewModel.updateSearch(text)
                },
                placeholderText = T("ls_Search", currentLanguage),
                onSearch = {
                    if (searchText.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            searchHistoryManager.addSearchQuery(searchText)
                        }
                        viewModel.performSearch()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(17.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)){
            Text(
                text = T("ls_Searchhistory",currentLanguage),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = PrimaryFontFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF363636),
                )
            )
        }
        Spacer(modifier = Modifier.height(17.dp))
        searchHistory.forEach { query ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = query,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF363636),
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            searchHistoryManager.removeSearchQuery(query)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "删除",
                        tint = Color(0xFF363636)
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(40.dp))
        if (tagList.isNotEmpty()) {
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = Int.MAX_VALUE,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                tagList.forEach { tag ->
                    Column(
                        modifier = Modifier
                            .border(width = 0.5.dp, color = Color(0xFF363636), shape = RoundedCornerShape(size = 10.dp))
                            .wrapContentWidth()
                            .height(31.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                            .padding(horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = tag.title,
                            style = TextStyle(
                                fontSize = 11.sp,
                                lineHeight = 11.sp,
                                fontFamily = PrimaryFontFamily,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF363636),
                            )
                        )
                    }
                }
            }
        }

    }

}