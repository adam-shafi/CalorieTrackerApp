package com.example.calorietracker.ui.daily_log

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.R
import com.example.calorietracker.data.DailyLog
import com.example.calorietracker.data.DailyLogRepository
import com.example.calorietracker.data.Food
import com.example.calorietracker.data.Meal
import com.example.calorietracker.data.NutritionBudget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

enum class Nutrition {
    Calories,
    Protein,
    Carbs,
    Fat
}

enum class Swipe {
    Left,
    Right
}


@HiltViewModel
class DailyLogViewModel @Inject constructor(
    private val repository: DailyLogRepository
) : ViewModel() {
    private var dateInstant by mutableStateOf(
        LocalDateTime.now(ZoneId.systemDefault()).toInstant(
            ZoneOffset.of("+0")
        )
    )
    private val _uiState = MutableStateFlow(DailyLogUiState())
    val uiState = _uiState.asStateFlow()


    init {
        val dateId = getDateId(dateInstant)
        val dateString = getDateString(dateInstant)
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(dateId = dateId, dateString = dateString)
        }
    }

    private suspend fun updateUiState(dateId: String, dateString: String) {
        val dailyLog = repository.getDailyLogByDate(dateId)
        if (dailyLog == null) {
            repository.insertDailyLog(DailyLog(date = dateId))
            repository.insertMeal(Meal(name = "Breakfast", logDate = dateId))
            repository.insertMeal(Meal(name = "Lunch", logDate = dateId))
            repository.insertMeal(Meal(name = "Dinner", logDate = dateId))
            repository.insertMeal(Meal(name = "Snacks", logDate = dateId))
        }
        val meals = repository.getAllMeal(dateId)
        val foods = repository.getAllFood(dateId)

        val eaten = calculateEaten(foods)
        _uiState.update { currentState ->
            currentState.copy(
                date = dateString,
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
                    meals = meals,
                    foods = foods
                )
            )
        }
    }

//    fun update() {
//        viewModelScope.launch {
//            val date = getDateId(dateInstant)
//            val foodLiveData = repository.getAllFood(date).asLiveData()
//            val mealLiveData = repository.getAllMeal(date).asLiveData()
//            _uiState.update { currentState ->
//                currentState.copy(
//                    meals = generateMealState(
//                        mealLiveData = mealLiveData,
//                        foodLiveData = foodLiveData
//                    )
//                )
//            }
//        }
//    }


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

    fun getDateId(instant: Instant = dateInstant, timezone: ZoneId = ZoneId.systemDefault()): String {
        val current = LocalDateTime.ofInstant(instant, timezone)
        return "${current.dayOfMonth}-${current.monthValue}-${current.year}"
    }

    private fun getDateString(instant: Instant, timezone: ZoneId = ZoneId.systemDefault()): String {
        val current = LocalDateTime.ofInstant(instant, timezone)
        var before = LocalDateTime
            .now()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        var after = LocalDateTime
            .now()
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
        if ((current.isEqual(before) || current.isAfter(before)) && current.isBefore(after)) {
            return "Today"
        }
        before = before.minusDays(1)
        after = after.minusDays(1)
        if ((current.isEqual(before) || current.isAfter(before)) && current.isBefore(after)) {
            return "Yesterday"
        }
        before = before.plusDays(2)
        after = after.plusDays(2)
        if ((current.isEqual(before) || current.isAfter(before)) && current.isBefore(after)) {
            return "Tomorrow"
        }
        return "${current.month.name.lowercase().capitalized()}, ${current.dayOfMonth}"
    }

    private fun calculateEaten(foods: List<Food>): NutritionBudget {
        var nutrition = NutritionBudget(
            caloriesBudget = 0f,
            proteinBudget = 0f,
            fatBudget = 0f,
            carbsBudget = 0f
        )

        foods.forEach { food ->
            nutrition = nutrition.copy(
                caloriesBudget = nutrition.caloriesBudget + food.calories,
                proteinBudget = nutrition.proteinBudget + food.protein,
                fatBudget = nutrition.fatBudget + food.fat,
                carbsBudget = nutrition.carbsBudget + food.carbs
            )
        }

        return nutrition
    }

    private fun generateMealState(
        meals: List<Meal>,
        foods: List<Food>
    ): List<MealState> {
        val mealList = mutableListOf<MealState>()

        meals.forEach { meal ->
            val foodList = mutableListOf<FoodState>()
            var nutrition = NutritionBudget(
                caloriesBudget = 0f,
                proteinBudget = 0f,
                carbsBudget = 0f,
                fatBudget = 0f
            )
            foods.forEach { food ->
                if (food.mealName == meal.name) {
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

    fun updateDateBySwipe(swipe: Swipe) {
        val updated = dateInstant.plus(if (swipe == Swipe.Left) -1L else 1L, ChronoUnit.DAYS)
        updateDate(updated)
    }

    fun updateDate(millisInUTC: Long) {
        val instant = Instant.ofEpochMilli(millisInUTC)
        updateDate(instant = instant)
    }

    private fun updateDate(instant: Instant) {
        val dateId = getDateId(instant, ZoneId.of("UTC"))
        val dateString = getDateString(instant, ZoneId.of("UTC"))
        dateInstant = instant
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(dateId = dateId, dateString = dateString)
        }
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

fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}


