/**
 * HomeScreen.kt: Este archivo contiene la implementación de la pantalla de inicio de la aplicación,
 * que muestra la vista principal del usuario autenticado.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define la pantalla de inicio, que proporciona acceso a las funcionalidades principales
 * de la aplicación después de que el usuario ha iniciado sesión.
 * Fecha de creación: 2024/04/11
 */

package com.laiamenmar.bunkervalencia.ui.screens.home

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
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager


/**
 * Función componible para renderizar la pantalla de inicio de la aplicación, que muestra
 * la vista principal del usuario autenticado.Se guardan los datos en el viewmodel del
 * usuario loggeado. Se declara la variable para poder salir de la aplicacion.
 *
 * @param analytics Instancia de AnalyticsManager para registrar eventos de análisis.
 * @param auth Instancia de AuthManager para manejar operaciones de autenticación.
 * @param navigation NavController para navegar entre componibles.
 * @param homeViewModel Instancia de HomeViewModel que contiene la lógica para la pantalla de inicio.
 * @param realtime Instancia de RealtimeManager para interactuar con la base de datos en tiempo real.
 */
@Composable
fun HomeScreen(
    analytics: AnalyticsManager,
    auth: AuthManager,
    navigation: NavController,
    homeViewModel: HomeViewModel,
    realtime: RealtimeManager
) {
    analytics.logScreenView(screenName = AppScreens.HomeScreen.route)

    val navController = rememberNavController()
    val dialogCloseApp: Boolean by homeViewModel.dialogCloseApp.observeAsState(false)
    val onLogoutConfirmed: () -> Unit = {
        auth.signOut()
        navigation.navigate(AppScreens.LoginScreen.route) {
            popUpTo(AppScreens.HomeScreen.route) {
                inclusive = true
            }
        }
    }

    val user = auth.getCurrentUser()
    if (user != null) {
        if (user.isAnonymous) {
            homeViewModel.setCurrentUser(
                UserModel(
                    user_id = user.uid,
                    display_name = "",
                    email = "Ánonimo",
                    urlPhoto = user.photoUrl.toString(),
                )
            )
        } else {
            val uid = user.uid
            LaunchedEffect(uid) {
                val userofbbdd = realtime.getuser(uid)
                if (userofbbdd != null) {
                    homeViewModel.setCurrentUser(userofbbdd)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarWelcome(homeViewModel = homeViewModel, navigation = navigation)
        },

        bottomBar = {
            BottomBar(navController = navController)
        },
        ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
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

/**
 * Función componible para mostrar la barra de la parte superior de la aplicación en la
 * pantalla principal,
 * dando la bienvenida al usuario y proporcionando opciones como cerrar sesión o acceder
 * a la pantalla para elegir los equipadores si eres el administrador.
 *
 * @param homeViewModel Instancia de HomeViewModel que contiene los datos del usuario actual.
 * @param navigation NavController para navegar entre componibles.
 */



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWelcome(homeViewModel: HomeViewModel, navigation: NavController) {
    val welcomeMessage by mutableStateOf("Hola! ")
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
                        text = if (!currentUser?.display_name.isNullOrEmpty()) welcomeMessage +
                                currentUser?.display_name else welcomeMessage,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (currentUser?.email.toString() != "null") "${currentUser?.email}"
                        else "Anónimo",
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

            if (currentUser != null && currentUser?.display_name.toString() == "admin") {
                IconButton(
                    onClick = {
                        navigation.navigate(AppScreens.RouteSetterScreen.route)
                    }
                ) {
                    Icon(Icons.Filled.Person, contentDescription = "Perfil")
                }
            }
        }
    )
}

/**
 * Función componible para mostrar la barra de navegación en la parte inferior de la pantalla,
 * que permite al usuario cambiar entre diferentes secciones de la aplicación.
 *
 * @param navController NavController para manejar la navegación entre las diferentes secciones.
 */
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

/**
 * Función componible para agregar un elemento a la barra de navegación en la parte inferior
 * de la pantalla.
 *
 * @param screens Objeto BottomNavScreen que representa la pantalla asociada al elemento
 * de navegación.
 * @param currentDestination Destino actual de la navegación.
 * @param navController NavController para manejar la navegación entre las diferentes secciones.
 */
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

/**
 * Sealed class que define las diferentes pantallas asociadas a los elementos de la barra de
 * navegación inferior.
 *
 * @property path Ruta asociada a la pantalla.
 * @property title Título de la pantalla.
 * @property icon Icono asociado a la pantalla.
 */
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
}


/**
 * Función componible que representa el gráfico de navegación para la barra de navegación inferior.
 * Esta función define las diferentes pantallas y sus rutas asociadas.
 *
 * @param navController Controlador de navegación de Compose.
 * @param authManager Instancia de AuthManager para manejar operaciones de autenticación.
 * @param homeViewModel Instancia de HomeViewModel que contiene la lógica relacionada
 * con la pantalla principal.
 * @param realtime Instancia de RealtimeManager para manejar operaciones en tiempo real.
 * @param navigation NavController para navegar entre componibles.
 */

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    authManager: AuthManager,
    homeViewModel: HomeViewModel,
    realtime: RealtimeManager,
    navigation: NavController
) {

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
    }
}

/**
 * Función componible que muestra un diálogo de confirmación para cerrar sesión.
 *
 * @param onConfirmLogout Acción a realizar cuando se confirma el cierre de sesión.
 * @param onDismiss Acción a realizar cuando se descarta el diálogo.
 */
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


