package com.example.calorietracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DailyLog::class, Meal::class, Food::class],
    version = 1
)
abstract class DailyLogDatabase : RoomDatabase() {
    abstract val dailyLogDao: DailyLogDao
    abstract val mealDao: MealDao
    abstract val foodDao: FoodDao
}