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
class LoginViewModel:ViewModel() {
//class LoginViewModel @Inject constructor() :ViewModel() {
    private val _emailInput = MutableStateFlow("")
    val emailInput: StateFlow<String> = _emailInput

    private val _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput

    private val _isLoginEnable = MutableStateFlow(false)
    val isLoginEnable: StateFlow<Boolean> = _isLoginEnable

    init {
        // Agregar un registro para verificar la inicialización de los flujos de datos
        Log.d(
            "LoginViewModel",
            "Flujos de datos inicializados: emailInput=${_emailInput.value}, passwordInput=${_passwordInput.value}, isLoginEnable=${_isLoginEnable.value}"
        )
    }

    fun onLoginChanged(email: String, password: String) {
        _emailInput.value = email
        _passwordInput.value = password
        _isLoginEnable.value = enableLogin(email, password)

        Log.d(
            "LoginViewModel",
            "Cambios en los flujos de datos: emailInput=${_emailInput.value}, passwordInput=${_passwordInput.value}, isLoginEnable=${_isLoginEnable.value}"
        )
    }
    fun onEmailChanged(email: String) {
        _emailInput.value = email
    }

    fun onPasswordChanged(password: String) {
        _passwordInput.value = password
    }

    private fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 7

    // Autenticación por correo electrónico
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
                        "Error SignUp: ${result.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }

        } else {
            Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_SHORT).show()
        }
    }
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
                    /*la pantalla RegisterScreen esta encima del LoginScreen, sólo hay que cerrarla*/
                    navigation.popBackStack()
                }

                is AuthRes.Error -> {
                    analytics.logError("Error SignUp: ${result.errorMessage}")
                    Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_LONG)
                        .show()
                }

                else -> {}
            }

        } else {
            Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_LONG).show()
        }
    }

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
                Toast.makeText(context, "Correo enviado a $email", Toast.LENGTH_SHORT)
                    .show()
                navigation.navigate(AppScreens.LoginScreen.route)
            }
            is AuthRes.Error -> {
                analytics.logError("Error Reset password $email: ${result.errorMessage}")
                Toast.makeText(context, "Error al enviar el correo", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {}
        }
    }

    // Autenticación anónima
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
                analytics.logError("Error SignIn Incognito: ${result.errorMessage}")
            }

            else -> {}
        }
    }

    // Autenticación con Google
    suspend fun signInWithGoogle(auth: AuthManager,
                                 analytics: AnalyticsManager,
                                 context: Context,
                                 navigation: NavController,
                                 result: ActivityResult,) {
        val result = auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))

        when (result) {
            is AuthRes.Success -> {
                val credential = GoogleAuthProvider.getCredential(result?.data?.idToken, null)
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
                Toast.makeText(context, "Error: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


