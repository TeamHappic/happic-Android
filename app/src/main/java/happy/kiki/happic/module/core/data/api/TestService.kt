package happy.kiki.happic.module.core.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.ApiServiceFactory.createService
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import retrofit2.http.GET

interface TestService {
    @GET("query")
    suspend fun query(): ApiResponse<List<Int>>

    @GET("query_no_data")
    suspend fun queryNoData(): NoDataApiResponse
}

val testService = createService<TestService>()