package com.example.musicx.presentation.common

import com.example.musicx.domain.model.Audio
import java.time.Duration

data class PlayerCardState(
    val isPlaying: Boolean = false,
    val currentPlayingAudio: Audio? = null,
    val currDuration: Long = 0L
)
