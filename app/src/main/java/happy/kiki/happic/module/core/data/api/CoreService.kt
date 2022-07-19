package happy.kiki.happic.module.core.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CoreService {

    @Serializable
    data class UploadPhotoRes(
        @SerialName("_id") val id: String, val link: String
    )

    @Multipart
    @POST("file/upload")
    suspend fun uploadPhoto(
        @Part file: MultipartBody.Part
    ): ApiResponse<UploadPhotoRes>
}

val coreService: CoreService = createService()