package com.example.musicx.presentation.home

sealed class HomeScreenEvent {
    data class ChanePagerState(val path: String) : HomeScreenEvent()

}
