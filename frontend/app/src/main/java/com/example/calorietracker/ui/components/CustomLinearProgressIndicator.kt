package com.example.calorietracker.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.example.calorietracker.ui.theme.dimen_10dp


@Composable
fun CustomLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color,
) {
    LinearProgressBar(
        modifier = modifier
            .height(dimen_10dp),
        progress = progress,
        color = color,
        strokeCap = StrokeCap.Round
    )
}
