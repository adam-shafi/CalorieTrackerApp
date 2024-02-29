package com.example.calorietracker.navigation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calorietracker.ui.add_food.AddFoodScreen
import com.example.calorietracker.ui.auth.AuthUiClient
import com.example.calorietracker.ui.daily_log.DailyLogScreen
import com.example.calorietracker.ui.profile.ProfileScreen
import com.example.calorietracker.ui.sign_in.SignInScreen
import com.example.calorietracker.ui.sign_in.SignInViewModel
import com.example.calorietracker.ui.sign_up.SignUpScreen
import com.example.calorietracker.ui.welcome.WelcomeScreen
import kotlinx.coroutines.launch

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {

    data object Welcome : Screen("welcome")
    data object SignIn : Screen("sign_in")

    data object SignUp : Screen("sign_up")

    data object Profile : Screen("profile")
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
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsState()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = authUiClient.googleSignInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            SignInScreen(
                state = state,
                onBackClick = {
                    navController.navigateUp()
                },
                onSignInSuccessful = {
                    navController.navigate(Screen.DailyLog.route)
                    viewModel.resetState()
                },
                onGoogleSignInClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = authUiClient.googleSignIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                onEmailAndPasswordSignInClick = { email, password ->
                    lifecycleScope.launch {
                        viewModel.onSignInResult(
                            authUiClient.signInWithEmailAndPassword(
                                email,
                                password
                            )
                        )
                    }
                },
            )
        }

        composable(route = Screen.SignUp.route) {
            SignUpScreen(
//                state = state,
//                onGoogleSignInClick = {
//                    lifecycleScope.launch {
//                        val signInIntentSender = authUiClient.googleSignIn()
//                        launcher.launch(
//                            IntentSenderRequest.Builder(
//                                signInIntentSender ?: return@launch
//                            ).build()
//                        )
//                    }
//                },
//                onEmailAndPasswordSignInClick = { email, password ->
//                    lifecycleScope.launch {
//                        viewModel.onSignInResult(authUiClient.signInWithEmailAndPassword(email, password))
//                    }
//                },
//                onEmailAndPasswordSignUpClick = { email, password ->
//                    lifecycleScope.launch {
//                        viewModel.onSignInResult(
//                            authUiClient.signUpWithEmailAndPassword(
//                                email,
//                                password
//                            )
//                        )
//                    }
//                }
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
                        Screen.AddFood.createRoute(mealName = mealName, dateId = dateId)
                    )
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
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