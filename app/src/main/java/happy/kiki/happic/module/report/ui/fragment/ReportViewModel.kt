package happy.kiki.happic.module.report.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.util.extension.collectFlow
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.report.data.api.reportService
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.model.ReportHomeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop

class ReportViewModel : ViewModel() {
    val currentYear = MutableStateFlow(now.year)
    val selectedYearMonth = MutableStateFlow(now.year to now.monthValue)

    val isMonthSelectOpened = MutableStateFlow(false)
    val selectedCategoryTab = MutableStateFlow(ReportCategoryOption.who)

    val reportHomeApi = useApi<Pair<Int, Int>, ReportHomeModel> { (year, month) ->
        reportService.reportHome(year, month)
    }

    init {
        collectFlow(selectedYearMonth.drop(1)) {
            reportHomeApi.call(it)
        }
    }

    fun fetchReportHome() {
        reportHomeApi.call(selectedYearMonth.value)
    }
}