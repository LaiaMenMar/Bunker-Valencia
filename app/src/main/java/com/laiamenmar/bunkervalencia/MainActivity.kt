package com.laiamenmar.bunkervalencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.laiamenmar.bunkervalencia.ui.LoginViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppNavigation
import com.laiamenmar.bunkervalencia.ui.theme.BunkerValenciaTheme

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()


    private lateinit var analytics: FirebaseAnalytics
   /*Inject lateinit var analyticsManager: AnalyticsManager
    @Inject lateinit var authManager: AuthManager*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        setContent {
            BunkerValenciaTheme {
                //AppNavigation(this,  rememberNavController(),  loginViewModel)
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
