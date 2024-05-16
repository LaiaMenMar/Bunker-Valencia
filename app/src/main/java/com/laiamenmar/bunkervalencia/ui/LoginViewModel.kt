/**
 * LoginViewModel.kt: Este archivo contiene la implementación del ViewModel para la pantalla de inicio de sesión.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define el ViewModel para la pantalla de inicio de sesión, proporcionando lógica para autenticar usuarios y manejar el flujo de datos relacionado.
 * Fecha de creación: 2024-02-07
 */
package com.laiamenmar.bunkervalencia.ui

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.GoogleAuthProvider
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.AuthRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel para la pantalla de inicio de sesión.
 * Proporciona la lógica para autenticar usuarios y manejar el flujo de datos relacionado.
 *
 * @property emailInput Flujo de estado para el correo electrónico.
 * @property passwordInput Flujo de estado para la contraseña.
 * @property isLoginEnable Flujo de estado para habilitar el inicio de sesión.
 */
class LoginViewModel : ViewModel() {
    private val _emailInput = MutableStateFlow("")
    val emailInput: StateFlow<String> = _emailInput

    private val _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput

    private val _isLoginEnable = MutableStateFlow(false)
    val isLoginEnable: StateFlow<Boolean> = _isLoginEnable

    /**
     * Actualiza el flujo de datos cuando cambia el valor del correo electrónico.
     *
     * @param email El nuevo valor del correo electrónico.
     */
    fun onEmailChanged(email: String) {
        _emailInput.value = email
    }

    /**
     * Actualiza el flujo de datos cuando cambia el valor de la contraseña.
     *
     * @param password El nuevo valor de la contraseña.
     */
    fun onPasswordChanged(password: String) {
        _passwordInput.value = password
    }

    /**
     * Verifica si el correo electrónico y la contraseña cumplen con los requisitos para habilitar el inicio de sesión.
     *
     * @param email El correo electrónico a verificar.
     * @param password La contraseña a verificar.
     * @return `true` si el correo electrónico y la contraseña son válidos y pueden habilitar el inicio de sesión, `false` de lo contrario.
     */
    private fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6

    /**
    * Actualiza los flujos de datos cuando cambia el valor de correo electrónico o contraseña.
    *
    * @param email El nuevo valor del correo electrónico.
    * @param password El nuevo valor de la contraseña.
    */
    fun onLoginChanged(email: String, password: String) {
        _emailInput.value = email
        _passwordInput.value = password
        _isLoginEnable.value = enableLogin(email, password)

        Log.d(
            "Laia",
            "Cambios en los flujos de datos: emailInput=${_emailInput.value}, passwordInput=${_passwordInput.value}, isLoginEnable=${_isLoginEnable.value}"
        )
    }

