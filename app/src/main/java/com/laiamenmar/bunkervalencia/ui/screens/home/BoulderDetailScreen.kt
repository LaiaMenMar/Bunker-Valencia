package com.laiamenmar.bunkervalencia.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.CameraAlt
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.screens.TopBarWelcome
import com.laiamenmar.bunkervalencia.ui.theme.md_theme_light_primary
import com.laiamenmar.bunkervalencia.utils.CloudStorageManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun BoulderDetailScreen(
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel,
    navigation: NavController,
    storage: CloudStorageManager,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val file = context.createImageFile()

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "io.laiamenmar.bunkervalencia" + ".provider", file
    )

    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            Toast.makeText(context, "Foto realizada", Toast.LENGTH_SHORT).show()
            capturedImageUri = uri
            capturedImageUri?.let { uri ->
                scope.launch {
                    val fileSizeInBytes = file.length()
                    val fileSizeInKb = fileSizeInBytes / 1024
                    Log.d(
                        "laia",
                        "Tamaño del archivo: $fileSizeInKb KB y Ruta de la imagen: ${file.absolutePath}"
                    )
                    storage.uploadFile(file.name, uri)
                }
            }
        } else {
            Toast.makeText(context, "No se pudo hacer la foto $it", Toast.LENGTH_SHORT).show()
        }

    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permiso autorizado", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    var arePermissionsGranted by remember { mutableStateOf(false) }



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


                        } else {
                            IconButton(
                                onClick = {
                                    val permissionCheckResult =
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CAMERA
                                        )
                                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                        cameraLauncher.launch(uri)

                                    } else {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }

                                    /* if (permissionState.status.isGranted) {
                                  navigation.navigate(AppScreens.CameraScreen.route)
                              } else {
                              //Text(text = "Permiso Denegado")
                          }
                              Log.i("CameraX", "ON CLICK")*/
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
                        // .weight(1f)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    TitleScreen("Parte2", Modifier.align(Alignment.CenterHorizontally))
                    var gallery by remember { mutableStateOf<List<String>>(listOf()) }
                    LaunchedEffect(Unit) {
                        gallery = storage.getUserImages()
                    }
                    LazyVerticalGrid(
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

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

