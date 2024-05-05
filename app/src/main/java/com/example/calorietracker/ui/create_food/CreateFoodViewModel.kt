package com.example.calorietracker.ui.create_food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.firestore.FirestoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


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
    val protein: String = "",
    val foodNameError: String? = null,
    val caloriesError: Boolean = false,
)

class CreateFoodViewModel(
    private val firestoreUseCase: FirestoreUseCase = FirestoreUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateFoodUiState())
    val uiState = _uiState.asStateFlow()

    fun updateFoodName(update: String) {
        _uiState.update {
            it.copy(
                foodNameError = null,
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
                servingType = validateNumber(update)
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
                servingAmount = validateNumber(update)
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
                calories = validateNumber(update),
                caloriesError = false
            )
        }
    }

    fun updateTotalFat(update: String) {
        _uiState.update {
            it.copy(
                totalFat = validateNumber(update)
            )
        }
    }

    fun updateSaturatedFat(update: String) {
        _uiState.update {
            it.copy(
                saturatedFat = validateNumber(update)
            )
        }
    }

    fun updateTransFat(update: String) {
        _uiState.update {
            it.copy(
                transFat = validateNumber(update)
            )
        }
    }

    fun updateCholesterol(update: String) {
        _uiState.update {
            it.copy(
                cholesterol = validateNumber(update)
            )
        }
    }

    fun updateSodium(update: String) {
        _uiState.update {
            it.copy(
                sodium = validateNumber(update)
            )
        }
    }

    fun updateTotalCarbohydrate(update: String) {
        _uiState.update {
            it.copy(
                totalCarbohydrate = validateNumber(update)
            )
        }
    }

    fun updateDietaryFiber(update: String) {
        _uiState.update {
            it.copy(
                dietaryFiber = validateNumber(update)
            )
        }
    }

    fun updateTotalSugars(update: String) {
        _uiState.update {
            it.copy(
                totalSugars = validateNumber(update)
            )
        }
    }

    fun updateProtein(update: String) {
        _uiState.update {
            it.copy(
                protein = validateNumber(update)
            )
        }
    }

    fun formatInput() {
        _uiState.update {
            it.copy(
                servingType = formatNumber(it.servingType),
                servingAmount = formatNumber(it.servingAmount),
                calories = formatNumber(it.calories),
                totalFat = formatNumber(it.totalFat),
                saturatedFat = formatNumber(it.saturatedFat),
                transFat = formatNumber(it.transFat),
                cholesterol = formatNumber(it.cholesterol),
                sodium = formatNumber(it.sodium),
                totalCarbohydrate = formatNumber(it.totalCarbohydrate),
                dietaryFiber = formatNumber(it.dietaryFiber),
                totalSugars = formatNumber(it.totalSugars),
                protein = formatNumber(it.protein)
            )
        }
    }

    fun formatNumber(value: String) : String {
        var formatted = value.trim()
        if(formatted.isEmpty()){
            return formatted
        }
        if(formatted.first() == '.') {
            formatted = "0$formatted"
        }
        if(formatted.last() == '.') {
            formatted = "${formatted}00"
        }
        return formatted
    }

    fun validateNumber(update: String) : String {
        return when {
            update.isEmpty() -> {
                update
            }

            update.last().isDigit() -> {
                update
            }

            update.last() == '.' -> {
                if(update.dropLast(1).contains('.')) {
                    update.dropLast(1)
                } else {
                    update
                }
            }

            else -> {
                update.dropLast(1)
            }
        }
    }

    fun updateFoodNameError(newError: String?) {
        _uiState.update {
            it.copy(
                foodNameError = newError
            )
        }
    }

    fun updateCaloriesError(newError: Boolean) {
        _uiState.update {
            it.copy(
                caloriesError = newError
            )
        }
    }

    private fun validInput(): Boolean {
        var passed = true
        if (_uiState.value.foodName.trim().isEmpty()) {
            updateFoodNameError(newError = "Please enter a food name")
            passed = false
        }
        if (_uiState.value.calories.isEmpty()) {
            updateCaloriesError(newError = true)
            passed = false
        }
        return passed
    }

    fun onSaveClick(): Boolean {
        formatInput()
        if (validInput().not()) {
            return false
        }
        viewModelScope.launch {
            firestoreUseCase.addFoodDocument(
                food = hashMapOf(
                    "food_name" to _uiState.value.foodName.trim(),
                    "brand_name" to _uiState.value.brandName.trim(),
                    "serving_type_count" to (_uiState.value.servingType.toFloatOrNull() ?: 1f),
                    "serving_type_units" to _uiState.value.servingTypeUnits,
                    "serving_amount_count" to (_uiState.value.servingAmount.toFloatOrNull() ?: 0f),
                    "serving_amount_units" to _uiState.value.servingAmountUnits,
                    "calories" to (_uiState.value.servingAmount.toFloatOrNull() ?: 0f),
                    "total_fat" to (_uiState.value.totalFat.toFloatOrNull() ?: 0f),
                    "saturated_fat" to (_uiState.value.saturatedFat.toFloatOrNull() ?: 0f),
                    "trans_fat" to (_uiState.value.transFat.toFloatOrNull() ?: 0f),
                    "cholesterol" to (_uiState.value.cholesterol.toFloatOrNull() ?: 0f),
                    "sodium" to (_uiState.value.sodium.toFloatOrNull() ?: 0f),
                    "total_carbohydrate" to (_uiState.value.totalCarbohydrate.toFloatOrNull()
                        ?: 0f),
                    "dietary_fiber" to (_uiState.value.dietaryFiber.toFloatOrNull() ?: 0f),
                    "total_sugars" to (_uiState.value.totalSugars.toFloatOrNull() ?: 0f),
                    "protein" to (_uiState.value.protein.toFloatOrNull() ?: 0f),
                )
            )

        }
        return true
    }
}