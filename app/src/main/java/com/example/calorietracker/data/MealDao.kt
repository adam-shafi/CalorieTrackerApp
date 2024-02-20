package com.example.calorietracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM Meal WHERE name = :name AND logDate = :logDate")
    suspend fun getMeal(name: String, logDate: String): Meal?

    @Query("SELECT * FROM Meal WHERE logDate = :date")
    fun getAllMeal(date: String): List<Meal>

    @Query("SELECT * FROM Meal")
    fun getAllMeal(): List<Meal>
}