package happy.kiki.happic.module.auth.data.api

import happy.kiki.happic.module.characterselect.data.enumerate.CharacterType
import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @Serializable
    data class SignInReq(val accessToken: String)

    @Serializable
    data class SignInRes(val jwtToken: String)

    @POST("user/signin")
    suspend fun signIn(@Body req: SignInReq): ApiResponse<SignInRes>

    @Serializable
    data class SignUpReq(val characterId: CharacterType, val characterName: String, val accessToken: String)

    @Serializable
    data class SignUpRes(val jwtToken: String)

    @POST("user/signup")
    suspend fun signUp(@Body req: SignUpReq): ApiResponse<SignUpRes>
}

val authService: AuthService = createService()
