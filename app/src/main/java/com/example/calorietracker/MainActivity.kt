package com.example.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calorietracker.ui.daily_log.DailyLogScreen
import com.example.calorietracker.ui.daily_log.DailyLogViewModel
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    val viewModel = DailyLogViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                DailyLogScreen(viewModel)
            }
        }
    }
}