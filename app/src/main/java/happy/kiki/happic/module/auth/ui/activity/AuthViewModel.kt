package happy.kiki.happic.module.auth.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.api.AuthService.SignInReq
import happy.kiki.happic.module.auth.data.api.AuthService.SignInRes
import happy.kiki.happic.module.auth.data.api.authService
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.FAIL
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.PENDING
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.SUCCESS
import happy.kiki.happic.module.auth.util.JwtUtil
import happy.kiki.happic.module.core.data.api.base.useApi
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel() {
    val autoSignInResult = MutableStateFlow(PENDING)

    private val signInApi = useApi<String, SignInRes>(onSuccess = {
        JwtUtil.save(it.jwtToken)
        autoSignInResult.value = SUCCESS
    }, onError = {
        autoSignInResult.value = FAIL
    }) { accessToken ->
        authService.signIn(SignInReq(accessToken))
    }

    init {
        tryAutoSignIn()
    }

    private fun tryAutoSignIn() {
        val accessToken = JwtUtil.load()
        if (accessToken == null) {
            autoSignInResult.value = FAIL
        } else {
            signInApi.call(accessToken)
        }
    }
}