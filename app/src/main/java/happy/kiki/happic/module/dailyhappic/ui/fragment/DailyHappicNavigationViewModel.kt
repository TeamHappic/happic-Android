package happy.kiki.happic.module.dailyhappic.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.util.SimpleEventFlow

class DailyHappicNavigationViewModel : ViewModel() {
    val onNavigateDetail = SimpleEventFlow()
}