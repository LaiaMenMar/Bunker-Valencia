/**
 * AppScreens.kt: Este archivo contiene la enumeración sellada que representa las distintas
 * pantallas de la aplicación.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define una enumeración sellada para representar las pantallas de la aplicación,
 * cada una con su ruta asociada para la navegación.
 * Fecha de creación: 2024-01-10
 */
package com.laiamenmar.bunkervalencia.ui.navigation
/**
 * Enumeración sellada que representa las distintas pantallas de la aplicación.
 * Todas las subclases de una clase sellada deben estar definidas dentro del mismo archivo.
 *
 * @property route Ruta asociada a la pantalla para la navegación.
 */
sealed class  AppScreens (val route: String) {
    object LoginScreen: AppScreens("Login Screen")
    object RegisterScreen : AppScreens("Register Screen")
    object ForgotPasswordScreen : AppScreens("ForgotPassword Screen")
    object HomeScreen: AppScreens("Home Screen")
    object RouteSetterScreen: AppScreens("RouteSetter Screen")
    object BoulderDetailScreen: AppScreens("BoulderDetail Screen")
    object CameraScreen: AppScreens("Camera Screen")
}
