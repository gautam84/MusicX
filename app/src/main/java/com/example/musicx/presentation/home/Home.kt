package com.example.musicx.presentation.home

import android.content.ContentUris
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicx.R
import com.example.musicx.data.util.FileUtils
import com.example.musicx.domain.model.Audio
import com.example.musicx.domain.model.Folder
import com.example.musicx.presentation.util.Screen
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home(
    playTrack: (audio: Audio) -> Unit,
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),

    ) {
    val context = LocalContext.current
    Column() {


        val pagerState = rememberPagerState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primary)
                .padding(8.dp, 0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_musicx),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(80.dp, 40.dp)

                    )
                }
                IconButton(onClick = {
                    navHostController.navigate(Screen.Favourites.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.White
                    )
                }
            }

            val list = listOf(Folder("All", "")) + viewModel.tabState.value.list

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Tabs(
                    pagerState = pagerState,
                    list
                )
                TabsContent(
                    pagerState = pagerState,
                    viewModel.tabState.value.list.map { it.name },
                    viewModel.getAudiosFromFolderPath(list[pagerState.currentPage].path)
                ) {
                    playTrack(
                        it
                    )
                }
            }
        }


    }
}


@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState, list: List<Folder>) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colors.primary,
        contentColor = colors.primary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 0.dp,
                color = colors.primary
            )
        },
        edgePadding = 0.dp

    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        text = list[index].name,
                        fontSize = 16.sp,
                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Light,
                        color = Color.White,
                        textAlign = TextAlign.Left
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                modifier = Modifier.fillMaxWidth()


            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(
    pagerState: PagerState,
    list: List<String>,
    audioList: List<Audio>,
    onClick: (audio: Audio) -> Unit
) {

    HorizontalPager(count = list.size + 1, state = pagerState) {
        AudioList(audioList, onClick)
    }

}

@Composable
fun AudioList(
    list: List<Audio>, onClick: (audio: Audio) -> Unit
) {
    LazyColumn() {
        items(list) { audio ->
            AudioCard(audio = audio, onClick)
        }

    }
}

@Composable
fun AudioCard(
    audio: Audio, onClick: (audio: Audio) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(true) {
                onClick(audio)
            }
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        val path = ContentUris.withAppendedId(artworkUri, audio.albumId)

        Row() {
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
                    text = audio.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(220.dp)

                )
                Row {
                    Text(
                        text = "${audio.artist} • ${FileUtils.name(FileUtils.parent(audio.data))}",
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.caption
                    )

                }

            }
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


