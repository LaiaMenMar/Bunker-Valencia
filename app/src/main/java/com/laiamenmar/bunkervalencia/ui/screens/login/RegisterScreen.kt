/**
 * RegisterScreen.kt: Este archivo contiene la implementación de la pantalla de registro
 * de la aplicación.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define la pantalla de registro, que permite a los usuarios crear una cuenta nueva.
 * Fecha de creación: 2024-01-12
 */
package com.laiamenmar.bunkervalencia.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import kotlinx.coroutines.launch

/**
 * Función componible para renderizar la pantalla de registro de la aplicación.
 *
 * @param authManager Instancia de AuthManager para manejar operaciones de autenticación.
 * @param analytics Instancia de AnalyticsManager para registrar eventos de análisis.
 * @param navigation NavController para navegar entre componibles.
 * @param loginViewModel Instancia de LoginViewModel que contiene la lógica para la pantalla
 * de inicio de sesión. */

@Composable
fun RegisterScreen(authManager: AuthManager, analytics: AnalyticsManager, navigation: NavController, loginViewModel: LoginViewModel) {
    val scope = rememberCoroutineScope()

    val emailInput by loginViewModel.emailInput.collectAsState()
    val passwordInput by loginViewModel.passwordInput.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderImage(
            Modifier
                .align(Alignment.CenterHorizontally)
        )

        TitleLogin("crear cuenta", Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.padding(4.dp))

        EmailField(
            value = emailInput,
            onValueChanged = { loginViewModel.onEmailChanged(it)},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        PasswordField(value = passwordInput,
            onValueChange = { loginViewModel.onPasswordChanged(it) },
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.padding(8.dp))

        ActionButton(
            onClick = {
                scope.launch {
                    loginViewModel.signUp(emailInput, passwordInput, authManager, analytics, context, navigation)
                }
            },
            buttonText = "registrese",
            loginEnable = true
        )

        ClickableTextButton(
            text = "¿Ya tienes cuenta? Inicia Sesión",
            onClick = {
                navigation.navigate(AppScreens.LoginScreen.route)
                analytics.logButtonClicked("Click: ¿Ya tienes cuenta? Inicia Sesión")
            },
            Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

