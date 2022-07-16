package happy.kiki.happic.module.auth.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @Serializable
    data class SignInReq(val accessToken: String)

    @POST("user/sign")
    suspend fun signIn(@Body req: SignInReq): ApiResponse<Unit>
}

val authApi = createService<AuthService>()