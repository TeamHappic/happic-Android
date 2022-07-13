package happy.kiki.happic.module.core.data.api.base

data class ResponseWrapper<T : Any>(
    val status: Int,
    val message: String,
    val data: T,
)