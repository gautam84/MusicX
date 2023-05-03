package com.example.musicx.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicx.domain.model.Audio

@Database(
    entities = [Audio::class],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract val musicDao: MusicDao
    companion object{
        const val DATABASE_NAME = "music_db"
    }
}