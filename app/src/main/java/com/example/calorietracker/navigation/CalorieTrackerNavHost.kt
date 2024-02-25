package com.example.calorietracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calorietracker.ui.add_food.AddFoodScreen
import com.example.calorietracker.ui.daily_log.DailyLogScreen

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object DailyLog : Screen("daily_log")

    data object AddFood : Screen(
        route = "add_food/{mealName}-{dateId}",
        navArguments = listOf(
            navArgument("mealName") {
                type = NavType.StringType
            },
            navArgument("dateId") {
                type = NavType.StringType
            },
        )
    ) {
        fun createRoute(mealName: String, dateId: String) = "add_food/${mealName}-${dateId}"
    }
}

@Composable
fun CalorieTrackerNavHost(
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = Screen.DailyLog.route) {
        composable(route = Screen.DailyLog.route) {
            DailyLogScreen(
                onAddClick = { mealName, dateId ->
                    navController.navigate(
                        Screen.AddFood.createRoute(mealName = mealName, dateId = dateId)
                    )
                }
            )
        }
        composable(
            route = Screen.AddFood.route,
            arguments = Screen.AddFood.navArguments
        ) {
            AddFoodScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}