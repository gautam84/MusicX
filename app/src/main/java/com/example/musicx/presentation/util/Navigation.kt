package com.example.musicx.presentation.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicx.R
import com.example.musicx.data.util.FileUtils
import com.example.musicx.domain.model.Audio
import com.example.musicx.presentation.favourites.Favourites
import com.example.musicx.presentation.home.Home
import com.example.musicx.presentation.player.Player

@Composable
fun NavigationGraph(
    navController: NavHostController,
    playTrack: (audio: Audio) -> Unit,
    currAudio: Audio?,
    isPlaying: Boolean,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onTogglePlayClicked: () -> Unit,


) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            Home(navHostController = navController, playTrack = playTrack)
        }

        composable(route = Screen.Favourites.route) {
            Favourites(navHostController = navController, playTrack = playTrack)
        }

        composable(route = Screen.Player.route) {
            Player(
                navHostController = navController,
                isPlaying = isPlaying,
                currAudio = currAudio,
                onNextClicked = onNextClicked,
                onPreviousClicked = onPreviousClicked,
                onTogglePlayClicked = onTogglePlayClicked,
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun SetupNavigation(
    currAudio: Audio?,
    isPlaying: Boolean,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onTogglePlayClicked: () -> Unit,
    playTrack: (audio: Audio) -> Unit,


) {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()




    Scaffold(
        bottomBar = {

            if (currAudio?.year != null) {
                if (navBackStackEntry?.destination?.route != Screen.Player.route) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .background(Color(0xFF404258))
                            .clickable {
                                navController.navigate(Screen.Player.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val artworkUri = Uri.parse("content://media/external/audio/albumart")
                            val path = ContentUris.withAppendedId(artworkUri, currAudio.albumId)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current).data(
                                        path
                                    ).build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(
                                            CircleShape
                                        ),
                                    placeholder = painterResource(id = R.drawable.no_album_art),
                                    fallback = painterResource(id = R.drawable.no_album_art),
                                    error = painterResource(id = R.drawable.no_album_art),

                                    )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier.width(150.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        text = currAudio.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row {
                                        Text(
                                            style = MaterialTheme.typography.caption,
                                            text = "${currAudio.artist} • ${
                                                FileUtils.name(FileUtils.parent(currAudio.data))
                                            }",
                                            modifier = Modifier.width(150.dp),

                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }

                                }
                            }

                            Row {
                                IconButton(onClick = {
                                    onPreviousClicked()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.SkipPrevious,
                                        contentDescription = "Previous Song",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = {
                                    onTogglePlayClicked()
                                }) {
                                    Icon(
                                        imageVector = if (isPlaying) {
                                            Icons.Default.Pause
                                        } else {
                                            Icons.Filled.PlayArrow
                                        },
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = {
                                    onNextClicked()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.SkipNext,
                                        contentDescription = "Options",
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    ) {
        NavigationGraph(
            navController = navController,
            playTrack = playTrack,
            currAudio = currAudio,
            isPlaying = isPlaying,
            onNextClicked = onNextClicked,
            onPreviousClicked = onPreviousClicked,
            onTogglePlayClicked = onTogglePlayClicked,

        )
    }


}





