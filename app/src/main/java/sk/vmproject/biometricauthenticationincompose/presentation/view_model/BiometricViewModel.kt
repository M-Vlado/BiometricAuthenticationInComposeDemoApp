package sk.vmproject.biometricauthenticationincompose.presentation.view_model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import sk.vmproject.biometricauthenticationincompose.domain.use_case.BiometricLoginUseCase
import sk.vmproject.biometricauthenticationincompose.domain.use_case.BiometricResult

class BiometricViewModel(
    private val biometricLoginUseCase: BiometricLoginUseCase,
) : ViewModel() {

    var biometricLoginState by mutableStateOf(BiometricResult())
        private set

    fun initBiometricLogin(
        context: Context,
        promptInfoTitle: String,
        promptInfoSubTitle: String,
        promptInfoNegativeButtonText: String,
    ) {
        biometricLoginUseCase.invoke(
            context = context,
            promptInfoTitle = promptInfoTitle,
            promptInfoSubTitle = promptInfoSubTitle,
            promptInfoNegativeButtonText = promptInfoNegativeButtonText,
            onResult = {
                biometricLoginState = it
            }
        )
    }

    fun clearBiometricLoginState() {
        biometricLoginState = BiometricResult()
    }
}