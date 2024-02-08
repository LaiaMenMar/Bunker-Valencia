package com.laiamenmar.bunkervalencia.ui.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.screens.ActionButton
import com.laiamenmar.bunkervalencia.ui.screens.ClickableTextButton
import com.laiamenmar.bunkervalencia.ui.screens.EmailField
import com.laiamenmar.bunkervalencia.ui.screens.HeaderImage
import com.laiamenmar.bunkervalencia.ui.screens.TitleLogin
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.AuthRes
import kotlinx.coroutines.launch


@Composable
fun ForgotPasswordScreen(authManager: AuthManager, analytics: AnalyticsManager, navigation: NavController, loginViewModel: LoginViewModel) {
    val scope = rememberCoroutineScope()
   /* var emailInput by remember { mutableStateOf("") }*/
    val emailInput by loginViewModel.emailInput.collectAsState()
    var context = LocalContext.current
    var showEmailSentMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)


    ) {
        HeaderImage(
            Modifier
                .align(Alignment.CenterHorizontally)
        )

        TitleLogin("recuperar contraseña", Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.padding(4.dp))

        EmailField(
            value = emailInput,
            onValueChanged = {loginViewModel.onEmailChanged(it)},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        ActionButton(
            onClick = {
                scope.launch {
                    loginViewModel.passwordReset(emailInput, authManager, analytics, context, navigation)
                }
            },
            buttonText = "recuperar",
            loginEnable = true
        )
        ClickableTextButton(
            text = "Volver",
            onClick = {
                navigation.navigate(AppScreens.LoginScreen.route)
                analytics.logButtonClicked("Click: Volver")
            },
            Modifier.align(Alignment.CenterHorizontally)
        )
    }
}


/*
@Preview
@Composable
fun ForgotPasswordScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var analytics: AnalyticsManager = AnalyticsManager(context)
    var authManager: AuthManager = AuthManager(context)

    BunkerValenciaTheme {
        Surface {
            ForgotPasswordScreen(
                analytics = analytics,
                navigation = navController,
                authManager = authManager,
            )
        }
    }
}
*/
