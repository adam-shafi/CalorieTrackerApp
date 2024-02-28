package com.example.calorietracker.ui.add_food.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.calorietracker.R

@Composable
fun Searchbar(
    searchText: String,
    updateSearchText: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchText,
        onValueChange = {
            updateSearchText(it)
        },
        label = {
            Text(text = "Search for food")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { updateSearchText("") }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.cancel),
                        contentDescription = "Clear"
                    )
                }
            }
        },
        shape = RoundedCornerShape(50)
    )
}