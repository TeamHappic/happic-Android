package happy.kiki.happic.module.dailyhappic.data.api

import happy.kiki.happic.BuildConfig
import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import happy.kiki.happic.module.core.data.api.base.successApiResponse
import happy.kiki.happic.module.core.util.Ipsum
import happy.kiki.happic.module.core.util.Picsum
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadReq
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.DailyHappicUploadRes
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.IsTodayUploadedRes
import happy.kiki.happic.module.dailyhappic.data.api.DailyHappicService.KeywordRankingForUploadRes
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicModel
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
    suspend fun dailyHappics(
        @Query("year") year: Int, @Query("month") month: Int
    ): ApiResponse<List<DailyHappicModel>>

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

}

val dailyHappic: DailyHappicService = createService()

val dailyHappicMockService = if (!BuildConfig.DEBUG) dailyHappic else object : DailyHappicService {
    override suspend fun keywordRankingForUpload(): ApiResponse<KeywordRankingForUploadRes> {
        return successApiResponse(
            KeywordRankingForUploadRes(
                "2022-01-20 20:24", listOf(
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ), listOf(
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ), listOf( //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                    //                    Ipsum.text(5),
                )
            )
        )
    }

    override suspend fun dailyHappics(year: Int, month: Int): ApiResponse<List<DailyHappicModel>> {
        return successApiResponse(
            listOf(
                DailyHappicModel(
                    "id", 1, Picsum.uri(400), Picsum.uri(100), 2, Ipsum.text(5), Ipsum.text(5), Ipsum.text(5)
                ),
                DailyHappicModel(
                    "id", 3, Picsum.uri(400), Picsum.uri(100), 3, Ipsum.text(5), Ipsum.text(5), Ipsum.text(5)
                ),
                DailyHappicModel(
                    "id", 5, Picsum.uri(400), Picsum.uri(100), 18, Ipsum.text(5), Ipsum.text(5), Ipsum.text(5)
                ),
                DailyHappicModel(
                    "id", 7, Picsum.uri(400), Picsum.uri(100), 4, Ipsum.text(5), Ipsum.text(5), Ipsum.text(5)
                ),
                DailyHappicModel(
                    "id", 22, Picsum.uri(400), Picsum.uri(100), 17, Ipsum.text(5), Ipsum.text(5), Ipsum.text(5)
                ),
            )
        )
    }

    override suspend fun isTodayUploaded(): ApiResponse<IsTodayUploadedRes> {
        return successApiResponse(IsTodayUploadedRes(false))
    }

    override suspend fun upload(req: DailyHappicUploadReq): ApiResponse<DailyHappicUploadRes> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): NoDataApiResponse {
        TODO("Not yet implemented")
    }
}