package happy.kiki.happic.module.dailyhappic.ui.fragment

import androidx.lifecycle.ViewModel
import happy.kiki.happic.module.core.data.api.base.useApi
import happy.kiki.happic.module.core.util.extension.collectFlow
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.dailyhappic.data.api.dailyHappicMockService
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicPhotoListModel
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicTagModel
import kotlinx.coroutines.flow.MutableStateFlow

class DailyHappicViewModel : ViewModel() {
    val currentYear = MutableStateFlow(now.year)
    val selectedYearMonth = MutableStateFlow(now.year to now.monthValue)

    val dailyHappicPhotosApi = useApi<Pair<Int, Int>, List<DailyHappicPhotoListModel>> { (year, month) ->
        dailyHappicMockService.photos(year, month)
    }

    val dailyHappicTagsApi = useApi<Pair<Int, Int>, List<DailyHappicTagModel>> { (year, month) ->
        dailyHappicMockService.tags(year, month)
    }

    init {
        collectFlow(selectedYearMonth) { (year, month) ->
            dailyHappicPhotosApi.call(Pair(year, month))
        }
        collectFlow(selectedYearMonth) { (year, month) ->
            dailyHappicTagsApi.call(Pair(year, month))
        }
    }
}