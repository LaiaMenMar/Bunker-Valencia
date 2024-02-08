package com.laiamenmar.bunkervalencia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager

//private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
private var welcomeMessage by mutableStateOf("Bienvenidx")
private var isButtonVisible by mutableStateOf(true)

val WELCOME_MESSAGE_KEY = "welcome_message"
val IS_BUTTON_VISIBLE_KEY = "is_button_visible"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen1(analytics: AnalyticsManager, auth: AuthManager, navigation: NavController) {
    analytics.logScreenView(screenName = AppScreens.HomeScreen1.route)
    val navController = rememberNavController()

//    initRemoteConfig()

    val user = auth.getCurrentUser()

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val onLogoutConfirmed: () -> Unit = {
        auth.signOut()
        navigation.navigate(AppScreens.LoginScreen.route) {
            popUpTo(AppScreens.HomeScreen1.route) {
                inclusive = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                placeholder = painterResource(id = R.drawable.profile),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )

                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = if (!user?.displayName.isNullOrEmpty()) "Hola, ${user?.displayName}" else "Bienvenido",
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (!user?.email.isNullOrEmpty()) "${user?.email}" else "Anónimo",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                                  showDialog = true
                        }
                        /* val crashlytics = FirebaseCrashlytics.getInstance()
                         crashlytics.setCustomKey("pruebaClaveHome", "Valor a enviar")
                         crashlytics.log("Mensaje log desde HomeScreen")
                         crashlytics.setUserId(user?.uid ?: "No Id Found")
                         crashlytics.setCustomKeys {
                             key("str", "hello")
                             key("bool", true)
                             key("int", 5)
                             key("long", 5.8)
                             key("float", 1.0f)
                             key("double", 1.0)
                         }
                         throw RuntimeException("Error forzado desde HomeScreen")
                     }*/,
                        modifier = Modifier.alpha(if (isButtonVisible) 1f else 0f)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Forzar Error")
                    }
                    IconButton(
                        onClick = {
                            //showDialog = true
                        }
                    ) {
                        Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },


        bottomBar = {
            BottomBar(navController = navController)
        },




    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialog) {
                LogoutDialog(onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialog = false
                }, onDismiss = { showDialog = false })
            }
           /* BottomNavGraph(navController = navController, context = context, authManager = auth)*/
        }
    }
   FloatingActionButton(
        onClick = {
            // Acción a realizar cuando se hace clic en el botón flotante
        },
        modifier = Modifier
            .padding(16.dp) ) {
        Icon(Icons.Default.Add, contentDescription = "Agregar")
    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    /*  val screens = listOf(
          BottomNavScreen.Contact,
          BottomNavScreen.Note,
          BottomNavScreen.Photos
      )*/
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        /*   screens.forEach { screens ->
               if (currentDestination != null) {
                   AddItem(
                       screens = screens,
                       currentDestination = currentDestination,
                       navController = navController
                   )
               }
           }*/
    }
}

@Composable
fun LogoutDialog(onConfirmLogout: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            Button(
                onClick = onConfirmLogout
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

