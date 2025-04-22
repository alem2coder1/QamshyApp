package kz.qamshy.app.ui.components.global

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kz.qamshy.app.R
import kz.qamshy.app.models.LanguageModel
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.theme.PrimaryFontFamily
import kz.qamshy.app.ui.theme.languageSelect
import kz.qamshy.app.common.Translator.T
import kz.qamshy.app.ui.theme.languageColor
import kz.sira.app.viewmodels.LanguageModalViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onLanguageSelected: (LanguageModel) -> Unit,
    viewModel: LanguageModalViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()
    val languageList by viewModel.languageList.observeAsState(emptyList())

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (languageList.isEmpty()) {
                    CircularProgressIndicator()
                } else {
                    languageList.forEachIndexed { index, language ->
                        LanguageOption(
                            language = language,
                            isSelected = language.languageCulture == currentLanguage.languageCulture,
                            onLanguageSelected = onLanguageSelected,
                            showDivider = index < languageList.size - 1 // 最后一行不显示分隔线
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun LanguageOption(
    language: LanguageModel,
    isSelected: Boolean,
    onLanguageSelected: (LanguageModel) -> Unit,
    showDivider: Boolean
) {
    Column (
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(if(isSelected) languageSelect else Color.White, shape = RoundedCornerShape(4.dp))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLanguageSelected(language) }
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = language.fullName,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight(400),
                color =languageColor,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_language),
                    contentDescription = "Selected",
                    tint = Color.Unspecified
                )
            }
        }

    }
}
