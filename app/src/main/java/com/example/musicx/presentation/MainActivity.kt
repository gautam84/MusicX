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

package com.example.musicx.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val permission = mutableStateOf(false)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            when {
                it.getOrDefault(Manifest.permission.READ_MEDIA_AUDIO, false) -> {
                    permission.value = true
                }
            }

        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.POST_NOTIFICATIONS
            )
        )




        super.onCreate(savedInstanceState)
        setContent {
            MusicXTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {

                    if (permission.value) {
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
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Please grant all permissions.",
                                color = Color.Black.copy(0.5f)
                            )
                        }
                    }

                }
            }
        }
    }
}

