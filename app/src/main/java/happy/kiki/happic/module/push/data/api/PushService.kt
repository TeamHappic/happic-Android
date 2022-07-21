package happy.kiki.happic.module.push.data.api

import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface PushService {
    @Serializable
    data class RegisterFcmTokenReq(val fcmToken: String)

    @POST("user/push")
    suspend fun registerFcmToken(@Body req: RegisterFcmTokenReq): NoDataApiResponse
}

val pushService: PushService = createService()