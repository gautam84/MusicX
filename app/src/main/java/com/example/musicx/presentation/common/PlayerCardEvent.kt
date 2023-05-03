package com.example.musicx.presentation.common

import com.example.musicx.domain.model.Audio

sealed class PlayerCardEvent {
    object OnPreviousClicked : PlayerCardEvent()
    object OnNextClicked : PlayerCardEvent()
    object OnTogglePlayClicked : PlayerCardEvent()
    data class PlayTrack(val audio: Audio) : PlayerCardEvent()

}

