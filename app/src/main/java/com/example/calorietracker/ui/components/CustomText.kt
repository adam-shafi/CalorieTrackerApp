package com.example.calorietracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.calorietracker.ui.theme.DescriptionGray

@Composable
fun VerticalText(
    title: String,
    description: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(text = title)
        Text(
            text = description,
            color = DescriptionGray
        )
    }
}

@Composable
fun VerticalText(
    title: String,
    description: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(text = title)
        Text(
            text = description,
            color = color
        )
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        fontSize = 24.sp,
    )
}