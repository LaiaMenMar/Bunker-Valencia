/**
 * CameraScreen.kt: Este archivo contiene la implementación de la pantalla de la cámara,
 * que permite a los usuarios capturar imágenes para asociarlas a un bloque de escalada.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define la funcionalidad para tomar fotografías con la cámara del dispositivo
 * y subirlas al almacenamiento en la nube para asociarlas con un bloque de escalada.
 * Fecha de creación: 2024/05/09
 */
package com.laiamenmar.bunkervalencia.ui.screens.home

import android.Manifest
import android.net.Uri
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.utils.CloudStorageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.Executor

/**
 * CameraScreen.kt: Este archivo contiene la implementación de la pantalla de la cámara,
 * que permite a los usuarios capturar imágenes para asociarlas a un bloque.
  *
 * @param navigation NavController para la navegación dentro de la aplicación.
 * @param storage Instancia del gestor de almacenamiento en la nube para subir las imágenes capturadas.
 * @param homeViewModel ViewModel de la pantalla de inicio para acceder a los
 * datos del bloque de escalada seleccionado.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable()
fun CameraScreen(navigation: NavController,  storage: CloudStorageManager, homeViewModel: HomeViewModel) {
    val selectedBoulder by homeViewModel.selectedBoulder.observeAsState()
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val lifecycle = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
    Scaffold(floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(onClick = {
            val executor = ContextCompat.getMainExecutor(context)
            takePicture(cameraController, executor, navigation, storage, scope, selectedBoulder, homeViewModel)
            Toast.makeText(context, "Subiendo imagen...", Toast.LENGTH_LONG).show()
        }) {
            Icon(
                imageVector = Icons.Sharp.Lens,
                contentDescription = "Take picture",
                tint = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    .padding(1.dp)
            )
        }
    }) {
        if (permissionState.status.isGranted) {
            CamaraComposable(cameraController, lifecycle, modifier = Modifier.padding(it))
        } else {
            Text(text = "Permiso Denegado!", modifier = Modifier.padding(it))
        }
    }
}

/**
 * Función que captura una imagen utilizando la cámara del dispositivo y
 * la sube al almacenamiento en la nube.
 *
 * @param cameraController Controlador de la cámara del dispositivo.
 * @param executor Executor para realizar operaciones en el hilo principal.
 * @param navigation NavController para la navegación dentro de la aplicación.
 * @param storage Instancia del gestor de almacenamiento en la nube para subir las imágenes capturadas.
 * @param scope Alcance de la coroutine para realizar operaciones asíncronas.
 * @param selectedBoulder Modelo del problema de escalada seleccionado al que se asociará la imagen.
 * @param homeViewModel ViewModel de la pantalla de inicio para actualizar la galería de imágenes
 * después de subir una nueva imagen.
 */
private fun takePicture(
    cameraController: LifecycleCameraController,
    executor: Executor,
    navigation: NavController,
    storage: CloudStorageManager,
    scope: CoroutineScope,
    selectedBoulder: BoulderModel?,
    homeViewModel: HomeViewModel
) {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val file = File.createTempFile(timeStamp + "photo", ".jpg")
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(
        outputDirectory,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println(outputFileResults.savedUri)
                scope.launch{
                  if (selectedBoulder != null) {
                        selectedBoulder.key?.let {
                            storage.uploadFileBoulder(file.name, Uri.fromFile(file),
                                it
                            )
                            val images = storage.getBoulderImage(it)
                            homeViewModel.updateGallery(images)
                            navigation.popBackStack()
                        }
                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                println()
            }
        },
    )
}

/**
 * Funcion componible que muestra la vista previa de la cámara del dispositivo.
 *
 * @param cameraController Controlador de la cámara del dispositivo.
 * @param lifecycle LifecycleOwner para vincular el ciclo de vida del Composable
 * con el ciclo de vida de la actividad o fragmento.
 * @param modifier Modificador para personalizar la apariencia y el diseño del Composable.
 */
@Composable
fun CamaraComposable(
    cameraController: LifecycleCameraController,
    lifecycle: LifecycleOwner,
    modifier: Modifier = Modifier,
) {
    cameraController.bindToLifecycle(lifecycle)
    AndroidView(modifier = modifier, factory = { context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        previewView.controller = cameraController

        previewView
    })
}