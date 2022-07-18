package happy.kiki.happic.module.main.ui.activity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {
    val tabIndex = MutableStateFlow(0)
}