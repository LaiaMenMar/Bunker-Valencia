package com.laiamenmar.bunkervalencia.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager

@Composable
fun HomeScreen(analytics: AnalyticsManager, navigation: NavController) {
          Column(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.Center,
              )
              {
              Text ("HOLA MUNDO!")
              }
}


