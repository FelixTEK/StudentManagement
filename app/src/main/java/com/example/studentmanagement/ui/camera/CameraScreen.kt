package com.example.studentmanagement.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    val previewView = remember {
        PreviewView(context)
    }

    var filter by remember {
        mutableStateOf("Original")
    }

    if (!hasPermission) {
        LaunchedEffect(Unit) {
            cameraPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    } else {
        LaunchedEffect(Unit) {
            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()

                preview.surfaceProvider = previewView.surfaceProvider

                val cameraSelector =
                    CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            }, ContextCompat.getMainExecutor(context))
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )

                when (filter) {
                    "Warm" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Yellow.copy(
                                        alpha = 0.15f
                                    )
                                )
                        )
                    }

                    "Cool" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Blue.copy(
                                        alpha = 0.15f
                                    )
                                )
                        )
                    }

                    "B&W" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Gray.copy(
                                        alpha = 0.25f
                                    )
                                )
                        )
                    }

                    "Vintage" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color(0xFF8B5A2B).copy(
                                        alpha = 0.25f
                                    )
                                )
                        )
                    }

                    "Dark" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Black.copy(
                                        alpha = 0.4f
                                    )
                                )
                        )
                    }
                }
            }

            Row {
                Button(
                    onClick = { filter = "Original" }
                ) {
                    Text("Original")
                }

                Button(
                    onClick = { filter = "Warm" }
                ) {
                    Text("Warm")
                }

                Button(
                    onClick = { filter = "Cool" }
                ) {
                    Text("Cool")
                }

                Button(
                    onClick = { filter = "B&W" }
                ) {
                    Text("B&W")
                }

                Button(
                    onClick = { filter = "Vintage" }
                ) {
                    Text("Vintage")
                }

                Button(
                    onClick = { filter = "Dark" }
                ) {
                    Text("Dark")
                }
            }
        }
    }
}
