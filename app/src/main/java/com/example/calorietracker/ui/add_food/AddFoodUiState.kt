package com.example.calorietracker.ui.add_food

data class AddFoodUiState (
    val selectedMealName: String = "",
    val mealNames: List<String> = listOf()
)