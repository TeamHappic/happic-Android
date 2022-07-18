package happy.kiki.happic.module.todayhappic.data.api

import happy.kiki.happic.BuildConfig
import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import happy.kiki.happic.module.core.data.api.base.successApiResponse
import happy.kiki.happic.module.todayhappic.data.api.TodayHappicService.KeywordRankingForUploadRes
import happy.kiki.happic.module.todayhappic.data.model.TodayHappicPhotoListModel
import happy.kiki.happic.module.todayhappic.data.model.TodayHappicPhotoModel
import happy.kiki.happic.module.todayhappic.data.model.TodayHappicTagModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TodayHappicService {
    @GET("daily")
    suspend fun photos(
        @Query("year") year: Int, @Query("month") month: Int
    ): ApiResponse<List<TodayHappicPhotoListModel>>

    @GET("daily/{id}")
    suspend fun photo(@Path("id") id: Int): ApiResponse<TodayHappicPhotoModel>

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
    suspend fun tags(@Query("year") year: Int, @Query("month") month: Int): ApiResponse<TodayHappicTagModel>
}

val todayHappicService: TodayHappicService = createService()

val todayHappicKeywordMockService = if (!BuildConfig.DEBUG) todayHappicService else object : TodayHappicService {
    override suspend fun keywordRankingForUpload(): ApiResponse<KeywordRankingForUploadRes> {
        return successApiResponse(
            KeywordRankingForUploadRes(
                "2022-01-20 20:24",
                listOf("연남", "연남", "연남", "연남", "연남", "연남", "연남", "연남", "연남"),
                listOf("송경", "송경", "송경", "송경", "송경", "송경", "송경", "송경"),
                listOf("귀여워", "귀여워", "귀여워", "귀여워", "귀여워", "귀여워", "귀여워", "귀여워", "귀여워")
            )
        )
    }
}