package happy.kiki.happic.module.report.data.api

import happy.kiki.happic.BuildConfig
import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.successApiResponse
import happy.kiki.happic.module.core.util.Ipsum
import happy.kiki.happic.module.core.util.Picsum
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.hour
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.what
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.where
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption.who
import happy.kiki.happic.module.report.data.model.ReportByCategoryModel
import happy.kiki.happic.module.report.data.model.ReportByKeywordModel
import happy.kiki.happic.module.report.data.model.ReportByMonthlyModel
import happy.kiki.happic.module.report.data.model.ReportHomeModel
import happy.kiki.happic.module.report.data.model.ReportHomeModel.Rank2
import happy.kiki.happic.module.report.data.model.ReportHomeModel.Rank3
import happy.kiki.happic.module.report.data.model.ReportHomeModel.Rank4
import kotlinx.coroutines.delay
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.random.Random

interface ReportService {
    @GET("mypage")
    suspend fun reportHome(
        @Query("year") year: Int, @Query("month") month: Int, @Query("option") option: ReportCategoryOption
    ): ApiResponse<ReportHomeModel>

    @GET("mypage/keyword")
    suspend fun reportByKeyword(
        @Query("year") year: Int, @Query("month") month: Int
    ): ApiResponse<List<ReportByKeywordModel>>

    @GET("mypage/category")
    suspend fun reportByCategory(
        @Query("year") year: Int, @Query("month") month: Int, @Query("option") option: ReportCategoryOption
    ): ApiResponse<List<ReportByCategoryModel>>

    @GET("mypage/monthly")
    suspend fun reportByMonthly(
        @Query("year") year: Int, @Query("month") month: Int
    ): ApiResponse<ReportByMonthlyModel>
}

val reportService: ReportService = createService()
val reportMockService = if (!BuildConfig.DEBUG) reportService else object : ReportService {
    override suspend fun reportHome(year: Int, month: Int, option: ReportCategoryOption): ApiResponse<ReportHomeModel> {
        delay(2000)
        return successApiResponse(
            ReportHomeModel(
                listOf(), listOf(
                    Rank2(Ipsum.text(5), who, Random.nextInt(0, 31)),
                    Rank2(Ipsum.text(5), where, Random.nextInt(0, 31)),
                    Rank2(Ipsum.text(5), what, Random.nextInt(0, 31)),
                    Rank2("19:00", hour, Random.nextInt(0, 31)),
                ), listOf(
                    Rank3(
                        Ipsum.text(5), listOf(Picsum.uri(100), Picsum.uri(98), Picsum.uri(99)), Random.nextInt(0, 31)
                    ),
                    Rank3(Ipsum.text(5), listOf(), Random.nextInt(0, 31)),
                    Rank3(Ipsum.text(5), listOf(), Random.nextInt(0, 31)),
                    Rank3("19:00", listOf(), Random.nextInt(0, 31)),
                ), Rank4(month, Random.nextInt(0, 31))
            )
        )
    }

    override suspend fun reportByKeyword(year: Int, month: Int): ApiResponse<List<ReportByKeywordModel>> {
        delay(2000)
        return successApiResponse(
            listOf(
                ReportByKeywordModel("햄식달식이", who, 20),
                ReportByKeywordModel("게임", where, 20),
                ReportByKeywordModel("귀여워", what, 20),
                ReportByKeywordModel("19:00", hour, 20),
            )
        )
    }

    override suspend fun reportByCategory(
        year: Int, month: Int, option: ReportCategoryOption
    ): ApiResponse<List<ReportByCategoryModel>> {
        delay(2000)
        return successApiResponse(
            listOf(
                ReportByCategoryModel(
                    Ipsum.text(5), listOf(
                        Picsum.uri(100),
                        Picsum.uri(100),
                        Picsum.uri(100),
                    ), 5
                ),
                ReportByCategoryModel(
                    Ipsum.text(5), listOf(
                    ), 5
                ),
                ReportByCategoryModel(
                    Ipsum.text(5), listOf(
                    ), 5
                ),
                ReportByCategoryModel(
                    Ipsum.text(5), listOf(
                    ), 5
                ),
            )
        )
    }

    override suspend fun reportByMonthly(year: Int, month: Int): ApiResponse<ReportByMonthlyModel> {
        delay(2000)
        return successApiResponse(ReportByMonthlyModel(month, Random.nextInt(0, 30), listOf(1, 3, 5)))
    }
}