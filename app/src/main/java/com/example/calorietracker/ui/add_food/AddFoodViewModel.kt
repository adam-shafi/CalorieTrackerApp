package com.example.calorietracker.ui.add_food

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.ServingAmount
import com.example.calorietracker.firestore.FirestoreUseCase
import com.example.calorietracker.ui.create_food.ServingAmountUiState
import com.example.calorietracker.ui.search_food.SearchFoodCardInfo
import com.example.calorietracker.util.Utility.Companion.validateNumber
import com.example.calorietracker.values.ServingSizes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ServingAmountUnit {
    class ServingAmount(val index: Int) : ServingAmountUnit()
    class ServingWeight(val index: Int) : ServingAmountUnit()
    class ServingVolume(val index: Int) : ServingAmountUnit()
}

data class AddFoodUiState(
    val foodName: String = "",
    val brandName: String? = null,
    val selectedMeal: String = "Breakfast",
    val selectedServingAmount: String = "1",
    val selectedServingAmountUnits: String = "",
    val servingAmountUnits: List<ServingAmountUnit> = listOf(),
    val servingAmounts: List<ServingAmount> = listOf(),
    val servingWeight: ServingAmount = ServingAmount(),
    val servingVolume: ServingAmount = ServingAmount(),
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
            val servingAmountList = foodData?.get("serving_amounts")
            val servingWeightMap = foodData?.get("serving_weight")
            val servingVolumeMap = foodData?.get("serving_volume")
            val servingAmountUnits = mutableListOf<ServingAmountUnit>()
            if (servingAmountList is List<*> && servingWeightMap is HashMap<*, *> && servingVolumeMap is HashMap<*, *>) {
                val servingAmounts = mutableListOf<ServingAmount>()
                for (servingAmountHashMap in servingAmountList) {
                    if (servingAmountHashMap is HashMap<*, *>) {
                        servingAmounts.add(
                            ServingAmount(
                                amount = servingAmountHashMap["amount"].toString().toFloatOrNull()
                                    ?: 0f,
                                units = servingAmountHashMap["units"].toString().toIntOrNull() ?: 0
                            )
                        )
                        if (servingAmounts.last().amount > 0f) {
                            servingAmountUnits.add(ServingAmountUnit.ServingAmount(servingAmounts.last().units))
                        }
                    }
                }
                val servingWeight = ServingAmount(
                    amount = servingWeightMap["amount"].toString().toFloatOrNull() ?: 0f,
                    units = servingWeightMap["units"].toString().toIntOrNull() ?: 0
                )
                if (servingWeight.amount > 0f) {
                    servingAmountUnits.add(ServingAmountUnit.ServingWeight(servingWeight.units))
                }
                val servingVolume = ServingAmount(
                    amount = servingVolumeMap["amount"].toString().toFloatOrNull() ?: 0f,
                    units = servingVolumeMap["units"].toString().toIntOrNull() ?: 0
                )
                if (servingVolume.amount > 0f) {
                    servingAmountUnits.add(ServingAmountUnit.ServingVolume(servingVolume.units))
                }


                updateUiState(
                    foodName = foodData["food_name"].toString(),
                    brandName = foodData["brand_name"].toString(),
                    servingAmountUnits = servingAmountUnits,
                    servingVolume = servingVolume,
                    servingWeight = servingWeight,
                    servingAmounts = servingAmounts
                )

                updateUiState(
                    selectedServingAmountUnits = getServingUnitsDropdownItems()[0]
                )

            }


        }

    }

    fun getServingUnitsDropdownItems(): List<String> {
        val isSingular = uiState.value.selectedServingAmount == "1"
        val dropdownItems = mutableListOf<String>()
        for (item in uiState.value.servingAmountUnits) {
            when (item) {
                is ServingAmountUnit.ServingAmount -> {
                    dropdownItems.add(
                        if (isSingular) ServingSizes.servingAmountSingular[item.index] else ServingSizes.servingAmountPlural[item.index]
                    )
                }

                is ServingAmountUnit.ServingWeight -> {
                    dropdownItems.add(
                        if (isSingular) ServingSizes.servingWeightUnitsSingular[item.index] else ServingSizes.servingWeightUnitsPlural[item.index]
                    )
                }

                is ServingAmountUnit.ServingVolume -> {
                    dropdownItems.add(
                        if (isSingular) ServingSizes.servingVolumeUnitsSingular[item.index] else ServingSizes.servingVolumeUnitsPlural[item.index]
                    )
                }
            }
        }
        return dropdownItems
    }

    fun updateSelectedServingAmount(
        amount: String,
    ) {
        val updatedAmount = validateNumber(amount)
        var updatedServingAmountUnits: String = uiState.value.selectedServingAmountUnits
        if(updatedAmount == "1" && updatedServingAmountUnits.endsWith("s")) {
            updatedServingAmountUnits = updatedServingAmountUnits.dropLast(1)
        }
        else if (updatedAmount != "1" && updatedServingAmountUnits.endsWith("s").not()) {
            updatedServingAmountUnits += "s"
        }
        updateUiState(
            selectedServingAmount = updatedAmount,
            selectedServingAmountUnits = updatedServingAmountUnits
        )
    }
    fun updateUiState(
        foodName: String = uiState.value.foodName,
        brandName: String? = uiState.value.brandName,
        selectedMeal: String = uiState.value.selectedMeal,
        selectedServingAmount: String = uiState.value.selectedServingAmount,
        selectedServingAmountUnits: String = uiState.value.selectedServingAmountUnits,
        servingAmountUnits: List<ServingAmountUnit> = uiState.value.servingAmountUnits,
        servingWeight: ServingAmount = uiState.value.servingWeight,
        servingVolume: ServingAmount = uiState.value.servingVolume,
        servingAmounts: List<ServingAmount> = uiState.value.servingAmounts
    ) {
        _uiState.update {
            it.copy(
                foodName = foodName,
                brandName = brandName,
                selectedMeal = selectedMeal,
                selectedServingAmount = selectedServingAmount,
                selectedServingAmountUnits = selectedServingAmountUnits,
                servingAmountUnits = servingAmountUnits,
                servingWeight = servingWeight,
                servingVolume = servingVolume,
                servingAmounts = servingAmounts
            )
        }
    }
}