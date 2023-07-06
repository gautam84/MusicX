/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

package com.example.musicx.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.musicx.data.util.FileUtils
import com.example.musicx.domain.model.Audio
import com.example.musicx.domain.model.Folder
import com.example.musicx.domain.repository.AudioRepository

class AudioRepositoryImpl(
    val context: Context
) : AudioRepository {


    private val AUDIO_PROJECTION
        get() = arrayOf(
            MediaStore.Audio.Media._ID, //0
            MediaStore.Audio.Media.TITLE, // 1
            MediaStore.Audio.Media.ARTIST, // 2
            MediaStore.Audio.Media.ALBUM, // 3
            MediaStore.Audio.Media.ALBUM_ID, // 4
            MediaStore.Audio.Media.DATE_ADDED,  //5
            MediaStore.Audio.Media.COMPOSER, // , // 6
            MediaStore.Audio.Media.YEAR, // 7
            MediaStore.Audio.Media.DATA, // 8
            MediaStore.Audio.Media.DURATION, // 9
            MediaStore.Audio.Media.MIME_TYPE, // 10
            MediaStore.Audio.Media.TRACK, // 11
            MediaStore.Audio.Media.SIZE, //12
            MediaStore.Audio.Media.DATE_MODIFIED, // 14

        )

    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFolders(): List<Folder> {
        val list = mutableListOf<Folder>()
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Media.DATA),
            null,
            null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(
                    Folder(
                        name = FileUtils.name(FileUtils.parent(cursor.getString(0))),
                        path = FileUtils.parent(cursor.getString(0))
                    )
                )
            }
        }

        return list.distinct()
    }

    @SuppressLint("Recycle")
    override suspend fun getAudioFromFolder(path: String): List<Audio> {

        val list = mutableListOf<Audio>()

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " +
                MediaStore.Audio.Media.DATA + " LIKE '" + path + "/%'"

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            AUDIO_PROJECTION, selection, null, null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(
                    Audio(
                        id = cursor.getLong(0),
                        name = cursor.getString(1) ?: MediaStore.UNKNOWN_STRING,
                        albumId = cursor.getLong(4),
                        data = cursor.getString(8) ?: "",
                        album = cursor.getString(3) ?: MediaStore.UNKNOWN_STRING,
                        artist = cursor.getString(2) ?: MediaStore.UNKNOWN_STRING,
                        composer = cursor.getString(6) ?: MediaStore.UNKNOWN_STRING,
                        mimeType = cursor.getString(10) ?: "",
                        track = cursor.getInt(11),
                        dateAdded = cursor.getLong(5) * 1000,
                        dateModified = cursor.getLong(13) * 1000,
                        duration = cursor.getInt(9),
                        size = cursor.getLong(12),
                        year = cursor.getInt(7)
                    )
                )
            }


        }

        return list
    }
}