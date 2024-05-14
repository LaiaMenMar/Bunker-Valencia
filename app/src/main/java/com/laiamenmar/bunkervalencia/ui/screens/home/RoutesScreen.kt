package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager

@Composable()
fun RoutesScreen(realtime: RealtimeManager, authManager: AuthManager){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray,)){
        Text(text = "Rutas")
    }
}