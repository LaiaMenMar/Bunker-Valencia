package com.laiamenmar.bunkervalencia.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.navigation.AppScreens
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager
import com.laiamenmar.bunkervalencia.utils.AuthManager

@Composable
fun LogoutDialog(onConfirmLogout: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            Button(
                onClick = onConfirmLogout
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

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