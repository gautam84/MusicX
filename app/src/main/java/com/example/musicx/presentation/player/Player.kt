package com.example.musicx.presentation.player

import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicx.R
import com.example.musicx.data.util.FileUtils
import com.example.musicx.domain.model.Audio
import com.example.musicx.presentation.player.util.timeStampToDuration

@Composable
fun Player(
    navHostController: NavHostController,
    viewModel: PlayerViewModel = hiltViewModel(),
    currAudio: Audio?,
    isPlaying: Boolean,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onTogglePlayClicked: () -> Unit,


) {
    val favouriteList = viewModel.favouriteList.value

    val artworkUri = Uri.parse("content://media/external/audio/albumart")
    val path = ContentUris.withAppendedId(artworkUri, currAudio!!.albumId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xff474E68),
                        Color(0x99474E68),
                    )
                )
            ), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,

                ) {
                IconButton(onClick = {
                    navHostController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

            }


            Row {
                IconButton(onClick = {
                    viewModel.onEvent(PlayerEvent.ToggleLikedState(currAudio))
                }) {
                    Icon(
                        imageVector = if (favouriteList.favouriteList.contains(currAudio)) {
                            Icons.Outlined.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        }, contentDescription = "Favourite", tint = Color.White
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White
                    )
                }
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(
                        path
                    ).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    placeholder = painterResource(id = R.drawable.no_album_art),
                    fallback = painterResource(id = R.drawable.no_album_art),
                    error = painterResource(id = R.drawable.no_album_art),

                    )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    style = MaterialTheme.typography.h6,

                    text = currAudio.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    style = MaterialTheme.typography.caption,
                    text = "${currAudio.artist} • ${FileUtils.name(FileUtils.parent(currAudio.data))}",
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = Color.White
                    )
                }
                Row {
                    IconButton(onClick = { onPreviousClicked() }) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Skip Previous",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onTogglePlayClicked() }) {
                        Icon(
                            imageVector = if (isPlaying) {
                                Icons.Default.Pause
                            } else {
                                Icons.Filled.PlayArrow
                            }, contentDescription = "Pause", tint = Color.White
                        )
                    }
                    IconButton(onClick = { onNextClicked() }) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Skip Next",
                            tint = Color.White
                        )
                    }

                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }

            }


        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DESIGNED AND CODED BY GAUTAM HAZARIKA",
                color = Color.White,
                style = MaterialTheme.typography.caption
            )
        }


    }
}

