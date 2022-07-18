package happy.kiki.happic.module.auth.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.api.AuthService.SignInReq
import happy.kiki.happic.module.auth.data.api.authService
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.util.SimpleEventFlow

class SignInViewModel : ViewModel() {
    val onSignInSuccess = SimpleEventFlow()
    val onSignInFail = SimpleEventFlow()

    val signInApi = useApi<String>(onSuccess = {
        onSignInSuccess.emit()
    }, onError = {
        onSignInFail.emit()
    }) { accessToken ->
        authService.signIn(SignInReq(accessToken))
    }
}