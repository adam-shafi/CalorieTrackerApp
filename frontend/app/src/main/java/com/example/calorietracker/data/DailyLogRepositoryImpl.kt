package com.example.calorietracker.data

class DailyLogRepositoryImpl(
    private val dailyLogDao: DailyLogDao,
    private val mealDao: MealDao,
    private val foodDao: FoodDao,
    private val nutritionBudgetDao: NutritionBudgetDao
) : DailyLogRepository {
    override suspend fun insertDailyLog(dailyLog: DailyLog) {
        dailyLogDao.insertDailyLog(dailyLog)
    }

    override suspend fun deleteDailyLog(dailyLog: DailyLog) {
        dailyLogDao.deleteDailyLog(dailyLog)
    }

    override suspend fun getDailyLogByDate(date: String): DailyLog? {
        return dailyLogDao.getDailyLogByDate(date)
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

    override fun getAllFoodInMeal(mealName: String): List<Food> {
        return foodDao.getAllFoodInMeal(mealName)
    }

    override fun getAllFood(date: String): List<Food> {
        return foodDao.getAllFood(date)
    }

    override suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    override suspend fun deleteMeal(meal: Meal) {
        mealDao.deleteMeal(meal)
    }

    override suspend fun getMeal(name: String, date: String): Meal? {
        return mealDao.getMeal(name, date)
    }

    override fun getAllMeal(date: String): List<Meal> {
        return mealDao.getAllMeal(date)
    }

    override fun getAllMeal(): List<Meal> {
        return mealDao.getAllMeal()
    }

    override suspend fun insertNutritionBudget(nutritionBudget: NutritionBudget) {
        nutritionBudgetDao.insertNutritionBudget(nutritionBudget)
    }

    override suspend fun deleteNutritionBudget(nutritionBudget: NutritionBudget) {
        nutritionBudgetDao.deleteNutritionBudget(nutritionBudget)
    }

    override suspend fun getNutritionBudget(): NutritionBudget? {
        return nutritionBudgetDao.getNutritionBudget()
    }
}