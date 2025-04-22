package kz.qamshy.app.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun CircularBarsLoading(
    modifier: Modifier = Modifier,
    barCount: Int = 12,
    size: Dp = 104.dp,
    barWidth: Dp = 4.dp,
    barHeight: Dp =24.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    durationMillis: Int = 1200
) {
    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        repeat(barCount) { index ->
            val offset = index * (2f * Math.PI.toFloat() / barCount)
            val wave = sin(progress * 2f * Math.PI.toFloat() + offset)
            val alpha = 0.2f + ((wave + 1f) / 2f) * (1f - 0.2f)
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = index * (360f / barCount)
                        this.alpha = alpha
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    }
                    .size(width = barWidth, height = barHeight)
                    .background(color, shape = RoundedCornerShape(percent = 50))
            )
        }
    }
}