    /**
     * Realiza el inicio de sesión con correo electrónico y contraseña.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param auth El gestor de autenticación.
     * @param analytics El gestor de analítica.
     * @param context El contexto de la aplicación.
     * @param navigation El controlador de navegación.
     */
    suspend fun emailPassSignIn(
        email: String,
        password: String,
        auth: AuthManager,
        analytics: AnalyticsManager,
        context: Context,
        navigation: NavController
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            when (val result = auth.signInWithEmailAndPassword(email, password)) {
                is AuthRes.Success -> {
                    analytics.logButtonClicked("Click: Iniciar sesion correo y Contraseña")
                    Toast.makeText(context, "Bienvenidx", Toast.LENGTH_SHORT).show()
                    navigation.navigate(AppScreens.HomeScreen.route) {
                        popUpTo(AppScreens.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }

                is AuthRes.Error -> {
                    analytics.logError("Error SignUp: ${result.errorMessage}")
                    Toast.makeText(
                        context,
                        "Error al iniciar sesión: ${result.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()}
            }

        } else {
            Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Realiza el registro de un nuevo usuario.
     *
     * @param email El correo electrónico del nuevo usuario.
     * @param password La contraseña del nuevo usuario.
     * @param auth El gestor de autenticación.
     * @param analytics El gestor de analítica.
     * @param context El contexto de la aplicación.
     * @param navigation El controlador de navegación.
     */
    suspend fun signUp(
        email: String,
        password: String,
        auth: AuthManager,
        analytics: AnalyticsManager,
        context: Context,
        navigation: NavController
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            when (val result = auth.createUserWithEmailAndPassword(email, password)) {
                is AuthRes.Success -> {
                    analytics.logButtonClicked(FirebaseAnalytics.Event.SIGN_UP)
                    Toast.makeText(context, "Registro existoso", Toast.LENGTH_SHORT).show()
                    navigation.popBackStack()
                }

                is AuthRes.Error -> {
                    analytics.logError("Error en el registro: ${result.errorMessage}")
                    Toast.makeText(
                        context,
                        "Error en el registro: ${result.errorMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()}
            }

        } else {
            Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Envia un correo para resetear la contraseña del usuario.
     *
     * @param email El correo electrónico del usuario.
     * @param auth El gestor de autenticación.
     * @param analytics El gestor de analítica.
     * @param context El contexto de la aplicación.
     * @param navigation El controlador de navegación.
     */
    suspend fun passwordReset(
        email: String,
        auth: AuthManager,
        analytics: AnalyticsManager,
        context: Context,
        navigation: NavController
    ) {
        when (val result = auth.resetPassword(email)) {
            is AuthRes.Success -> {
                analytics.logButtonClicked("Click: Reset password $email")
                Toast.makeText(context, "Correo enviado a $email", Toast.LENGTH_SHORT).show()
                navigation.navigate(AppScreens.LoginScreen.route)
            }

            is AuthRes.Error -> {
                analytics.logError("Error Reset password $email: ${result.errorMessage}")
                Toast.makeText(
                    context,
                    "Error al enviar el correo $email: ${result.errorMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Realiza el inicio de sesión anónimo.
     *
     * @param auth El gestor de autenticación.
     * @param analytics El gestor de analítica.
     * @param navigation El controlador de navegación.
     */
    suspend fun incognitoSignIn(
        auth: AuthManager,
        analytics: AnalyticsManager,
        navigation: NavController
    ) {
        when (val result = auth.singInAnonymously()) {
            is AuthRes.Success -> {
                analytics.logButtonClicked("Click: Continuar como invitado")
                navigation.navigate(AppScreens.HomeScreen.route) {
                    popUpTo(AppScreens.LoginScreen.route) {
                        inclusive = true
                    }
                }
            }

            is AuthRes.Error -> {
                analytics.logError("Error al iniciar sesión como Incognito: ${result.errorMessage}")
            }
        }
    }

    /**
     * Realiza el inicio de sesión con Google.
     *
     * @param auth El gestor de autenticación.
     * @param analytics El gestor de analítica.
     * @param context El contexto de la aplicación.
     * @param navigation El controlador de navegación.
     * @param result El resultado de la actividad.
     */
    suspend fun signInWithGoogle(
        auth: AuthManager,
        analytics: AnalyticsManager,
        context: Context,
        navigation: NavController,
        result: ActivityResult,
    ) {

        when (val result = auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
            is AuthRes.Success -> {
                val credential = GoogleAuthProvider.getCredential(result.data?.idToken, null)
                val fireUser = auth.signInWithGoogleCredential(credential)

                if (fireUser != null) {
                    Toast.makeText(context, "Bienvenidx", Toast.LENGTH_SHORT).show()
                    navigation.navigate(AppScreens.HomeScreen.route) {
                        popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                    }
                }
            }

            is AuthRes.Error -> {
                analytics.logError("Error SignIn: ${result.errorMessage}")
                Toast.makeText(
                    context,
                    "Error al iniciar error con Google: ${result.errorMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


