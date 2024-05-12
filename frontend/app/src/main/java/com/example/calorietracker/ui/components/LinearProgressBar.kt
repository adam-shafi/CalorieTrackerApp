package com.example.calorietracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap


@Composable
fun LinearProgressBar(
    modifier: Modifier = Modifier,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    color: Color = ProgressIndicatorDefaults.linearColor,
    progress: Float,
    strokeCap: StrokeCap = StrokeCap.Round,
    animationDuration: Int = 1000,
    animationDelay: Int = 0
) {

    // State to animate the progress indicator.
    var percentage by remember {
        mutableFloatStateOf(0f)
    }

    // Animate the progress number.
    val animateProgress by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        ), label = ""
    )

    // Trigger the LaunchedEffect to start the animation when the composable is first launched.
    LaunchedEffect(key1 = progress) {
        percentage = if (progress > 1F) 1F else progress
    }

    // Canvas drawing for the progress indicator.
    Canvas(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Draw the background indicator.
        drawLine(
            color = trackColor,
            cap = strokeCap,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f)
        )

        // Calculate and draw the progress indicator.
        val progressValue = animateProgress * size.width

        drawLine(
            color = color,
            cap = strokeCap,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = progressValue, y = 0f)
        )


    }
}