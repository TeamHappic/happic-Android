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
    suspend fun tags(@Query("year") year: Int, @Query("month") month: Int): ApiResponse<List<DailyHappicTagModel>>
}

val dailyHappic: DailyHappicService = createService()

val dailyHappicMockService = if (!BuildConfig.DEBUG) dailyHappic else object : DailyHappicService {
    override suspend fun keywordRankingForUpload(): ApiResponse<KeywordRankingForUploadRes> {
        return successApiResponse(
            KeywordRankingForUploadRes(
                "2022-01-20 20:24",
                listOf(Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5)),
                listOf(Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5)),
                listOf(Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),Ipsum.text(5),)
            )
        )
    }

    override suspend fun photos(year: Int, month: Int): ApiResponse<List<DailyHappicPhotoListModel>> {
        return successApiResponse(
            listOf(
                DailyHappicPhotoListModel("id", "25", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "24", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "23", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "22", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "21", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "20", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "19", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "18", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "17", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "16", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "15", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "14", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "13", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "12", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "11", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "10", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "9", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "8", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "7", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "6", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "5", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "4", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "3", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "2", Picsum.uri(100)),
                DailyHappicPhotoListModel("id", "1", Picsum.uri(100)),
            )
        )
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

    override suspend fun tags(year: Int, month: Int): ApiResponse<List<DailyHappicTagModel>> {
        return successApiResponse(
            listOf(
                DailyHappicTagModel(
                    "id",
                    "2022-01-20 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-19 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-18 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-17 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-16 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-15 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-14 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-13 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-12 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-11 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-10 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-09 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-08 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
                DailyHappicTagModel(
                    "id",
                    "2022-01-07 20:24",
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5),
                    Ipsum.text(5)
                ),
            )
        )
    }
}