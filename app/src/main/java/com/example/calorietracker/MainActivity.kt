package com.example.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.calorietracker.navigation.CalorieTrackerNavHost
import com.example.calorietracker.ui.auth.AuthUiClient
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authUiClient by lazy {
        AuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                CalorieTrackerNavHost(authUiClient = authUiClient)
            }
        }
    }



}