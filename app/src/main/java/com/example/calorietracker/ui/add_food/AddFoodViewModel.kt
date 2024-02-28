package com.example.calorietracker.ui.add_food

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.data.DailyLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DailyLogRepository,
) : ViewModel() {
    val mealName: String = savedStateHandle.get<String>("mealName")!!
    private val dateId: String = savedStateHandle.get<String>("dateId")!!
    private val _uiState = MutableStateFlow(AddFoodUiState())
    val uiState = _uiState.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState(
                selectedMealName =  mealName,
                mealNames = getMealNames()
            )
        }
    }

    private fun updateUiState(selectedMealName: String? = null, mealNames: List<String>? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedMealName = selectedMealName ?: currentState.selectedMealName,
                mealNames = mealNames ?: currentState.mealNames
            )
        }
    }

    fun updateSelectedMealName(newMealName: String) {
        updateUiState(selectedMealName = newMealName)
    }

    private fun getMealNames() : List<String> {
        val meals = repository.getAllMeal(dateId)
        val mealNames = mutableListOf<String>()
        meals.forEach { meal ->
            mealNames.add(meal.name)
        }
        return mealNames.toList()
    }


}