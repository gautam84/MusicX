package com.example.musicx.presentation.home

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicx.domain.model.Audio
import com.example.musicx.domain.repository.AudioRepository
import com.example.musicx.media.util.MediaServiceConnection
import com.example.musicx.media.util.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
) : ViewModel() {





    private val _tabState = mutableStateOf(TabsState())
    val tabState: State<TabsState> = _tabState

    init {
        viewModelScope.launch {

            _tabState.value = tabState.value.copy(
                list = audioRepository.getFolders().toMutableList()
            )
        }
    }




     fun getAudiosFromFolderPath(path: String): List<Audio> {
         var list = listOf<Audio>()
        viewModelScope.launch {
            list = audioRepository.getAudioFromFolder(path)

        }
         return  list

    }


}