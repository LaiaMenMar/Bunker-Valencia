package com.laiamenmar.bunkervalencia.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.AuthRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {


    /*  private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable*/


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
                    navigation.navigate(AppScreens.HomeScreen1.route) {
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
            }

        } else {
            Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_SHORT).show()
        }
    }


    suspend fun incognitoSignIn(
        auth: AuthManager,
        analytics: AnalyticsManager,
        context: Context,
        navigation: NavController
    ) {
        when (val result = auth.singInAnonymously()) {
            is AuthRes.Success -> {
                analytics.logButtonClicked("Click: Continuar como invitado")
                navigation.navigate(AppScreens.HomeScreen1.route) {
                    popUpTo(AppScreens.LoginScreen.route) {
                        inclusive = true
                    }
                }
            }

            is AuthRes.Error -> {
                analytics.logError("Error SignIn Incognito: ${result.errorMessage}")
            }
        }
    }
}


