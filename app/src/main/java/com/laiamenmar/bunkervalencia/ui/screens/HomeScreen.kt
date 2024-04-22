package com.laiamenmar.bunkervalencia.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.GoogleAuthProvider
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.screens.home.BouldersScreen
import com.laiamenmar.bunkervalencia.ui.screens.home.RoutesScreen
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import com.laiamenmar.bunkervalencia.R

//private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
private var welcomeMessage by mutableStateOf("Hola! ")

//private var isButtonVisible by mutableStateOf(true)

@Composable
fun HomeScreen(
    analytics: AnalyticsManager,
    auth: AuthManager,
    navigation: NavController,
    homeViewModel: HomeViewModel,
    realtime: RealtimeManager
) {
    analytics.logScreenView(screenName = AppScreens.HomeScreen.route)



    /*Este nav Contorler es para pasar de la screen de rutas a la scrren de boulder*/
    val navController = rememberNavController()

    val dialogCloseApp: Boolean by homeViewModel.dialogCloseApp.observeAsState(false)


//    initRemoteConfig()
    //  val context = LocalContext.current

    val user = auth.getCurrentUser()
    val onLogoutConfirmed: () -> Unit = {
        auth.signOut()
        navigation.navigate(AppScreens.LoginScreen.route) {
            popUpTo(AppScreens.HomeScreen.route) {
                inclusive = true
            }
        }
    }

    if (user != null) {
        val displayName = when {
            user.isAnonymous -> ""
            user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID } -> user.displayName
            else -> user.email?.split("@")?.get(0) ?: ""}

        val email = when {
            user.isAnonymous -> "Anónimo"
            else -> user.email.toString()}

        homeViewModel.setCurrentUser(UserModel(
            user_id = user.uid,
            display_name = displayName.toString(),
            email = email,
            urlPhoto = user.photoUrl.toString()))
    }

    Scaffold(
        topBar = {
                TopBarWelcome(homeViewModel = homeViewModel, navigation= navigation )
        },

        bottomBar = {
            BottomBar(navController = navController)
        },


        ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            /* Controla el dialogo para cerrar la aplicación */
            if (dialogCloseApp) {
                LogoutDialog(onConfirmLogout = {
                    onLogoutConfirmed()
                    homeViewModel.dialogCloseApp_close()
                }, onDismiss = { homeViewModel.dialogCloseApp_close() })
            }

            BottomNavGraph(
                navController = navController,
                authManager = auth,
                homeViewModel = homeViewModel,
                realtime = realtime,
                navigation
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWelcome(homeViewModel: HomeViewModel, navigation: NavController ) {
    val currentUser: UserModel? by homeViewModel.currentUser.observeAsState()
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentUser != null && currentUser?.urlPhoto.toString() != "null") {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentUser?.urlPhoto)
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
                       text = if (!currentUser?.display_name.isNullOrEmpty()) welcomeMessage + currentUser?.display_name else welcomeMessage,
                       fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (currentUser?.email.toString() != "null") "${currentUser?.email}" else "Anónimo",
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
                    homeViewModel.dialogCloseApp_show()
                }
            ) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
            }
            IconButton(
                onClick = {
                    navigation.navigate(AppScreens.RouteSetterScreen.route)
                    //analytics.logButtonClicked("Click: No tienes una cuenta? Regístrate")
                }
            ) {
                Icon(Icons.Filled.Person, contentDescription = "Perfil")
            }
            IconButton(
                onClick = {
                  //  navigation.navigateUp()
                }
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Atras")
            }
        }
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavScreen.Bloques,
        BottomNavScreen.Rutas,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        screens.forEach { screens ->
            if (currentDestination != null) {
                AddItem(
                    screens = screens,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screens: BottomNavScreen,
    currentDestination: NavDestination,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { Text(text = screens.title) },
        icon = { Icon(imageVector = screens.icon, contentDescription = "Icons") },
        selected = currentDestination.hierarchy?.any {
            it.route == screens.path
        } == true,
        onClick = {
            navController.navigate(screens.path) {
                popUpTo(navController.graph.id)
                launchSingleTop = true
            }
        }
    )
}

sealed class BottomNavScreen(val path: String, val title: String, val icon: ImageVector) {
    object Rutas : BottomNavScreen(
        path = "rutas",
        title = "Rutas",
        icon = Icons.Default.ArrowForward
    )

    object Bloques : BottomNavScreen(
        path = "bloques",
        title = "Bloques",
        icon = Icons.Default.MoreVert
    )
    /* object Photos : BottomNavScreen(
         path = "photos",
         title = "Photos",
         icon = Icons.Default.Face
     )*/
}

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    authManager: AuthManager,
    homeViewModel: HomeViewModel,
    realtime: RealtimeManager,
    navigation: NavController
) {

    // val firestore = FirestoreManager(context)
    // val storage = CloudStorageManager(context)
    NavHost(navController = navController, startDestination = BottomNavScreen.Bloques.path) {
        composable(route = BottomNavScreen.Bloques.path) {
            BouldersScreen(
                realtime = realtime,
                authManager = authManager,
                homeViewModel = homeViewModel,
                navigation = navigation
            )
        }

        composable(route = BottomNavScreen.Rutas.path) {
            RoutesScreen(realtime = realtime, authManager = authManager)
        }


        /*
       composable(route = BottomNavScreen.Photos.route) {
            CloudStorageScreen(storage = storage)
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


