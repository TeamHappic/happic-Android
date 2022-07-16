package happy.kiki.happic.module.auth.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.api.AuthService.SignInReq
import happy.kiki.happic.module.auth.data.api.authApi
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.FAIL
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.PENDING
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.SUCCESS
import happy.kiki.happic.module.auth.util.AccessTokenUtil
import happy.kiki.happic.module.core.data.api.base.useApi
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel() {
    val autoSignInResult = MutableStateFlow(PENDING)

    private val signInApi = useApi<String>(onSuccess = {
        autoSignInResult.value = SUCCESS
    }, onError = {
        autoSignInResult.value = FAIL
    }) { accessToken ->
        authApi.signIn(SignInReq(accessToken))
    }

    init {
        tryAutoSignIn()
    }

    private fun tryAutoSignIn() {
        val accessToken = AccessTokenUtil.getAccessToken()
        if (accessToken == null) {
            autoSignInResult.value = FAIL
        } else {
            signInApi.call(accessToken)
        }
    }
}