package com.example.calorietracker.ui.add_food

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.firestore.FirestoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddFoodUiState(
    val foodName: String = "",
    val brandName: String? = null,
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
)

class AddFoodViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val firestoreUseCase: FirestoreUseCase = FirestoreUseCase()
    private val foodId: String = savedStateHandle["foodId"] ?: ""
    private val _uiState = MutableStateFlow(AddFoodUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val foodData = firestoreUseCase.getFoodDocument(foodId)
            foodData?.let {
                updateUiState(
                    foodName = foodData["food_name"].toString(),
                    brandName = foodData["brand_name"].toString(),
                )
            }

        }

    }

    fun updateUiState(foodName: String, brandName: String? = null) {
        _uiState.update {
            it.copy(
                foodName = foodName,
                brandName = brandName
            )
        }
    }
}