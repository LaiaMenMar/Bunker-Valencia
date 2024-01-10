package com.laiamenmar.bunkervalencia.navigation

sealed class AppScreens (val route: String) {
    object LoginScreen: AppScreens("Login Screen")
    object HomeScreen: AppScreens("Home Screen")
    object SignUpScreen : AppScreens("SignUp Screen")
    object ForgotPasswordScreen : AppScreens("ForgotPassword Screen")
}
