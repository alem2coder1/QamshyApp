package kz.qamshy.app.ui.components.global

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.qamshy.app.ui.QamshyApp
import kz.qamshy.app.ui.theme.PrimaryFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeholderText: String,
    isError: Boolean = false
) {
    var textState by remember { mutableStateOf(text) }
    val currentLanguage by QamshyApp.currentLanguage.collectAsState()

    TextField(
        value = textState,
        onValueChange = { input ->
            textState = input
            onTextChange(textState)
        },
        placeholder = {
            Text(
                text = placeholderText + "...",
                color =  MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = PrimaryFontFamily,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFB6B4B4),
                    textAlign = TextAlign.Start
                )
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF58A0C8)
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 26.sp,
            fontFamily = PrimaryFontFamily,
            fontWeight = FontWeight(500),
            color = MaterialTheme.colorScheme.onBackground,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color(0xFF58A0C8), shape = RoundedCornerShape(25.dp))
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Gray,
            errorTextColor = Color.Red,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White.copy(alpha = 0.5f),
            errorContainerColor = if (isError) Color.Red.copy(alpha = 0.1f) else Color.White,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            errorCursorColor = Color.Red,
            selectionColors = TextSelectionColors(
                handleColor = Color.Black,
                backgroundColor = Color.LightGray.copy(alpha = 0.4f)
            ),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            unfocusedPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            disabledPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            errorPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
        )
    )
}