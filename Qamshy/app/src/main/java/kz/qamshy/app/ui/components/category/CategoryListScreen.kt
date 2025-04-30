package kz.qamshy.app.ui.components.category

import android.content.Context
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.flow.distinctUntilChanged
import kz.qamshy.app.R
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.models.site.CategoryGroup
import kz.qamshy.app.models.site.CategoryModel
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.CategoryViewModel

@Composable
fun CategoryListScreen(
    context: Context,
    viewModel: CategoryViewModel,
    currentLanguage: LanguageModel,
    categoryList: CategoryModel
) {
    val selectedTeacherId by viewModel.selectedTeacherId.collectAsState()
    val listState = rememberLazyListState()
    val savedPosition by viewModel.scrollPosition.collectAsState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                viewModel.saveScrollPosition(index, offset)
            }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(savedPosition.first, savedPosition.second)
    }
    LazyRow (
        state = listState
    ){
        items(categoryList.categories[0].categoryList.size) { index ->
            val category = categoryList.categories[0].categoryList[index]
            val isSelected = selectedTeacherId == category.id
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(36.dp)
                    .background(
                        color = if (isSelected) Color(0xFF58A0C8) else Color.White,
                        shape = RoundedCornerShape(size = 50.dp)
                    )
                    .border(1.dp,Color(0xFF58A0C8), RoundedCornerShape(size = 50.dp))
                    .padding(horizontal = 18.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        viewModel.toggleTitleSelection("")
                        viewModel.toggleTeacherSelection(category.id) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = category.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(400),
                        color = if (isSelected) Color.White else Color(0xFF363636),
                        textAlign = TextAlign.Center,
                    )
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
        item{
            val isSelected = categoryList.categories[1].categoryList.any { it.id == selectedTeacherId }
            val painter = if (isSelected) {painterResource(id = R.drawable.selected_down)}else{
                painterResource(id = R.drawable.down_icon)
            }
            var showDialog by remember { mutableStateOf(false) }
            if (showDialog) {
                DetailsModel(onDismissRequest = { showDialog = false }, viewModel = viewModel, categoryList = categoryList.categories[1].categoryList)
            }
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(36.dp)
                    .background(
                        color = if (isSelected) Color(0xFF58A0C8) else Color.White,
                        shape = RoundedCornerShape(size = 50.dp)
                    )
                    .border(1.dp,Color(0xFF58A0C8), RoundedCornerShape(size = 50.dp))
                    .padding(horizontal = 18.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showDialog = true

                         },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = categoryList.categories[1].title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(400),
                        color = if (isSelected) Color.White else Color(0xFF363636),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))

                Image(
                    painter = painter,
                    contentDescription = "down",
                )
            }
            Spacer(modifier = Modifier.width(4.dp))


        }
        item{
            val isSelected = categoryList.categories[2].categoryList.any { it.id == selectedTeacherId }
            val painter = if (isSelected) {painterResource(id = R.drawable.selected_down)}else{
                painterResource(id = R.drawable.down_icon)
            }
            var showDialog by remember { mutableStateOf(false) }
            if (showDialog) {
                DetailsModel(onDismissRequest = { showDialog = false }, viewModel = viewModel, categoryList = categoryList.categories[2].categoryList)
            }
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(36.dp)
                    .background(
                        color = if (isSelected) Color(0xFF58A0C8) else Color.White,
                        shape = RoundedCornerShape(size = 50.dp)
                    )
                    .border(1.dp,Color(0xFF58A0C8), RoundedCornerShape(size = 50.dp))
                    .padding(horizontal = 18.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showDialog = true
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = categoryList.categories[2].title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontFamily = PrimaryFontFamily,
                        fontWeight = FontWeight(400),
                        color = if (isSelected) Color.White else Color(0xFF363636),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painter,
                    contentDescription = "down",
                )
            }
            Spacer(modifier = Modifier.width(4.dp))


        }
    }
    val selectedTitle by viewModel.selectedTitle.collectAsState()
    if(selectedTitle.isNotEmpty()){
        Spacer(modifier = Modifier.height(17.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = selectedTitle,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = PrimaryFontFamily,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF747474),
                )
            )
        }

        Spacer(modifier = Modifier.height(17.dp))
    }
}