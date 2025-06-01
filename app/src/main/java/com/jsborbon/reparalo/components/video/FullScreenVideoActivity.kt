package com.jsborbon.reparalo.components.video

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class FullScreenVideoActivity : ComponentActivity() {

    private var playerView: PlayerView? = null
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.apply {
            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val videoUrl = intent.getStringExtra("video_url") ?: return

        exoPlayer = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl.toUri()))
            prepare()
            playWhenReady = true
        }

        playerView = PlayerView(this).apply {
            player = exoPlayer
            useController = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }

        setContentView(playerView)
    }

    override fun onStop() {
        super.onStop()
        exoPlayer?.release()
        playerView = null
        exoPlayer = null
    }
}
