/**
 * BoulderDetailScreen.kt: Este archivo contiene la implementación de la pantalla de detalle de
 * un bloque de escalada, que muestra su información detallada, incluyendo su imagen, notas,
 * fecha de creación, grado de dificultad, etc.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define la estructura y la lógica de la pantalla de detalle de un bloque de escalada.
 * Fecha de creación: 2024/04/22
 */
package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.RocketLaunch
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.theme.onPrimaryContainerLight
import com.laiamenmar.bunkervalencia.ui.theme.surfaceVariantLight
import com.laiamenmar.bunkervalencia.ui.theme.tertiaryLight
import com.laiamenmar.bunkervalencia.utils.CloudStorageManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Función componible para mostrar la pantalla de detalle de un problema de boulder.
 *
 * @param realtime Instancia de RealtimeManager para realizar operaciones de datos en tiempo real.
 * @param homeViewModel ViewModel de la pantalla de inicio para acceder a los datos del usuario
 * actual y del problema de boulder seleccionado.
 * @param navigation NavController para la navegación dentro de la aplicación.
 * @param storage Instancia de CloudStorageManager para cargar y eliminar imágenes asociadas
 * a un problema de boulder.
 */
@Composable
fun BoulderDetailScreen(
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel,
    navigation: NavController,
    storage: CloudStorageManager,

    ) {
    val currentUser: UserModel? by homeViewModel.currentUser.observeAsState()
    var showimagen by remember { mutableStateOf(false) }
    val selectedBoulder by homeViewModel.selectedBoulder.observeAsState()
    var dialogDeleteBoulder by remember { mutableStateOf(false) }
    var boulderImage by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val keySelect = selectedBoulder?.key

    val gallery by homeViewModel.gallery.collectAsState(initial = emptyList())
    val date = selectedBoulder?.let { Date(it.id) }
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val formattedDate = sdf.format(date)

    val firstImageUrl = gallery.firstOrNull()


    LaunchedEffect(keySelect) {
        keySelect?.let { key ->
            val imagesFlow = storage.getBoulderImage(key)
            homeViewModel.updateGallery(imagesFlow)
        }
    }

    DeleteBoulderDialog(
        dialogDeleteBoulder = dialogDeleteBoulder,
        onDismiss = { dialogDeleteBoulder = false },
        onBoulderConfirmDelete = {
            scope.launch { homeViewModel.onBoulder_Delete(realtime, keySelect) }
            dialogDeleteBoulder = false
            navigation.popBackStack()
        })

    Scaffold(
        topBar = {
            TopBarWelcome(homeViewModel = homeViewModel, navigation = navigation)
        },
        content = { paddingValues ->
           if (showimagen){
               if (firstImageUrl != null) {
                   ImagenDialog (
                       { showimagen = false },firstImageUrl
                   )
               }
           }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (gallery.isEmpty() || boulderImage) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(4.dp)
                            .border(
                                BorderStroke(width = 2.dp, color = Color.Gray),
                                shape = RoundedCornerShape(5)
                            ),

                        contentAlignment = Alignment.Center

                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    if(currentUser != null && currentUser?.router_setter==true) {
                                        navigation.navigate(AppScreens.CameraScreen.route)
                                    }},
                                modifier = Modifier,
                                content = {
                                    Icon(
                                        imageVector = Icons.Sharp.CameraAlt,
                                        contentDescription = "Take picture",
                                        tint = onPrimaryContainerLight,
                                        modifier = Modifier
                                            .size(600.dp)
                                            .padding(1.dp)
                                        //   .border(1.dp, Color.White, CircleShape)
                                    )
                                }
                            )
                            Text(
                                text = "Imagen no encontrada",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Gray
                            )
                        }
                    }
                } else {

                    if (firstImageUrl != null) {
                        Box(modifier = Modifier.clickable { showimagen=true }
                            .weight(1f)){
                            CoilImage(
                                imageUrl = firstImageUrl,
                                contentDescription = null,
                                modifier = Modifier,
                                contentScale = FillBounds
                            )
                        }



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
                            .background(surfaceVariantLight)
                    ) {
                        Text(
                            text = boulder.note,
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
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
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Equipado por " + boulder.name_routeSeter,
                                    //fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
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

                                Row(Modifier.padding(top = 8.dp, start = 32.dp)) {
                                    SocialIcon(
                                        modifier = Modifier.weight(1f),
                                        unselecetedIcon = {
                                            Icon(
                                                modifier = Modifier.size(28.dp),
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = "likes",
                                                tint = Color.Gray
                                            )
                                        },
                                        selectedIcon = {
                                            Icon(
                                                modifier = Modifier.size(28.dp),
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = "likes",
                                                tint = Color.Red
                                            )
                                        },
                                        isSelected = true
                                    ) {
                                        // chat = !chat
                                    }

                                    SocialIcon(
                                        modifier = Modifier.weight(1f),
                                        unselecetedIcon = {
                                            Icon(
                                                modifier = Modifier.size(28.dp),
                                                imageVector = Icons.Outlined.RocketLaunch,
                                                contentDescription = "done",
                                                tint = Color.Gray
                                            )
                                        },
                                        selectedIcon = {
                                            Icon(
                                                modifier = Modifier.size(28.dp),
                                                imageVector = Icons.Outlined.RocketLaunch,
                                                contentDescription = "done",
                                                tint = tertiaryLight
                                            )
                                        },
                                        isSelected = true
                                    ) {
                                        // rt = !rt
                                    }
                                }
                            }
                        }
                        if (currentUser != null && currentUser?.router_setter==true) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)) {
                                Icon(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .size(24.dp)
                                        .clickable {dialogDeleteBoulder = true},
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete",
                                    tint = Color.Gray
                                )
                                if (gallery.isNotEmpty()) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .size(24.dp)
                                            .clickable {
                                                scope.launch {
                                                    boulder.key?.let {
                                                        storage.deleteBoulderImage(it)
                                                        boulderImage= true
                                                    }
                                                }
                                            },
                                        imageVector = Icons.Outlined.Cameraswitch,
                                        contentDescription = "delete photo",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

/**
 * Función componible para mostrar una imagen utilizando Coil con esquinas redondeadas.
 *
 * @param imageUrl URL de la imagen que se mostrará.
 * @param contentDescription Descripción del contenido para accesibilidad.
 * @param modifier Modificador para estilizar y personalizar el diseño.
 * @param contentScale Tipo de escala para el contenido de la imagen.
 */
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

/**
 * Función componible para mostrar un diálogo que contiene una imagen.
 *
 * @param onDismissRequest Función de devolución de llamada invocada cuando se cierra el diálogo.
 * @param imageUrl URL de la imagen que se mostrará.
 */
@Composable
fun ImagenDialog(onDismissRequest: () -> Unit,  imageUrl: String) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(modifier=Modifier.fillMaxSize()){
            CoilImage(
                imageUrl = imageUrl,
                contentDescription = null,
                modifier = Modifier,
                contentScale = Crop
            )

        }

    }
}