package com.example.studentmanagement.ui.sensor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentmanagement.sensor.StepCounterManager

@Composable
fun StepCounterScreen(manager: StepCounterManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Today's Steps",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "${manager.steps}",
            style = MaterialTheme.typography.displayLarge
        )

        Text("steps")

        LinearProgressIndicator(
            progress = { manager.steps / 10000f }
        )
    }
}
