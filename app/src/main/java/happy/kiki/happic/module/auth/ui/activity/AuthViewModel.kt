package happy.kiki.happic.module.auth.ui.activity

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.FAIL
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.PENDING
import happy.kiki.happic.module.auth.data.enumerate.AutoSignInResult.SUCCESS
import happy.kiki.happic.module.auth.util.JwtUtil
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel() {
    val autoSignInResult = MutableStateFlow(PENDING)

    init {
        tryAutoSignIn()
    }

    private fun tryAutoSignIn() {
        val accessToken = JwtUtil.load()
        if (accessToken == null) {
            autoSignInResult.value = FAIL
        } else {
            autoSignInResult.value = SUCCESS
        }
    }
}