package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.CameraAlt
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.screens.TopBarWelcome
import com.laiamenmar.bunkervalencia.ui.theme.md_theme_light_primary
import com.laiamenmar.bunkervalencia.utils.CloudStorageManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager


@OptIn(ExperimentalPermissionsApi::class)
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

                TitleScreen("Boulder", Modifier.align(Alignment.CenterHorizontally))

                Column(
                    modifier = Modifier
                        .weight(1f) // Ocupa la mitad del espacio disponible
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (false) { //hay foto en firebase
                            TitleScreen("EXISTE", Modifier)

                        } else {
                            IconButton(
                                onClick = {
                                    navigation.navigate(AppScreens.CameraScreen.route)
                                },
                                modifier = Modifier.align(Alignment.Center),
                                content = {
                                    Icon(
                                        imageVector = Icons.Sharp.CameraAlt,
                                        contentDescription = "Take picture",
                                        tint = md_theme_light_primary,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(1.dp)
                                            .border(1.dp, Color.White, CircleShape)
                                    )
                                }
                            )
                        }
                    }

                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    TitleScreen("Parte2", Modifier.align(Alignment.CenterHorizontally))


                    if (gallery.isEmpty()){
                        Text(text = " No hay fotos")

                    } else { Text(text = "Si hay")}
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
