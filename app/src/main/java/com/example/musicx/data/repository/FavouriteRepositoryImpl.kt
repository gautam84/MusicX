package com.example.musicx.data.repository

import com.example.musicx.data.data_source.MusicDao
import com.example.musicx.domain.model.Audio
import com.example.musicx.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow

class FavouriteRepositoryImpl(private val musicDao: MusicDao) : FavouriteRepository {
    override suspend fun getFavourites(): Flow<List<Audio>> {
        return musicDao.getFavourites()
    }

    override suspend fun addToFavourite(audio: Audio) {
        musicDao.insert(audio)
    }

    override suspend fun removeFromFavourite(audio: Audio) {
        musicDao.delete(audio)
    }
}