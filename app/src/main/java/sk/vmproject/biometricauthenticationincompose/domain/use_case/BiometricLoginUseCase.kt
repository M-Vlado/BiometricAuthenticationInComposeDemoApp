package sk.vmproject.biometricauthenticationincompose.domain.use_case

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.fragment.app.FragmentActivity

class BiometricLoginUseCase {

    private lateinit var biometricManager: BiometricManager
    private lateinit var promptInfo: PromptInfo
    private lateinit var biometricPrompt: BiometricPrompt

    operator fun invoke(
        context: Context,
        promptInfoTitle: String,
        promptInfoSubTitle: String,
        promptInfoNegativeButtonText: String,
        onResult: (BiometricResult) -> Unit
    ) {
        biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricPrompt = BiometricPrompt(
                    context as FragmentActivity,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            onResult(
                                BiometricResult(
                                    success = true
                                )
                            )
                        }

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            onResult(
                                BiometricResult(
                                    error = errorCode
                                )
                            )
                        }
                    }
                )
                promptInfo = PromptInfo.Builder()
                    .setTitle(promptInfoTitle)
                    .setDescription(promptInfoSubTitle)
                    .setNegativeButtonText(promptInfoNegativeButtonText)
                    .build()
                biometricPrompt.authenticate(promptInfo)
            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onResult(BiometricResult(canNeverEnroll = true))
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onResult(
                    BiometricResult(
                        canSystemEnroll = true
                    )
                )
            }
        }
    }
}


data class BiometricResult(
    val canSystemEnroll: Boolean? = null,
    val canNeverEnroll: Boolean? = null,
    val success: Boolean? = null,
    val error: Int? = null,
)