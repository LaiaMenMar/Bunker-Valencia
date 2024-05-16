/**
 * AppNavigation.kt: Este archivo contiene la función `AppNavigation`, que define la estructura de
 * navegación de la aplicación utilizando Jetpack Compose y el componente NavHost.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define la estructura de navegación de la aplicación, estableciendo las pantallas
 * y sus rutas asociadas.
 * También inicializa los gestores de análisis, autenticación y almacenamiento en la nube
 * necesarios para la navegación y la lógica de la aplicación.
 * Fecha de creación: 2024-01-10
 */
package com.laiamenmar.bunkervalencia.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.screens.home.HomeScreen
import com.laiamenmar.bunkervalencia.ui.screens.Screen
import com.laiamenmar.bunkervalencia.ui.screens.home.BoulderDetailScreen
import com.laiamenmar.bunkervalencia.ui.screens.home.CameraScreen
import com.laiamenmar.bunkervalencia.ui.screens.home.RouteSetterScreen
import com.laiamenmar.bunkervalencia.ui.screens.login.ForgotPasswordScreen
import com.laiamenmar.bunkervalencia.ui.screens.login.LoginScreen
import com.laiamenmar.bunkervalencia.ui.screens.login.RegisterScreen
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.CloudStorageManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager

/**
 * AppNavigation: Esta función define la navegación principal de la aplicación utilizando Jetpack Compose y Navigation.
 * Incluye las pantallas de inicio de sesión, registro, restablecimiento de contraseña y la pantalla principal de la aplicación.
 *
 * @param context El contexto de la aplicación.
 * @param navController El controlador de navegación que gestiona la navegación entre las pantallas.
 * @param loginViewModel ViewModel para la pantalla de inicio de sesión.
 * @param homeViewModel ViewModel para la pantalla principal de la aplicación.
 */
@Composable
fun AppNavigation(
    context: Context,
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
) {

    val analytics = AnalyticsManager(context)
    val realtime = RealtimeManager()
    val authManager = AuthManager(context, realtime)
    val storage = CloudStorageManager(context, realtime)

    Screen {
        NavHost(
            navController = navController,
            startDestination = AppScreens.LoginScreen.route/*if (user == null) AppScreens.LoginScreen.route else AppScreens.HomeScreen1.route*/

        ) {
            composable(route = AppScreens.LoginScreen.route) {
                LoginScreen(
                    authManager, analytics, navController, loginViewModel
                )
            }
            composable(route = AppScreens.RegisterScreen.route) {
                RegisterScreen(
                    authManager, analytics, navController, loginViewModel
                )
            }
            composable(route = AppScreens.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(
                    authManager, analytics, navController, loginViewModel
                )
            }
            composable(route = AppScreens.HomeScreen.route) {
                HomeScreen(
                    analytics, authManager, navController, homeViewModel, realtime
                )
            }
            composable(route = AppScreens.RouteSetterScreen.route) {
                RouteSetterScreen(
                    realtime, homeViewModel, navController
                )
            }
            composable(route = AppScreens.BoulderDetailScreen.route) {
                BoulderDetailScreen(
                    realtime, homeViewModel, navController, storage
                )
            }

            composable(route = AppScreens.CameraScreen.route) {
                CameraScreen(navController, storage, homeViewModel
                )
            }
        }
    }
}



