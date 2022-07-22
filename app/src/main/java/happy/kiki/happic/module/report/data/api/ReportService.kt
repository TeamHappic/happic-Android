package happy.kiki.happic.module.report.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.model.ReportByCategoryModel
import happy.kiki.happic.module.report.data.model.ReportByKeywordModel
import happy.kiki.happic.module.report.data.model.ReportByMonthlyModel
import happy.kiki.happic.module.report.data.model.ReportHomeModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {
    @GET("mypage")
    suspend fun reportHome(
        @Query("year") year: Int, @Query("month") month: Int,
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