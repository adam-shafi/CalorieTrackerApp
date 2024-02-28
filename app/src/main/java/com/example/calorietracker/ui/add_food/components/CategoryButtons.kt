package com.example.calorietracker.ui.add_food.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.calorietracker.ui.theme.dimen_8dp


@Composable
fun CategoryButtons(
    buttons: List<String>,
    scrollToPage: (Int) -> Unit,
    currentPage: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimen_8dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        buttons.forEachIndexed { index, buttonName ->
            CategoryButton(
                onClick = { scrollToPage(index) },
                text = buttonName,
                selected = currentPage == index
            )
        }
    }
}
@Composable
fun CategoryButton(
    onClick: () -> Unit,
    text: String,
    selected: Boolean
) {
    if(selected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(25),
        ) {
            Text(text)
        }
    }
    else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(25),
        ) {
            Text(text)
        }
    }


}