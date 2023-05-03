package com.example.musicx.presentation.home

import com.example.musicx.domain.model.Audio
import com.example.musicx.domain.model.Folder

data class TabsState(
    val list: MutableList<Folder> = mutableListOf(),
)
