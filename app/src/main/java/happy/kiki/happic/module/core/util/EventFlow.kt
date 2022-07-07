package happy.kiki.happic.module.core.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

open class EventFlow<T> {
    private val sharedFlow = MutableSharedFlow<T>(0, 0)
    val flow: Flow<T> get() = sharedFlow
    open suspend fun emit(value: T) = sharedFlow.emit(value)
}

class SimpleEventFlow : EventFlow<Unit>() {
    suspend fun emit() = super.emit(Unit)
}

fun <T> ViewModel.emitEvent(eventFlow: EventFlow<T>, value: T) {
    viewModelScope.launch { eventFlow.emit(value) }
}

fun ViewModel.emitEvent(eventFlow: SimpleEventFlow) {
    viewModelScope.launch { eventFlow.emit() }
}

fun <T> LifecycleOwner.emitEvent(eventFlow: EventFlow<T>, value: T) {
    lifecycleScope.launch { eventFlow.emit(value) }
}

fun LifecycleOwner.emitEvent(eventFlow: SimpleEventFlow) {
    lifecycleScope.launch { eventFlow.emit() }
}