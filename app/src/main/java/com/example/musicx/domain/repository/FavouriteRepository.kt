package com.example.musicx.domain.repository

import com.example.musicx.domain.model.Audio
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    suspend fun getFavourites(): Flow<List<Audio>>
    suspend fun addToFavourite(audio: Audio)
    suspend fun removeFromFavourite(audio: Audio)
}