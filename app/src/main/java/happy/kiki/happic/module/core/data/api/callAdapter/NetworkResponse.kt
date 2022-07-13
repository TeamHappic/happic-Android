package happy.kiki.happic.module.core.data.api.callAdapter

sealed interface NetworkResponse<T> {
    data class Success<T>(
        val data: T
    ) : NetworkResponse<T>

    sealed interface Error<T> : NetworkResponse<T>

    abstract class LogicalError<T> : Error<T>
    sealed interface UnHandledError<T> : Error<T>

    class ServerError<T> : UnHandledError<T>
    class JsonParsingError<T> : UnHandledError<T>
    class NetworkError<T> : UnHandledError<T>
    class UnknownError<T> : UnHandledError<T>
}