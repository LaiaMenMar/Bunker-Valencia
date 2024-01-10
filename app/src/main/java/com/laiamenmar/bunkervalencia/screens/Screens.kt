package com.laiamenmar.bunkervalencia.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme
import com.laiamenmar.bunkervalencia.utils.AnalyticsManager

@Composable
fun Screen (content: @Composable () -> Unit) {
    BunkerValenciaTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

