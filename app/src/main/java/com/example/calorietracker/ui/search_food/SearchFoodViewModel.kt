package com.example.calorietracker.ui.search_food

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.ServingAmount
import com.example.calorietracker.firestore.FirestoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchFoodCardInfo(
    val foodName: String = "",
    val brandName: String = "",
    val servingAmount: ServingAmount = ServingAmount(),
    val calories: String = "",
    val foodId: String = "",
//    val painter: Painter
)

data class SearchFoodUiState(
    val selectedMealName: String = "",
    val mealNames: List<String> = listOf(),
    val foodCards: List<SearchFoodCardInfo> = listOf()
)

class SearchFoodViewModel(
    private val firestoreUseCase: FirestoreUseCase = FirestoreUseCase(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchFoodUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateSavedState()
    }

    fun updateSavedState() {
        viewModelScope.launch(Dispatchers.IO) {
            val documents = firestoreUseCase.getAllFoodDocuments()
            val foodCards = mutableListOf<SearchFoodCardInfo>()
            documents.forEach {
                val document = it.data
                val servingAmountList = document?.get("serving_amounts")
                if (servingAmountList is List<*>) {
                    val servingAmountHashMap = servingAmountList[0]
                    if (servingAmountHashMap is HashMap<*, *>) {
                        val servingAmount = ServingAmount(
                            amount = servingAmountHashMap["amount"].toString().toFloatOrNull() ?: 0f,
                            units = servingAmountHashMap["units"].toString().toIntOrNull() ?: 0
                        )
                        foodCards.add(
                            SearchFoodCardInfo(
                                foodId = it.id,
                                foodName = document["food_name"].toString(),
                                brandName = document["brand_name"].toString(),
                                servingAmount = servingAmount,
                                calories = document["calories"].toString()
                            )
                        )
                    }
                }
            }

            updateUiState(
                selectedMealName = "Breakfast",
                mealNames = getMealNames(),
                foodCards = foodCards
            )
        }
    }

    private fun updateUiState(
        selectedMealName: String = _uiState.value.selectedMealName,
        mealNames: List<String> = _uiState.value.mealNames,
        foodCards: List<SearchFoodCardInfo> = _uiState.value.foodCards
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedMealName = selectedMealName,
                mealNames = mealNames,
                foodCards = foodCards
            )
        }
    }

    fun updateSelectedMealName(newMealName: String) {
        updateUiState(selectedMealName = newMealName)
    }

    private fun getMealNames(): List<String> {
//        val meals = repository.getAllMeal(dateId)
//        val mealNames = mutableListOf<String>()
//        meals.forEach { meal ->
//            mealNames.add(meal.name)
//        }
//        return mealNames.toList()

        return listOf("Breakfast", "Lunch", "Dinner")
    }


}