package com.example.calorietracker.ui.daily_log

import androidx.compose.runtime.Stable
import com.example.calorietracker.R

@Stable
data class DailyLogUiState(
    var date: String = "s",
    var totalCalories: MacroInfoState = MacroInfoState(1f, 0f),
    var totalProtein: MacroInfoState = MacroInfoState(150f, 0f),
    var totalCarbs: MacroInfoState = MacroInfoState(5220f, 0f),
    var totalFat: MacroInfoState = MacroInfoState(60f, 0f),
    var meals: List<MealState> = listOf(
        MealState(
            name = "Breakfast",
            calories = 420f,
            protein = 50f,
            carbs = 25f,
            fat = 16f,
            foods = listOf(
                FoodState(
                    name = "Ramen",
                    calories = 160f,
                    iconId = R.drawable.chinese_food,
                    servingAmount = 162f,
                    servingUnits = "grams"
                ),
                FoodState(
                    name = "Rice",
                    calories = 150f,
                    iconId = R.drawable.hot_food,
                    servingAmount = 72f,
                    servingUnits = "grams"
                )

            )
        ),
        MealState(
            name = "Lunch",
            calories = 130f,
            protein = 30f,
            carbs = 2f,
            fat = 3f,
            foods = listOf(
                FoodState(
                    name = "Canned Tuna",
                    calories = 130f,
                    iconId = R.drawable.canned_fish_food,
                    servingAmount = 1f,
                    servingUnits = "can"
                )

            )
        ),
        MealState(
            name = "Dinner",
            calories = 0f,
            protein = 0f,
            carbs = 0f,
            fat = 0f,
            foods = listOf()
        ),
        MealState(
            name = "Snacks",
            calories = 0f,
            protein = 0f,
            carbs = 0f,
            fat = 0f,
            foods = listOf()
        )
    )
)

data class MealState(
    var name: String,
    var calories: Float,
    var protein: Float,
    var carbs: Float,
    var fat: Float,
    var foods: List<FoodState>,
)

data class FoodState(
    var name: String,
    var calories: Float,
    var iconId: Int,
    var servingAmount: Float,
    var servingUnits: String
)

data class MacroInfoState(
    var budget: Float,
    var foodEaten: Float
)

data class DerivedMacroInfoState(
    var percent: Float,
    var remainder: Float = 0f,
    var isOver: Boolean = false
)

