package com.example.calorietracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM Meal WHERE mealId = :mealId")
    suspend fun getMealById(mealId: Int): Meal?

    @Query("SELECT * FROM Meal WHERE logDate = :date")
    fun getAllMeal(date: String): Flow<List<Meal>>
}