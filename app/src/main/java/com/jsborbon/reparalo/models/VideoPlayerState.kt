package com.jsborbon.reparalo.models


sealed class VideoPlayerState {
    object LOADING : VideoPlayerState()
    object BUFFERING : VideoPlayerState()
    object READY : VideoPlayerState()
    object ENDED : VideoPlayerState()
    object IDLE : VideoPlayerState()
    data class ERROR(val message: String) : VideoPlayerState()
}
