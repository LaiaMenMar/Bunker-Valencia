package com.laiamenmar.bunkervalencia.navigation

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseUser
import com.laiamenmar.bunkervalencia.screens.*
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager


/**
 * Se encarga de la navegación entre pantallas
 */
@Composable
fun AppNavigation(context: Context, navController: NavHostController = rememberNavController()) {
    var authManager: AuthManager = AuthManager()
    val user: FirebaseUser? = authManager.getCurrentUser()

    var analytics: AnalyticsManager = AnalyticsManager(context)


    Screen {
        NavHost(
            navController = navController,
            startDestination = if (user == null) AppScreens.LoginScreen.route else AppScreens.HomeScreen.route

        ) {

            composable(route = AppScreens.LoginScreen.route) {
                LoginScreen(
                   authManager, analytics, navController,
                )
            }
            composable(route = AppScreens.HomeScreen.route) {
                HomeScreen(
                    authManager, analytics, navController
                    )
            }
            composable(route = AppScreens.RegisterScreen.route) {
                RegisterScreen(
                    authManager, analytics, navController,
                )
            }
            composable(route = AppScreens.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(
                    authManager, analytics, navController,
                )
            }

        }
    }
}



