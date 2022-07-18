package happy.kiki.happic.module.report.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.report.data.api.reportMockService
import happy.kiki.happic.module.report.data.enumerate.ReportCategoryOption
import happy.kiki.happic.module.report.data.model.ReportByCategoryModel
import happy.kiki.happic.module.report.data.model.ReportByKeywordModel
import happy.kiki.happic.module.report.data.model.ReportByMonthlyModel
import kotlinx.coroutines.flow.MutableStateFlow

class ReportDetailViewModel(initialTabIndex: Int) : ViewModel() {
    val tabIndex = MutableStateFlow(initialTabIndex)

    val keywordApi = useApi<Pair<Int, Int>, List<ReportByKeywordModel>> { (year, month) ->
        reportMockService.reportByKeyword(year, month)
    }

    val categoryApi =
        useApi<Triple<Int, Int, ReportCategoryOption>, List<ReportByCategoryModel>> { (year, month, category) ->
            reportMockService.reportByCategory(year, month, category)
        }

    val monthlyApi = useApi<Pair<Int, Int>, ReportByMonthlyModel> { (year, month) ->
        reportMockService.reportByMonthly(year, month)
    }

    fun callApi(year: Int, month: Int, category: ReportCategoryOption) {
        keywordApi.call(year to month)
        categoryApi.call(Triple(year, month, category))
        monthlyApi.call(year to month)
    }
}