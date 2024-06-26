package com.example.calorietracker.ui.create_food

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.NutritionFactsTextField
import com.example.calorietracker.ui.components.ServingSizeInput
import com.example.calorietracker.ui.theme.dimen_8dp
import com.example.calorietracker.values.ServingSizes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFoodScreen(
    viewModel: CreateFoodViewModel,
    onBackClick: () -> Unit,
    onSaveClick: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create New Food",
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
                            coroutineScope.launch {
                                val foodId = viewModel.onSaveClick()
                                if (foodId.isNotEmpty()) {
                                    onSaveClick(foodId)
                                } else {
                                    if (uiState.foodNameError != null) {
                                        listState.scrollToItem(index = 1)
                                    } else if (uiState.caloriesError) {
                                        listState.scrollToItem(index = 7)
                                    }
                                }
                            }

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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(dimen_8dp)
                .fillMaxWidth(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            item {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.chinese_food),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Food Name") },
                    value = uiState.foodName,
                    onValueChange = viewModel::updateFoodName,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    isError = uiState.foodNameError != null,
                    supportingText = {
                        uiState.foodNameError?.let {
                            Text(it)
                        }
                    }
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Brand Name (Optional)") },
                    value = uiState.brandName,
                    onValueChange = viewModel::updateBrandName,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Text("Serving Amount")
                Spacer(modifier = Modifier.height(10.dp))
            }
            itemsIndexed(uiState.servingAmounts) { index, servingAmount ->
                ServingSizeInput(
                    amount = servingAmount.amount,
                    onAmountChange = { viewModel.updateServingAmounts(index, amount = it) },
                    amountPlaceholder = "1",
                    dropdownText = servingAmount.dropdownText,
                    onDropdownTextChange = { viewModel.updateServingAmounts(index, unitsString = it) },
                    updateDropdownItems = { viewModel.getServingAmountDropdownItems(index) },
                    formatInput = viewModel::formatInput,
                    onDelete = if (uiState.servingAmounts.size > 1) {
                        { viewModel.deleteServingAmount(index) }
                    } else {
                        {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Must have at least one serving amount"
                                )
                            }
                        }
                    }
                )
            }
            item {
                if(uiState.servingAmounts.size < ServingSizes.servingAmountPlural.size) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.addServingAmount() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                textAlign = TextAlign.Center,
                                text = "Add Serving Amount"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            item {
                Text("Serving Weight")
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                ServingSizeInput(
                    amount = uiState.servingWeight.amount,
                    onAmountChange = { viewModel.updateServingWeight(amount = it) },
                    amountPlaceholder = "0",
                    dropdownText = uiState.servingWeight.dropdownText,
                    onDropdownTextChange = { viewModel.updateServingWeight(unitsString = it) },
                    updateDropdownItems = { viewModel.getServingWeightDropdownItems() },
                    formatInput = viewModel::formatInput
                )
            }
            item {
                Text("Serving Volume")
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                ServingSizeInput(
                    amount = uiState.servingVolume.amount,
                    onAmountChange = { viewModel.updateServingVolume(amount = it) },
                    amountPlaceholder = "0",
                    dropdownText = uiState.servingVolume.dropdownText,
                    onDropdownTextChange = { viewModel.updateServingVolume(unitsString = it) },
                    updateDropdownItems = { viewModel.getServingVolumeDropdownItems() },
                    formatInput = viewModel::formatInput
                )
            }

            item {
                Text("Nutrition Facts")
            }
            item {
                NutritionFactsTextField(
                    title = "Calories",
                    placeholder = "Required",
                    text = uiState.calories,
                    onValueChange = viewModel::updateCalories,
                    isError = uiState.caloriesError,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Total Fat (g)",
                    text = uiState.totalFat,
                    onValueChange = viewModel::updateTotalFat,
                    formatInput = viewModel::formatInput
                )

            }
            item {
                NutritionFactsTextField(
                    title = "Saturated Fat (g)",
                    text = uiState.saturatedFat,
                    onValueChange = viewModel::updateSaturatedFat,
                    tabs = 1,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Trans Fat (g)",
                    text = uiState.transFat,
                    onValueChange = viewModel::updateTransFat,
                    tabs = 1,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Cholesterol (mg)",
                    text = uiState.cholesterol,
                    onValueChange = viewModel::updateCholesterol,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Sodium (mg)",
                    text = uiState.sodium,
                    onValueChange = viewModel::updateSodium,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Total Carbohydrate (g)",
                    text = uiState.totalCarbohydrate,
                    onValueChange = viewModel::updateTotalCarbohydrate,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Dietary Fiber (g)",
                    text = uiState.dietaryFiber,
                    onValueChange = viewModel::updateDietaryFiber,
                    tabs = 1,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Total Sugars (g)",
                    text = uiState.totalSugars,
                    onValueChange = viewModel::updateTotalSugars,
                    tabs = 1,
                    formatInput = viewModel::formatInput
                )
            }
            item {
                NutritionFactsTextField(
                    title = "Protein (g)",
                    text = uiState.protein,
                    onValueChange = viewModel::updateProtein,
                    isLast = true,
                    formatInput = viewModel::formatInput
                )
            }
        }
    }

}