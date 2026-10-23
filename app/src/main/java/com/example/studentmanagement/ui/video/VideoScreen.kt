package com.example.studentmanagement.ui.video

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.studentmanagement.R

@Composable
fun VideoScreen() {
    val context = LocalContext.current

    AndroidView(
        factory = {
            VideoView(context).apply {
                val uri = Uri.parse(
                    "android.resource://" +
                        context.packageName +
                        "/" +
                        R.raw.campus
                )

                setVideoURI(uri)

                setMediaController(
                    MediaController(context).also {
                        it.setAnchorView(this)
                    }
                )

                start()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    )
}
