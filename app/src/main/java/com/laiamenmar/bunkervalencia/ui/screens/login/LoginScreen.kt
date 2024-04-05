package com.laiamenmar.bunkervalencia.ui.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    authManager: AuthManager,
    analytics: AnalyticsManager,
    navigation: NavController,
    loginViewModel: LoginViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val emailInput by loginViewModel.emailInput.collectAsState()
    val passwordInput by loginViewModel.passwordInput.collectAsState()
    val isLoginEnable by loginViewModel.isLoginEnable.collectAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        scope.launch {
            loginViewModel.signInWithGoogle(authManager, analytics, context, navigation, result)
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.align(Alignment.Center)) {
            HeaderImage(
                Modifier
                    .align(Alignment.CenterHorizontally)
            )

            EmailField(
                value = emailInput,
                onValueChanged = { loginViewModel.onLoginChanged(it, passwordInput) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(4.dp))

            PasswordField(
                value = passwordInput,
                onValueChange = { loginViewModel.onLoginChanged(emailInput, it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(8.dp))

            ActionButton(
                buttonText = "Iniciar Sesión",
                onClick = {
                    scope.launch {
                        loginViewModel.emailPassSignIn(
                            emailInput,
                            passwordInput,
                            authManager,
                            analytics,
                            context,
                            navigation
                        )
                    }
                },
                loginEnable = isLoginEnable
            )

            Spacer(modifier = Modifier.padding(4.dp))

            ClickableTextButton(
                text = "¿Olvidaste tu contraseña?",
                onClick = {
                    navigation.navigate(AppScreens.ForgotPasswordScreen.route) {
                        popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "-------- o --------",
                Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            SocialMediaButton(
                onClick = {
                    scope.launch {
                        loginViewModel.incognitoSignIn(authManager, analytics, navigation)
                    }
                },
                text = "Continuar como invitado",
                icon = R.drawable.ic_incognito,
                color = Color(0xFF363636)
            )
            Spacer(modifier = Modifier.height(4.dp))

            SocialMediaButton(
                onClick = {
                    authManager.signInWithGoogle(googleSignInLauncher)
                },
                text = "Continuar con Google",
                icon = R.drawable.ic_google,
                color = Color(0xFFF1F1F1)
            )

            Spacer(modifier = Modifier.height(6.dp))

            ClickableTextButton(
                text = "¿No tienes una cuenta? Regístrate",
                onClick = {
                    navigation.navigate(AppScreens.RegisterScreen.route)
                    analytics.logButtonClicked("Click: No tienes una cuenta? Regístrate")
                },
                Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}




