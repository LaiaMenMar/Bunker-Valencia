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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
        colors = TextFieldDefaults.colors(

        ),

        label = { Text("Email") },
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    // var passwordVisibility by remember { mutableStateOf(false) }

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
        /*  trailingIcon = {
              val image = if (passwordVisibility) {

              } else {

              }
              IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                  Image(
                      painter = painterResource(id = image), contentDescription = "show password"
                  )
              }
          }*/
    )
}

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
      //  colors = ButtonDefaults.buttonColors(
        //    containerColor = md_theme_light_primary,
        //    disabledContainerColor = md_theme_light_primaryContainer,
          //  contentColor= Color.White,
           // disabledContentColor = Color.White),



        enabled = loginEnable
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
        style = TextStyle(
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

