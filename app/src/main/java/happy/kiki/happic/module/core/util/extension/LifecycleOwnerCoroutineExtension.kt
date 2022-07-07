package happy.kiki.happic.module.core.util.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> AppCompatActivity.collectFlowWhenStarted(
    flow: Flow<T>, crossinline block: suspend CoroutineScope.(T) -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        flow.collect {
            block(it)
        }
    }
}

inline fun <reified T> Fragment.collectFlowWhenStarted(
    flow: Flow<T>, crossinline block: suspend CoroutineScope.(T) -> Unit
) = viewLifecycleOwner.lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        flow.collect {
            block(it)
        }
    }
}
