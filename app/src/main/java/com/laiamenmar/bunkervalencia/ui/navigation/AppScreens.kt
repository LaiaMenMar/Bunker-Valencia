package com.laiamenmar.bunkervalencia.ui.navigation

sealed class AppScreens (val route: String) {
    object LoginScreen: AppScreens("Login Screen")
    object HomeScreen: AppScreens("Home Screen")
    object RegisterScreen : AppScreens("Register Screen")
    object ForgotPasswordScreen : AppScreens("ForgotPassword Screen")
    object HomeScreen1: AppScreens("Home Screen1")
}
