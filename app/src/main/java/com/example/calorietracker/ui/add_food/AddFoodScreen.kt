package com.example.calorietracker.ui.add_food

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorietracker.R
import com.example.calorietracker.ui.add_food.components.AddFoodTopAppBar
import com.example.calorietracker.ui.add_food.components.CategoryButtons
import com.example.calorietracker.ui.add_food.components.Searchbar
import com.example.calorietracker.ui.theme.dimen_8dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddFoodScreen(
    viewModel: AddFoodViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
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
                updateSelectedMealName = viewModel::updateSelectedMealName
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
                    Text("HELLO")
                }

            }
        }
    }
}


