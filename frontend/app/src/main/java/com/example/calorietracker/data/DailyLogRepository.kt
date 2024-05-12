package com.example.calorietracker.data

interface DailyLogRepository {
    suspend fun insertDailyLog(dailyLog: DailyLog)

    suspend fun deleteDailyLog(dailyLog: DailyLog)

    suspend fun getDailyLogByDate(date: String): DailyLog?

    suspend fun insertFood(food: Food)

    suspend fun deleteFood(food: Food)

    suspend fun getFoodById(foodId: Int): Food?

    fun getAllFoodInMeal(mealName: String): List<Food>
    fun getAllFood(date: String): List<Food>

    suspend fun insertMeal(meal: Meal)

    suspend fun deleteMeal(meal: Meal)

    suspend fun getMeal(name: String, date: String): Meal?

    fun getAllMeal(date: String): List<Meal>

    fun getAllMeal(): List<Meal>

    suspend fun insertNutritionBudget(nutritionBudget: NutritionBudget)

    suspend fun deleteNutritionBudget(nutritionBudget: NutritionBudget)

    suspend fun getNutritionBudget(): NutritionBudget?

}