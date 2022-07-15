package happy.kiki.happic.module.core.data.api.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
    private val coroutineScope: CoroutineScope,
    private val onSuccess: (TData) -> Unit = {},
    private val onError: (t: Throwable) -> Unit = {},
    private val callback: suspend (params: TParam) -> ApiResponse<TData>
) {
    private val _state = MutableStateFlow<NetworkState<TData>>(NetworkState.Idle())
    val state: StateFlow<NetworkState<TData>> get() = _state
    val isIdle = state.map { it is NetworkState.Idle }.asStateFlow(true)
    val isSuccess = state.map { it is NetworkState.Success }.asStateFlow(false)
    val isLoading = state.map { it is NetworkState.Loading }.asStateFlow(false)
    val isFetching = state.map { it is NetworkState.Loading || it is NetworkState.Fetching }.asStateFlow(false)
    val isFail = state.map { it is NetworkState.Failure }.asStateFlow(false)
    val data = state.map {
        if (it !is NetworkState.Success) null
        else it.data
    }.asStateFlow(null)

    suspend fun callBlocking(params: TParam): TData {
        if (isSuccess.value) _state.value = NetworkState.Fetching()
        else _state.value = NetworkState.Loading()

        try {
            val ret = callback(params)
            _state.value = NetworkState.Success(ret.data)
            onSuccess(ret.data)
            return ret.data
        } catch (e: Throwable) {
            _state.value = NetworkState.Failure(e)
            onError(e)
            throw e
        }
    }

    fun call(params: TParam) {
        coroutineScope.launch {
            kotlin.runCatching {
                callBlocking(params)
            }
        }
    }

    private fun <T> Flow<T>.asStateFlow(initialValue: T) =
        stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), initialValue)
}

class EmptyParamsApiState<TData>(
    coroutineScope: CoroutineScope,
    onSuccess: (TData) -> Unit = {},
    onError: (t: Throwable) -> Unit = {},
    private val callback: suspend () -> ApiResponse<TData>
) : ApiState<TData, Unit>(coroutineScope, onSuccess, onError, { callback() }) {
    fun call() {
        super.call(Unit)
    }

    suspend fun callBlocking(): TData {
        return super.callBlocking(Unit)
    }
}

fun <TData, TParam> useApi(
    coroutineScope: CoroutineScope,
    onSuccess: (TData) -> Unit = {},
    onError: (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponse<TData>
): ApiState<TData, TParam> {
    return ApiState(coroutineScope, onSuccess, onError, callback)
}

fun <TData> useApi(
    coroutineScope: CoroutineScope,
    onSuccess: (TData) -> Unit = {},
    onError: (t: Throwable) -> Unit = {},
    callback: suspend () -> ApiResponse<TData>
): EmptyParamsApiState<TData> {
    return EmptyParamsApiState(coroutineScope, onSuccess, onError, callback)
}

fun <TData, TParam> ViewModel.useApi(
    onSuccess: (TData) -> Unit = {},
    onError: (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponse<TData>
) = useApi(viewModelScope, onSuccess, onError, callback)

fun <TData> ViewModel.useApi(
    onSuccess: (TData) -> Unit = {}, onError: (t: Throwable) -> Unit = {}, callback: suspend () -> ApiResponse<TData>
) = useApi(viewModelScope, onSuccess, onError, callback)

fun <TData, TParam> AppCompatActivity.useApi(
    onSuccess: (TData) -> Unit = {},
    onError: (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponse<TData>
) = useApi(lifecycleScope, onSuccess, onError, callback)

fun <TData> AppCompatActivity.useApi(
    onSuccess: (TData) -> Unit = {}, onError: (t: Throwable) -> Unit = {}, callback: suspend () -> ApiResponse<TData>
) = useApi(lifecycleScope, onSuccess, onError, callback)

fun <TData, TParam> Fragment.useApi(
    onSuccess: (TData) -> Unit = {},
    onError: (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponse<TData>
) = useApi(viewLifecycleOwner.lifecycleScope, onSuccess, onError, callback)

fun <TData> Fragment.useApi(
    onSuccess: (TData) -> Unit = {}, onError: (t: Throwable) -> Unit = {}, callback: suspend () -> ApiResponse<TData>
) = useApi(viewLifecycleOwner.lifecycleScope, onSuccess, onError, callback)