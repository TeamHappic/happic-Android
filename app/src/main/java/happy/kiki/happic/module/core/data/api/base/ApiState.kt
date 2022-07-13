package happy.kiki.happic.module.core.data.api.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class ApiState<TData, TParam>(
    private val coroutineScope: CoroutineScope, private val callback: suspend (params: TParam) -> ApiResponse<TData>
) {
    private val _state = MutableStateFlow<NetworkState<TData>>(NetworkState.Idle())
    val state: StateFlow<NetworkState<TData>> get() = _state
    val isIdle = state.map { it is NetworkState.Idle }.asStateFlow(true)
    val isSuccess = state.map { it is NetworkState.Success }.asStateFlow(false)
    val isLoading = state.map { it is NetworkState.Loading }.asStateFlow(false)
    val isError = state.map { it is NetworkState.Error }.asStateFlow(false)
    val data = state.map {
        if (it !is NetworkState.Success) null
        else it.data
    }.asStateFlow(null)

    fun call(params: TParam) {
        _state.value = NetworkState.Loading()
        coroutineScope.launch {
            try {
                _state.value = NetworkState.Success(callback(params).data)
            } catch (e: Throwable) {
                _state.value = NetworkState.Error(e)
            }
        }
    }

    suspend fun callBlocking(params: TParam): TData {
        _state.value = NetworkState.Loading()
        try {
            val ret = callback(params)
            _state.value = NetworkState.Success(ret.data)
            return ret.data
        } catch (e: Throwable) {
            _state.value = NetworkState.Error(e)
            throw e
        }
    }

    private fun <T> Flow<T>.asStateFlow(initialValue: T) =
        stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), initialValue)
}

class EmptyParamsApiState<TData>(
    coroutineScope: CoroutineScope, private val callback: suspend () -> ApiResponse<TData>
) : ApiState<TData, Unit>(coroutineScope, { callback() }) {
    fun call() {
        super.call(Unit)
    }

    suspend fun callBlocking(): TData {
        return super.callBlocking(Unit)
    }
}

fun <TData, TParam> useApi(
    coroutineScope: CoroutineScope, callback: suspend (params: TParam) -> ApiResponse<TData>
): ApiState<TData, TParam> {
    return ApiState(coroutineScope, callback)
}

fun <TData> useApi(
    coroutineScope: CoroutineScope, callback: suspend () -> ApiResponse<TData>
): EmptyParamsApiState<TData> {
    return EmptyParamsApiState(coroutineScope, callback)
}

fun <TData, TParam> ViewModel.useApi(callback: suspend (params: TParam) -> ApiResponse<TData>) =
    useApi(viewModelScope, callback)

fun <TData> ViewModel.useApi(callback: suspend () -> ApiResponse<TData>) = useApi(viewModelScope, callback)
