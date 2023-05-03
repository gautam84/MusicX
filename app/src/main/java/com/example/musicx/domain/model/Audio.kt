package com.example.musicx.domain.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_table")

data class Audio(
    @PrimaryKey
    val id: Long,
    val name: String,
    val albumId: Long,
    val data: String,
    val album: String,
    val artist: String,
    val composer: String,
    val mimeType: String,
    val track: Int,
    val dateAdded: Long,
    val dateModified: Long,
    val duration: Int,
    val size: Long,
    val year: Int,
)
