package com.jsborbon.reparalo.components.video

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.components.video.components.ControlsOverlay
import com.jsborbon.reparalo.models.VideoPlayerState
import com.jsborbon.reparalo.ui.theme.RepairYellow
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false,
    showFullscreenControl: Boolean = true
) {
    val context = LocalContext.current

    var playerState by remember { mutableStateOf<VideoPlayerState>(VideoPlayerState.LOADING) }
    var playbackPosition by remember { mutableLongStateOf(0L) }
    var bufferingProgress by remember { mutableIntStateOf(0) }
    var showControls by remember { mutableStateOf(true) }

    val fullScreenLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getLongExtra(FullScreenVideoActivity.EXTRA_PLAYBACK_POSITION, 0L)?.let {
                playbackPosition = it
            }
        }
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()
    }

    DisposableEffect(videoUrl, exoPlayer) {
        try {
            playerState = VideoPlayerState.LOADING

            exoPlayer.setMediaItem(MediaItem.fromUri(videoUrl.toUri()))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = autoPlay

            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    playerState = when (state) {
                        Player.STATE_BUFFERING -> {
                            bufferingProgress = exoPlayer.bufferedPercentage
                            VideoPlayerState.BUFFERING
                        }
                        Player.STATE_READY -> VideoPlayerState.READY
                        Player.STATE_ENDED -> VideoPlayerState.ENDED
                        else -> VideoPlayerState.IDLE
                    }
                }

                override fun onIsPlayingChanged(playing: Boolean) {
                    showControls = !playing
                }

                override fun onPlayerError(error: PlaybackException) {
                    playerState = VideoPlayerState.ERROR(
                        when (error.errorCode) {
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED ->
                               "Fallo en la conexión de red"
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                               "Tiempo de espera agotado en la conexión de red"
                            PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED ->
                                "Formato de video no válido o dañado"
                            else -> "Error desconocido al reproducir el video"
                        }
                    )
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    playbackPosition = newPosition.positionMs
                }
            }

            exoPlayer.addListener(listener)
        } catch (_: Exception) {
            playerState = VideoPlayerState.ERROR("Error al inicializar el reproductor de video")
        }

        onDispose {
            playbackPosition = exoPlayer.currentPosition
            exoPlayer.release()
        }
    }

    LaunchedEffect(playerState, playbackPosition) {
        if (playerState == VideoPlayerState.READY && playbackPosition > 0) {
            exoPlayer.seekTo(playbackPosition)
            playbackPosition = 0L
        }
    }

    LaunchedEffect(playerState) {
        if (playerState == VideoPlayerState.READY && exoPlayer.isPlaying) {
            delay(3000)
            showControls = false
        }
    }

    val controlsScale by animateFloatAsState(
        targetValue = if (showControls) 1f else 0f,
        animationSpec = tween(300),
        label = "controlsScale"
    )

    Card(
        modifier = modifier
            .height(240.dp)
            .semantics {
                contentDescription = when (playerState) {
                    is VideoPlayerState.ERROR -> "Reproductor con error"
                    VideoPlayerState.LOADING, VideoPlayerState.BUFFERING -> "Reproductor cargando"
                    else -> "Reproductor de video"
                }
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (playerState) {
                is VideoPlayerState.ERROR -> ErrorState(
                    message = (playerState as VideoPlayerState.ERROR).message,
                    onRetry = {
                        exoPlayer.seekTo(0)
                        exoPlayer.prepare()
                        playerState = VideoPlayerState.LOADING
                    }
                )

                VideoPlayerState.LOADING,
                VideoPlayerState.BUFFERING -> LoadingState(
                    message = if (bufferingProgress > 0)
                        "Cargando... $bufferingProgress%"
                    else "Preparando video..."
                )

                else -> {
                    AndroidView(
                        factory = {
                            PlayerView(it).apply {
                                player = exoPlayer
                                useController = false
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                    )

                    ControlsOverlay(
                        exoPlayer = exoPlayer,
                        showControls = showControls,
                        controlsScale = controlsScale,
                        showFullscreenControl = showFullscreenControl,
                        onFullscreen = {
                            playbackPosition = exoPlayer.currentPosition
                            Intent(context, FullScreenVideoActivity::class.java).apply {
                                putExtra(FullScreenVideoActivity.EXTRA_VIDEO_URL, videoUrl)
                                putExtra(FullScreenVideoActivity.EXTRA_PLAYBACK_POSITION, playbackPosition)
                                fullScreenLauncher.launch(this)
                            }
                        }
                    )
                }
            }
        }
    }
}
