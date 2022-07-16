package happy.kiki.happic.module.core.data.api.base

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T,
)

typealias NoDataApiResponse = ApiResponse<Nothing?>