package com.laiamenmar.bunkervalencia.screens


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.laiamenmar.bunkervalencia.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.AuthRes
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(authManager: AuthManager, analytics: AnalyticsManager, navigation: NavController) {
    val scope = rememberCoroutineScope()
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var context = LocalContext.current

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
            onValueChanged = { emailInput = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        PasswordField(value = passwordInput,
            onValueChange = { passwordInput = it },
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.padding(8.dp))

        ActionButton(
            buttonText = "Registrese",
            onClick = {
                scope.launch {
                    signUp(emailInput, passwordInput, authManager, analytics, context, navigation)
                }
            }
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

private suspend fun signUp(
    email: String,
    password: String,
    auth: AuthManager,
    analytics: AnalyticsManager,
    context: Context,
    navigation: NavController
) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        when (val result = auth.createUserWithEmailandPassword(email, password)) {
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
        }

    } else {
        Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_LONG).show()
    }
}


@Preview
@Composable
fun PreviewRegisterScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var analytics: AnalyticsManager = AnalyticsManager(context)
    var authManager: AuthManager = AuthManager(context)

    BunkerValenciaTheme {
        Surface {
            RegisterScreen(
                analytics = analytics,
                navigation = navController,
                authManager = authManager,
            )
        }
    }
}
