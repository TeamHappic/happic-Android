package happy.kiki.happic.module.auth.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.api.AuthService.SignInReq
import happy.kiki.happic.module.auth.data.api.authApi
import happy.kiki.happic.module.core.data.api.base.useApi

class SignInViewModel : ViewModel() {
    val signInApi = useApi<String> {
        authApi.signIn(SignInReq(it))
    }
}