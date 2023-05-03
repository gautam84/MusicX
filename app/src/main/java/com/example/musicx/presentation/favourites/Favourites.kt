package com.example.musicx.presentation.favourites

import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicx.R
import com.example.musicx.data.util.FileUtils
import com.example.musicx.domain.model.Audio

@Composable
fun Favourites(
    playTrack: (audio: Audio) -> Unit,
    navHostController: NavHostController,
    viewModel: FavouritesViewModel = hiltViewModel()
) {


    val list = viewModel.favouriteList.value.favouritesList
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(onClick = {
                navHostController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(style = MaterialTheme.typography.h6, text = "Favourites", color = Color.White)

        }

        if (list.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(list.size) {
                    FavouriteCard(
                        audio = list[it],
                        playTrack = { audio -> playTrack(audio) },
                        removeAudio = { audio ->
                            viewModel.onEvent(FavouritesEvent.RemoveFromFavourite(audio))
                        })

                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Empty", color = Color.White.copy(0.5f))
            }
        }

    }
}

@Composable
fun FavouriteCard(
    audio: Audio,
    playTrack: (audio: Audio) -> Unit,
    removeAudio: (audio: Audio) -> Unit,
) {
    val artworkUri = Uri.parse("content://media/external/audio/albumart")
    val path = ContentUris.withAppendedId(artworkUri, audio.albumId)

    Row(
        modifier = Modifier
            .clickable {
                playTrack(audio)
            }
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
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
                    text = audio.name,
                    color = Color.White,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(200.dp)
                )
                Text(
                    style = MaterialTheme.typography.caption,
                    text = "${audio.artist} • ${
                        FileUtils.name(
                            FileUtils.parent(audio.data)
                        )
                    }",
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                removeAudio(audio)
            }) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favourite",
                    tint = Color.White
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
}