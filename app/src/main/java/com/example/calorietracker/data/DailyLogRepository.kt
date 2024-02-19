package com.example.calorietracker.data

import kotlinx.coroutines.flow.Flow

interface DailyLogRepository {
    suspend fun insertDailyLog(dailyLog: DailyLog)

    suspend fun deleteDailyLog(dailyLog: DailyLog)

    suspend fun getTodoByDate(date: Long): DailyLog?

    suspend fun insertFood(food: Food)

    suspend fun deleteFood(food: Food)

    suspend fun getFoodById(foodId: Int): Food?

    fun getAllFood(): Flow<List<Food>>

    suspend fun insertMeal(meal: Meal)

    suspend fun deleteMeal(meal: Meal)

    suspend fun getMealById(mealId: Int): Meal?

    fun getAllMeal(): Flow<List<Meal>>

}