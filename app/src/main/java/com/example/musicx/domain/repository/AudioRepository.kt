package com.example.musicx.domain.repository

import com.example.musicx.domain.model.Audio
import com.example.musicx.domain.model.Folder

interface AudioRepository {
    suspend fun getFolders(): List<Folder>
    suspend fun getAudioFromFolder(path:String):List<Audio>

}