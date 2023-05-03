package com.example.musicx.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.musicx.presentation.common.MainViewModel
import com.example.musicx.presentation.common.PlayerCardEvent
import com.example.musicx.presentation.util.SetupNavigation
import com.example.musicx.ui.theme.MusicXTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicXTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    SetupNavigation(
                        mainViewModel.playerCardState.value.currentPlayingAudio,
                        mainViewModel.playerCardState.value.isPlaying,
                        {
                            mainViewModel.onEvent(PlayerCardEvent.OnNextClicked)
                        },
                        {
                            mainViewModel.onEvent(PlayerCardEvent.OnPreviousClicked)
                        },
                        {
                            mainViewModel.onEvent(PlayerCardEvent.OnTogglePlayClicked)

                        },
                        {
                            mainViewModel.onEvent(PlayerCardEvent.PlayTrack(it))
                        },


                    )
                }
            }
        }
    }
}

