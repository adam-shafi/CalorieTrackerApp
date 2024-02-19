package com.example.calorietracker.ui.daily_log

import androidx.lifecycle.ViewModel
import com.example.calorietracker.R
import com.example.calorietracker.data.DailyLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.MonthDay
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.math.abs

enum class Nutrition {
    Calories,
    Protein,
    Carbs,
    Fat
}

@HiltViewModel
class DailyLogViewModel @Inject constructor(
    private val repository: DailyLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyLogUiState())
    val uiState = _uiState.asStateFlow()


    fun modify() {
        _uiState.update { currentState ->
            val list = currentState.meals.toMutableList()
            list[0] = Meal(
                name = "Breakfast",
                calories = 1,
                protein = 0,
                carbs = 0,
                fat = 0,
                foods = listOf(
                    Food(
                        name = "Canned Tuna",
                        calories = 130,
                        iconId = R.drawable.canned_fish_food,
                        servingAmount = 1,
                        servingUnits = "can"
                    )
                )
            )
            currentState.copy(
                meals = list.toList()
            )
        }
    }

    fun increment(type: Nutrition) {
        when (type) {
            Nutrition.Calories -> {
                _uiState.update { currentState ->
                    currentState.copy(totalCalories = currentState.totalCalories.copy(foodEaten = currentState.totalCalories.foodEaten + 100f))
                }
            }

            Nutrition.Protein -> {
                _uiState.update { currentState ->
                    currentState.copy(totalProtein = currentState.totalProtein.copy(foodEaten = currentState.totalProtein.foodEaten + 10f))
                }
            }

            Nutrition.Carbs -> {
                _uiState.update { currentState ->
                    currentState.copy(totalCarbs = currentState.totalCarbs.copy(foodEaten = currentState.totalCarbs.foodEaten + 10f))
                }
            }

            Nutrition.Fat -> {
                _uiState.update { currentState ->
                    currentState.copy(totalFat = currentState.totalFat.copy(foodEaten = currentState.totalFat.foodEaten + 10f))
                }
            }
        }
    }

    fun updateBudget() {
        _uiState.update { currentState ->
            currentState.copy(totalCalories = currentState.totalCalories.copy(budget = currentState.totalCalories.budget - 100f))
        }
    }

    fun calculateCalorieState(budget: Float, current: Float): DerivedMacroInfoState {
        return DerivedMacroInfoState(
            remainder = calculateRemainder(budget, current),
            percent = calculatePercent(budget, current),
            isOver = isOver(budget, current)
        )
    }

    fun calculateMacroState(budget: Float, current: Float): DerivedMacroInfoState {
        return DerivedMacroInfoState(
            percent = calculatePercent(budget, current)
        )
    }

    private fun calculateRemainder(budget: Float, current: Float): Float {
        return abs(budget - current)
    }

    private fun calculatePercent(budget: Float, current: Float): Float {
        return current.div(budget) * 100
    }

    private fun isOver(budget: Float, current: Float): Boolean {
        return budget < current
    }

}