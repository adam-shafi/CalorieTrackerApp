package com.example.calorietracker.ui.search_food

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.calorietracker.R
import com.example.calorietracker.ui.search_food.components.AddFoodCard
import com.example.calorietracker.ui.search_food.components.AddFoodTopAppBar
import com.example.calorietracker.ui.search_food.components.CategoryButtons
import com.example.calorietracker.ui.search_food.components.Searchbar
import com.example.calorietracker.ui.theme.dimen_8dp
import com.example.calorietracker.values.ServingSizes
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFoodScreen(
    viewModel: SearchFoodViewModel,
    onBackClick: () -> Unit,
    onCreateFoodClick: () -> Unit,
    onAddFoodClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchText by rememberSaveable { mutableStateOf("") }
    val pageState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val scrollCoroutine = rememberCoroutineScope()

    fun scrollToPage(pageNumber: Int) {
        scrollCoroutine.launch {
            pageState.animateScrollToPage(pageNumber)
        }
    }

    Scaffold(
        topBar = {
            AddFoodTopAppBar(
                onBackClick = onBackClick,
                mealNames = uiState.mealNames,
                selectedMealName = uiState.selectedMealName,
                updateSelectedMealName = viewModel::updateSelectedMealName,
                onCreateFoodClick = { onCreateFoodClick() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.barcode_scanner),
                    contentDescription = "Barcode Scanner"
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(dimen_8dp)
            ) {

                Searchbar(searchText = searchText, updateSearchText = { searchText = it })

                CategoryButtons(
                    buttons = listOf("All", "My Foods", "Meals", "Recipes"),
                    scrollToPage = { scrollToPage(it) },
                    currentPage = pageState.currentPage
                )

                HorizontalPager(
                    state = pageState,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(items = uiState.foodCards) {
                            AddFoodCard(
                                foodName = it.foodName,
                                brandName = it.brandName,
                                servingAmount = it.servingAmount.amount.toString(),
                                servingAmountUnits = if (it.servingAmount.amount.toString() == "1") {
                                    ServingSizes.servingAmountSingular[it.servingAmount.units]
                                } else {
                                    ServingSizes.servingAmountPlural[it.servingAmount.units]
                                },
                                calories = it.calories,
                                imageVector = ImageVector.vectorResource(R.drawable.chinese_food),
                                onAddClick = {
                                    onAddFoodClick(it.foodId)
                                }
                            )
                        }
                    }

                }

            }
        }
    }
}


