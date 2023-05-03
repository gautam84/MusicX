package com.example.musicx.presentation.common

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicx.domain.model.Audio
import com.example.musicx.domain.repository.AudioRepository
import com.example.musicx.media.service.MusicService.Companion.currentDuration
import com.example.musicx.media.util.Constants
import com.example.musicx.media.util.MediaServiceConnection
import com.example.musicx.media.util.isPlaying
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val serviceConnection: MediaServiceConnection
) : ViewModel() {


    var audioList = mutableStateListOf<Audio>()


    private lateinit var rootMediaId: String
    private var updatePosition = true

    private val subscriptionCallback = object
        : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            super.onChildrenLoaded(parentId, children)
        }
    }

    private val isConnected = serviceConnection.isConnected


    private val playbackState = serviceConnection.plaBackState


    private val _playerCardState = mutableStateOf(
        PlayerCardState(
            isPlaying = false,
            currentPlayingAudio = null
        )
    )
    val playerCardState: State<PlayerCardState> = _playerCardState

//    private val serviceConnection = serviceConnection.apply {
//        updatePlayBack()
//    }

    init {

        viewModelScope.launch {
            updatePlayBack()

            isConnected.collect {
                if (it) {
                    rootMediaId = serviceConnection.rootMediaId
                    serviceConnection.plaBackState.collect { playbackState ->
                        if (playbackState != null) {
                            _playerCardState.value = playerCardState.value.copy(
                                isPlaying = playbackState.isPlaying,
                                currDuration = playbackState.position
                            )
                        }

                    }

                    serviceConnection.subscribe(rootMediaId, subscriptionCallback)

                }

            }

        }


//        viewModelScope.launch {
//            serviceConnection.plaBackState.collect { playbackState ->
//                if (playbackState != null) {
//                    _playerCardState.value = playerCardState.value.copy(
//                        isPlaying = playbackState.isPlaying,
//                        currDuration = playbackState.position
//                    )
//                }
//            }
//
//            audioList += audioRepository.getAudioFromFolder("")
//
//
//
//
//
//        }


        viewModelScope.launch {
            audioList += audioRepository.getAudioFromFolder("")

            Log.d("tag", audioList.size.toString())

            serviceConnection.currentPlayingAudio.collect {
                if (it != null) {
                    _playerCardState.value = playerCardState.value.copy(
                        currentPlayingAudio = audioList.find { audio ->
                            audio.id.toString() == it.description.mediaId
                        }
                    )

                }
            }
        }
    }


    private fun updatePlayBack() {
        viewModelScope.launch {

            playbackState.collect { playbackState ->
                if (playbackState != null) {
                    _playerCardState.value = playerCardState.value.copy(
//                        isPlaying = playbackState.isPlaying,
                        currDuration = playbackState.position
                    )
                }

            }





            delay(Constants.PLAYBACK_UPDATE_INTERVAL)
            if (updatePosition) {
                updatePlayBack()
            }


        }

    }


    fun onEvent(event: PlayerCardEvent) {
        when (event) {
            is PlayerCardEvent.OnNextClicked -> {
                serviceConnection.skipToNext()

            }

            is PlayerCardEvent.OnPreviousClicked -> {
                serviceConnection.skipToPrevious()
            }

            is PlayerCardEvent.OnTogglePlayClicked -> {
                viewModelScope.launch {

                    if (_playerCardState.value.isPlaying) {
                        serviceConnection.transportControl.pause()


                    } else {
                        serviceConnection.transportControl.play()
                    }

                }


            }

            is PlayerCardEvent.PlayTrack -> {
                serviceConnection.playAudio(audioList)
                viewModelScope.launch {
                    serviceConnection.transportControl
                        .playFromMediaId(
                            event.audio.id.toString(),
                            null
                        )
                }
            }


        }
    }
}
