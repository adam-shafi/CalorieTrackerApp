package com.example.calorietracker.ui.create_food

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CreateFoodViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val mealName: String = savedStateHandle.get<String>("mealName")!!
    val dateId: String = savedStateHandle.get<String>("dateId")!!
}