package com.example.musicx.presentation.player

import com.example.musicx.domain.model.Audio

sealed class PlayerEvent {
    data class ToggleLikedState(val audio: Audio) : PlayerEvent()
}
