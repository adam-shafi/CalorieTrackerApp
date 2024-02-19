package com.example.calorietracker.ui.daily_log

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.calorietracker.R
import com.example.calorietracker.data.DailyLogRepository
import com.example.calorietracker.data.Food
import com.example.calorietracker.data.Meal
import com.example.calorietracker.data.NutritionBudget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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
    private val dateInstant by mutableStateOf(Instant.now())
    private val _uiState = MutableStateFlow(DailyLogUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun initializeUiState() {
        val date = getDateId(dateInstant)
        val foodFlow = repository.getAllFood(date)
        val mealFlow = repository.getAllMeal(date)
        val eaten = calculateEaten(foodFlow)
        _uiState.update { currentState ->
            currentState.copy(
                date = getDateString(dateInstant),
                totalCalories = MacroInfoState(
                    budget = repository.getNutritionBudget()?.caloriesBudget ?: 2000f,
                    foodEaten = eaten.caloriesBudget
                ),
                totalProtein = MacroInfoState(
                    budget = repository.getNutritionBudget()?.proteinBudget ?: 150f,
                    foodEaten = eaten.proteinBudget
                ),
                totalCarbs = MacroInfoState(
                    budget = repository.getNutritionBudget()?.carbsBudget ?: 50f,
                    foodEaten = eaten.carbsBudget
                ),

                totalFat = MacroInfoState(
                    budget = repository.getNutritionBudget()?.fatBudget ?: 60f,
                    foodEaten = eaten.fatBudget
                ),
                meals = generateMealState(
                    mealFlow = mealFlow,
                    foodFlow = foodFlow
                )
            )
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

    private fun getDateId(instant: Instant): String {
        val current = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
        return "${current.dayOfMonth}-${current.monthValue}-${current.year}"
    }

    private fun getDateString(instant: Instant): String {
        val current = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        val before = ZonedDateTime
            .now()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        val after = ZonedDateTime
            .now()
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
        if (current.isAfter(before) && current.isBefore(after)) {
            return "Today"
        }
        before.minusDays(1)
        after.minusDays(1)
        if (current.isAfter(before) && current.isBefore(after)) {
            return "Yesterday"
        }
        before.plusDays(2)
        after.plusDays(2)
        if (current.isAfter(before) && current.isBefore(after)) {
            return "Tomorrow"
        }
        return "${current.month.name}, ${current.dayOfMonth}"
    }

    private suspend fun calculateEaten(foodFlow: Flow<List<Food>>): NutritionBudget {
        var nutrition = NutritionBudget(
            caloriesBudget = 0f,
            proteinBudget = 0f,
            fatBudget = 0f,
            carbsBudget = 0f
        )
        val foodLiveData = foodFlow.asLiveData()

        foodLiveData.value?.forEach { food ->
            nutrition = nutrition.copy(
                caloriesBudget = nutrition.caloriesBudget + food.calories,
                proteinBudget = nutrition.proteinBudget + food.protein,
                fatBudget = nutrition.fatBudget + food.fat,
                carbsBudget = nutrition.carbsBudget + food.carbs
            )
        }

        return nutrition
    }

    private suspend fun generateMealState(
        mealFlow: Flow<List<Meal>>,
        foodFlow: Flow<List<Food>>
    ): List<MealState> {
        val meals = mealFlow.asLiveData()
        val foods = foodFlow.asLiveData()
        val mealList = mutableListOf<MealState>()


        meals.value?.forEach { meal ->
            val foodList = mutableListOf<FoodState>()
            var nutrition = NutritionBudget(
                caloriesBudget = 0f,
                proteinBudget = 0f,
                carbsBudget = 0f,
                fatBudget = 0f
            )
            foods.value?.forEach { food ->
                if (food.mealId == meal.mealId) {
                    foodList.add(
                        FoodState(
                            name = food.name,
                            calories = food.calories,
                            iconId = food.iconId,
                            servingAmount = food.servingAmount,
                            servingUnits = food.servingUnits
                        )
                    )
                    nutrition = nutrition.copy(
                        caloriesBudget = nutrition.caloriesBudget + food.calories,
                        proteinBudget = nutrition.proteinBudget + food.protein,
                        fatBudget = nutrition.fatBudget + food.fat,
                        carbsBudget = nutrition.carbsBudget + food.carbs
                    )
                }
            }
            mealList.add(
                MealState(
                    name = meal.name,
                    calories = nutrition.caloriesBudget,
                    protein = nutrition.proteinBudget,
                    fat = nutrition.fatBudget,
                    carbs = nutrition.carbsBudget,
                    foods = foodList.toList()
                )
            )
        }

        return mealList.toList()
    }


    fun modify() {
        _uiState.update { currentState ->
            val list = currentState.meals.toMutableList()
            list[0] = MealState(
                name = "Breakfast",
                calories = 1f,
                protein = 0f,
                carbs = 0f,
                fat = 0f,
                foods = listOf(
                    FoodState(
                        name = "Canned Tuna",
                        calories = 130f,
                        iconId = R.drawable.canned_fish_food,
                        servingAmount = 1f,
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

}




