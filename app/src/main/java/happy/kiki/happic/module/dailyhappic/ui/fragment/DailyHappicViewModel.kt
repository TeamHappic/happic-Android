package happy.kiki.happic.module.dailyhappic.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.util.extension.collectFlow
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicKeywordMockService
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicPhotoListModel
import kotlinx.coroutines.flow.MutableStateFlow

class DailyHappicViewModel : ViewModel() {
    val dailyHappicPhotosApi = useApi<Pair<Int, Int>, List<DailyHappicPhotoListModel>> { (year, month) ->
        dailyHappicKeywordMockService.photos(year, month)
    }

    val currentYear = MutableStateFlow(now.year)
    val selectedYearMonth = MutableStateFlow(now.year to now.monthValue)

    init {
        collectFlow(selectedYearMonth) { (year, month) ->
            dailyHappicPhotosApi.call(Pair(year, month))
        }
    }
}