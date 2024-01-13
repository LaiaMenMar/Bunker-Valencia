package com.laiamenmar.bunkervalencia.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import kotlinx.coroutines.launch
import android.content.Context
import android.widget.Toast
import com.laiamenmar.bunkervalencia.utils.AuthRes
import kotlinx.coroutines.CoroutineScope


@Composable
fun LoginScreen(authManager: AuthManager, analytics: AnalyticsManager, navigation: NavController) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var passwordInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }

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
                onValueChanged = { emailInput = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(4.dp))

            PasswordField(value = passwordInput,
                onValueChange = { passwordInput = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(8.dp))

            ActionButton(
                buttonText = "Iniciar Sesión",
                onClick = {
                    scope.launch {
                        emailPassSignIn(emailInput, passwordInput, authManager, analytics, context, navigation)
                    }
                }
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
                        incognitoSignIn(authManager, analytics, context, navigation)
                    }
                },
                text = "Continuar como invitado",
                icon = R.drawable.ic_incognito,
                color = Color(0xFF363636)
            )
            Spacer(modifier = Modifier.height(4.dp))

            SocialMediaButton(
                onClick = {

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


@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "logo del bunker",
        modifier = modifier.size(300.dp)
    )
}

@Composable
fun EmailField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        label = { Text("Email") },
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = PasswordVisualTransformation(),
        modifier = modifier,
        placeholder = { Text(text = "Contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        label = { Text("Contraseña") },
    )
}

@Composable
fun ActionButton(onClick: () -> Unit,
                 buttonText: String ) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    )
    {
        Text(
            text = buttonText.uppercase(),
            fontSize = 16.sp
        )
    }
}

@Composable
fun SocialMediaButton(onClick: () -> Unit, text: String, icon: Int, color: Color) {
    var click by remember { mutableStateOf(false) }
    Surface(
        onClick = onClick,
        modifier = Modifier.clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            width = 1.dp,
            color = if (icon == R.drawable.ic_incognito) color else Color.Gray
        ),
        color = color
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(24.dp),
                contentDescription = text,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$text",
                color = if (icon == R.drawable.ic_incognito) Color.White else Color.Black
            )
            click = true
        }
    }
}
@Composable
fun ClickableTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ClickableText(
        text = AnnotatedString(text),
        onClick = {
            onClick()
        },
        modifier = modifier
            .padding(10.dp),
        style =  TextStyle(
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
    )
    )
}

@Composable
fun TitleLogin(title: String, modifier: Modifier) {
    Text(
        text = title.uppercase(),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.25.sp
        ),
        modifier = modifier.padding(16.dp)
    )
}



private suspend fun emailPassSignIn(
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
                Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    } else {
        Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_SHORT).show()

    }
}

private suspend fun incognitoSignIn(
    auth: AuthManager,
    analytics: AnalyticsManager,
    context: Context,
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

    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Preview(showSystemUi = true)
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var analytics: AnalyticsManager = AnalyticsManager(context)
    var authManager: AuthManager = AuthManager()


    BunkerValenciaTheme {
        Surface {
            LoginScreen(
                analytics = analytics,
                navigation = navController,
                authManager = authManager,
            )
        }
    }
}




