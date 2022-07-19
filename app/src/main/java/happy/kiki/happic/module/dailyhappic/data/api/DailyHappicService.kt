package happy.kiki.happic.module.dailyhappic.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicPhotoListModel
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicPhotoModel
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicTagModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DailyHappicService {
    @GET("daily")
    suspend fun photos(
        @Query("year") year: Int, @Query("month") month: Int
    ): ApiResponse<List<DailyHappicPhotoListModel>>

    @GET("daily/{id}")
    suspend fun photo(@Path("id") id: Int): ApiResponse<DailyHappicPhotoModel>

    @Serializable
    data class IsTodayUploadedRes(
        val isPosted: Boolean
    )

    @GET("daily/posted")
    suspend fun isTodayUploaded(): ApiResponse<IsTodayUploadedRes>

    @Serializable
    data class KeywordRankingForUploadRes(
        val currentDate: String, val where: List<String>, val who: List<String>, val what: List<String>
    )

    @GET("daily/keyword")
    suspend fun keywordRankingForUpload(): ApiResponse<KeywordRankingForUploadRes>

    @Serializable
    data class TodayHappicUploadReq(
        val photo: String, @SerialName("when") val when1: String, val `where`: String, val who: String, val what: String
    )

    @Serializable
    data class TodayHappicUploadRes(
        val id: String
    )

    @POST("daily")
    suspend fun upload(@Body req: TodayHappicUploadReq): ApiResponse<TodayHappicUploadRes>

    @DELETE("daily/{id}")
    suspend fun delete(@Path("id") id: Int): NoDataApiResponse

    @GET("daily/title")
    suspend fun tags(@Query("year") year: Int, @Query("month") month: Int): ApiResponse<DailyHappicTagModel>
}

val todayHappicService: DailyHappicService = createService()