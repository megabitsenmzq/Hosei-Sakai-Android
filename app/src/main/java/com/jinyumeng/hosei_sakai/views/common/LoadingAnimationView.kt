package com.jinyumeng.hosei_sakai.views.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinyumeng.hosei_sakai.ui.theme.HoseiSakaiTheme
import kotlinx.coroutines.delay

@Composable
fun LoadingAnimationView() {
    val circles = listOf(
        remember { Animatable(initialValue = 1f) },
        remember { Animatable(initialValue = 1f) },
        remember { Animatable(initialValue = 1f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 200L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        1f at 0 with EaseInOut
                        0.5f at 500 with EaseInOut
                        1f at 1000 with EaseInOut
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                circleValues.forEach { value ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .scale(value)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
//                    Image(
//                        painter = painterResource(id = R.drawable.rabbit_head),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .alpha(value)
//                            .width(50.dp)
//                    )
                }
            }
            Text(
                text = "読み込み中",
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingAnimationViewViewPreview() {
    HoseiSakaiTheme {
        LoadingAnimationView()
    }
}