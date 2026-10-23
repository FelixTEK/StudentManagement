package com.example.studentmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentmanagement.data.remote.TokenStorage
import com.example.studentmanagement.ui.camera.CameraScreen
import com.example.studentmanagement.ui.home.HomeScreen
import com.example.studentmanagement.ui.login.LoginScreen
import com.example.studentmanagement.ui.music.MusicScreen
import com.example.studentmanagement.ui.sensor.StepCounterScreen
import com.example.studentmanagement.ui.theme.StudentManagementTheme
import com.example.studentmanagement.ui.video.VideoScreen
import com.example.studentmanagement.worker.SyncScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val HOME = "home"
const val CAMERA = "camera"
const val MUSIC = "music"
const val VIDEO = "video"
const val SENSOR = "sensor"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenStorage: TokenStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SyncScheduler.schedulePeriodicSync(this)

        setContent {
            StudentManagementTheme {
                var isLoggedIn by remember {
                    mutableStateOf(tokenStorage.getToken() != null)
                }

                if (isLoggedIn) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = HOME
                    ) {
                        composable(HOME) {
                            HomeScreen(
                                onCameraClick = { navController.navigate(CAMERA) },
                                onMusicClick = { navController.navigate(MUSIC) },
                                onVideoClick = { navController.navigate(VIDEO) },
                                onSensorClick = { navController.navigate(SENSOR) }
                            )
                        }

                        composable(CAMERA) { CameraScreen() }
                        composable(MUSIC) { MusicScreen() }
                        composable(VIDEO) { VideoScreen() }
                        composable(SENSOR) { StepCounterScreen() }
                    }
                } else {
                    LoginScreen(
                        onLoginSuccess = { isLoggedIn = true }
                    )
                }
            }
        }
    }
}
