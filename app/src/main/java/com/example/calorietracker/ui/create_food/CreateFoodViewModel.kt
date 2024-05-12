package com.example.calorietracker.ui.create_food

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.calorietracker.data.ServingAmount
import com.example.calorietracker.firestore.FirestoreUseCase
import com.example.calorietracker.util.Utility
import com.example.calorietracker.util.Utility.Companion.validateNumber
import com.example.calorietracker.values.ServingSizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

data class ServingAmountUiState(
    val amount: String = "",
    val units: Int = 0,
    val dropdownText: String = "",
)


data class CreateFoodUiState(
    val foodName: String = "",
    val brandName: String = "",
    val servingAmounts: List<ServingAmountUiState> = listOf(
        ServingAmountUiState(
            dropdownText = ServingSizes.servingAmountSingular[0],
        )
    ),
    val servingWeight: ServingAmountUiState = ServingAmountUiState(
        dropdownText = ServingSizes.servingWeightUnitsPlural[0],
    ),
    val servingVolume: ServingAmountUiState = ServingAmountUiState(
        dropdownText = ServingSizes.servingVolumeUnitsPlural[0],
    ),
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
    val servingsError: Boolean = false,
)

class CreateFoodViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val firestoreUseCase: FirestoreUseCase = FirestoreUseCase()
    private val _uiState = MutableStateFlow(CreateFoodUiState())
    val uiState = _uiState.asStateFlow()


    fun addServingAmount() {
        _uiState.update { createFoodUiState ->

            var minUnused = 0
            createFoodUiState.servingAmounts.sortedBy {
                it.units
            }.forEach {
                if (it.units == minUnused) {
                    minUnused += 1
                }
            }

            val updatedServingAmounts = createFoodUiState.servingAmounts.toMutableList()
            updatedServingAmounts.add(
                ServingAmountUiState(
                    units = minUnused,
                    dropdownText = ServingSizes.servingAmountSingular[minUnused],
                )
            )
            createFoodUiState.copy(
                servingAmounts = updatedServingAmounts
            )
        }
    }

    fun getServingAmountDropdownItems(index: Int): List<String> {
        val isSingular =
            _uiState.value.servingAmounts[index].amount == "1" || _uiState.value.servingAmounts[index].amount.isEmpty()
        val dropdownItems: MutableList<String> = if (isSingular) {
            ServingSizes.servingAmountSingular.toMutableList()
        } else {
            ServingSizes.servingAmountPlural.toMutableList()
        }
        _uiState.value.servingAmounts.forEachIndexed { i, servingAmount ->
            if (i != index) {
                var text = servingAmount.dropdownText
                if (isSingular.not() && text.endsWith("s").not()) {
                    text += "s"
                } else if (isSingular && text.endsWith("s")) {
                    text = text.dropLast(1)
                }
                dropdownItems.remove(text)
            }
        }
        return dropdownItems
    }

    fun getServingWeightDropdownItems(): List<String> {
        val isSingular = _uiState.value.servingWeight.amount == "1"
        return if (isSingular) ServingSizes.servingWeightUnitsSingular else ServingSizes.servingWeightUnitsPlural
    }

    fun getServingVolumeDropdownItems(): List<String> {
        val isSingular = _uiState.value.servingVolume.amount == "1"
        return if (isSingular) ServingSizes.servingVolumeUnitsSingular else ServingSizes.servingVolumeUnitsPlural
    }

    fun deleteServingAmount(index: Int) {
        _uiState.update {
            val updatedServingAmounts = it.servingAmounts.toMutableList()
            updatedServingAmounts.removeAt(index)
            if (updatedServingAmounts.isEmpty()) {
                it.copy(
                    servingsError = true
                )
            } else {
                it.copy(
                    servingAmounts = updatedServingAmounts
                )
            }

        }
    }

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

    fun updateServingWeight(
        amount: String = _uiState.value.servingWeight.amount,
        unitsString: String = _uiState.value.servingWeight.dropdownText
    ) {
        _uiState.update {
            val updatedAmount = validateNumber(amount)
            val units = if (unitsString.endsWith("s")) {
                ServingSizes.servingWeightUnitsPlural.indexOf(unitsString)
            } else {
                ServingSizes.servingWeightUnitsSingular.indexOf(unitsString)
            }
            it.copy(
                servingWeight = ServingAmountUiState(
                    amount = updatedAmount,
                    units = units,
                    dropdownText = if (updatedAmount == "1") ServingSizes.servingWeightUnitsSingular[units] else ServingSizes.servingWeightUnitsPlural[units],
                )
            )
        }
    }

    fun updateServingVolume(
        amount: String = _uiState.value.servingVolume.amount,
        unitsString: String = _uiState.value.servingVolume.dropdownText
    ) {
        _uiState.update {
            val updatedAmount = validateNumber(amount)
            val units = if (unitsString.endsWith("s")) {
                ServingSizes.servingVolumeUnitsPlural.indexOf(unitsString)
            } else {
                ServingSizes.servingVolumeUnitsSingular.indexOf(unitsString)
            }
            it.copy(
                servingVolume = ServingAmountUiState(
                    amount = updatedAmount,
                    units = units,
                    dropdownText = if (updatedAmount == "1") ServingSizes.servingVolumeUnitsSingular[units] else ServingSizes.servingVolumeUnitsPlural[units],
                )
            )
        }
    }

    fun updateServingAmounts(
        index: Int,
        amount: String = _uiState.value.servingAmounts[index].amount,
        unitsString: String = _uiState.value.servingAmounts[index].dropdownText
    ) {
        _uiState.update {
            val updatedServingAmounts = it.servingAmounts.toMutableList()
            val updatedAmount = validateNumber(amount)
            val units = if (unitsString.endsWith("s")) {
                ServingSizes.servingAmountPlural.indexOf(unitsString)
            } else {
                ServingSizes.servingAmountSingular.indexOf(unitsString)
            }
            updatedServingAmounts[index] =
                ServingAmountUiState(
                    amount = updatedAmount,
                    units = units,
                    dropdownText = if (updatedAmount == "1" || updatedAmount.isEmpty()) ServingSizes.servingAmountSingular[units] else ServingSizes.servingAmountPlural[units],
                )
            it.copy(
                servingAmounts = updatedServingAmounts,
                servingsError = false
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
            val formattedServingAmounts = it.servingAmounts.map { servingAmount ->
                ServingAmountUiState(
                    amount = formatNumber(servingAmount.amount),
                    units = servingAmount.units,
                    dropdownText = servingAmount.dropdownText,
                )
            }.toMutableList()
            it.copy(
                servingWeight = ServingAmountUiState(
                    amount = formatNumber(it.servingWeight.amount),
                    units = it.servingWeight.units,
                    dropdownText = it.servingWeight.dropdownText,
                ),
                servingVolume = ServingAmountUiState(
                    amount = formatNumber(it.servingVolume.amount),
                    units = it.servingVolume.units,
                    dropdownText = it.servingVolume.dropdownText,
                ),
                servingAmounts = formattedServingAmounts,
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

    private fun formatNumber(value: String): String {
        var formatted = value.trim()
        if (formatted.isEmpty()) {
            return formatted
        }
        if (formatted.first() == '.') {
            formatted = "0$formatted"
        }
        if (formatted.last() == '.') {
            formatted = "${formatted}00"
        }
        return formatted
    }



    private fun updateFoodNameError(newError: String?) {
        _uiState.update {
            it.copy(
                foodNameError = newError
            )
        }
    }

    private fun updateCaloriesError(newError: Boolean) {
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

    private fun formatServingAmounts(): List<ServingAmount> {
        val filteredServingAmounts = _uiState.value.servingAmounts.filter { it.amount.isNotBlank() }

        return if (filteredServingAmounts.isEmpty()) {
            listOf(
                ServingAmount(
                    amount = 1f,
                    units = _uiState.value.servingAmounts[0].units
                )
            )
        } else {
            filteredServingAmounts.map {
                ServingAmount(
                    amount = it.amount.toFloat(),
                    units = it.units
                )
            }
        }

    }

    suspend fun onSaveClick(): String = withContext(Dispatchers.IO) {
        var foodId = ""
        formatInput()
        if (validInput().not()) {
            return@withContext foodId
        }
        val job = async {
            foodId = firestoreUseCase.addFoodDocument(
                food = hashMapOf(
                    "food_name" to _uiState.value.foodName.trim(),
                    "brand_name" to _uiState.value.brandName.trim(),
                    "serving_weight" to (
                            ServingAmount(
                                amount = _uiState.value.servingWeight.amount.toFloatOrNull() ?: 0f,
                                units = _uiState.value.servingWeight.units
                            )
                            ),
                    "serving_volume" to (
                            ServingAmount(
                                amount = _uiState.value.servingVolume.amount.toFloatOrNull() ?: 0f,
                                units = _uiState.value.servingVolume.units
                            )
                            ),
                    "serving_amounts" to (formatServingAmounts()),
                    "calories" to (_uiState.value.calories.toFloatOrNull() ?: 0f),
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
        job.await()
        return@withContext foodId


    }
}