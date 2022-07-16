package happy.kiki.happic.module.core.data.api.base

import kotlinx.serialization.Serializable

sealed interface ApiResponseType<T> {
    val status: Int
    val message: String
    val data: T?
}

@Serializable
data class ApiResponse<T>(
    override val status: Int = -1,
    override val message: String = "",
    override val data: T,
) : ApiResponseType<T>

@Serializable
data class NoDataApiResponse(
    override val status: Int = -1, override val message: String = "", override val data: Unit? = null
) : ApiResponseType<Unit>
