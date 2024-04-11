package com.example.calorietracker.ui.create_food

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class CreateFoodUiState(
    val foodName: String = "",
    val brandName: String = "",
    val servingType: String = "",
    val servingTypeUnits: String = "serving",
    val servingAmount: String = "",
    val servingAmountUnits: String = "grams",
    val calories: String = "",
    val totalFat: String = "",
    val saturatedFat: String = "",
    val transFat: String = "",
    val cholesterol: String = "",
    val sodium: String = "",
    val totalCarbohydrate: String = "",
    val dietaryFiber: String = "",
    val totalSugars: String = "",
    val protein: String = ""
)

class CreateFoodViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val mealName: String = savedStateHandle.get<String>("mealName")!!
    val dateId: String = savedStateHandle.get<String>("dateId")!!

    private val _uiState = MutableStateFlow(CreateFoodUiState())
    val uiState = _uiState.asStateFlow()

    fun updateFoodName(update: String) {
        _uiState.update {
            it.copy(
                foodName = update
            )
        }
    }

    fun updateBrandName(update: String) {
        _uiState.update {
            it.copy(
                brandName = update
            )
        }
    }

    fun updateServingType(update: String) {
        _uiState.update {
            it.copy(
                servingType = update
            )
        }
    }

    fun updateServingTypeUnits(update: String) {
        _uiState.update {
            it.copy(
                servingTypeUnits = update
            )
        }
    }

    fun updateServingAmount(update: String) {
        _uiState.update {
            it.copy(
                servingAmount = update
            )
        }
    }

    fun updateServingAmountUnits(update: String) {
        _uiState.update {
            it.copy(
                servingAmountUnits = update
            )
        }
    }

    fun updateCalories(update: String) {
        _uiState.update {
            it.copy(
                calories = update
            )
        }
    }

    fun updateTotalFat(update: String) {
        _uiState.update {
            it.copy(
                totalFat = update
            )
        }
    }

    fun updateSaturatedFat(update: String) {
        _uiState.update {
            it.copy(
                saturatedFat = update
            )
        }
    }

    fun updateTransFat(update: String) {
        _uiState.update {
            it.copy(
                transFat = update
            )
        }
    }

    fun updateCholesterol(update: String) {
        _uiState.update {
            it.copy(
                cholesterol = update
            )
        }
    }

    fun updateSodium(update: String) {
        _uiState.update {
            it.copy(
                sodium = update
            )
        }
    }

    fun updateTotalCarbohydrate(update: String) {
        _uiState.update {
            it.copy(
                totalCarbohydrate = update
            )
        }
    }

    fun updateDietaryFiber(update: String) {
        _uiState.update {
            it.copy(
                dietaryFiber = update
            )
        }
    }

    fun updateTotalSugars(update: String) {
        _uiState.update {
            it.copy(
                totalSugars = update
            )
        }
    }

    fun updateProtein(update: String) {
        _uiState.update {
            it.copy(
                protein = update
            )
        }
    }
}