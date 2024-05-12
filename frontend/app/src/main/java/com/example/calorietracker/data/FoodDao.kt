package com.example.calorietracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM Food WHERE foodId = :foodId")
    suspend fun getFoodById(foodId: Int): Food?

    @Query("SELECT * FROM Food WHERE mealName = :mealName")
    fun getAllFoodInMeal(mealName: String): List<Food>

    @Query("SELECT * FROM Food WHERE date = :date")
    fun getAllFood(date: String): List<Food>
}