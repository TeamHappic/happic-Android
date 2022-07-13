package happy.kiki.happic.module.auth.provider

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object AuthProvider {
    suspend fun signInWithKakao(context: Context) = withContext(Dispatchers.IO) {
        suspendCoroutine<String> { continuation ->
            fun signIn(callback: (token: OAuthToken?, error: Throwable?) -> Unit) {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) UserApiClient.instance.loginWithKakaoTalk(
                    context,
                    callback = callback,
                ) else UserApiClient.instance.loginWithKakaoAccount(
                    context, callback = callback,
                )
            }

            signIn { token, error ->
                token?.accessToken?.run {
                    continuation.resume(this)
                } ?: continuation.resumeWithException(error ?: RuntimeException())
            }
        }
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        suspendCoroutine<Boolean> { continuation ->
            UserApiClient.instance.logout {
                continuation.resume(it == null)
            }
        }
    }
}