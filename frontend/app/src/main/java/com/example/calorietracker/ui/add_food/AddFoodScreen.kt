package com.example.calorietracker.ui.add_food

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.ui.components.ServingSizeInput
import com.example.calorietracker.ui.components.TitleAndDropdown
import com.example.calorietracker.ui.theme.dimen_8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    viewModel: AddFoodViewModel,
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Food",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(dimen_8dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            item {
                Text(
                    text = if (uiState.brandName.isNullOrBlank()
                            .not()
                    ) "${uiState.foodName} (${uiState.brandName})" else uiState.foodName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                TitleAndDropdown(
                    title = "Meal",
                    dropdownText = uiState.selectedMeal,
                    onDropdownTextChange = { viewModel.updateUiState(selectedMeal = it)},
                    dropdownItems = listOf("Breakfast", "Lunch", "Dinner")
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                ServingSizeInput(
                    amount = uiState.selectedServingAmount,
                    onAmountChange = { viewModel.updateSelectedServingAmount(amount = it)},
                    dropdownText = uiState.selectedServingAmountUnits,
                    onDropdownTextChange = { viewModel.updateUiState(selectedServingAmountUnits = it)},
                    updateDropdownItems = { viewModel.getServingUnitsDropdownItems() },
                    formatInput = { /*TODO*/ })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }
}