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

package com.example.musicx.media.util

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.example.musicx.domain.model.Audio
import com.example.musicx.media.service.MusicService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MediaServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {

    private val _playBackState: MutableStateFlow<PlaybackStateCompat?> =
        MutableStateFlow(null)
    val plaBackState: Flow<PlaybackStateCompat?>
        get() = _playBackState

    private val _isConnected: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isConnected: StateFlow<Boolean>
        get() = _isConnected


    val rootMediaId: String
        get() = mediaBrowser.root

    val transportControl: MediaControllerCompat.TransportControls
        get() = mediaControllerCompat.transportControls


    private val _currentPlayingAudio: MutableStateFlow<MediaMetadataCompat?> =
        MutableStateFlow(null)
    val currentPlayingAudio: Flow<MediaMetadataCompat?>
        get() = _currentPlayingAudio


//    val currentPlayingAudio = mutableStateOf<Audio?>(null)

    lateinit var mediaControllerCompat: MediaControllerCompat

    private val mediaBrowserServiceCallback =
        MediaBrowserConnectionCallBack(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MusicService::class.java),
        mediaBrowserServiceCallback,
        null

    ).apply {
        connect()
    }
    private var audioList = listOf<Audio>()


    fun playAudio(audios: List<Audio>) {
        audioList = audios
        mediaBrowser.sendCustomAction(Constants.START_MEDIA_PLAY_ACTION, null, null)
    }


    fun skipToPrevious() {
        transportControl.skipToPrevious()
    }

    fun skipToNext() {
        transportControl.skipToNext()
    }

    fun subscribe(
        parentId: String,
        callBack: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.subscribe(parentId, callBack)
    }

    fun unSubscribe(
        parentId: String,
        callBack: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.unsubscribe(parentId, callBack)
    }

    fun refreshMediaBrowserChildren() {
        mediaBrowser.sendCustomAction(
            Constants.REFRESH_MEDIA_PLAY_ACTION,
            null,
            null
        )
    }

    private inner class MediaBrowserConnectionCallBack(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            _isConnected.value = true
            mediaControllerCompat = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
                registerCallback(MediaControllerCallBack())
            }
        }

        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }


    private inner class MediaControllerCallBack : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playBackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            _currentPlayingAudio.value = metadata
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserServiceCallback.onConnectionSuspended()
        }


    }


}