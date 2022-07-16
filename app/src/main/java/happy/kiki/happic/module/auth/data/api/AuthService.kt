package happy.kiki.happic.module.auth.data.api

import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @Serializable
    data class SignInReq(val accessToken: String)

    @POST("user/sign")
    suspend fun signIn(@Body req: SignInReq): NoDataApiResponse
}

val authApi = createService<AuthService>()