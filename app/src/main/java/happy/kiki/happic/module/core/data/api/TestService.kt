package happy.kiki.happic.module.core.data.api

import happy.kiki.happic.module.core.data.api.base.ApiResponse
import kotlinx.coroutines.delay

object TestService {
    suspend fun getList(): ApiResponse<List<Int>> {
        delay(1000)
        return ApiResponse(200, "", listOf(1, 2, 3))
    }
}