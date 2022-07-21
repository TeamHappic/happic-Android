package happy.kiki.happic.module.dailyhappic.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.core.util.SimpleEventFlow
import happy.kiki.happic.module.core.util.extension.collectFlow
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicMockService
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicModel
import kotlinx.coroutines.flow.MutableStateFlow

class DailyHappicViewModel : ViewModel() {
    val currentYear = MutableStateFlow(now.year)
    val selectedYearMonth = MutableStateFlow(now.year to now.monthValue)
    val isMonthSelectOpened = MutableStateFlow(false)

    val dailyHappicApi = useApi<Pair<Int, Int>, List<DailyHappicModel>> { (year, month) ->
        dailyHappicMockService.dailyHappics(year, month)
    }

    val onNavigateUpload = SimpleEventFlow()
    val onNavigateUploadFailedByMultipleUpload = SimpleEventFlow()

    val navigateUploadApi = useApiNoParams(onSuccess = {
        if (!it.isPosted) onNavigateUpload.emit()
        else onNavigateUploadFailedByMultipleUpload.emit()
    }) {
        dailyHappicMockService.isTodayUploaded()
    }

    init {
        collectFlow(selectedYearMonth) { (year, month) -> dailyHappicApi.call(Pair(year, month)) }
    }
}