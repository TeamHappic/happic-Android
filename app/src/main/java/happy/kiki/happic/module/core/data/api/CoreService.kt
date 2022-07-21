package happy.kiki.happic.module.core.data.api

import happy.kiki.happic.BuildConfig
import happy.kiki.happic.module.core.data.api.CoreService.UploadPhotoRes
import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.successApiResponse
import happy.kiki.happic.module.core.util.Picsum
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CoreService {

    @Serializable
    data class UploadPhotoRes(
        val id: String, val link: String
    )

    @Multipart
    @POST("file/upload")
    suspend fun uploadPhoto(
        @Part file: MultipartBody.Part
    ): ApiResponse<UploadPhotoRes>
}

val coreService: CoreService = createService()

val coreMockService = if (!BuildConfig.DEBUG) coreService else object : CoreService {
    override suspend fun uploadPhoto(file: MultipartBody.Part): ApiResponse<UploadPhotoRes> {
        return successApiResponse(
            UploadPhotoRes(
                "628ff9e6b54e87592b47c8d9", Picsum.uri(100)
            )
        )
    }

}