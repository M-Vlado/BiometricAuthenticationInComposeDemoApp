package sk.vmproject.biometricauthenticationincompose.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import sk.vmproject.biometricauthenticationincompose.R
import sk.vmproject.biometricauthenticationincompose.domain.use_case.BiometricLoginUseCase
import sk.vmproject.biometricauthenticationincompose.presentation.view_model.BiometricViewModel
import sk.vmproject.biometricauthenticationincompose.ui.dimensions.IconSizing
import sk.vmproject.biometricauthenticationincompose.ui.dimensions.Spacing

@Composable
fun BiometricScreen(
    onSuccessLogIn: () -> Unit
) {
    val viewModel = viewModel<BiometricViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BiometricViewModel(
                    biometricLoginUseCase = BiometricLoginUseCase()
                ) as T
            }
        }
    )

    val context = LocalContext.current
    val biometricLoginState = viewModel.biometricLoginState
    var canGoToNextScreen by remember {
        mutableStateOf(true)
    }

    val launcher = rememberLauncherForActivityResult(BiometricsSettings()) {
        viewModel.initBiometricLogin(
            context = context,
            promptInfoTitle = context.getString(R.string.prompt_info_title),
            promptInfoSubTitle = context.getString(R.string.prompt_info_subtitle),
            promptInfoNegativeButtonText = context.getString(R.string.btn_cancel)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.extraExtraLarge))

            Text(
                text = stringResource(R.string.biometrics_screen_title),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            Text(text = stringResource(R.string.biometrics_screen_subtitle))

            Spacer(modifier = Modifier.height(Spacing.extraExtraLarge))

            Icon(
                painter = painterResource(id = R.drawable.fingerprint_icon),
                contentDescription = stringResource(R.string.finger_print_icon_content_description),
                modifier = Modifier
                    .size(IconSizing.large)

            )
        }

        Button(
            onClick = {
                viewModel.initBiometricLogin(
                    context = context,
                    promptInfoTitle = context.getString(R.string.prompt_info_title),
                    promptInfoSubTitle = context.getString(R.string.prompt_info_subtitle),
                    promptInfoNegativeButtonText = context.getString(R.string.btn_cancel)
                )
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.fingerprint_icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = Spacing.small)
            )

            Text(text = stringResource(id = R.string.btn_login))
        }

        if (biometricLoginState.canNeverEnroll == true) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearBiometricLoginState() }) {
                        Text(text = stringResource(R.string.btn_ok))
                    }
                },
                title = {
                    Text(text = stringResource(R.string.error_failed_to_activate_biometrics_title))
                },
                text = {
                    Text(text = stringResource(R.string.error_failed_to_activate_biometrics_description))
                }
            )
        }

        if (biometricLoginState.canSystemEnroll == true) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearBiometricLoginState()
                            launcher.launch(null)
                        }
                    ) {
                        Text(text = stringResource(R.string.btn_redirect))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.clearBiometricLoginState() }) {
                        Text(text = stringResource(id = R.string.btn_cancel))
                    }
                },
                title = {
                    Text(text = stringResource(R.string.error_biometrics_missing_title))
                },
                text = {
                    Text(text = stringResource(R.string.error_biometrics_missing_description))
                }
            )
        }

        if (biometricLoginState.success == true) {
            if (canGoToNextScreen) {
                onSuccessLogIn()
                canGoToNextScreen = false
            }
        }

        if (biometricLoginState.error == BiometricPrompt.ERROR_LOCKOUT) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearBiometricLoginState() }) {
                        Text(text = stringResource(id = R.string.btn_ok))
                    }
                },
                title = {
                    Text(text = stringResource(R.string.error_biometrics_lockout_title))
                },
                text = {
                    Text(text = stringResource(R.string.error_biometrics_lockout_description))
                }
            )
        }

        if (biometricLoginState.error == BiometricPrompt.ERROR_LOCKOUT_PERMANENT) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearBiometricLoginState() }) {
                        Text(text = stringResource(id = R.string.btn_ok))
                    }
                },
                title = {
                    Text(text = stringResource(R.string.error_biometrics_lockout_title))
                },
                text = {
                    Text(text = stringResource(R.string.error_biometrics_lockout_permanent_description))
                }
            )
        }

    }
}

private class BiometricsSettings : ActivityResultContract<Void?, Void?>() {
    @CallSuper
    override fun createIntent(context: Context, input: Void?): Intent {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                @Suppress("DEPRECATION")
                (Intent(Settings.ACTION_FINGERPRINT_ENROLL))
            } else {
                Intent(Settings.ACTION_SETTINGS)
            }
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Void? {
        return null
    }
}