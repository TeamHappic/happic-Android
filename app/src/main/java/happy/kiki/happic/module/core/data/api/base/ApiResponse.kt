package happy.kiki.happic.module.core.data.api.base

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T,
)

typealias NoDataApiResponse = ApiResponse<Nothing?>