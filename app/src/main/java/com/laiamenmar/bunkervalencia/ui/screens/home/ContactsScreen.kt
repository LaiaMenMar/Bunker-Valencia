package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable()
fun ContactsScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray,)){
        Text(text = "Contactos")
        
    }
}