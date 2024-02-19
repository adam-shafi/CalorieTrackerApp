package com.example.calorietracker.data

import kotlinx.coroutines.flow.Flow

class DailyLogRepositoryImpl(
    private val dailyLogDao: DailyLogDao,
    private val mealDao: MealDao,
    private val foodDao: FoodDao
) : DailyLogRepository {
    override suspend fun insertDailyLog(dailyLog: DailyLog) {
        dailyLogDao.insertDailyLog(dailyLog)
    }

    override suspend fun deleteDailyLog(dailyLog: DailyLog) {
        dailyLogDao.deleteDailyLog(dailyLog)
    }

    override suspend fun getTodoByDate(date: Long): DailyLog? {
        return dailyLogDao.getTodoByDate(date)
    }

    override suspend fun insertFood(food: Food) {
        foodDao.insertFood(food)
    }

    override suspend fun deleteFood(food: Food) {
        foodDao.deleteFood(food)
    }

    override suspend fun getFoodById(foodId: Int): Food? {
        return foodDao.getFoodById(foodId)
    }

    override fun getAllFood(): Flow<List<Food>> {
        return foodDao.getAllFood()
    }

    override suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    override suspend fun deleteMeal(meal: Meal) {
        mealDao.deleteMeal(meal)
    }

    override suspend fun getMealById(mealId: Int): Meal? {
        return mealDao.getMealById(mealId)
    }

    override fun getAllMeal(): Flow<List<Meal>> {
        return mealDao.getAllMeal()
    }


}