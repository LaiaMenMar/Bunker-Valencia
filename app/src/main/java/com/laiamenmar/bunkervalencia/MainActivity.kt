package com.laiamenmar.bunkervalencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.laiamenmar.bunkervalencia.navigation.AppNavigation
import com.laiamenmar.bunkervalencia.screens.LoginScreen
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme

class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        setContent {
            BunkerValenciaTheme {
                AppNavigation(this)
            }
        }
    }
}
/**

@Preview()
@Composable
fun DefautlPreview(){
    BunkerValenciaTheme {
        AppNavigation()
    }
}*/
