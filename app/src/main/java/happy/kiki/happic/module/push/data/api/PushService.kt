package happy.kiki.happic.module.push.data.api

import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.PUT

interface PushService {
    @Serializable
    data class RefreshFcmTokenReq(val fcmToken: String)

    @PUT("user/refresh")
    suspend fun refreshFcmToken(@Body req: RefreshFcmTokenReq): NoDataApiResponse

    @Serializable
    data class RegisterFcmTokenReq(val fcmToken: String)

    @PUT("user")
    suspend fun registerFcmToken(@Body req: Serializable): NoDataApiResponse
}

val pushService: PushService = createService()