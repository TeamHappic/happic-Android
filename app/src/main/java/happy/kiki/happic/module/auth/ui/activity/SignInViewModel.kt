package happy.kiki.happic.module.auth.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.api.AuthService.SignInReq
import happy.kiki.happic.module.auth.data.api.AuthService.SignInRes
import happy.kiki.happic.module.auth.data.api.authService
import happy.kiki.happic.module.auth.util.JwtUtil
import happy.kiki.happic.module.characterselect.provider.CharacterSelectFlowProvider
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.util.SimpleEventFlow

class SignInViewModel : ViewModel() {
    val onSignInSuccess = SimpleEventFlow()
    val onSignInFail = SimpleEventFlow()

    val signInApi = useApi<String, SignInRes>(onSuccess = {
        JwtUtil.save(it.jwtToken)
        onSignInSuccess.emit()
    }, onError = {
        onSignInFail.emit()
    }) { accessToken ->
        CharacterSelectFlowProvider.initForSignUp(accessToken)
        authService.signIn(SignInReq(accessToken))
    }
}