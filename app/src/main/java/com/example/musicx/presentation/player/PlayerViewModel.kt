package com.example.musicx.presentation.player

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicx.domain.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val favouriteRepository: FavouriteRepository,
) : ViewModel() {


    private val _favouriteList = mutableStateOf(PlayerState())
    val favouriteList: State<PlayerState> = _favouriteList

    init {
        viewModelScope.launch {
            favouriteRepository.getFavourites().collect {
                _favouriteList.value = favouriteList.value.copy(
                    favouriteList = it
                )
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.ToggleLikedState -> {
                viewModelScope.launch {
                    if (_favouriteList.value.favouriteList.contains(event.audio)) {
                        viewModelScope.launch {
                            favouriteRepository.removeFromFavourite(event.audio)
                        }
                    } else {
                        viewModelScope.launch {
                            favouriteRepository.addToFavourite(event.audio)
                        }
                    }
                }
            }
        }
    }
}