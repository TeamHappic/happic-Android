package happy.kiki.happic.module.dailyhappic.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.data.api.base.useApiNoParams
import happy.kiki.happic.module.core.util.SimpleEventFlow
import happy.kiki.happic.module.core.util.extension.collectFlow
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicService
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn

class DailyHappicViewModel : ViewModel() {
    val currentYear = MutableStateFlow(now.year)
    val selectedYearMonth = MutableStateFlow(now.year to now.monthValue)
    val isMonthSelectOpened = MutableStateFlow(false)

    val dailyHappicApi = useApi<Pair<Int, Int>, List<DailyHappicModel>> { (year, month) ->
        val res = dailyHappicService.dailyHappics(year, month)
        res.copy(data = res.data.sortedByDescending { it.day })
    }
    val detailDailyHappicIndex = MutableStateFlow(-1)
    val detailDailyHappicItem = dailyHappicApi.dataOnlySuccess.combine(detailDailyHappicIndex) { data, index ->
        if (data != null && index >= 0 && data.size > index) {
            data[index]
        } else {
            null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val onNavigateUpload = SimpleEventFlow()
    val onNavigateUploadFailedByMultipleUpload = SimpleEventFlow()

    val navigateUploadApi = useApiNoParams(onSuccess = {
        if (!it.isPosted) onNavigateUpload.emit()
        else onNavigateUploadFailedByMultipleUpload.emit()
    }) {
        dailyHappicService.isTodayUploaded()
    }

    val onDeleteHappic = SimpleEventFlow()
    val deleteHappic = useApi<String>(onSuccess = {
        onDeleteHappic.emit()
    }) {
        dailyHappicService.delete(it)
    }

    init {
        collectFlow(selectedYearMonth.drop(1)) { (year, month) -> dailyHappicApi.call(Pair(year, month)) }
    }

    fun fetchDailyHappics() {
        dailyHappicApi.call(selectedYearMonth.value)
    }
}