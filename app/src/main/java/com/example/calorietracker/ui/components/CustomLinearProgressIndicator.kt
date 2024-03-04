package com.example.calorietracker.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.calorietracker.ui.theme.dimen_10dp


@Composable
fun CustomLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color,
) {
    LinearProgressIndicator(
        modifier = modifier
            .height(dimen_10dp),
        progress = progress,
        color = color,
    )
}
