package com.jsborbon.reparalo.components.video

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class FullScreenVideoActivity : ComponentActivity() {

    private var playerView: PlayerView? = null
    private var exoPlayer: ExoPlayer? = null
    private var videoUrl: String? = null
    private var playbackPosition = 0L
    private var isPlayerReady = true

    companion object {
        const val EXTRA_VIDEO_URL = "video_url"
        const val EXTRA_PLAYBACK_POSITION = "playback_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.insetsController?.apply {
            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL)
        playbackPosition = intent.getLongExtra(EXTRA_PLAYBACK_POSITION, 0L)

        if (videoUrl == null) {
            finish()
            return
        }

        setupVideoPlayer()
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun setupVideoPlayer() {
        try {
            exoPlayer = ExoPlayer.Builder(this)
                .setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000)
                .build().apply {
                    videoUrl?.let { url ->
                        setMediaItem(MediaItem.fromUri(url.toUri()))
                        prepare()
                        seekTo(playbackPosition)
                        playWhenReady = true
                    }

                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            isPlayerReady = playbackState != Player.STATE_BUFFERING
                        }

                        override fun onPlayerError(error: PlaybackException) {
                            handlePlayerError(error)
                        }
                    })
                }

            playerView = PlayerView(this).apply {
                player = exoPlayer
                useController = true
                controllerAutoShow = true
                controllerHideOnTouch = true
                controllerShowTimeoutMs = 3000

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                setShowRewindButton(true)
                setShowFastForwardButton(true)
                setShowPreviousButton(false)
                setShowNextButton(false)

                contentDescription = "Reproductor de video en pantalla completa"
            }

            setContentView(playerView)

        } catch (e: Exception) {
            handlePlayerError(e)
        }
    }

    private fun handlePlayerError(error: Throwable) {
        Log.e("FullScreenVideo", "Player error: ${error.message}", error)
        finish()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun pausePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            isPlayerReady = player.playWhenReady
            player.playWhenReady = false
        }
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            isPlayerReady = player.playWhenReady
            player.release()
        }
        exoPlayer = null
        playerView = null
    }

    override fun onResume() {
        super.onResume()
        if (exoPlayer == null && videoUrl != null) {
            setupVideoPlayer()
        } else {
            exoPlayer?.playWhenReady = isPlayerReady
        }
    }

    override fun finish() {
        exoPlayer?.let { player ->
            intent.putExtra(EXTRA_PLAYBACK_POSITION, player.currentPosition)
            setResult(RESULT_OK, intent)
        }
        super.finish()
    }
}
