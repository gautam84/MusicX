package com.example.musicx.presentation.player

import com.example.musicx.domain.model.Audio

data class PlayerState(
    val favouriteList: List<Audio> = listOf()
)
