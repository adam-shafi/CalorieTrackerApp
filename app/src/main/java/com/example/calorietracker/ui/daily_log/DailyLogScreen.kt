package com.example.calorietracker.ui.daily_log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.CircularProgressbar
import com.example.calorietracker.ui.components.LinearProgressBar
import com.example.calorietracker.ui.theme.CactusGreen
import com.example.calorietracker.ui.theme.ChillRed
import com.example.calorietracker.ui.theme.DescriptionGray
import com.example.calorietracker.ui.theme.dimen_10dp
import com.example.calorietracker.ui.theme.dimen_110dp
import com.example.calorietracker.ui.theme.dimen_16dp
import com.example.calorietracker.ui.theme.dimen_180dp
import com.example.calorietracker.ui.theme.dimen_20dp
import com.example.calorietracker.ui.theme.dimen_8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyLogScreen(
    viewModel: DailyLogViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.daily_log))
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.bar_chart),
                            contentDescription = stringResource(R.string.statistics)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.edit)
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Column {
                MacroSnapshotCard(
                    budget = 2000,
                    food = 1500,
                    result = 500,
                    dataUsage = 75f
                )
                LazyColumn {

                }
            }
        }
    }
}

@Composable
fun MacroSnapshotCard(
    budget: Int,
    food: Int,
    result: Int,
    dataUsage: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimen_10dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(dimen_20dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TitleText(text = "Calories")
            CalorieInfo(budget = budget, result = result, food = food, dataUsage = dataUsage)
            Spacer(modifier = Modifier.height(4.dp))
            TitleText(text = "Macros")
            MacroInfo()
        }

    }
}

@Composable
fun CalorieInfo(
    budget: Int,
    result: Int,
    food: Int,
    dataUsage: Float,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalText(title = budget.toString(), description = stringResource(id = R.string.budget))
        CircularProgressbar(
            name = result.toFloat(),
            size = 140.dp,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            description = if (dataUsage > 100f) stringResource(id = R.string.over) else stringResource(
                id = R.string.remaining
            ),
            dataUsage = dataUsage,
            titleTextStyle = TextStyle(
                fontSize = 24.sp,
                color = if (dataUsage > 100f) ChillRed else CactusGreen
            ),
            descriptionTextStyle = TextStyle(
                fontSize = 20.sp,
                color = DescriptionGray
            )
        )
        VerticalText(title = food.toString(), description = stringResource(id = R.string.food))
    }
}

@Composable
fun CalorieInfo(
    budget: String,
    food: String,
    total: String,
    isOver: Boolean
) {
    TitleText(text = "Calories")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimen_10dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        VerticalText(title = budget, description = stringResource(id = R.string.budget))
        TotalCaloriesText(calories = total, isOver = isOver)
        VerticalText(title = food, description = stringResource(id = R.string.food))

    }
}

@Composable
fun MacroInfo() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MacroElement(
            title = "Protein",
            current = 125,
            total = 175
        )
        MacroElement(
            title = "Carbs",
            current = 112,
            total = 70
        )
        MacroElement(
            title = "Fat",
            current = 20,
            total = 65
        )
    }


}

@Composable
fun VerticalText(
    title: String,
    description: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(text = title)
        Text(
            text = description,
            color = DescriptionGray
        )
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        fontSize = 24.sp,
    )
}

@Composable
fun TotalCaloriesText(
    calories: String,
    isOver: Boolean
) {
    CircularProgressIndicator(
        progress = .25f,
        modifier = Modifier.width(dimen_180dp),
        strokeCap = StrokeCap.Round
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = calories,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isOver) ChillRed else CactusGreen
        )
        Text(
            text = if (isOver) stringResource(id = R.string.over) else stringResource(id = R.string.remaining),
            color = DescriptionGray
        )
    }
}

@Composable
fun RowScope.MacroElement(
    title: String,
    current: Int,
    total: Int,
) {
    val modifier = Modifier
        .weight(1f)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = dimen_16dp),
            text = title,
            fontSize = 20.sp
        )
        LinearProgressBar(
            modifier = Modifier.padding(horizontal = dimen_8dp),
            indicatorNumber = ((current.toFloat() / total) * 100).toInt()
        )
        Text(text = "$current / $total g", fontSize = 14.sp)
    }
}