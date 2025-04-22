package kz.qamshy.app.ui.components.global

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import kz.qamshy.app.R
import kz.qamshy.app.ui.theme.hrBacColor


@Composable
fun ComposHr(){
    Divider(
        color = hrBacColor,
        thickness = 1.dp,
    )
}