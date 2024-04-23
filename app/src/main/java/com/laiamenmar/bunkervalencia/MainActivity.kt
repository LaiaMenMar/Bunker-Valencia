package com.laiamenmar.bunkervalencia

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppNavigation
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme
import android.Manifest
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.laiamenmar.bunkervalencia.utils.CameraView
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)

    private var photoUri: Uri? = null
    private var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("camara", "Permiso concedido")
            shouldShowCamera.value = true
        } else {
            Log.i("camara", "Permiso d")

        }
    }


    private val loginViewModel: LoginViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics

        setContent {
            BunkerValenciaTheme {
           //     AppNavigation(this, rememberNavController(), loginViewModel, homeViewModel)
                if(shouldShowCamera.value) {
                    CameraView(
                        outputDirectory = outputDirectory,
                        executor = cameraExecutor,
                        onImageCaptured = ::handleIamgeCapture,
                        onError = {Log.e("camera", "View error", it)}
                    )
                    
                }

                if (shouldShowPhoto.value && photoUri != null) {
                    Image(painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }

        }
        requestCameraPermission()


        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("camara", "Permiso previamente concedido")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ->
                Log.i("camara", "Mostrar dialogo de permisos de la camara")

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        }
    }

    private fun handleIamgeCapture(uri: Uri){
        Log.i("camara", "Image captured: $uri")
        shouldShowCamera.value = false

        photoUri = uri
        shouldShowPhoto.value = true
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
