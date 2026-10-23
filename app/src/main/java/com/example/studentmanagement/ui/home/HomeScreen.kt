package com.example.studentmanagement.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.studentmanagement.ui.animation.LoadingAnimation

@Composable
fun HomeScreen(
    steps: Int,
    onCameraClick: () -> Unit,
    onMusicClick: () -> Unit,
    onVideoClick: () -> Unit,
    onSensorClick: () -> Unit
) {
    var headerVisible by remember {
        mutableStateOf(false)
    }

    var featuresVisible by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var showMessage by remember {
        mutableStateOf(false)
    }

    var selected by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        headerVisible = true
        featuresVisible = true
    }

    val cardHeight by animateDpAsState(
        targetValue = if (expanded) 220.dp else 120.dp,
        label = "cardHeight"
    )

    val transition = updateTransition(
        targetState = selected,
        label = "cardTransition"
    )

    val scale by transition.animateFloat(
        label = "scale"
    ) { state ->
        if (state) 1.05f
        else 1f
    }

    val alpha by transition.animateFloat(
        label = "alpha"
    ) { state ->
        if (state) 1f
        else 0.7f
    }

    val progress by animateFloatAsState(
        targetValue = steps / 10000f,
        label = "stepProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            "Student Life",
            style = MaterialTheme.typography.headlineLarge
        )

        AnimatedVisibility(
            visible = headerVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Text(
                "Hello, Student 👋",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(
            Modifier.height(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .clickable { selected = !selected }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "👟 $steps Steps",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(
            Modifier.height(12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { expanded = !expanded }
            ) {
                Text(
                    if (expanded) "Collapse" else "Expand"
                )
            }

            Button(
                onClick = { showMessage = !showMessage }
            ) {
                Text("Show Message")
            }
        }

        AnimatedVisibility(
            visible = showMessage,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "🎉 Keep Going!",
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        Spacer(
            Modifier.height(16.dp)
        )

        AnimatedVisibility(
            visible = featuresVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(
                modifier = Modifier.animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureCard(
                        title = "Camera",
                        onClick = onCameraClick,
                        modifier = Modifier.weight(1f)
                    )

                    FeatureCard(
                        title = "Music",
                        onClick = onMusicClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureCard(
                        title = "Video",
                        onClick = onVideoClick,
                        modifier = Modifier.weight(1f)
                    )

                    FeatureCard(
                        title = "Sensor",
                        onClick = onSensorClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(
            Modifier.height(16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingAnimation(steps = steps)
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            title,
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
