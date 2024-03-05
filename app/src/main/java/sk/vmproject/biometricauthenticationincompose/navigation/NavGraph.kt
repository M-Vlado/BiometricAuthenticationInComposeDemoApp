package sk.vmproject.biometricauthenticationincompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sk.vmproject.biometricauthenticationincompose.presentation.BiometricScreen
import sk.vmproject.biometricauthenticationincompose.presentation.HomeScreen

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.BiometricScreen.route
        ) {
            BiometricScreen(
                onSuccessLogIn = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.BiometricScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(
                logOut = {
                    navController.navigate(Screen.BiometricScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}