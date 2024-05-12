package com.example.calorietracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NutritionBudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionBudget(nutritionBudget: NutritionBudget)

    @Delete
    suspend fun deleteNutritionBudget(nutritionBudget: NutritionBudget)

    @Query("SELECT * FROM NutritionBudget WHERE id = :id")
    suspend fun getNutritionBudget(id: Int = 1): NutritionBudget?

}