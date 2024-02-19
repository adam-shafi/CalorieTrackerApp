package com.example.calorietracker.data

import kotlinx.coroutines.flow.Flow

interface DailyLogRepository {
    suspend fun insertDailyLog(dailyLog: DailyLog)

    suspend fun deleteDailyLog(dailyLog: DailyLog)

    suspend fun getDailyLogByDate(date: String): DailyLog?

    suspend fun insertFood(food: Food)

    suspend fun deleteFood(food: Food)

    suspend fun getFoodById(foodId: Int): Food?

    fun getAllFoodInMeal(mealId: Int): Flow<List<Food>>
    fun getAllFood(date: String): Flow<List<Food>>

    suspend fun insertMeal(meal: Meal)

    suspend fun deleteMeal(meal: Meal)

    suspend fun getMealById(mealId: Int): Meal?

    fun getAllMeal(date: String): Flow<List<Meal>>

    suspend fun insertNutritionBudget(nutritionBudget: NutritionBudget)

    suspend fun deleteNutritionBudget(nutritionBudget: NutritionBudget)

    suspend fun getNutritionBudget(): NutritionBudget?

}