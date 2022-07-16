package happy.kiki.happic.module.core.util.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> AppCompatActivity.collectFlowWhen(
    flow: Flow<T>, state: Lifecycle.State, crossinline block: suspend (T) -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(state) {
        flow.collect {
            block(it)
        }
    }
}

inline fun <reified T> AppCompatActivity.collectFlowWhenStarted(
    flow: Flow<T>, crossinline block: suspend (T) -> Unit
) = collectFlowWhen(flow, Lifecycle.State.STARTED, block)

inline fun <reified T> Fragment.collectFlowWhen(
    flow: Flow<T>, state: Lifecycle.State, crossinline block: suspend (T) -> Unit
) = viewLifecycleOwner.lifecycleScope.launch {
    repeatOnLifecycle(state) {
        flow.collect {
            block(it)
        }
    }
}

inline fun <reified T> Fragment.collectFlowWhenStarted(
    flow: Flow<T>, crossinline block: suspend (T) -> Unit
) = collectFlowWhen(flow, Lifecycle.State.STARTED, block)

inline fun <reified T> ViewModel.collectFlow(
    flow: Flow<T>, crossinline block: suspend (T) -> Unit
) = viewModelScope.launch {
    flow.collect {
        block(it)
    }
}
