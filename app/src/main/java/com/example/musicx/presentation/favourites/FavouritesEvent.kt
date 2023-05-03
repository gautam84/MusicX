package com.example.musicx.presentation.favourites

import com.example.musicx.domain.model.Audio

sealed class FavouritesEvent{
    data class RemoveFromFavourite(val audio: Audio) : FavouritesEvent()
}
