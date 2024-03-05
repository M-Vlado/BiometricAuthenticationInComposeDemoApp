package sk.vmproject.biometricauthenticationincompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sk.vmproject.biometricauthenticationincompose.navigation.Screen
import sk.vmproject.biometricauthenticationincompose.navigation.SetupNavGraph
import sk.vmproject.biometricauthenticationincompose.ui.theme.BiometricAuthenticationInComposeTheme

class MainActivity : FragmentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiometricAuthenticationInComposeTheme {
                navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(
                        startDestination = Screen.BiometricScreen.route,
                        navController = navController
                    )
                }
            }
        }
    }
}