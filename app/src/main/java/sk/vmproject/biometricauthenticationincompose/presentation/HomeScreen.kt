package sk.vmproject.biometricauthenticationincompose.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import sk.vmproject.biometricauthenticationincompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    logOut: () -> Unit
) {

    BackHandler {
        logOut()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { stringResource(id = R.string.home_screen_title) },
                actions = {
                    IconButton(
                        onClick = { logOut() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.logout_icon),
                            contentDescription = stringResource(R.string.logout_icon_content_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(text = stringResource(id = R.string.home_screen_title))
        }
    }
}