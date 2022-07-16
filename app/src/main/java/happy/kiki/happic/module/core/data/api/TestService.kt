package happy.kiki.happic.module.core.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import happy.kiki.happic.module.core.data.api.base.NoDataApiResponse
import kotlinx.coroutines.delay
import kotlin.math.roundToLong

object TestService {
    var a = 1
    suspend fun itemWithoutParameter(): ApiResponse<List<Int>> {
        delay((Math.random() * 5000 + 1000).roundToLong())
        a += 1
        return ApiResponse(200, "", List(a) { it })
    }

    suspend fun itemWithParameter(params: Int): ApiResponse<List<Int>> {
        delay((Math.random() * 5000 + 1000).roundToLong())
        return ApiResponse(200, "", List(params) { it })
    }

    suspend fun nullReturn(): NoDataApiResponse {
        return ApiResponse(200, "", null)
    }
}