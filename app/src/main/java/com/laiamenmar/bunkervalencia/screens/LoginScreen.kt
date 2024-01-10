package com.laiamenmar.bunkervalencia.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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


@Composable
fun LoginScreen(analytics: AnalyticsManager, navigation: NavController) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        ClickableText(
            text = AnnotatedString("¿No tienes una cuenta? Regístrate"),
            onClick = {

            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(40.dp),
            style = TextStyle(
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer

            )
        )
        Login(Modifier.align(Alignment.Center))
    }
}

@Composable
fun Login(modifier: Modifier) {
    Column(modifier = modifier) {
        val email = remember {
            mutableStateOf(TextFieldValue())
        }
        val password = remember {
            mutableStateOf(TextFieldValue())
        }

        HeaderImage(Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.padding(16.dp))

        EmailField()

        Spacer(modifier = Modifier.padding(4.dp))

        PasswordField()

        Spacer(modifier = Modifier.padding(8.dp))

        ForgotPassword(Modifier.align(Alignment.End))

        Spacer(modifier = Modifier.padding(16.dp))

        LoginButton()

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
        Text(text = "Iniciar Sesion", fontSize = 16.sp )
   }

}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(text = "Olvidaste la contraseña?",

        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )

}


@Composable
fun PasswordField() {
    TextField(value = "", onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder= { Text(text = "Email")},
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
        modifier = modifier.size(250.dp))
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Preview(showSystemUi = true)
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var analytics: AnalyticsManager = AnalyticsManager(context)

    BunkerValenciaTheme {
        Surface {
            LoginScreen(
                analytics = analytics,
                navigation = navController,
            )
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


