package com.example.studentmanagement.ui.music

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.studentmanagement.R

@Composable
fun MusicScreen() {
    val context = LocalContext.current

    val mediaPlayer = remember {
        MediaPlayer.create(
            context,
            R.raw.music
        )
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Student Music Player",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            Modifier.height(30.dp)
        )

        Row {
            Button(
                onClick = {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        isPlaying = false
                    } else {
                        mediaPlayer.start()
                        isPlaying = true
                    }
                }
            ) {
                Text(
                    if (isPlaying)
                        "Pause"
                    else
                        "Play"
                )
            }

            Button(
                onClick = {
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                    isPlaying = false
                }
            ) {
                Text("Stop")
            }
        }
    }
}
