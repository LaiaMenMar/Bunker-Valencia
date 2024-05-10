package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation

import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.screens.TopBarWelcome

import com.laiamenmar.bunkervalencia.utils.CloudStorageManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun BoulderDetailScreen(
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel,
    navigation: NavController,
    storage: CloudStorageManager,

) {
    val selectedBoulder by homeViewModel.selectedBoulder.observeAsState()


    val keySelect = selectedBoulder?.key

    val gallery by homeViewModel.gallery.collectAsState(initial = emptyList())
    val date = selectedBoulder?.let { Date(it.id) }
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val formattedDate = sdf.format(date)

    LaunchedEffect(keySelect) {
        keySelect?.let { key ->
            val imagesFlow = storage.getBoulderImage(key)
            homeViewModel.updateGallery(imagesFlow)
        }
    }


    Scaffold(
        topBar = {
            TopBarWelcome(homeViewModel = homeViewModel, navigation = navigation)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (gallery.isEmpty()) {
                    Box(modifier = Modifier.weight(1f)) {
                        IconButton(
                            onClick = {
                                navigation.navigate(AppScreens.CameraScreen.route)
                            },
                            modifier = Modifier.align(Alignment.Center),
                            content = {
                                Icon(
                                    imageVector = Icons.Sharp.CameraAlt,
                                    contentDescription = "Take picture",
                                    //tint = md_theme_light_primary,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(1.dp)
                                        .border(1.dp, Color.White, CircleShape)
                                )
                            }
                        )
                    }
                } else {
                    val firstImageUrl = gallery.firstOrNull()
                    if (firstImageUrl != null) {
                        CoilImage(
                            imageUrl = firstImageUrl,
                            contentDescription = null,
                            modifier = Modifier.
                                    weight(1f),
                            contentScale = ContentScale.Crop
                        )
                    }
                    /*LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(gallery.size) { index ->
                            val imageUrl = gallery[index]
                            CoilImage(
                                imageUrl = imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop

                            )

                        }
                    }*/
                }

                Divider(modifier = Modifier.padding(vertical = 2.dp))
                selectedBoulder?.let { boulder ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                //    TitleScreen("Búlder", Modifier.align(Alignment.CenterHorizontally))
                    Text(
                        text = boulder.note,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = boulder.wall_id,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formattedDate,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Black
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            val buttonColors = if (boulder.color == "difficulty_6") {
                                ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = getColorlikeColor(boulder.color)
                                )
                            } else {
                                ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = getColorlikeColor(boulder.color)
                                )
                            }
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(56.dp)
                                    .padding(horizontal = 16.dp),
                                colors = buttonColors
                            ) {
                                Text(
                                    text = boulder.grade,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Likes",
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = boulder.likes.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.ThumbUp,
                                contentDescription = "Ascensos",
                                tint = Color.Green,
                                modifier = Modifier.size(24.dp).padding(start = 16.dp)
                            )
                            Text(
                                text = boulder.ascents.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }

            }
        }
    )
}



@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {

    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalContext.current)
            .data(data = imageUrl)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                transformations(
                    RoundedCornersTransformation(
                        topLeft = 20f,
                        topRight = 20f,
                        bottomLeft = 20f,
                        bottomRight = 20f
                    )
                )
            })
            .build()
    )
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.padding(6.dp),
        contentScale = contentScale
    )

}
