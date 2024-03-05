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

data class Line(
    val progress: Float,
    val color: Color
)

@Composable
fun MultiColoredLinearProgressBar(
    modifier: Modifier = Modifier,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    lines: List<Line> = listOf(
        Line(progress = 1f, color = ProgressIndicatorDefaults.linearColor)
    ),
    strokeCap: StrokeCap = StrokeCap.Round,
    animationDuration: Int = 3000,
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

    var totalProgress = 0f
    lines.forEach {
        totalProgress += it.progress
    }

    // Trigger the LaunchedEffect to start the animation when the composable is first launched.
    LaunchedEffect(key1 = totalProgress) {
        percentage = if (totalProgress > 1F) 1F else totalProgress
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

        val previousProgressValue = MutableList(lines.size) { 0f }
        val progressValue = MutableList(lines.size) { 0f }
        drawLine(
            color = lines.first().color,
            cap = strokeCap,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = 0f, y = 0f)
        )
        lines.forEachIndexed { index, line ->
            var multiplier = animateProgress / line.progress
            if (multiplier > 1f) { multiplier = 1f }
            progressValue[index] = (previousProgressValue[index] + line.progress * multiplier * size.width)
            if(progressValue[index] > size.width) {
                progressValue[index] = size.width
            }
            drawLine(
                color = line.color,
                cap = StrokeCap.Butt,
                strokeWidth = size.height,
                start = Offset(x = previousProgressValue[index], y = 0f),
                end = Offset(x = progressValue[index], y = 0f)
            )
            if(index != lines.lastIndex) {
                previousProgressValue[index + 1] = progressValue[index]
            }

        }
        drawLine(
            color = lines.last().color,
            cap = strokeCap,
            strokeWidth = size.height,
            start = Offset(x = progressValue[lines.lastIndex], y = 0f),
            end = Offset(x = progressValue[lines.lastIndex], y = 0f)
        )

    }
}