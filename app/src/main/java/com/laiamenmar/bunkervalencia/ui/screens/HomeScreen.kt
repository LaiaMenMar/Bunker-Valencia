package com.laiamenmar.bunkervalencia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager
import coil.compose.AsyncImage
import coil.request.ImageRequest



@Composable
fun HomeScreen(authManager: AuthManager, analytics: AnalyticsManager, navigation: NavController) {

    var showDialog by remember { mutableStateOf(false) }

    val user = authManager.getCurrentUser()

    val onLogoutConfirmed: () -> Unit = {
        authManager.signOut()
        navigation.navigate(AppScreens.LoginScreen.route){
            popUpTo(AppScreens.HomeScreen.route){
                inclusive = true
            }
        }


    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        if (user?.photoUrl != null){
            AsyncImage (  model = ImageRequest.Builder(LocalContext.current)
                .data(user?.photoUrl)
                .crossfade(true)
                .build(),
                contentDescription = "Imagen",
                placeholder = painterResource(id = R.drawable.profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )

        } else {
            Image (
               painter = painterResource(R.drawable.profile),
                contentDescription = "Foto de perfil por defecto",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier=Modifier.width(10.dp))

        Text(
            text = if(!user?.displayName.isNullOrEmpty()) "Hola ${user?.displayName}" else "Bienvenido",
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = if(!user?.email.isNullOrEmpty()) "${user?.email}" else "Anónimo",
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)

        Text("HOLA MUNDO!")


        Button(onClick = { showDialog = true }) {
            Text("Mostrar Diálogo")
        }

        if (showDialog) {
            LogoutDialog(
                onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialog = false
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }
    }
}