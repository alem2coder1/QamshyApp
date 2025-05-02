package kz.qamshy.app.ui.components.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.mylibrary.ThemeHelper
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.qamshy.app.R
import kz.qamshy.app.models.site.CategoryList
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.viewmodels.CategoryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsModel(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: CategoryViewModel,
    categoryList:List<CategoryList>
) {
    val scope = rememberCoroutineScope()
    var sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val context = LocalContext.current
    val themeHelper = ThemeHelper(context)
    val isDarkMode = themeHelper.isDarkModeEnabled()
    val selectedTeacherId by viewModel.selectedTeacherId.collectAsState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(
            modifier = Modifier.padding(0.dp),
            width = 65.dp,
            height = 5.dp,
            color = colorResource(id = R.color.switch_bac_co),
            shape = RoundedCornerShape(10.dp)
        )},
        containerColor = Color(0xFFFFFFFF)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val availableHeight = maxHeight * 0.6f
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier.fillMaxWidth()
                    .height(availableHeight)
                    .background(Color(0xFFFFFFFF))

            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFFFFF))
                        .padding(vertical = 16.dp, horizontal = 40.dp)
                       ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn (
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        items(categoryList){ category ->
                            val isSelected = selectedTeacherId == category.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable (
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ){
                                        viewModel.toggleTitleSelection(category.title)
                                        viewModel.toggleTeacherSelection(category.id)
                                        onDismissRequest()
                                    }
                            ){
                                Text(
                                    text = category.title,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 15.4.sp,
                                        fontFamily = PrimaryFontFamily,
                                        fontWeight = FontWeight(400),
                                        color = if(isSelected) Color(0xFF58A0C8) else Color(0xFF000000),
                                    )
                                )
                            }
                        }

                    }

                }
                Spacer(modifier = Modifier.height(32.dp))
            }

        }


    }
}