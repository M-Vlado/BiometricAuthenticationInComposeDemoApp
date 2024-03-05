package sk.vmproject.biometricauthenticationincompose.navigation

sealed class Screen(val route: String) {
    data object BiometricScreen : Screen(route = "biometric_screen")
    data object HomeScreen : Screen(route = "home_screen")
}