package happy.kiki.happic.module.auth.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.api.AuthService.SignInReq
import happy.kiki.happic.module.auth.data.api.authService
import happy.kiki.happic.module.core.data.api.base.useApi

class SignInViewModel : ViewModel() {
    val signInApi = useApi<String> {
        authService.signIn(SignInReq(it))
    }
}