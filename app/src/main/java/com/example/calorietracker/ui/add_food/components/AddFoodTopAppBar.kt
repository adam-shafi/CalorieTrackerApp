package com.example.calorietracker.ui.add_food.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.theme.dimen_8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodTopAppBar(
    onBackClick: () -> Unit,
    mealNames: List<String>,
    selectedMealName: String,
    updateSelectedMealName: (String) -> Unit

) {
    var isMealSelectOpen by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
        },
        title = {
            TextButton(
                onClick = { isMealSelectOpen = true }
            ) {
                Text(
                    selectedMealName,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                )
                Spacer(Modifier.width(dimen_8dp))
                Icon(
                    imageVector = if (isMealSelectOpen) ImageVector.vectorResource(id = R.drawable.arrow_drop_up) else Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            DropdownMenu(
                expanded = isMealSelectOpen,
                onDismissRequest = { isMealSelectOpen = false }) {
                mealNames.forEach { mealName ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                mealName,
                                fontSize = 16.sp
                            )
                        },
                        onClick = {
                            updateSelectedMealName(mealName)
                            isMealSelectOpen = false
                        }
                    )
                }
            }


        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.create_food),
                    contentDescription = "Create Food"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.create_recipe),
                    contentDescription = "Create Recipe"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.quick_add_calories),
                    contentDescription = "Quick Add"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}