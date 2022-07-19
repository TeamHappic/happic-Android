package happy.kiki.happic.module.dailyhappic.data.api

import happy.kiki.happic.BuildConfig
import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import happy.kiki.happic.module.core.data.api.base.successApiResponse
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.IsTodayUploadedRes
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.KeywordRankingForUploadRes
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadReq
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadRes
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
    data class DailyHappicUploadReq(
        val photo: String, @SerialName("when") val when1: String, val `where`: String, val who: String, val what: String
    )

    @Serializable
    data class DailyHappicUploadRes(
        val id: String
    )

    @POST("daily")
    suspend fun upload(@Body req: DailyHappicUploadReq): ApiResponse<DailyHappicUploadRes>

    @DELETE("daily/{id}")
    suspend fun delete(@Path("id") id: Int): NoDataApiResponse

    @GET("daily/title")
    suspend fun tags(@Query("year") year: Int, @Query("month") month: Int): ApiResponse<DailyHappicTagModel>
}

val dailyHappic: DailyHappicService = createService()

val dailyHappicKeywordMockService = if (!BuildConfig.DEBUG) dailyHappic else object : DailyHappicService {
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

    override suspend fun photos(year: Int, month: Int): ApiResponse<List<DailyHappicPhotoListModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun photo(id: Int): ApiResponse<DailyHappicPhotoModel> {
        TODO("Not yet implemented")
    }

    override suspend fun isTodayUploaded(): ApiResponse<IsTodayUploadedRes> {
        TODO("Not yet implemented")
    }

    override suspend fun upload(req: DailyHappicUploadReq): ApiResponse<DailyHappicUploadRes> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): NoDataApiResponse {
        TODO("Not yet implemented")
    }

    override suspend fun tags(year: Int, month: Int): ApiResponse<DailyHappicTagModel> {
        TODO("Not yet implemented")
    }
}