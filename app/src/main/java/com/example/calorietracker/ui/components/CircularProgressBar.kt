package com.example.calorietracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.ui.theme.dimen_110dp
import com.example.calorietracker.ui.theme.dimen_16dp
import com.example.calorietracker.ui.theme.dimen_8dp


/**
 * A composable function that creates a circular progress bar with data usage information and a display text.
 *
 * @param name The name or label associated with the progress bar.
 * @param size The size of the circular progress bar.
 * @param foregroundIndicatorColor The color of the progress bar indicating the data usage.
 * @param shadowColor The color of the shadow surrounding the circular progress bar.
 * @param indicatorThickness The thickness of the progress bar indicator.
 * @param dataUsage The percentage of data usage to display.
 * @param animationDuration The duration of the animation for data usage change.
 * @param descriptionTextStyle The style for displaying the data usage text.
 */
@Composable
fun CircularProgressbar(
    name: Float,
    description: String,
    size: Dp = dimen_110dp,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    shadowColor: Color = Color.LightGray,
    indicatorThickness: Dp = dimen_8dp,
    dataUsage: Float = 60f,
    animationDuration: Int = 1000,
    titleTextStyle: TextStyle = TextStyle(fontSize = 12.sp),
    descriptionTextStyle: TextStyle = TextStyle(fontSize = 12.sp),
) {
    // State to hold the data usage value for animation
    var dataUsageRemember by remember {
        mutableFloatStateOf(-1f)
    }

    var titleUsageRemember by remember {
        mutableFloatStateOf(-1f)
    }

    // State for animating the data usage value
    val dataUsageAnimate = animateFloatAsState(
        targetValue = dataUsageRemember,
        animationSpec = tween(
            durationMillis = animationDuration
        ), label = ""
    )

    val titleUsageAnimate = animateFloatAsState(
        targetValue = titleUsageRemember,
        animationSpec = tween(
            durationMillis = animationDuration
        ), label = ""
    )

    // Trigger the LaunchedEffect to start the animation when the composable is first launched.
    LaunchedEffect(Unit) {
        dataUsageRemember = dataUsage
        titleUsageRemember = name
    }

    // Box to hold the entire composable
    Box(
        modifier = Modifier
            .size(size)
            .padding(top = dimen_8dp),
        contentAlignment = Alignment.Center
    ) {
        // Canvas drawing for the circular progress bar
        Canvas(
            modifier = Modifier.size(size)
        ) {
            // Draw the shadow around the circular progress bar
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(shadowColor, backgroundColor),
                    center = Offset(x = this.size.width / 2, y = this.size.height / 2),
                    radius = this.size.height / 2
                ),
                radius = this.size.height / 2,
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            // Draw the white background of the circular progress bar
            drawCircle(
                color = backgroundColor,
                radius = (size / 2 - indicatorThickness).toPx(),
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            // Calculate and draw the progress indicator
            val sweepAngle = (dataUsageAnimate.value) * 360 / 100
            drawArc(
                color = foregroundIndicatorColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
                size = Size(
                    width = (size - indicatorThickness).toPx(),
                    height = (size - indicatorThickness).toPx()
                ),
                topLeft = Offset(
                    x = (indicatorThickness / 2).toPx(),
                    y = (indicatorThickness / 2).toPx()
                )
            )
        }

        // Display text below the circular progress bar
        DisplayText(
            animateNumber = titleUsageAnimate,
            description = description,
            titleTextStyle = titleTextStyle,
            descriptionTextStyle = descriptionTextStyle
        )
    }

}

/**
 * A private composable function to display the name and data usage percentage text.
 *
 * @param description The name or label associated with the circular progress bar.
 * @param animateNumber The animated data usage percentage value.
 * @param titleTextStyle The style for displaying the data usage text.
 */
@Composable
private fun DisplayText(
    animateNumber: State<Float>,
    description: String,
    titleTextStyle: TextStyle,
    descriptionTextStyle: TextStyle,

) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(dimen_8dp)
    ) {
        // Display the name with bold font and ellipsis for overflow
        Text(
            text = (animateNumber.value).toInt().toString(),
            fontWeight = FontWeight.Bold,
            style = titleTextStyle,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = description,
            style = descriptionTextStyle
        )

    }
}