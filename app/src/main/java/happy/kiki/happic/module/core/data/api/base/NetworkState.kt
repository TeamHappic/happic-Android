package happy.kiki.happic.module.core.data.api.base

sealed interface NetworkState<T> {
    class Idle<T> : NetworkState<T>
    class Loading<T> : NetworkState<T>
    data class Success<T>(val data: T) : NetworkState<T>
    data class Error<T>(val throwable: Throwable? = null) : NetworkState<T>
}
