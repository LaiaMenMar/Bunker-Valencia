package com.laiamenmar.bunkervalencia.ui.navigation


sealed class AppScreens (val route: String) {
    object LoginScreen: AppScreens("Login Screen")
    object RegisterScreen : AppScreens("Register Screen")
    object ForgotPasswordScreen : AppScreens("ForgotPassword Screen")
    object HomeScreen: AppScreens("Home Screen")
    object RouteSetterScreen: AppScreens("RouteSetter Screen")
    object BoulderDetailScreen: AppScreens("BoulderDetail Screen")
    object CameraScreen: AppScreens("Camera Screen")


}
