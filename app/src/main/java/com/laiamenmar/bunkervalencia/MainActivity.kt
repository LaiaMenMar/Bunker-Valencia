/**
 *MainActivity.kt: Esta clase representa la actividad principal de la aplicación.
 *
 * Autor: Laia Méndez Martínez
 * Función: Inicializa Firebase Analytics y establece el contenido de la aplicación utilizando Jetpack Compose, incluyendo la configuración del tema y la navegación.
 * Fecha de creación: 2024-01-10
 */
package com.laiamenmar.bunkervalencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppNavigation
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme

/**
 * MainActivity: Esta clase representa la actividad principal de la aplicación.
 *
 * Esta actividad inicializa Firebase Analytics y establece el contenido de la aplicación
 * utilizando Jetpack Compose.
 *
 * @property analytics Instancia de Firebase Analytics para rastrear eventos y métricas.
 * @property loginViewModel ViewModel para la pantalla de inicio de sesión.
 * @property homeViewModel ViewModel para la pantalla principal de la aplicación.
 */
class MainActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private val loginViewModel: LoginViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    /**
     * Método onCreate de la MainActivity.
     *
     * Este método se llama cuando la actividad se está iniciando. Aquí es donde se realiza la
     * inicialización de la actividad, como la creación de la interfaz de usuario, la
     * inicialización de componentes y la configuración de escuchadores de eventos.
     *
     * @param savedInstanceState Bundle que contiene el estado de la actividad anteriormente
     * guardado, que puede ser nulo si la actividad se está iniciando por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics

        setContent {
            BunkerValenciaTheme {
                AppNavigation(this, rememberNavController(), loginViewModel, homeViewModel)
            }
        }
    }
}





