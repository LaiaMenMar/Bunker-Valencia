/**
 * AuthComponents.kt: Este archivo contiene la implementación de componentes reutilizables
 * relacionados con la autenticación en la aplicación.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define componentes comunes utilizados en las pantallas de autenticación, como campos de
 * entrada de correo y contraseña, botones de acción y botones de inicio de sesión social.
 * Fecha de creación: 2024-02-07
 */

package com.laiamenmar.bunkervalencia.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laiamenmar.bunkervalencia.R

/**
 * HeaderImage: Este componente representa de encabezado utilizado en diferentes partes de la
 * aplicación siendo la imagen del logotipo del bunker con el logotipo de climbingExplore.
 *
 * @param modifier El modificador para aplicar al componente.
 */

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo_bunker),
        contentDescription = "logo del bunker",
        modifier = modifier.size(300.dp)
    )
    Row(modifier = modifier.padding(8.dp)) {
        Text(
            text = "Powered by ",
            style = TextStyle(color = Color.Gray),
            modifier = modifier.padding(top = 4.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.logo_climbing),
            contentDescription = "logo climbing Explore",
            modifier = modifier.size(50.dp),
        )
    }
}

/**
 * EmailField: Este componente representa representa un campo de entrada de correo electrónico.
 *
 * @param value El valor actual del campo de entrada.
 * @param onValueChanged La función de callback que se llama cuando cambia el valor del
 * campo de entrada.
 * @param modifier El modificador para aplicar al componente.
 *
 */
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

/**
 * PasswordField: Este componente representa un campo de entrada de contraseña.
 *
 * @param value El valor actual del campo de entrada.
 * @param onValueChanged La función de callback que se llama cuando cambia el valor del
 * campo de entrada.
 * @param modifier El modificador para aplicar al componente.
 *
 */
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
        label = { Text("Contraseña") }
    )
}

/**
 * ActionButton: Este componente representa un botón de acción utilizado en la interfaz de usuario.
 *
 * @param onClick La acción a realizar cuando se hace clic en el botón.
 * @param buttonText El texto que se muestra en el botón.
 * @param loginEnable Un indicador booleano que especifica si el botón está habilitado o no.
 */

@Composable
fun ActionButton(
    onClick: () -> Unit,
    buttonText: String,
    loginEnable: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = loginEnable
    )
    {
        Text(
            text = buttonText.uppercase(),
            fontSize = 16.sp
        )
    }
}

/**
 * SocialMediaButton: Este componente representa un botón de inicio de sesión a través
 * de redes sociales o de manera anónima.
 *
 * @param onClick La acción a realizar cuando se hace clic en el botón.
 * @param buttonText El texto que se muestra en el botón.
 * @param icon El recurso de imagen que se muestra junto al texto del botón.
 * @param color El color de fondo del botón.
 */
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

/**
 * ClickableTextButton:Este componente representa un botón de texto que es clickable para navegar
 * entre pantallas en la auténtificación.
 *
 * @param text El texto que se muestra en el botón.
 * @param onClick La acción a realizar cuando se hace clic en el botón.
 * @param modifier Modificador para personalizar el aspecto y diseño del botón.
 */
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
        style = TextStyle(
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

/**
 * TitleLogin: Este componente muestra un título en negrita con un tamaño de fuente
 * y espaciado específicos.
 *
 * @param title El texto del título a mostrar.
 * @param modifier Modificador para personalizar el aspecto y diseño del título.
 */
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

