package com.example.musicx.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicx.domain.model.Audio
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {


    @Query("SELECT * FROM music_table")
    fun getFavourites(): Flow<List<Audio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audio: Audio)

    @Delete
    suspend fun delete(audio: Audio)

//    @Query("DELETE FROM news_table WHERE newsTitle = :title")
//    suspend fun deleteByTitle(title: String)


}
