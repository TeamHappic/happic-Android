package happy.kiki.happic.module.core.data.api.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import happy.kiki.happic.module.core.util.debugE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class ApiState<TData, TParam>(
    private val coroutineScope: CoroutineScope,
    private val onSuccess: suspend (TData) -> Unit = {},
    private val onError: suspend (t: Throwable) -> Unit = {},
    private val callback: suspend (params: TParam) -> ApiResponseType<TData>
) {
    private val _state = MutableStateFlow<NetworkState<TData>>(NetworkState.Idle())

    val state: StateFlow<NetworkState<TData>> get() = _state
    val data = MutableStateFlow<TData?>(null)

    val isIdle = state.map { it is NetworkState.Idle }.asStateFlow(true)
    val isSuccess = state.map { it is NetworkState.Success }.asStateFlow(false)
    val isLoading = state.map { it is NetworkState.Loading }.asStateFlow(false)
    val isFail = state.map { it is NetworkState.Failure }.asStateFlow(false)

    private var latestCallId = 0L
    private fun getNextCallId(): Long {
        latestCallId += 1
        return latestCallId
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun callBlocking(params: TParam): TData {
        val thisCallId = getNextCallId()

        _state.value = NetworkState.Loading()

        fun isLatestCall() = thisCallId == latestCallId

        return try {
            val response = withContext(Dispatchers.IO) { callback(params) }
            if (isLatestCall()) {
                when (response) {
                    is ApiResponse<TData> -> {
                        _state.value = NetworkState.Success(response.data)
                        this@ApiState.data.value = response.data
                        onSuccess(response.data)
                    }
                    is NoDataApiResponse -> {
                        _state.value = NetworkState.Success(Unit as TData)
                        onSuccess(Unit as TData)
                    }
                }
            }
            response.data as TData
        } catch (e: Throwable) {
            debugE(e)
            if (isLatestCall()) {
                _state.value = NetworkState.Failure(e)
                onError(e)
            }
            throw e
        }
    }

    fun call(params: TParam) = coroutineScope.launch {
        kotlin.runCatching {
            callBlocking(params)
        }
    }

    private fun <T> Flow<T>.asStateFlow(initialValue: T) =
        stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), initialValue)
}

class NoParamsApiState<TData>(
    coroutineScope: CoroutineScope,
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    private val callback: suspend () -> ApiResponseType<TData>
) : ApiState<TData, Unit>(coroutineScope, onSuccess, onError, { callback() }) {
    fun call() {
        super.call(Unit)
    }

    suspend fun callBlocking(): TData {
        return super.callBlocking(Unit)
    }
}

fun <TParam, TData> useApi(
    coroutineScope: CoroutineScope,
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponseType<TData>
): ApiState<TData, TParam> {
    return ApiState(coroutineScope, onSuccess, onError, callback)
}

fun <TData> useApiNoParams(
    coroutineScope: CoroutineScope,
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend () -> ApiResponseType<TData>
): NoParamsApiState<TData> {
    return NoParamsApiState(coroutineScope, onSuccess, onError, callback)
}

fun <TParam, TData> ViewModel.useApi(
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponseType<TData>
) = useApi(viewModelScope, onSuccess, onError, callback)

fun <TParam> ViewModel.useApi(
    onSuccess: suspend () -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> NoDataApiResponse
) = useApi(viewModelScope, { onSuccess() }, onError, callback)

fun <TData> ViewModel.useApiNoParams(
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend () -> ApiResponseType<TData>
) = useApiNoParams(viewModelScope, onSuccess, onError, callback)

fun <TParam, TData> AppCompatActivity.useApi(
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponseType<TData>
) = useApi(lifecycleScope, onSuccess, onError, callback)

fun <TParam> AppCompatActivity.useApi(
    onSuccess: suspend () -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> NoDataApiResponse
) = useApi(lifecycleScope, { onSuccess() }, onError, callback)

fun <TData> AppCompatActivity.useApiNoParams(
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend () -> ApiResponseType<TData>
) = useApiNoParams(lifecycleScope, onSuccess, onError, callback)

fun <TParam, TData> Fragment.useApi(
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> ApiResponseType<TData>
) = useApi(viewLifecycleOwner.lifecycleScope, onSuccess, onError, callback)

fun <TParam> Fragment.useApi(
    onSuccess: suspend () -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend (params: TParam) -> NoDataApiResponse
) = useApi(viewLifecycleOwner.lifecycleScope, { onSuccess() }, onError, callback)

fun <TData> Fragment.useApiNoParams(
    onSuccess: suspend (TData) -> Unit = {},
    onError: suspend (t: Throwable) -> Unit = {},
    callback: suspend () -> ApiResponseType<TData>
) = useApiNoParams(viewLifecycleOwner.lifecycleScope, onSuccess, onError, callback)