package com.example.musicx.presentation.favourites

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicx.domain.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: FavouriteRepository,
) : ViewModel() {
    private val _favouriteList = mutableStateOf(FavouritesState())
    val favouriteList: State<FavouritesState> = _favouriteList

    init {
        viewModelScope.launch {
            repository.getFavourites().collect {
                _favouriteList.value = favouriteList.value.copy(
                    favouritesList = it
                )
            }
        }
    }

    fun onEvent(event: FavouritesEvent) {
        when (event) {
            is FavouritesEvent.RemoveFromFavourite -> {
                viewModelScope.launch {
                    repository.removeFromFavourite(event.audio)
                }
            }
        }
    }
}