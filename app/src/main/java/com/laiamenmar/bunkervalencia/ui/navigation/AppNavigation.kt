package com.laiamenmar.bunkervalencia.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.screens.HomeScreen
import com.laiamenmar.bunkervalencia.ui.screens.HomeScreen1
import com.laiamenmar.bunkervalencia.ui.screens.Screen
import com.laiamenmar.bunkervalencia.ui.screens.login.ForgotPasswordScreen
import com.laiamenmar.bunkervalencia.ui.screens.login.LoginScreen
import com.laiamenmar.bunkervalencia.ui.screens.login.RegisterScreen
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager



/**
 * Se encarga de la navegación entre pantallas
 */
@Composable
fun AppNavigation (context: Context, navController: NavHostController = rememberNavController(), loginViewModel: LoginViewModel, homeViewModel: HomeViewModel) {

    var authManager: AuthManager = AuthManager(context)
    val user: FirebaseUser? = authManager.getCurrentUser()

   var analytics: AnalyticsManager = AnalyticsManager(context)


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
            composable(route = AppScreens.HomeScreen.route) {
                HomeScreen(
                    authManager, analytics, navController
                )
            }
            composable(route = AppScreens.HomeScreen1.route) {
                HomeScreen1(
                    analytics, authManager, navController, homeViewModel
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

        }
    }
}



