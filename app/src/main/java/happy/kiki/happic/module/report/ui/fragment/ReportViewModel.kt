package happy.kiki.happic.module.report.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.TestService
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.util.now
import kotlinx.coroutines.flow.MutableStateFlow

class ReportViewModel : ViewModel() {
    val currentYear = MutableStateFlow(now.year)
    val selectedYearMonth = MutableStateFlow(now.year to now.monthValue)

    val isMonthSelectOpened = MutableStateFlow(false)

    val api = useApi<List<Int>> { TestService.getList() }

    init {
        api.call()
    }
}