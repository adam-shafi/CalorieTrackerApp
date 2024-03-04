package com.example.calorietracker.ui.daily_log

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorietracker.R
import com.example.calorietracker.ui.daily_log.components.DatePickerBar
import com.example.calorietracker.ui.daily_log.components.MealCard
import com.example.calorietracker.ui.daily_log.components.NutritionSnapshot
import com.example.calorietracker.ui.theme.CaloriesDark
import com.example.calorietracker.ui.theme.CaloriesLight
import com.example.calorietracker.ui.theme.CarbRedDark
import com.example.calorietracker.ui.theme.CarbRedLight
import com.example.calorietracker.ui.theme.FatGreenDark
import com.example.calorietracker.ui.theme.FatGreenLight
import com.example.calorietracker.ui.theme.ProteinBlueDark
import com.example.calorietracker.ui.theme.ProteinBlueLight
import kotlinx.coroutines.launch

data class MacroColorsState(
    val calories: Color,
    val fat: Color,
    val protein: Color,
    val carbs: Color
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DailyLogScreen(
    viewModel: DailyLogViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onAddClick: (String, String) -> Unit
) {
    val macroColorsState: MacroColorsState =
        if (isSystemInDarkTheme()) {
            MacroColorsState(
                calories = CaloriesDark,
                protein = ProteinBlueDark,
                fat = FatGreenDark,
                carbs = CarbRedDark
            )
        } else {
            MacroColorsState(
                calories = CaloriesLight,
                protein = ProteinBlueLight,
                fat = FatGreenLight,
                carbs = CarbRedLight
            )
        }

    val uiState by viewModel.uiState.collectAsState()
    val scrollCoroutine = rememberCoroutineScope()
    val pageState = rememberPagerState(initialPage = 150, pageCount = { 300 })
    val lazyListState = rememberLazyListState()
    LaunchedEffect(pageState) {
        var previousPage = pageState.currentPage
        snapshotFlow { pageState.currentPage }.collect {
            if (previousPage != it) {
                viewModel.updateDateBySwipe(swipe = if (previousPage > it) Swipe.Left else Swipe.Right)
            }
            previousPage = it
        }
    }
    val totalCalorieInfoState =
        remember(key1 = uiState.totalCalories.budget, key2 = uiState.totalCalories.foodEaten) {
            viewModel.calculateCalorieState(
                uiState.totalCalories.budget,
                uiState.totalCalories.foodEaten
            )
        }
    val totalProteinInfoState =
        remember(key1 = uiState.totalProtein.budget, key2 = uiState.totalProtein.foodEaten) {
            viewModel.calculateMacroState(
                uiState.totalProtein.budget,
                uiState.totalProtein.foodEaten
            )
        }
    val totalCarbInfoState =
        remember(key1 = uiState.totalCarbs.budget, key2 = uiState.totalCarbs.foodEaten) {
            viewModel.calculateMacroState(uiState.totalCarbs.budget, uiState.totalCarbs.foodEaten)
        }
    val totalFatInfoState =
        remember(key1 = uiState.totalFat.budget, key2 = uiState.totalFat.foodEaten) {
            viewModel.calculateMacroState(uiState.totalFat.budget, uiState.totalFat.foodEaten)
        }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.daily_log))
                },
                actions = {
                    IconButton(onClick = { viewModel.modify() }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.bar_chart),
                            contentDescription = stringResource(R.string.statistics)
                        )
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                },
//                colors = topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
//                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
//                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,

            ) {


            DatePickerBar(
                date = uiState.date,
                updateDate = { updatedMillis -> viewModel.updateDate(updatedMillis) },
                updateDateBySwipe = { swipe ->
                    scrollCoroutine.launch {
                        when (swipe) {
                            Swipe.Left -> {
                                pageState.animateScrollToPage(pageState.currentPage - 1)
                            }

                            Swipe.Right -> {
                                pageState.animateScrollToPage(pageState.currentPage + 1)
                            }
                        }
                    }

                }
            )

            NutritionSnapshot(
                uiState = uiState,
                macroColorsState = macroColorsState,
                calorieInfoState = totalCalorieInfoState,
                proteinInfoState = totalProteinInfoState,
                carbInfoState = totalCarbInfoState,
                fatInfoState = totalFatInfoState
            )

            Divider()

            HorizontalPager(
                state = pageState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(uiState.meals) { mealState ->
                        MealCard(
                            mealState = mealState,
                            onAddClick = { mealName -> onAddClick(mealName, viewModel.getDateId()) },
                            macroColorsState = macroColorsState
                        )
                    }
                }
            }
        }
    }
}



