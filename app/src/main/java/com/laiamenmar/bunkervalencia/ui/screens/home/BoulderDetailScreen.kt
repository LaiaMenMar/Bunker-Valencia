package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.screens.TopBarWelcome
import com.laiamenmar.bunkervalencia.utils.RealtimeManager

@Composable
fun BoulderDetailScreen (
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel,
    navigation: NavController
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBarWelcome(homeViewModel = homeViewModel, navigation= navigation )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TitleScreen("Boulder", Modifier.align(Alignment.CenterHorizontally))
                Column(
                    modifier = Modifier
                        .weight(1f) // Ocupa la mitad del espacio disponible
                        .padding(horizontal = 16.dp)
                ) {
                    TitleScreen("Parte1", Modifier.align(Alignment.CenterHorizontally))
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    TitleScreen("Parte2", Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    )
}