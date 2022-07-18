package happy.kiki.happic.module.report.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.util.EventFlow

class ReportNavigationViewModel : ViewModel() {
    val onNavigateDetail = EventFlow<ReportDetailFragment.Argument>()
}