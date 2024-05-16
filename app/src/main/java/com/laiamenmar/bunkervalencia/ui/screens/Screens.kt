/**
 *Screens.kt:  Contiene la función Screen, que define el diseño base para las pantallas de la
 *  aplicación.
 *
 * Autor: Laia Méndez Martínez
 * Función: Proporciona un contenedor de superficie con el tema y el tamaño de pantalla adecuados
 * para el contenido de la aplicación.
 * Fecha de creación: 2024-01-10
 */
package com.laiamenmar.bunkervalencia.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme

/**
 * Define un diseño base para las pantallas de la aplicación.
 *
 * @param content El contenido de la pantalla.
 */
@Composable
fun Screen (content: @Composable () -> Unit) {
    BunkerValenciaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

