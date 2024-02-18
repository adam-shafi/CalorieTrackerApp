package com.example.calorietracker.ui.daily_log

import com.example.calorietracker.R


data class DailyLogUiState(
    var date: String = "",
    var totalCalories: MacroInfo = MacroInfo(2000f, 1500f),
    var totalProtein: MacroInfo = MacroInfo(175f, 50f),
    var totalCarbs: MacroInfo = MacroInfo(50f, 0f),
    var totalFat: MacroInfo = MacroInfo(30f, 0f),
    var meals: List<Meal> = listOf(
        Meal(
            name = "Breakfast",
            calories = 420,
            protein = 50,
            carbs = 25,
            fat = 16,
            foods = listOf(
                Food(
                    name = "Ramen",
                    calories = 160,
                    iconId = R.drawable.chinese_food,
                    servingAmount = 162,
                    servingUnits = "grams"
                ),
                Food(
                    name = "Rice",
                    calories = 150,
                    iconId = R.drawable.hot_food,
                    servingAmount = 72,
                    servingUnits = "grams"
                )

            )
        ),
        Meal(
            name = "Lunch",
            calories = 130,
            protein = 30,
            carbs = 2,
            fat = 3,
            foods = listOf(
                Food(
                    name = "Canned Tuna",
                    calories = 130,
                    iconId = R.drawable.canned_fish_food,
                    servingAmount = 1,
                    servingUnits = "can"
                )

            )
        ),
        Meal(
            name = "Dinner",
            calories = 0,
            protein = 0,
            carbs = 0,
            fat = 0,
            foods = listOf()
        ),
        Meal(
            name = "Snacks",
            calories = 0,
            protein = 0,
            carbs = 0,
            fat = 0,
            foods = listOf()
        )
    )
)

data class Meal(
    var name: String,
    var calories: Int,
    var protein: Int,
    var carbs: Int,
    var fat: Int,
    var foods: List<Food>,
)

data class Food(
    var name: String,
    var calories: Int,
    var iconId: Int,
    var servingAmount: Int,
    var servingUnits: String
)

data class MacroInfo(
    var budget: Float,
    var foodEaten: Float
)

data class DerivedMacroInfoState(
    var percent: Float,
    var remainder: Float = 0f,
    var isOver: Boolean = false
)

