package com.example.calorietracker.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calorietracker.firestore.FirestoreUseCase
import com.example.calorietracker.ui.add_food.AddFoodScreen
import com.example.calorietracker.ui.add_food.AddFoodViewModel
import com.example.calorietracker.ui.search_food.SearchFoodScreen
import com.example.calorietracker.ui.search_food.SearchFoodViewModel
import com.example.calorietracker.ui.auth.AuthUiClient
import com.example.calorietracker.ui.create_food.CreateFoodScreen
import com.example.calorietracker.ui.create_food.CreateFoodViewModel
import com.example.calorietracker.ui.daily_log.DailyLogScreen
import com.example.calorietracker.ui.forgot_password.ForgotPasswordScreen
import com.example.calorietracker.ui.forgot_password.ForgotPasswordViewModel
import com.example.calorietracker.ui.profile.ProfileScreen
import com.example.calorietracker.ui.sign_in.SignInScreen
import com.example.calorietracker.ui.sign_in.SignInViewModel
import com.example.calorietracker.ui.sign_up.SignUpScreen
import com.example.calorietracker.ui.sign_up.SignUpViewModel
import com.example.calorietracker.ui.welcome.WelcomeScreen
import kotlinx.coroutines.launch

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {

    data object Welcome : Screen("welcome")
    data object SignIn : Screen("sign_in")
    data object ForgotPassword : Screen("forgot_password")

    data object SignUp : Screen("sign_up")

    data object Profile : Screen("profile")
    data object DailyLog : Screen("daily_log")

    data object SearchFood : Screen(
        route = "search_food/{mealName}-{dateId}",
        navArguments = listOf(
            navArgument("mealName") {
                type = NavType.StringType
            },
            navArgument("dateId") {
                type = NavType.StringType
            },
        )
    ) {
        fun createRoute(mealName: String, dateId: String) = "search_food/${mealName}-${dateId}"
    }

    data object CreateFood : Screen(
        route = "create_food/{mealName}-{dateId}",
        navArguments = listOf(
            navArgument("mealName") {
                type = NavType.StringType
            },
            navArgument("dateId") {
                type = NavType.StringType
            },
        )
    ) {
        fun createRoute(mealName: String, dateId: String) = "create_food/${mealName}-${dateId}"
    }

    data object AddFood: Screen(
        route = "add_food/{foodId}",
        navArguments = listOf(
            navArgument("foodId") {
                type = NavType.StringType
            }
        )
    ) {
        fun createRoute(foodId: String) = "add_food/${foodId}"
    }
}

@Composable
fun CalorieTrackerNavHost(
    navController: NavHostController = rememberNavController(),
    authUiClient: AuthUiClient
) {
    val lifecycleScope = rememberCoroutineScope()
    val startDestination = if (authUiClient.getSignedInUser() != null) {
        Screen.DailyLog.route
    } else {
        Screen.Welcome.route
    }
    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLogInClick = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        composable(route = Screen.SignIn.route) {
            val viewModel = viewModel<SignInViewModel>(
                factory = viewModelFactory {
                    initializer {
                        SignInViewModel(authUiClient = authUiClient)
                    }
                }
            )
            SignInScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onSignUpClick = {
                    navController.popBackStack(Screen.Welcome.route, false)
                    navController.navigate(Screen.SignUp.route)
                },
                onSignInSuccessful = {
                    while (navController.currentBackStackEntry != null) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.DailyLog.route)
                    viewModel.resetUiState()
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            val viewModel = viewModel<ForgotPasswordViewModel>(
                factory = viewModelFactory {
                    initializer {
                        ForgotPasswordViewModel(authUiClient = authUiClient)
                    }
                }
            )
            ForgotPasswordScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = Screen.SignUp.route) {
            val viewModel = viewModel<SignUpViewModel>(
                factory = viewModelFactory {
                    initializer {
                        SignUpViewModel(authUiClient = authUiClient)
                    }
                }
            )
            SignUpScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onSignInClick = {
                    navController.popBackStack(Screen.Welcome.route, false)
                    navController.navigate(Screen.SignIn.route)
                },
                onSignInSuccessful = {
                    while (navController.currentBackStackEntry != null) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.DailyLog.route)
                },
            )
        }

        composable(route = Screen.Profile.route) {
            val context = LocalContext.current
            ProfileScreen(
                userData = authUiClient.getSignedInUser(),
                onSignOut = {
                    navController.navigate(Screen.Welcome.route)
                    lifecycleScope.launch {
                        authUiClient.signOut()
                        Toast.makeText(
                            context,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }


        composable(route = Screen.DailyLog.route) {
            DailyLogScreen(
                onAddClick = { mealName, dateId ->
                    navController.navigate(
                        Screen.SearchFood.createRoute(mealName = mealName, dateId = dateId)
                    )
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        composable(
            route = Screen.SearchFood.route,
            arguments = Screen.SearchFood.navArguments
        ) {
            val viewModel = viewModel<SearchFoodViewModel>(
                factory = viewModelFactory {
                    initializer {
                        SearchFoodViewModel()
                    }
                }
            )
            SearchFoodScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() },
                onCreateFoodClick = {
                    navController.navigate(
                        Screen.CreateFood.route
                    )
                },
                onAddFoodClick = {foodId ->
                    navController.navigate(
                        Screen.AddFood.createRoute(foodId)
                    )
                }
            )
        }

        composable(
            route = Screen.CreateFood.route,
            arguments = Screen.CreateFood.navArguments
        ) {
            val viewModel = viewModel<CreateFoodViewModel>()
            CreateFoodScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() },
                onSaveClick = { foodId ->
                    navController.navigate(
                        Screen.AddFood.createRoute(foodId)
                    )
                }
            )
        }

        composable(
            route = Screen.AddFood.route,
        ) {
            val viewModel = viewModel<AddFoodViewModel>()
            AddFoodScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
