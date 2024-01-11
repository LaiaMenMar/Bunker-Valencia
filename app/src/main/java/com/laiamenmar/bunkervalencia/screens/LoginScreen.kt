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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import kotlinx.coroutines.launch
import android.R.*
import android.content.Context
import com.laiamenmar.bunkervalencia.utils.AuthRes
import kotlinx.coroutines.CoroutineScope


@Composable
fun LoginScreen(authManager: AuthManager, analytics: AnalyticsManager, navigation: NavController) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ClickableText(
            text = AnnotatedString("¿No tienes una cuenta? Regístrate"),
            onClick = {

            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            style = TextStyle(
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer

            )
        )
        Login(Modifier.align(Alignment.Center),authManager, analytics, navigation, scope, context)
    }
}

@Composable
fun Login(modifier: Modifier, auth: AuthManager, analytics: AnalyticsManager, navigation: NavController, scope:CoroutineScope, context: Context) {
    Column(modifier = modifier) {
        val email = remember {
            mutableStateOf(TextFieldValue())
        }
        val password = remember {
            mutableStateOf(TextFieldValue())
        }

        HeaderImage(Modifier
            .align(Alignment.CenterHorizontally)
            )

        EmailField()

        Spacer(modifier = Modifier.padding(4.dp))

        PasswordField()

        Spacer(modifier = Modifier.padding(8.dp))

        LoginButton()

        Spacer(modifier = Modifier.padding(8.dp))

        ForgotPassword(Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "-------- o --------", Modifier.align(Alignment.CenterHorizontally), style = TextStyle(color = Color.Gray))

        Spacer(modifier = Modifier.height(16.dp))

        SocialMediaButton(
            onClick = {
                      scope.launch {
                          incognitoSignIn (auth, analytics, context, navigation)
                      }

            },
            text = "Continuar como invitado",
            icon = R.drawable.ic_incognito,
            color = Color(0xFF363636)
        )
        Spacer(modifier = Modifier.height(15.dp))
        SocialMediaButton(
            onClick = {

            },
            text = "Continuar con Google",
            icon = R.drawable.ic_google,
            color = Color(0xFFF1F1F1)
        )
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
fun SocialMediaButton() {
    Button(onClick = { }, modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    )
    {
        Text(text = "Continuar con Google",
            fontSize = 16.sp )
    }
}

/*.background(MaterialTheme.colorScheme.onPrimaryContainer)
MaterialTheme.tyoigraphy.subtitle1)*/
@Composable
fun LoginButton() {
    Button(onClick = { }, modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        )
    {
        Text(text = "Iniciar Sesion".uppercase(),
            fontSize = 16.sp )
   }

}

@Composable
fun ForgotPassword(modifier: Modifier) {
    ClickableText(
        text = AnnotatedString("¿Olvidaste tu contraseña?"),
        onClick = {

        },
        modifier= modifier,
        style = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ))
}


@Composable
fun PasswordField() {
    var password by remember { mutableStateOf("") }
    TextField(value = password,
        onValueChange = {password= it},
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        placeholder= { Text(text = "Password")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1
        )
}

@Composable
fun EmailField() {
    var email by remember { mutableStateOf("") }

    TextField(value = email,
        onValueChange = {email = it},
        modifier = Modifier.fillMaxWidth(),
        placeholder= { Text(text = "Email")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1)
}



@Composable
fun HeaderImage(modifier: Modifier) {
    Image(painter = painterResource(id = R.drawable.logo),
        contentDescription = "logo del bunker",
        modifier = modifier.size(300.dp))
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

@Composable
fun SocialMediaButton(onClick: () -> Unit, text: String, icon: Int, color: Color, ) {
    var click by remember { mutableStateOf(false) }
    Surface(
        onClick = onClick,
        modifier = Modifier.clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = if(icon == R.drawable.ic_incognito) color else Color.Gray),
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
            Text(text = "$text", color = if(icon == R.drawable.ic_incognito) Color.White else Color.Black)
            click = true
        }
    }
}

private suspend fun incognitoSignIn(auth: AuthManager, analytics: AnalyticsManager, context: Context, navigation: NavController) {
    when (val result = auth.singInAnonymously()){
        is AuthRes.Success -> {
            analytics.logButtonClicked("Click: Continuar como invitado")
            navigation.navigate(AppScreens.HomeScreen.route){
                popUpTo(AppScreens.LoginScreen.route){
                    inclusive = true
                }
            }
        }
        is AuthRes.Error -> {
            analytics.logError("Error SignIn Incognito: ${result.errorMessage}")

        }

    }
}


/*@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Preview(showSystemUi = true)
@Composable
fun PreviewComponent() {
    BunkerValenciaTheme {
        EmailField()
    }
}
*/


